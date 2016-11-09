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
package org.xapagy.activity.shadowmaintenance;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.shadows.Shadows;

/**
 * @author Ladislau Boloni
 * Created on: Oct 5, 2012
 */
public class SaicHelper {

    /**
     * Applies an SAIC to the instance shadows (relies on the parameters)
     * 
     * @param agent
     * @param saic
     */
    public static void applySAIC_Instances(Agent agent,
            ShmAddItemCollection saic, double timeSlice, String calledFrom) {
        Shadows sf = agent.getShadows();
        int items =
                agent.getParameters().getInteger("A_SHM",
                        "G_GENERAL",
                        "N_SHADOW_ITEMS_PER_ITERATION");
        List<ShmAddItem> list = saic.choose(items, agent.getRandom());
        for (ShmAddItem sai : list) {
            EnergyQuantum<Instance> sq =
                    EnergyQuantum.createAdd(sai.getFocusInstance(),
                            sai.getShadowInstance(), sai.getAddedEnergy(),
                            timeSlice, sai.getEnergyColorInstance(), calledFrom
                                    + " + SaicHelper.applySAIC_Instances");
            sf.applyInstanceEnergyQuantum(sq);
        }
    }

    /**
     * Applies an SAIC to the VI shadows (relies on the parameters)
     * 
     * @param agent
     * @param saic
     */
    public static void applySAIC_VisAndInstances(Agent agent,
            ShmAddItemCollection saic, double timeSlice, String calledFrom) {
        Shadows sf = agent.getShadows();
        int items =
                agent.getParameters().getInteger("A_SHM",
                        "G_GENERAL",
                        "N_SHADOW_ITEMS_PER_ITERATION");
        List<ShmAddItem> list = saic.choose(items, agent.getRandom());
        for (ShmAddItem sai : list) {
            sf.compositeAdd(sai.getFocusVi(), sai.getShadowVi(),
                    sai.getAddedEnergy(), timeSlice, calledFrom
                            + " + SaicHelper.applySAIC_Vis",
                    sai.getEnergyColorVi(), sai.getEnergyColorInstance());
        }
    }

}
