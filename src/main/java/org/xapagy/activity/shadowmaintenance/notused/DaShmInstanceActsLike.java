/*
   This file is part of the Xapagy project
   Created on: Nov 9, 2011
 
   org.xapagy.activity.DaShadowInstanceActsLike
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance.notused;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.reference.rrState;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * A diffusion activity which maintains the impact of the acts-like verbs in the
 * shadow
 * 
 * As of May 2014, I disconnected it, as it is rarely used...
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmInstanceActsLike extends AbstractDaFocusIterator {

    private static final long serialVersionUID = -4881396970416624267L;

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmInstanceActsLike(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * Strengthens / weakens the shadows such that they match the acts-like
     * component
     * 
     * @param fvi
     * @param timeSlice
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
        if (fvi.getVerbs().getEnergy(
                agent.getVerbDB().getConcept(Hardwired.VM_ACTS_LIKE)) == 0.0) {
            return;
        }
        Instance instSubject = fvi.getSubject();
        ConceptOverlay co = fvi.getAdjective();
        // reinforce the ones which can be considered as references
        for (Instance si : sf.getMembers(instSubject,
                EnergyColors.SHI_GENERIC)) {
            rrState resconf = rrState.createCalculated(co, si.getConcepts());
            if (resconf.getOverallScore() > 0) {
                EnergyQuantum<Instance> sq = EnergyQuantum.createAdd(
                        instSubject, si, resconf.getOverallScore(), timeSlice,
                        EnergyColors.SHI_GENERIC,
                        "DaShmInstanceActsLike + applyFocusVi");
                sf.applyInstanceEnergyQuantum(sq);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaShmInstanceActsLike");
    }

}
