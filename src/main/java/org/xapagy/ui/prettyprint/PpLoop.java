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
import org.xapagy.agents.Loop;
import org.xapagy.ui.formatters.Formatter;

/**
 * 
 * Pretty prints the loop with various filterings etc
 * 
 * @author Ladislau Boloni
 * Created on: Sep 14, 2011
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
