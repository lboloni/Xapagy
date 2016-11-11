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
package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.reference.rrState;

/**
 * @author Ladislau Boloni
 * Created on: Dec 26, 2011
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
