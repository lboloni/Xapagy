/*
   This file is part of the Xapagy project
   Created on: Oct 5, 2012
 
   org.xapagy.activity.shadowmaintenance.SaicHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.shadows.Shadows;

/**
 * @author Ladislau Boloni
 * 
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
