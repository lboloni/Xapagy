/*
   This file is part of the Xapagy project
   Created on: Apr 15, 2014
 
   org.xapagy.ui.prettygeneral.xwRrState
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.reference.rrState;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class xwRrState {

    public static String pwDetailed(IXwFormatter xwf, rrState rrs, Agent agent) {
        xwf.addLabelParagraph("RrState");
        xwf.indent();
        xwf.is("compositionParent", rrs.getCompositionParent());
        xwf.is("phase", rrs.getPhase());
        xwf.is("justification", rrs.getJustification());
        xwf.is("scoreIncompatibility", rrs.getScoreIncompatibility());
        xwf.is("scoreSimilarity", rrs.getScoreSimilarity());
        xwf.is("scoreBias", rrs.getScoreBias());
        xwf.is("overallScore (calculated)", rrs.getOverallScore());
        xwf.deindent();
        return xwf.toString();
    }
}
