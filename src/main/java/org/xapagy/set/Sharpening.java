/*
   This file is part of the Xapagy project
   Created on: May 13, 2014
 
   org.xapagy.set.Sharpening
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.set;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.instances.XapagyComponent;

/**
 * Functions for the implementation of sharpening, understood as a re-balancing
 * of energies.
 * 
 * @author Ladislau Boloni
 * 
 */
public class Sharpening {

    /**
     * Implementation of the sharpening algorithm (for a pair energy set). This
     * transformation changes the energy values for a certain for a group of
     * chosen components, in such a way that their total energy remains the same
     * but the distribution of the energy changes.
     * 
     * The sharpening parameter sigma has the following functionality: sigma = 0
     * no change, sigma > 0 sharpening, sigma < 0 softening
     * 
     * @param es
     * @param components
     * @param ec
     * @param sigma
     * @param timeSlices
     * @return
     */
    public static <T extends XapagyComponent> List<EnergyQuantum<T>> sharpen(
            PairEnergySet<T> es, List<SimpleEntry<T, T>> components,
            String ec, double sigma, double timeSlice) {
        List<EnergyQuantum<T>> retval = new ArrayList<>();
        // calculate the scaling factor
        double sumEn = 0;
        double sumEnSigma = 0;
        for (SimpleEntry<T, T> entry : components) {
            double energy =
                    es.valueEnergy(entry.getKey(), entry.getValue(), ec);
            sumEn += energy;
            sumEnSigma += Math.pow(energy, sigma);
        }
        // now generate the energy quantums
        for (SimpleEntry<T, T> entry : components) {
            double energy =
                    es.valueEnergy(entry.getKey(), entry.getValue(), ec);
            double multiplicativeFactor =
                    (Math.pow(energy, sigma - 1.0) * sumEn) / sumEnSigma;
            EnergyQuantum<T> eq =
                    EnergyQuantum.createMult(entry.getKey(), entry.getValue(),
                            timeSlice, multiplicativeFactor, ec, "Sharpening");
            retval.add(eq);
        }
        return retval;
    }

}
