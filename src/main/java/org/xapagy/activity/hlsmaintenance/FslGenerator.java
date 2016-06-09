/*
   This file is part of the Xapagy project
   Created on: Jun 30, 2013
 
   org.xapagy.activity.hlsmaintenance.FslGenerator
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.instances.VerbInstance;
import org.xapagy.links.Links;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;

/**
 * @author Ladislau Boloni
 * 
 */
public class FslGenerator {

    /**
     * Adds the FSL to a list if its total support passes the threshold
     * 
     * @param agent
     *            - passed here to get the parameter
     * @param list
     * @param fsl
     */
    static void addFslIfStrongEnough(Agent agent, List<FocusShadowLinked> list,
            FocusShadowLinked fsl) {
        double minimumStrength =
                agent.getParameters().get("A_HLSM",
                        "G_GENERAL", "N_MINIMUM_FSL_STRENGTH");
        if (fsl.getTotalSupport(agent) > minimumStrength) {
            list.add(fsl);
        }
    }

    /**
     * Generate the list of all the FSLs.
     * 
     * @return
     */
    public static List<FocusShadowLinked> generateFsls(Agent agent) {
        // generate all the direct FSLs
        Focus fc = agent.getFocus();
        List<FocusShadowLinked> allFsls = new ArrayList<>();
        for (VerbInstance vishRootVi : fc.getViList(EnergyColors.FOCUS_VI)) {
            List<FocusShadowLinked> fsls =
                    FslGenerator.generateFslsFromViFocus(agent, vishRootVi);
            allFsls.addAll(fsls);
        }
        // if the option is turned on, generate the choice based FSLs
        boolean generateIndirectFsl =
                agent.getParameters().getBoolean("A_HLSM",
                        "G_GENERAL", "N_GENERATE_INDIRECT_FSL");
        if (generateIndirectFsl) {
            for (Choice choice : agent.getHeadlessComponents().getChoices()
                    .values()) {
                List<FocusShadowLinked> fsls =
                        FslGenerator.generateIndirectFslsFromAChoice(agent,
                                choice);
                allFsls.addAll(fsls);
            }
        }
        return allFsls;
    }

    /**
     * Generates all the Direct FSL starting from a certain viFocus.
     * 
     * WARNING: A number of guards limit the FSLs generated to be only those
     * which are larger than the minimumStrength - they rely on
     * the fact that the totalSupport of the FSL will be smaller than any of the
     * focus value, shadow absolute value and the link value.
     * 
     * WARNING: this is only called from generateAllFsls, it is only public
     * because of some tests
     * 
     * @param vishRoot
     * @param agent
     * @return
     */
    public static List<FocusShadowLinked> generateFslsFromViFocus(Agent agent,
            VerbInstance viFocus) {
        double minimumStrength =
                agent.getParameters().get("A_HLSM",
                        "G_GENERAL", "N_MINIMUM_FSL_STRENGTH");
        List<FocusShadowLinked> retval = new ArrayList<>();
        // if the focus VI itself does not have the minimum strength, drop it
        // now
        if (agent.getFocus().getSalience(viFocus, EnergyColors.FOCUS_VI) < minimumStrength) {
            return retval;
        }
        Shadows sf = agent.getShadows();
        Links la = agent.getLinks();
        // for all the members of the shadow
        for (VerbInstance viShadow : sf.getMembers(viFocus, EnergyColors.SHV_GENERIC)) {
            // if the value is smaller than the minimum, skip this
            if (sf.getSalience(viFocus, viShadow, EnergyColors.SHV_GENERIC) < minimumStrength) {
                continue;
            }
            // for all the relation types
            for (FslType svirType : FslType.values()) {
                if (svirType.equals(FslType.IN_SHADOW)) {
                    FocusShadowLinked item =
                            new FocusShadowLinked(agent, FslType.IN_SHADOW,
                                    viFocus, viShadow, viShadow);
                    FslGenerator.addFslIfStrongEnough(agent, retval, item);
                    continue;
                }
                String linkName = FslType.getLinksForFslType(svirType);
                ViSet links = la.getLinksByLinkName(viShadow, linkName);
                for (VerbInstance viLinked : links.getParticipants()) {
                    FocusShadowLinked item =
                            new FocusShadowLinked(agent, svirType, viFocus,
                                    viShadow, viLinked);
                    FslGenerator.addFslIfStrongEnough(agent, retval, item);
                }
            }
        }
        return retval;
    }

    /**
     * Generates all the Indirect FSL-s, of all the types for a shadow started
     * from a choice
     * 
     * this is only called from generateAllFsls, it is only public because of
     * some tests
     * 
     * @param choice
     * @param agent
     * @return
     */
    public static List<FocusShadowLinked> generateIndirectFslsFromAChoice(
            Agent agent, Choice choice) {
        double minimumStrength =
                agent.getParameters().get("A_HLSM",
                        "G_GENERAL", "N_MINIMUM_FSL_STRENGTH");
        List<FocusShadowLinked> retval = new ArrayList<>();
        Links la = agent.getLinks();
        // if the choice's support itself is not the minimum, drop it
        if (choice.getChoiceScore().getScoreDependent() < minimumStrength) {
            return retval;
        }
        // for all the members of the shadow
        ViSet virtualShadow = choice.getChoiceScore().getVirtualShadow();
        for (VerbInstance viShadow : virtualShadow.getParticipants()) {
            // if the value is smaller than the minimum, skip this
            if (virtualShadow.value(viShadow) < minimumStrength) {
                continue;
            }
            // for all the relation types
            for (FslType svirType : FslType.values()) {
                if (svirType.equals(FslType.IN_SHADOW)) {
                    FocusShadowLinked item =
                            new FocusShadowLinked(agent, FslType.IN_SHADOW,
                                    choice, viShadow, viShadow);
                    FslGenerator.addFslIfStrongEnough(agent, retval, item);
                    continue;
                }
                String linkName = FslType.getLinksForFslType(svirType);
                ViSet links = la.getLinksByLinkName(viShadow, linkName);
                for (VerbInstance viLinked : links.getParticipants()) {
                    FocusShadowLinked item =
                            new FocusShadowLinked(agent, svirType, choice,
                                    viShadow, viLinked);
                    FslGenerator.addFslIfStrongEnough(agent, retval, item);
                }
            }
        }
        return retval;
    }

}
