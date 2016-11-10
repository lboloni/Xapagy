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
package org.xapagy.set;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;

/**
 * A generic class which associates energy values to pairs of XapagyComponents
 * (instances or VIs). This is normally used to represent the energy in the
 * shadow
 * 
 * @author Ladislau Boloni
 * Created on: Jan 30, 2012
 */
public class PairEnergySet<T extends XapagyComponent> implements Serializable {

    private static final long serialVersionUID = -3998950004762350338L;
    private Agent agent;
    /**
     * How many quantums had been applied - allows to sort them in the order of
     * application
     */
    private long appliedQuantumCount = 0;
    /**
     * The collection of energies indexed by the ShadowColor
     * 
     * The energy of the shadow: the first item is the focus item, while the
     * next one the shadow item.
     */
    private Map<String, Map<T, Map<T, Double>>> energies;
    /**
     * Storage of the quantums
     */
    private Map<T, Map<T, List<EnergyQuantum<T>>>> quantums = new HashMap<>();

    /**
     * Constructor / initializes the color repositories
     */
    public PairEnergySet(Agent agent) {
        this.agent = agent;
        energies = new HashMap<>();
        for (String sc : agent.getEnergyColors().getAllEnergies()) {
            Map<T, Map<T, Double>> energy = new HashMap<>();
            energies.put(sc, energy);
        }
    }

    /**
     * Applies the shadow quantum. Applies the time slice and strength
     * 
     * @param eq
     *            - the shadow quantum to be applied
     * 
     */
    public void applyEnergyQuantum(EnergyQuantum<T> eq) {
        T f = eq.getFocusComponent();
        T s = eq.getShadowComponent();
        double value = valueEnergy(f, s, eq.getEnergyColor());
        double multiplication =
                Math.pow(eq.getMultiplicativeChange(), eq.getTimeSlice());
        double addition = eq.getTimeSlice() * eq.getAdditiveChange();
        double newValue = value * multiplication + addition;
        // no negative energy values
        newValue = Math.max(0, newValue);
        // setEnergy(f, s, newValue, sq);

        //
        // update the quantum with the value
        //
        eq.setEnergyBeforeQuantum(valueEnergy(f, s, eq.getEnergyColor()));
        eq.setEnergyAfterQuantum(newValue);
        //
        // debug code, should not be triggered
        //
        if (Double.isNaN(newValue)) {
            throw new Error(
                    "ShadowEnergy.setEnergy - trying to set the energy to not a number.");
        }
        // debug for vi's with different type
        if (f instanceof VerbInstance) {
            VerbInstance vf = (VerbInstance) f;
            VerbInstance vs = (VerbInstance) s;
            if (vf.getViType() != vs.getViType()) {
                throw new Error("Should not have VI's of different types!!!");
            }
        }
        //
        // record the new energy value
        //
        Map<T, Double> shadow = getEnergy(eq.getEnergyColor()).get(f);
        double valueForColor = valueEnergy(f, s, eq.getEnergyColor());
        double newValueForColor = valueForColor * multiplication + addition;
        if (shadow == null) {
            shadow = new HashMap<>();
            getEnergy(eq.getEnergyColor()).put(f, shadow);
        }
        shadow.put(s, newValueForColor);
        //
        // if the parameter says so, store the quantum (the quantum of different
        // energies is stored together)
        //
        boolean debugRecordShadowQuantums =
                agent.getParameters().getBoolean("A_DEBUG",
                        "G_GENERAL",
                        "N_RECORD_SHADOW_QUANTUMS");
        if (debugRecordShadowQuantums) {
            Map<T, List<EnergyQuantum<T>>> map = quantums.get(f);
            if (map == null) {
                map = new HashMap<>();
                quantums.put(f, map);
            }
            List<EnergyQuantum<T>> list = map.get(s);
            if (list == null) {
                list = new ArrayList<>();
                map.put(s, list);
            }
            list.add(eq);
            eq.markApplication(appliedQuantumCount++, agent.getTime());
        } else {
            // clear the quantums to save space
            quantums.clear();
        }
    }

