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
import org.xapagy.activity.shadowmaintenance.AmLookup;
import org.xapagy.activity.shadowmaintenance.SaicHelper;
import org.xapagy.activity.shadowmaintenance.ShmAddItem;
import org.xapagy.activity.shadowmaintenance.ShmAddItemCollection;
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
 * Created on: Apr 22, 2011
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
