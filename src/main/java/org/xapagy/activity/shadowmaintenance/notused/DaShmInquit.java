/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
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
 * Created on: Apr 14, 2013
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
