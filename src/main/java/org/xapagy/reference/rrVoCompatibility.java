/*
   This file is part of the Xapagy project
   Created on: Jan 22, 2014
 
   org.xapagy.reference.rrVerbReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
