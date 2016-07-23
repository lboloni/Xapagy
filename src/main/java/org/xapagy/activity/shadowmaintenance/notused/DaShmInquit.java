/*
   This file is part of the Xapagy project
   Created on: Apr 14, 2013
 
   org.xapagy.activity.shadowmaintenance.DaShmInquit
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance.notused;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * This Da reinforces the inquits of quotes where the quote itself has a strong
 * match.
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmInquit extends AbstractDaFocusIterator {

    private static final long serialVersionUID = -8000469947934908377L;

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmInquit(Agent agent, String name) {
        super(agent, name);
    }

    
    /**
     * For VIs of type quote, the inquit is strengthened with the salience of the
     * quote
     * 
     * FIXME: this absolutely makes no sense!!! 
     * 
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
        if (fvi.getViType() != ViType.QUOTE) {
            return;
        }
        VerbInstance fviQuote = fvi.getQuote();
        for (VerbInstance shQuote : sf.getMembers(fviQuote, EnergyColors.SHV_GENERIC)) {
            if (shQuote.getViType() != ViType.QUOTE) {
                continue;
            }
            VerbInstance shInquit = shQuote.getInquit();
            double addEnergy =
                    sf.getSalience(fviQuote, shQuote, EnergyColors.SHV_GENERIC);
            sf.compositeAdd(fvi, shInquit, addEnergy, timeSlice,
                    "DaShmInquit", EnergyColors.SHV_GENERIC, EnergyColors.SHI_GENERIC);
        }
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
        fmt.add("DaShmInquit");
    }

}
