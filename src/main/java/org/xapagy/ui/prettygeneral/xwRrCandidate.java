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
import org.xapagy.ui.prettyprint.PpInstance;

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
     * @param xwf
     * @param rrc
     * @param agent
     * @return
     */
    public static String xwDetailed(IXwFormatter xwf, rrCandidate rrc,
            Agent agent) {
        xwf.addLabelParagraph("RrCandidate");
        xwf.indent();
        xwf.is("assignedScore", rrc.getAssignedScore());
        // FIXME: this should be done instead with xwDetailed
        xwf.is("instance", PpInstance.ppConcise(rrc.getInstance(), agent));
        xwf.addLabelParagraph("rrc (the rrContext)");
        xwf.indent();
        xwRrContext.xwDetailed(xwf, rrc.getRrc(), agent);
        xwf.deindent();
        rrState rrs = rrc.getState();
        if (rrs != null) {
            xwf.addLabelParagraph("state (the rrState)");
            xwf.indent();
            xwRrState.xwDetailed(xwf, rrs, agent);
            xwf.deindent();
        } else {
            xwf.addLabelParagraph("state (the rrState) = <null>");
        }
        xwf.deindent();
        return xwf.toString();
    }
}
