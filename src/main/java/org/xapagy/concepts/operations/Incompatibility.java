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
package org.xapagy.concepts.operations;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Calibration;
import org.xapagy.concepts.AbstractConcept;
import org.xapagy.concepts.Overlay;
import org.xapagy.ui.TextUi;

/**
 * The helper functions in this class decide about the incompatibility between a
 * source overlay and a reference overlay.
 * 
 * In general, these are incompatible, if the reference overlay cannot be
 * "merged" into the source overlay. There can be two reasons for this:
 * 
 * <ul>
 * <li>there a negative impact between the source and the reference (this
 * happens if you add a negation of an existing concept)
 * <li>there is an overflow - this happens, for instance, if we add mutually
 * exclusive category members
 * </ul>
 * 
 * @author Ladislau Boloni
 * Created on: Jan 29, 2013
 */
public class Incompatibility {

    /**
     * A version of the incompatibility metric which is thresholded for a
     * decision (currently at 0.5).
     * 
     * @param coSource
     * @param coReference
     * @return
     */
    public static <T extends Overlay<P>, P extends AbstractConcept> boolean
            decideIncompatibility(T coSource, T coReference) {
        double value =
                Incompatibility.scoreIncompatibility(coSource, coReference);
        return value > Calibration.decideIncompatibility; //
    }

    /**
     * The incompatibility components.
     * 
     * <ul>
     * <li>the negative impacts (if any)
     * <li>the overimpact
     * </ul>
     * 
     * @param <P>
     * @param ovrSource
     *            - the source (can be unresolved)
     * @param ovrReference
     *            - the reference (normally unresolved)
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Overlay<P>, P extends AbstractConcept>
            SimpleEntry<Double, Double> incompatibilityComponents(T ovrSource,
                    T ovrReference) {
        //
        // first, resolve the impacts of the source on itself
        //
        T ovrResolvedSource = (T) ovrSource.newOverlay();
        ovrResolvedSource.addOverlayImpacted(ovrSource);
        //
        // the surplus of the reference over the source, as we are looking for
        // incompatibility, remove the commonn stuff
        //
        // old: T ovrReferenceSurplus = (T)
        // ovrReference.subtract(ovrUnresolvedSource);
        T ovrReferenceSurplus = (T) ovrReference.subtract(ovrResolvedSource);
        //
        // perform the impact of the surplus over the reference
        //
        T collectOverImpact = (T) ovrResolvedSource.newOverlay();
        SimpleEntry<Double, Double> impactEnergy =
                ovrResolvedSource.impact(ovrReferenceSurplus, 1.0, true,
                        collectOverImpact);
        //
        // the negative impact energy is the amount of implied negations between
        // the
        // resolved source and the reference - its maximum amount is the energy
        // of the resolved reference
        //
        double negativeImpactEnergy = 0;
        if (impactEnergy.getValue() < 0.0) {
            negativeImpactEnergy = -impactEnergy.getValue();
        }
        //
        // the impact energy is the amount at which the reference surplus
        // would add more energy to the concepts of the source than they can
        // hold.
        // The maximum value is, again, the is the energy of the resolved
        // reference
        //
        double overImpactEnergy = collectOverImpact.getTotalEnergy();
        SimpleEntry<Double, Double> entry =
                new SimpleEntry<>(negativeImpactEnergy, overImpactEnergy);
        return entry;
    }

    /**
     * 
     * Takes the incompatibility components, and assembled them into a score.
     * 
     * This will be thresholded in the decision, but it will be used as it is in
     * the scoring of the reference
     * 
     * @param coOriginal
     * @param coAdd
     * @param agent
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Overlay<P>, P extends AbstractConcept> double
            scoreIncompatibility(T ovrSource, T ovrReference) {
        SimpleEntry<Double, Double> entry =
                Incompatibility.incompatibilityComponents(ovrSource,
                        ovrReference);
        double negativeImpactEnergy = entry.getKey();
        double overImpactEnergy = entry.getValue();
        double incompatibleEnergy = negativeImpactEnergy + overImpactEnergy;
        //
        // Scaling factor: either of them are supposed to be smaller than the
        // resolved reference
        //
        T ovrResolvedReference = (T) ovrReference.newOverlay();
        ovrResolvedReference.addOverlayImpacted(ovrReference);
        double scalingFactor = ovrResolvedReference.getTotalEnergy();
        // old: double scalingFactor = ovrReference.getTotalEnergy();

        // now let us scale this value
        double retval = incompatibleEnergy / scalingFactor;
        if (retval > 1.0) {
            // val2 = 1.0;
            TextUi.abort("scoreIncompatibitity is not supposed to be larger than 1.0 it is "
                    + retval);
        }
        return retval;
    }

}