    /**
     * Stochastic garbage collection - this is garbage collecting shadows which
     * are otherwise active.
     * 
     * The same function, at the end performs a cleaning: it collects shadows
     * and quantums which belong to items which are not in the focus any more
     * 
     * The garbage collection happens based on the ALL energy color - but when
     * the decision to remove happens, it will be done from all energy colors.
     * 
     * Probability that something will be garbage collected in any given t
     * period of time:
     * 
     * $p = t \cdot e^{\frac{-\beta x}{E_{min}-x}}$
     * 
     * Note that this is only called when the shadow is there!
     * 
     * @param keyF
     *            - the focus component of the shadow
     * @param timeSlice
     *            - the timeslice over which the garbage collection is happening
     * @param safeEnergy
     *            - if the shadow has this amount of energy, it is safe and
     *            won't be garbage collected
     * @param inflectionPopulation
     *            - above this population, the values increase - this is the
     *            size of the shadow we would like to have
     * @beta the steepness of the angle
     * 
     */
    public void garbageCollectStochastic(T keyF, double timeSlice,
            double safeEnergy, double inflectionPopulation, double beta,
            List<String> ecs) {
        // TextUi.println("garbageCollectStochastic: energy items"
        // + energy.keySet().size());
        // Map<T, Double> shadow = getEnergy(ec).get(keyF);
        Map<T, List<EnergyQuantum<T>>> quantumset = quantums.get(keyF);
        // if (shadow == null) {
        // return;
        // }
        List<SimpleEntry<T, Double>> dropProbabilities =
                gcCalculateDropProbabilities(keyF, timeSlice, safeEnergy,
                        inflectionPopulation, beta, ecs);
        //
        // Create a dice, and perform the dropping
        //
        List<T> toRemove = new ArrayList<>();
        for (SimpleEntry<T, Double> entry : dropProbabilities) {
            double probability = entry.getValue();
            double dice = agent.getRandom().nextDouble();
            if (dice <= probability) {
                toRemove.add(entry.getKey());
                // TextUi.println(PrettyPrint.ppConcise(entry.getKey(), agent)
                // + " dropped, p was " + probability + " dice was "
                // + dice);
            }
        }
        //
        // Now, clean up
        //
        for (T rem : toRemove) {
            // remove from all colors
            for (String sc : agent.getEnergyColors().getAllEnergies()) {
                Map<T, Double> map2 = getEnergy(sc).get(keyF);
                if (map2 != null) {
                    map2.remove(rem);
                }
            }
            if (quantumset != null) {
                quantumset.remove(rem);
            }
        }
        //
        // Now clean up those shadows and quantums where the head is not in the
        // focus any more
        //

        toRemove = new ArrayList<>();
        Focus fc = agent.getFocus();
        if (keyF instanceof Instance) {
            if (fc.getSalience((Instance) keyF, EnergyColors.FOCUS_INSTANCE) == 0.0) {
                energies.remove(keyF);
                quantums.remove(keyF);
            }
        } else {
            if (fc.getSalience((VerbInstance) keyF, EnergyColors.FOCUS_VI) == 0.0) {
                energies.remove(keyF);
                quantums.remove(keyF);
            }
        }
    }

    /**
     * Calculate the drop probabilities based on a sum of energies of various
     * types
     * 
     * FIXME: debug it, because the inflection, for instance, does not make a
     * lot of sense
     * 
     * @param keyF
     *            - the focus item
     * @param timeSlice
     *            - the time slice on which we are calculating the probabilities
     * @param safeEnergy
     *            - if the total energy
     * @param beta
     *            - FIXME
     * @param ec
     *            - the energy color used
     * 
     * @return
     */
    public List<SimpleEntry<T, Double>> gcCalculateDropProbabilities(T keyF,
            double timeSlice, double safeEnergy, double inflectionPopulation,
            double beta, List<String> energiesConsidered) {
        // collect all the shadows of the listed energy
        Set<T> shadows = new HashSet<>();
        for (String ecx : energiesConsidered) {
            Map<T, Double> map = getEnergy(ecx).get(keyF);
            if (map != null) {
                shadows.addAll(map.keySet());
            }
        }
        //
        // if it is larger than the inflection population, increase the drop
        // probability, but still must be at most 1.0
        //
        double baseProbability = timeSlice;
        if (shadows.size() > inflectionPopulation) {
            baseProbability =
                    baseProbability * (shadows.size() / inflectionPopulation);
        }
        baseProbability = Math.min(1.0, baseProbability);
        //
        // calculate the probabilities of dropping for each shadow, create a
        // dice, and then perform the dropping
        //
        List<SimpleEntry<T, Double>> dropProbabilities = new ArrayList<>();
        for (T keyS : shadows) {
            // FIXME: this should be a list of energies
            // double en = shadow.get(keyS);
            double en = 0;
            for (String ecx : energiesConsidered) {
                en += valueEnergy(keyF, keyS, ecx);
            }
            double probability;
            if (en < safeEnergy) {
                probability =
                        baseProbability
                                * Math.exp(-en * beta / (safeEnergy - en));
            } else {
                probability = 0.0;
            }
            // TextUi.println(PrettyPrint.ppConcise(keyF, agent) + " -->"
            // + PrettyPrint.ppConcise(keyS, agent) + " drop p="
            // + probability);
            dropProbabilities.add(new SimpleEntry<>(keyS, probability));
        }
        return dropProbabilities;
    }

    /**
     * Returns the components in a certain shadow, or empty if it is empty
     * 
     * @return
     */
    public List<T> getComponents(T f, String sc) {
        List<T> retval = new ArrayList<>();
        Map<T, Double> shadow = getEnergy(sc).get(f);
        if (shadow != null) {
            retval.addAll(shadow.keySet());
        }
        return retval;
    }

    /**
     * Returns a double hashmap for the given energy color
     * 
     * @param sc
     * @return
     */
    private Map<T, Map<T, Double>> getEnergy(String sc) {
        return energies.get(sc);
    }

    /**
     * Returns all the heads (for garbage collection purposes)
     * 
     * @return
     */
    public List<T> getHeads(String ec) {
        Map<T, Map<T, Double>> energy = energies.get(ec);
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
    public List<EnergyQuantum<T>> getEnergyQuantums(T f, T s, String ec) {
        try {
            List<EnergyQuantum<T>> all = quantums.get(f).get(s);
            List<EnergyQuantum<T>> retval = new ArrayList<>();
            for (EnergyQuantum<T> eq : all) {
                if (eq.getEnergyColor() == ec) {
                    retval.add(eq);
                }
            }
            return retval;
        } catch (NullPointerException npe) {
            return new ArrayList<>();
        }
    }

    /**
     * Returns the value of the energy of the shadow s in focus f
     * 
     * @param f
     * @param s
     * @return
     */
    public double valueEnergy(T f, T s, String sc) {
        Map<T, Double> shadow = getEnergy(sc).get(f);
        if (shadow == null) {
            return 0;
        }
        Double value = shadow.get(s);
        if (value == null) {
            return 0;
        }
        if (value.isNaN()) {
            throw new Error("The energy value is a NaN!");
        }
        return value;
    }
}
