/*
   This file is part of the Xapagy project
   Created on: Apr 1, 2014
 
   org.xapagy.set.EnergySet
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.set;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.instances.XapagyComponent;

/**
 * A generic class which associates energy values to XapagyComponents (instances
 * or VIs). This is normally used to represent the energy in the focus and in
 * the autobiographical memory
 * 
 * @author Ladislau Boloni
 * 
 */
public class EnergySet<T extends XapagyComponent> implements Serializable {

    private static final long serialVersionUID = -6109061246990673508L;
    private Agent agent;
    /**
     * How many quantums had been applied - allows to sort them in the order of
     * application
     */
    private long appliedQuantumCount = 0;
    /**
     * The collection of energies indexed by the EnergyColor
     */
    private Map<String, Map<T, Double>> energies;
    /**
     * Storage of the quantums
     */
    private Map<T, List<EnergyQuantum<T>>> quantums = new HashMap<>();

    /**
     * Constructor / initializes the color repositories
     */
    public EnergySet(Agent agent) {
        this.agent = agent;
        energies = new HashMap<>();
        for (String sc : agent.getEnergyColors().getAllEnergies()) {
            Map<T, Double> energy = new HashMap<>();
            energies.put(sc, energy);
        }
    }

    /**
     * Applied an energy quantum to the energy set
     * 
     * @param eq - the energy set
     */
    public void applyEnergyQuantum(EnergyQuantum<T> eq) {
        T f = eq.getFocusComponent();
        double value = valueEnergy(f, eq.getEnergyColor());
        double multiplication =
                Math.pow(eq.getMultiplicativeChange(),
                        eq.getTimeSlice());
        double addition =
                 eq.getTimeSlice() * eq.getAdditiveChange();
        double newValue = value * multiplication + addition;
        // no negative energy values
        newValue = Math.max(0, newValue);
        //
        // update the quantum with the value 
        //
        eq.setEnergyBeforeQuantum(valueEnergy(f, eq.getEnergyColor()));
        eq.setEnergyAfterQuantum(newValue);
        //
        // debug code, should not be triggered
        //
        if (Double.isNaN(newValue)) {
            throw new Error(
                    "EnergySet.applyQuantum - trying to set the energy to not a number.");
        }
        //
        // record the new energy value
        //
        Map<T, Double> energyMap = getEnergy(eq.getEnergyColor());
        double valueForColor = valueEnergy(f, eq.getEnergyColor());
        double newValueForColor = valueForColor * multiplication + addition;
        energyMap.put(f, newValueForColor);
        //
        // if the parameter says so, store the quantum
        //
        boolean debugRecordFocusMemoryQuantums =
                agent.getParameters().getBoolean("A_DEBUG",
                        "G_GENERAL",
                        "N_RECORD_FOCUS_MEMORY_QUANTUMS");
        if (debugRecordFocusMemoryQuantums) {
            List<EnergyQuantum<T>> list = quantums.get(f);
            if (list == null) {
                list = new ArrayList<>();
                quantums.put(f, list);
            }
            list.add(eq);
            eq.markApplication(appliedQuantumCount++, agent.getTime());
        } else {
            // clear the quantums to save space
            quantums.clear();
        }
    }

    /**
     * Returns a double hashmap for the given energy color
     * 
     * @param sc
     * @return
     */
    private Map<T, Double> getEnergy(String sc) {
        return energies.get(sc);
    }

    /**
     * Returns the list of participants for a specific energy
     * 
     * @return
     */
    public List<T> getParticipants(String ec) {
        Map<T, Double> energy = getEnergy(ec);
        return new ArrayList<>(energy.keySet());
    }
    
    /**
     * Returns the shadow quantums for a specific focus and shadow component or
     * and empty list if none
     * 
     * @param fvi
     * @param svi
     * @return
     */
    public List<EnergyQuantum<T>> getEnergyQuantums(T f, String ec) {
        try {
            List<EnergyQuantum<T>> all = quantums.get(f);
            List<EnergyQuantum<T>> retval = new ArrayList<>();
            for (EnergyQuantum<T> eq : all) {
                if (eq.getEnergyColor().equals(ec)) {
                    retval.add(eq);
                }
            }
            return retval;
        } catch (NullPointerException npe) {
            return new ArrayList<>();
        }
    }

    /**
     * Remove a component (its energies and quantums)
     * 
     */
    public void remove(T component) {
        for (String sc : energies.keySet()) {
            Map<T, Double> energy = getEnergy(sc);
            energy.remove(component);
        }
        quantums.remove(component);
    }

    /**
     * Returns the value of the energy of color ec of component f.
     * 
     * @param f
     * @param ec the energy color
     * @return
     */
    public double valueEnergy(T f, String ec) {
        Map<T, Double> energy = getEnergy(ec);
        if (energy == null) {
            return 0;
        }
        Double value = energy.get(f);
        if (value == null) {
            return 0;
        }
        if (value.isNaN()) {
            throw new Error("The energy value is a NaN!");
        }
        return value;
    }

}
