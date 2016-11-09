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
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.InstanceSet;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * For each instance shadow, increase the instances which are matching the
 * existing components of the shadow. This Da allows shadowing where there is no
 * direct overlap between the attributes of the focus instance and the shadow
 * instance
 * 
 * As of May 2014 this is disconnected as too expensive...
 * 
 * @author Ladislau Boloni
 * Created on: Apr 23, 2011
 */
public class DaShmInstanceMatchingBody extends AbstractDaFocusIterator {

    private static final long serialVersionUID = -314368013802146707L;

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmInstanceMatchingBody(Agent agent, String name) {
        super(agent, name);
    }
 
    /**
     * The salience of the root in the shadow initiates a additive the energy
     * transfer to the secondary
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double timeSlice) {
        ShmAddItemCollection saic = new ShmAddItemCollection();
        String ec = EnergyColors.SHI_GENERIC;
        for (Instance si : sf.getMembers(fi, ec)) {
            double salience = sf.getSalience(fi, si, ec);
            InstanceSet coreMatches =
                    AmLookup.lookupCo(agent.getAutobiographicalMemory(),
                            si.getConcepts(), EnergyColors.AM_INSTANCE);
            for (Instance matchInstance : coreMatches.getParticipants()) {
                // a guard for preventing scenes to shadow non-scenes
                if (matchInstance.isScene()) {
                    continue;
                }
                double matchLevel = coreMatches.value(matchInstance);
                double addEnergy = salience * matchLevel;
                ShmAddItem sai =
                        new ShmAddItem(agent, matchInstance, fi, addEnergy, ec);
                saic.addShmAddItem(sai);
            }
        }
        SaicHelper.applySAIC_Instances(agent, saic, timeSlice, "DaShmInstanceMatchingBody.applyFNSI_Stochastic");
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
        fmt.add("DaShmInstanceMatchingBody");
    }

}
