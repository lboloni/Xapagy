/*
   This file is part of the Xapagy project
   Created on: Aug 25, 2010
 
   org.xapagy.domain.basic.vbSuccessor
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * This verb is normally associated to an action VI. In fact its presence marks
 * the action VI.
 * 
 * The successor meta-verb creates the successor relations between VIs, and
 * pushes out predecessors.
 * 
 * At the same time it creates context links to the relation VIs
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvSuccessor extends AbstractSaMetaVerb {

    private static final long serialVersionUID = 1997770812823593412L;

    /**
     * 
     * @param agent
     */
    public SaMvSuccessor(Agent agent) {
        super(agent, "SaMvSuccessor", null);
    }

    /**
     * This verb will interact with all the "matching" verbs in the focus. In
     * this particular case, the matching verbs are those which have common
     * scenes with this verb.
     * 
     * 
     * <ul>
     * <li>for action VIs: increase the successor link between current VI and
     * other VI with the focus value of other VI and performs a pushout only if
     * this is not a coincidence link
     * <li>for relation VIs: increase the context link between current VI and
     * other relation VI with the focus value of the other VI.
     * </ul>
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Focus fc = agent.getFocus();
        for (VerbInstance fvi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (fvi.equals(verbInstance)) {
                continue;
            }
            //
            // For coincidence VIs the location in the linkmesh is determined
            // by the head VI of the coincidence group
            //
            if (ViClassifier.decideViClass(ViClass.COINCIDENCE, verbInstance,
                    agent)) {
                continue;
            }
            double match = matchFilter(verbInstance, fvi, agent);
            if (match > 0.0) {
                //
                // The value of the links to be created: scaled with the
                // strength of the other VI, and with the strength of this
                // verb
                //
                // The 0.5 here comes from the old setting where ActionVerb was
                // scaled
                double value =
                        match * fc.getSalience(fvi, EnergyColors.FOCUS_VI) * 0.5;
                // for action verbs, create link, and push out
                if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
                    agent.getLinks().changeLinkByName(Hardwired.LINK_SUCCESSOR, fvi, verbInstance, value, "SaMvSuccessor/ActionClass"
                    + "+changeSuccessor");
                    double multiplier = Math.pow(1.0 - value, 3.0);
                    EnergyQuantum<VerbInstance> eq =
                            EnergyQuantum.createMult(fvi, multiplier,
                                    EnergyColors.FOCUS_VI, "SaMvSuccessor");
                    fc.applyViEnergyQuantum(eq);
                }
                // for relation verbs, add the context relation, but don't
                // change
                if (ViClassifier.decideViClass(ViClass.RELATION, fvi, agent)) {
                    //vidb.changeLink(ViLinkDB.IR_CONTEXT, fvi, verbInstance,
                    //        value);
                    agent.getLinks().changeLinkByName(Hardwired.LINK_IR_CONTEXT, fvi, verbInstance, value, "SaMvSuccessor/RelationClass"
                    + "+changeIrContext");
                }
            }
        }
    }

    /**
     * Returns the match level -if all is set: 1.0 -if is not set: 1.0 if it has
     * common elements.
     * 
     * For all and immediate, it also multiplies with the scene match!!!
     * 
     * @param thisInstance
     * @param to
     * @return
     */
    protected double matchFilter(VerbInstance vi1, VerbInstance vi2, Agent a) {
        // don't consider quotes
        if (CommonInstanceHelper.haveCommonScenes(vi1, vi2, a, false)) {
            return 1.0;
        }
        return 0.0;
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvSuccessor");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
    
}
