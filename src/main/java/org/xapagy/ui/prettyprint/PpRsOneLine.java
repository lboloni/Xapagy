/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2013
 
   org.xapagy.ui.prettyprint.PpRsOneLine
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;
import org.xapagy.debug.storygenerator.RsOneLine;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpRsOneLine {

    /**
     * Fall back on detailed
     * 
     * @param rsol
     * @param agent
     * @return
     */
    public static String ppConcise(RsOneLine rsol, Agent agent) {
        return PpRsOneLine.ppDetailed(rsol, agent);
    }

    public static String ppDetailed(RsOneLine rsol, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("RsOneLine");
        fmt.indent();
        fmt.add("" + rsol.getLineNo() + ":"
                + rsol.getAbStory().getLine(rsol.getLineNo()));
        fmt.add("LoopItems");
        fmt.indent();
        for (LoopItem li : rsol.getLoopItems()) {
            fmt.add(PpLoopItem.ppDetailed(li, agent));
        }
        fmt.deindent();
        fmt.deindent();
        return fmt.toString();
    }

}
