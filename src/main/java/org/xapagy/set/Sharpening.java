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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.instances.XapagyComponent;

/**
 * Functions for the implementation of sharpening, understood as a re-balancing
 * of energies.
 * 
 * @author Ladislau Boloni
 * Created on: May 13, 2014
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
