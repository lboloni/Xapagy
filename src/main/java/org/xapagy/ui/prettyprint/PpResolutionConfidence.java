/*
   This file is part of the Xapagy project
   Created on: Dec 26, 2011
 
   org.xapagy.ui.prettyprint.PpResolutionConfidence
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.reference.rrState;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpResolutionConfidence {

    /**
     * Concise printing: fall back on detailed
     * 
     * @param rc
     * @param agent
     * @return
     */
    public static String ppConcise(rrState rc, Agent agent) {
        return PpResolutionConfidence.ppDetailed(rc, agent);
    }

    public static String ppDetailed(rrState rc, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("ResolutionConfidence " + rc.getJustification() + " - "
                + rc.getJustification());
        if (rc.getPhase().equals(rrState.rrJustification.UNDETERMINED)) {
            // nothing else can be done
            return fmt.toString();
        }
        fmt.add("Scores:");
        fmt.indent();
        fmt.is("overall", rc.getOverallScore());
        fmt.is("strength", rc.getScoreBias());
        fmt.is("incompatibility", rc.getScoreIncompatibility());
        fmt.is("similarity", rc.getScoreSimilarity());
        return fmt.toString();
    }

}
