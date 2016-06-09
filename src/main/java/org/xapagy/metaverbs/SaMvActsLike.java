/*
   This file is part of the Xapagy project
   Created on: Nov 8, 2011
 
   org.xapagy.metaverbs.SaMvActsLike
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.operations.Incompatibility;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class SaMvActsLike extends AbstractSaMetaVerb {
    private static final long serialVersionUID = -331369907021910792L;

    /**
     * Returns the relations between the same nodes (or on the same node if
     * unary)
     * 
     * To be used by SaMvRemoveRelation as well
     * 
     * @return
     */
    public static List<VerbInstance> returnMatchingActsLikes(
            VerbInstance verbInstance, Agent agent) {
        Focus fc = agent.getFocus();
        Verb vbActsLike = agent.getVerbDB().getConcept(Hardwired.VM_ACTS_LIKE);
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (vi.getViType() != ViType.S_ADJ) {
                continue;
            }
            if (vi.getSubject() != verbInstance.getSubject()) {
                continue;
            }
            if (vi.getVerbs().getEnergy(vbActsLike) > 0) {
                retval.add(vi);
            }
        }
        return retval;
    }

    public SaMvActsLike(Agent agent) {
        super(agent, "SaMvActsLike", null);
    }

    /**
     * The relation will be this VI itself.
     * 
     * Here, we are going to expire those relations which are not similar
     * 
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Focus fc = agent.getFocus();
        List<VerbInstance> candidates =
                SaMvActsLike.returnMatchingActsLikes(verbInstance, agent);

        List<VerbInstance> actsLikeToRemove = new ArrayList<>();
        ConceptOverlay coNew = verbInstance.getAdjective();
        for (VerbInstance vi : candidates) {
            ConceptOverlay co = vi.getAdjective();
            if (Incompatibility.decideIncompatibility(co, coNew)) {
                actsLikeToRemove.add(vi);
            }
        }
        for (VerbInstance vi : actsLikeToRemove) {
            EnergyQuantum<VerbInstance> eq =
                    EnergyQuantum.createMult(vi, 0.0, EnergyColors.FOCUS_VI,
                            "SaMvActsLike");
            fc.applyViEnergyQuantum(eq);
        }
    }
    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvActsLike");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
