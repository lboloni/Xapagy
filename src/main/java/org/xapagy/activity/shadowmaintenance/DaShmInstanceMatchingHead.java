/*
   This file is part of the Xapagy project
   Created on: Apr 23, 2011
 
   org.xapagy.story.activity.DAIMatchingHead
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.InstanceSet;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * This DA adds SHADOW_GENERIC energy to the memory instances which match the
 * focus head.
 * 
 * The amount of energy added is defined by whatever AmLookup.lookupCo returns
 * as the matching amount.
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmInstanceMatchingHead extends AbstractDaFocusIterator {

    private static final long serialVersionUID = 1705064759740445621L;

    /**
     * Multiplies the amount of energy SHI_ATTRIBUTE energy added to all
     * in-focus instances based on attribute matching
     */
    private double scaleInstanceByAttribute;
    /**
     * Multiplies the amount of energy SHI_ATTRIBUTE energy added to all
     * in-focus instances based on VI matching
     */
    private double scaleInstanceInAction;

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        scaleInstanceByAttribute =
                getParameterDouble("scaleInstanceByAttribute");
        scaleInstanceInAction = getParameterDouble("scaleInstanceInAction");
    }

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmInstanceMatchingHead(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * Performs an instance reinforcement for all the instances which are part
     * of the focus, in the specific instance energy
     * 
     * @param fi
     * @param timeSlice
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double timeSlice) {
        addShadowsBasedOnMatch(fi, timeSlice, scaleInstanceByAttribute,
                EnergyColors.SHI_ATTRIBUTE);
    }

    /**
     * Experiment: perform instance reinforcement only for the active action
     * instances
     * 
     * This would prevent the shadows accumulating based on purely structural
     * features for quiescent instances.
     * 
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
        if (!ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
            return;
        }
        for (ViPart part : ViStructureHelper
                .getAllowedInstanceParts(fvi.getViType())) {
            Instance instance = (Instance) fvi.getPart(part);
            addShadowsBasedOnMatch(instance, timeSlice, scaleInstanceInAction,
                    EnergyColors.SHI_ATTRIBUTE);
        }
    }

    /**
     * This function adds shadows (by adding a certain shadow energy) to a focus
     * instance based on the match between the attributes of the focus instance
     * and the shadow instance.
     * 
     * @param fi
     *            - the focus instance
     * @param timeSlice
     * @param scale
     *            - parameter scaling the shadow energy added
     * @param ec
     *            - the energy color of the energy added
     */
    private void addShadowsBasedOnMatch(Instance fi, double timeSlice,
            double scale, String ec) {
        ShmAddItemCollection saic = new ShmAddItemCollection();
        InstanceSet matches =
                AmLookup.lookupCo(agent.getAutobiographicalMemory(),
                        fi.getConcepts(), EnergyColors.AM_INSTANCE);
        for (Instance match : matches.getParticipants()) {
            // a guard to prevent scenes shadowing non scenes
            if (match.isScene()) {
                continue;
            }
            double matchLevel = matches.value(match);
            double addLevel = matchLevel * scale;
            ShmAddItem sai = new ShmAddItem(agent, match, fi, addLevel, ec);
            saic.addShmAddItem(sai);
        }
        SaicHelper.applySAIC_Instances(agent, saic, timeSlice,
                "DaShmInstanceMatchingHead");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters
     * .IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaShmInstanceMatchingHead");
        fmt.indent();
        fmt.is("scaleInstanceByAttribute", scaleInstanceByAttribute);
        fmt.explanatoryNote(
                "Multiplies the amount of energy SHI_ATTRIBUTE energy added to all "
                        + "in-focus instances based on attribute matching");
        fmt.is("scaleInstanceInAction", scaleInstanceInAction);
        fmt.explanatoryNote(
                "Multiplies the amount of energy SHI_ATTRIBUTE energy added to all "
                        + "in-focus instances based on VI matching");
        fmt.deindent();
    }

}
