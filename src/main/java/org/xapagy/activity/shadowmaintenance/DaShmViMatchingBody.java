/*
   This file is part of the Xapagy project
   Created on: Apr 22, 2011
 
   org.xapagy.story.activity.DAViMatchingShadow
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * Looks up in the memory verb instances which core match instances in the
 * current shadow. Adds them to the instances shadow - and their corresponding
 * parts to the corresponding parts shadow. This contributes to the gradual
 * spreading of the shadow.
 * 
 * As of May 2014, not enabled by the default, as it is too expensive
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmViMatchingBody extends AbstractDaFocusIterator {

    private static final long serialVersionUID = 3871766778625658580L;

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmViMatchingBody(Agent agent, String name) {
        super(agent, name);
    }

    @Override
    protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
        ShmAddItemCollection saic = new ShmAddItemCollection();
        for (VerbInstance si : sf.getMembers(fvi, EnergyColors.SHV_GENERIC)) {
            // FIXME: we might not need to multiply with the focus here
            double parentWeight =
                    sf.getSalience(fvi, si, EnergyColors.SHV_GENERIC)
                            * fc.getSalience(fvi, EnergyColors.FOCUS_VI);
            ViSet coreMatches = AmLookup.lookupVi(agent, si, EnergyColors.AM_VI);
            for (VerbInstance match : coreMatches.getParticipants()) {
                double matchLevel = coreMatches.value(match);
                double addEnergy = parentWeight * matchLevel;
                ShmAddItem sai =
                        new ShmAddItem(agent, match, fvi, addEnergy,
                                EnergyColors.SHV_GENERIC, EnergyColors.SHI_GENERIC);
                saic.addShmAddItem(sai);
            }
        }
        SaicHelper.applySAIC_VisAndInstances(agent, saic, timeSlice, "DaShmViMatchingBody.applyFocusVi_Stochastic");
    }

    /* (non-Javadoc)
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
        fmt.add("DaShmViMatchingBody");
    }



}
