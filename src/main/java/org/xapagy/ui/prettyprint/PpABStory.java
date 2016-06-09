/*
   This file is part of the Xapagy project
   Created on: Sep 24, 2012
 
   org.xapagy.ui.prettyprint.PpABStory
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.autobiography.ABStory;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpABStory {

    public static String pp(ABStory abs, Agent agent, PrintDetail detailLevel) {
        if (detailLevel == PrintDetail.DTL_DETAIL) {
            return PpABStory.ppDetailed(abs, agent);
        }
        if (detailLevel == PrintDetail.DTL_CONCISE) {
            return PpABStory.ppConcise(abs, agent);
        }
        throw new Error("Unsupported detailLevel" + detailLevel);
    }

    public static String ppConcise(ABStory abs, Agent agent) {
        return abs.toString();
    }

    /**
     * Detailed printing, lists the
     * 
     * @param choice
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String ppDetailed(ABStory abs, Agent agent) {
        Formatter fmt = new Formatter();
        for (int i = 0; i != abs.length(); i++) {
            String temp = Formatter.padTo(i, 5) + " " + abs.getLine(i);
            fmt.add(temp);
        }
        return fmt.toString();
    }

}
