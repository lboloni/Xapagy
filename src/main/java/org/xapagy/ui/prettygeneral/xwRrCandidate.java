/*
   This file is part of the Xapagy project
   Created on: Apr 15, 2014
 
   org.xapagy.ui.prettygeneral.xwRrCandidate
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
