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
package org.xapagy.reference;

import org.xapagy.concepts.VerbOverlay;
import org.xapagy.concepts.operations.Coverage;
import org.xapagy.concepts.operations.Incompatibility;

/**
 * Ok, in the old reference there was a full support for VI references. That is
 * not really the case, there is still an issue of matching relations. This
 * stuff had been moved here, separately from the reference resolution
 * 
 * @author Ladislau Boloni
 * Created on: Jan 22, 2014
 */
public class rrVoCompatibility {

    /**
     * A naive way to define compatibility of VOs
     * 
     * @param verbs
     * @param voRelation
     * @return
     */
    public static boolean areCompatible(VerbOverlay voSource,
            VerbOverlay voReference) {
        double scoreSimilarity = Coverage.scoreCoverage(voSource, voReference);
        double scoreIncompatibility =
                Incompatibility.scoreIncompatibility(voSource, voReference);
        double overallScore = 10 * scoreSimilarity - 100 * scoreIncompatibility;
        boolean retval = overallScore > 0;
        return retval;
    }

}
