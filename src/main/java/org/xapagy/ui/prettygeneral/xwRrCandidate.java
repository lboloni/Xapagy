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
package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.reference.rrCandidate;
import org.xapagy.reference.rrState;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * General formatter printing of the rrCandidate object
 * 
 * @author Ladislau Boloni
 * Created on: Apr 15, 2014
 */
public class xwRrCandidate {

    /**
     * Prints a detailed description of the candidate
     * 
     * @param xw
     * @param rrc
     * @param agent
     * @return
     */
    public static String xwDetailed(IXwFormatter xw, rrCandidate rrc,
            Agent agent) {
        xw.addLabelParagraph("RrCandidate");
        xw.indent();
        xw.is("assignedScore", rrc.getAssignedScore());
        xw.is("instance", xwInstance.xwConcise(xw.getEmpty(), rrc.getInstance(), agent));
        xw.addLabelParagraph("rrc (the rrContext)");
        xw.indent();
        xwRrContext.xwDetailed(xw, rrc.getRrc(), agent);
        xw.deindent();
        rrState rrs = rrc.getState();
        if (rrs != null) {
            xw.addLabelParagraph("state (the rrState)");
            xw.indent();
            xwRrState.xwDetailed(xw, rrs, agent);
            xw.deindent();
        } else {
            xw.addLabelParagraph("state (the rrState) = <null>");
        }
        xw.deindent();
        return xw.toString();
    }
}
