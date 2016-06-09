/*
   This file is part of the Xapagy project
   Created on: May 27, 2013
 
   org.xapagy.ui.prettyhtml.PwScenePreferences
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.instances.SceneParameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.formatters.PwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class PwScenePreferences {

    /**
     * Prints a separate subsection for the fired shadow VIs
     * 
     * @param fmt
     * @param instance
     * @param agent
     * @param gq
     */
    private static void pwFiredShadowVis(PwFormatter fmt, SceneParameters sp,
            Agent agent, RESTQuery query) {
        fmt.addLabelParagraph("firedShadowVis");
        fmt.explanatoryNote("The set of the shadow VIs which had been fired by a "
                + "continuation choice in this scene. These will be inhibited for firing again.");
        ViSet vis = sp.getFiredShadowVis();
        PwQueryLinks.linkToViSet(fmt, agent, query, vis);
    }

    /**
     * Prints the scene preferences
     * 
     * @param fmt
     * @param instance
     * @param agent
     * @param gq
     */
    public static void pwScenePreferences(PwFormatter fmt, SceneParameters sp,
            Agent agent, RESTQuery query) {
        fmt.is("energyContinuation", sp.getEnergy(EnergyColors.SCENE_CONTINUATION));
        fmt.is("energyInterstitial", sp.getEnergy(EnergyColors.SCENE_INTERSTITIAL));
        //
        // Characterization weight
        //
        fmt.addLabelParagraph("Characterization weights");
        fmt.indent();
        fmt.is("characterizationWeightEssence",
                sp.getCharacterizationWeightEssence());
        fmt.is("characterizationWeightUniqueness",
                sp.getCharacterizationWeightUniqueness());
        fmt.is("characterizationWeightTargetDifference",
                sp.getCharacterizationWeightTargetDifference());
        fmt.deindent();
        //
        // Choice preferences
        //
        fmt.addLabelParagraph("Choice type preferences");
        fmt.indent();
        for (ChoiceType ct : ChoiceType.values()) {
            fmt.is(ct.toString(), sp.getChoiceTypePreferences().get(ct));
        }
        fmt.deindent();
        //
        // Story consistency weights
        //
        fmt.addLabelParagraph("Story consistency weights");
        fmt.indent();
        for (String linkType : agent.getLinks().getLinkTypeNames()) {
            fmt.is(linkType, sp.getLinkStructureWeight().get(linkType));
        }
        fmt.deindent();
        //
        // Fired shadow VIs
        //
        PwScenePreferences.pwFiredShadowVis(fmt, sp, agent, query);

    }

}
