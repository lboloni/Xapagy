/*
   This file is part of the Xapagy project
   Created on: May 12, 2014
 
   org.xapagy.ui.tempdyn.GraphEvolutionDescriptor
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.set.EnergyColors.EnergyColorType;

/**
 * This class is used to describe the properties of a graph
 * 
 * @author Ladislau Boloni
 *
 */
public class GraphEvolutionDescriptor {

    /**
     * If true, plot the focus saliences
     */
    public boolean graphFocusSalience;
    /**
     * If true, plot the focus energies
     */
    public boolean graphFocusEnergy;
    /**
     * The focus instance energy colors we want to plot
     */
    public List<String> focusInstanceEnergyColors;
    /**
     * The focus vi energy colors we want to plot
     */
    public List<String> focusViEnergyColors;
    /**
     * If true, plot the memory salience values
     */
    public boolean graphMemorySalience;
    /**
     * If true, plot the memory energy values
     */
    public boolean graphMemoryEnergy;
    /**
     * The memory energy colors we want to plot
     */
    public List<String> memoryInstanceEnergyColors;
    /**
     * The memory energy colors we want to plot
     */
    public List<String> memoryViEnergyColors;
    /**
     * If true, plot the shadow energy values
     */
    public boolean graphShadowEnergy;
    /**
     * If true, plot the shadow salience values
     */
    public boolean graphShadowSalience;
    /**
     * The instance shadow energy colors we want to plot
     */
    public List<String> shadowInstanceEnergyColors;
    /**
     * The instance shadow energy colors we want to plot
     */
    public List<String> shadowViEnergyColors;

    /**
     * Creates a descriptor which tells the system to plot everything.
     * 
     * @return
     */
    public static GraphEvolutionDescriptor createKitchenSink(Agent agent) {
        GraphEvolutionDescriptor ged = new GraphEvolutionDescriptor();
        // print all the energies and saliences for the focus
        ged.graphFocusEnergy = true;
        ged.graphFocusSalience = true;
        ged.focusInstanceEnergyColors =
                new ArrayList<>(agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE));
        ged.focusViEnergyColors =
                new ArrayList<>(agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI));
        // print all the energies and saliences for the memory
        ged.graphMemoryEnergy = true;
        ged.graphMemorySalience = true;
        ged.memoryInstanceEnergyColors =
                new ArrayList<>(agent.getEnergyColors().getEnergies(EnergyColorType.AM_INSTANCE));
        ged.memoryViEnergyColors =
                new ArrayList<>(agent.getEnergyColors().getEnergies(EnergyColorType.AM_VI));
        // print all the energies and saliences for the shadows
        ged.graphShadowEnergy = true;
        ged.graphShadowSalience = true;
        ged.shadowInstanceEnergyColors =
                new ArrayList<>(agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE));
        ged.shadowViEnergyColors =
                new ArrayList<>(agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI));
        
        return ged;
    }

}
