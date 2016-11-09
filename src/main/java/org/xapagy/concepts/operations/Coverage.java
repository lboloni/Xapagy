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

import org.xapagy.agents.Calibration;
import org.xapagy.concepts.AbstractConcept;
import org.xapagy.concepts.Overlay;

/**
 * Contains various metrics of similarity between concept overlays
 * 
 * @author Ladislau Boloni
 * Created on: Jan 30, 2013
 */
public class Coverage {

    /**
     * Returns true if two overlays are "the same for most purposes"
     * 
     * For instance, used to to verify when S-ADJ types map to the same HLS
     * (ViSimilarityHelper).
     * 
     * @param vo1
     * @param vo2
     * @param agent
     * @return
     */
    public static <T extends Overlay<P>, P extends AbstractConcept> boolean
            decideSimilarity(T co1, T co2) {
        double coverage_forward = Coverage.scoreCoverage(co2, co1);
        double coverage_backward = Coverage.scoreCoverage(co1, co2);
        double coverage_min = Math.min(coverage_backward, coverage_forward);
        return coverage_min > Calibration.decideSimilarity;
    }

    /**
     * Scores the degree at which ovrReference is covered by the ovrSource
     * 
     * The returned value is between 0 (no overlap) and 1 (everything in the 
     * reference is also in the source)
     * 
     * Typically used for the matching between instance shadows and other
     * shadows.
     * 
     * @param ovrSource
     *            (the shadow, or the referred entity)
     * @param ovrReference
     *            (the focus or the reference)
     * 
     * @return
     */
    public static <T extends Overlay<P>, P extends AbstractConcept> double
            scoreCoverage(T ovrSource, T ovrReference) {
        double overlapSum = ovrReference.overlapEnergy(ovrSource);
        if (overlapSum == 0.0) {
            return 0.0;
        }
        double scale = ovrReference.getTotalEnergy();
        double retval = overlapSum / scale;
        return retval;
    }
}
