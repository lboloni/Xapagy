/*
   This file is part of the Xapagy project
   Created on: Sep 14, 2011
 
   org.xapagy.ui.prettyprint.PpLoop
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Loop;

/**
 * 
 * Pretty prints the loop with various filterings etc
 * 
 * @author Ladislau Boloni
 * 
 */
public class PpLoop {

    /**
     * Concise printing of the loop: only the numbers
     * 
     * @param loop
     * @param agent
     * @return
     */
    public static String ppConcise(Loop loop, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("Loop at " + Formatter.fmt(agent.getTime()));
        fmt.indent();
        if (loop.getInExecution() == null) {
            fmt.add("None in execution.");
        } else {
            fmt.add("One loop item currently in execution");
        }
        fmt.is("Readings left:", loop.getReadings().size());
        fmt.is("Scheduled left:", loop.getScheduled().size());
        fmt.is("Executed items history:", loop.getHistory().size());
        return fmt.toString();
    }

    /**
     * Detailed printing of the loop. Falls back on ppDetailed with numbers
     * 
     * @param loop
     * @param agent
     * @return
     */
    public static String ppDetailed(Loop loop, Agent agent) {
        return PpLoop.ppDetailedWithNumbers(loop, agent, 3, 3, 3,
                PrintDetail.DTL_DETAIL);
    }

    /**
     * Detailed printing of the loop, with a limited number of each of the item
     * types printed
     * 
     * @param loop
     * @param agent
     * @param countReadings
     * @param countScheduled
     * @param countHistory
     * @return
     */
    public static String ppDetailedWithNumbers(Loop loop, Agent agent,
            int countReadings, int countScheduled, int countHistory,
            PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        fmt.add("Loop (detailed printing)");
        if (loop.getInExecution() == null) {
            fmt.add("None in execution.");
        } else {
            fmt.add("One loop item currently in execution:");
            fmt.addIndented(PrettyPrint.pp(loop.getInExecution(), agent,
                    detailLevel));
        }
        fmt.is("Readings left:", loop.getReadings().size());
        fmt.addIndented(PpListPartial.ppListPartial(loop.getReadings(), agent,
                detailLevel, countReadings));
        fmt.is("Scheduled left:", loop.getScheduled().size());
        fmt.addIndented(PpListPartial.ppListPartial(loop.getScheduled(), agent,
                detailLevel, countScheduled));
        fmt.is("Executed items history:", loop.getHistory().size());
        fmt.addIndented(PpListPartial.ppListPartial(loop.getScheduled(), agent,
                detailLevel, countHistory));
        return fmt.toString();
    }

}
