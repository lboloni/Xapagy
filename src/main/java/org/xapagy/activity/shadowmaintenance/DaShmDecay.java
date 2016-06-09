/*
   This file is part of the Xapagy project
   Created on: Apr 22, 2011
 
   org.xapagy.story.activity.DADecayShadows
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.HashMap;
import java.util.Map;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * A general decay of the shadows, only dependent on the current strenght
 * 
 * It also performs a garbage collection.
 * 
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmDecay extends AbstractDaFocusIterator {
    /**
     * 
     */
    private static final long serialVersionUID = -5751314173536351172L;
    /**
     * The multiplier for the energy decay for instance shadows
     */
    private Map<String, Double> instanceShadowMultiplier;
    /**
     * The multiplier for the energy decay for VI shadows
     */
    private Map<String, Double> viShadowMultiplier;

    /**
     * @param agent
     * @param name
     */
    public DaShmDecay(Agent agent, String name) {
        super(agent, name);
    }


    /* (non-Javadoc)
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        // instance shadow energies
        instanceShadowMultiplier = new HashMap<>();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            double value = getParameterDouble("ISh_Mult_" + ec.toString());
            instanceShadowMultiplier.put(ec.toString(), value);
        }
        // VI shadow energies
        viShadowMultiplier = new HashMap<>();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            double value = getParameterDouble("ViSh_Mult_" + ec.toString());
            viShadowMultiplier.put(ec.toString(), value);
        }
    }

    
    
    /**
     * Decaying of the shadows of non-scene instances
     * 
     * @param fi
     * @param timeSlice
     */
    @Override
    protected void applyFocusNonSceneInstance(Instance fi, double timeSlice) {
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            double instanceRemainingEnergyRatio = instanceShadowMultiplier.get(ec.toString());
 //                   p.get("A_SHM",
 //                           "G_DECAY",
 //                           "N_INSTANCE_SHADOW_ENERGY_DECAY_MULTIPLIER"
 //                                   + ec);
            for (Instance si : sf.getMembers(fi, ec)) {
                EnergyQuantum<Instance> sq =
                        EnergyQuantum.createMult(fi, si,
                                instanceRemainingEnergyRatio, timeSlice, ec,
                                "DaShmDecay + applyFocusNonSceneInstance");
                sf.applyInstanceEnergyQuantum(sq);
            }
        }
    }

    /**
     * Decaying of the shadows of scenes
     * 
     * @param fi
     * @param timeSlice
     */
    @Override
    protected void applyFocusScene(Instance fi, double timeSlice) {
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            double instanceRemainingEnergyRatio = instanceShadowMultiplier.get(ec.toString());
              //      p.get("A_SHM",
              //              "G_DECAY",
              //              "N_INSTANCE_SHADOW_ENERGY_DECAY_MULTIPLIER"
              //                      + ec);
            for (Instance si : sf.getMembers(fi, ec)) {
                EnergyQuantum<Instance> sq =
                        EnergyQuantum.createMult(fi, si,
                                instanceRemainingEnergyRatio, timeSlice, ec,
                                "DaShmDecay + applyFocusScene");
                sf.applyInstanceEnergyQuantum(sq);
            }
        }
    }

    /**
     * @param fvi
     * @param timeSlice
     */
    @Override
    protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            double viRemainingEnergyRatio =
                    viShadowMultiplier.get(ec.toString());
            //        
            //        p.get("A_SHM", "G_DECAY",
            //                "N_VI_SHADOW_ENERGY_DECAY_MULTIPLIER"
            //                        + ec);
            for (VerbInstance svi : sf.getMembers(fvi, ec)) {
                EnergyQuantum<VerbInstance> sq =
                        EnergyQuantum.createMult(fvi, svi,
                                viRemainingEnergyRatio, timeSlice, ec,
                                "DaShmDecay + applyFocusVi");
                sf.applyViEnergyQuantum(sq);
            }
        }
    }


    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("DaShmDecay");
        fmt.indent();
        fmt.add("Instance shadow energy multipliers");
        fmt.indent();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            double value = instanceShadowMultiplier.get(ec.toString());
            fmt.is("instanceShadowMultiplier_" + ec, value);
        }
        fmt.deindent();
        fmt.add("VI shadow energy multipliers");
        fmt.indent();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            double value = viShadowMultiplier.get(ec.toString());
            fmt.is("viShadowMultiplier_" + ec, value);
        }
        fmt.deindent();
        fmt.deindent();
    }

}
