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
package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Loop;
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.agents.liViBased;
import org.xapagy.agents.liXapiReading;
import org.xapagy.agents.liXapiScheduled;
import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_LOOP_HISTORY implements IQueryHandler {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery gq, Session session) {
        //
        // Identifier block
        //
        String redheader = "History";
        fmt.addH2(redheader, "class=identifier");
        qh_LOOP_HISTORY.listOfHistory(fmt, agent, gq, 0, 15);
        qh_LOOP_HISTORY.listOfReadings(fmt, agent, gq, 0, 15);
        qh_LOOP_HISTORY.listOfExternals(fmt, agent, gq, 0, 15);
    }

    /**
     * A list of future readings
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param location
     * @param size
     * @return
     */
    public static String listOfExternals(IXwFormatter fmt, Agent agent,
            RESTQuery gq, int location, int size) {
        fmt.addH3("future external sensing - " + location + " to "
                + (location + size));
        Loop loop = agent.getLoop();
        List<liXapiScheduled> list = loop.getScheduled();
        List<liXapiScheduled> toShow = new ArrayList<>();
        toShow.addAll(list.subList(location, Math.min(list.size(), size)));
        for (AbstractLoopItem li : toShow) {
            fmt.openP();
            String color = "black";
            PwQueryLinks.linkToLoopItem(fmt, agent, gq, li, "style=\"color: "
                    + color + ";\"");
            fmt.closeP();
        }
        return fmt.toString();
    }

    /**
     * Creates a list of items from the history...
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param location
     *            - the listing ends at now - location items
     * @param size
     *            - the lenght of the listing
     * @return
     */
    public static String listOfHistory(PwFormatter fmt, Agent agent,
            RESTQuery gq, int location, int size) {
        Loop loop = agent.getLoop();
        fmt.addH3("history of VIs from now - " + (location + size)
                + " to now -" + location);
        List<AbstractLoopItem> list = loop.getHistory();
        List<AbstractLoopItem> toShow = new ArrayList<>();
        toShow.addAll(list.subList(Math.max(0, list.size() - location - size),
                list.size() - location));
        for (AbstractLoopItem li : toShow) {
            fmt.openP();
            String color = "black";
            if (li instanceof liXapiReading) {
                color = "black";            	
            }
            if (li instanceof liXapiScheduled) {
            	color = "yellow";
            }
            if (li instanceof liHlsChoiceBased) {
                color = "green";
            }
            if (li instanceof liViBased) {
                color = "red";
            }
            PwQueryLinks.linkToLoopItem(fmt, agent, gq, li, "style=\"color: "
                    + color + ";\"");
            fmt.closeP();
        }
        if (loop.getInExecution() == null) {
            fmt.addLabelParagraph("No item is in execution.");
        } else {
            String color = "red";
            fmt.openP();
            PwQueryLinks.linkToLoopItem(fmt, agent, gq, loop.getInExecution(),
                    "style=\"color: " + color + ";\"");
            fmt.closeP();
        }
        return fmt.toString();
    }

    /**
     * A list of future readings
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param location
     * @param size
     * @return
     */
    public static String listOfReadings(IXwFormatter fmt, Agent agent,
            RESTQuery gq, int location, int size) {
        fmt.addH3("future readings - " + location + " to " + (location + size));
        Loop loop = agent.getLoop();
        List<AbstractLoopItem> list = loop.getReadings();
        List<AbstractLoopItem> toShow = new ArrayList<>();
        toShow.addAll(list.subList(location, Math.min(list.size(), size)));
        for (AbstractLoopItem li : toShow) {
            fmt.openP();
            String color = "black";
            PwQueryLinks.linkToLoopItem(fmt, agent, gq, li, "style=\"color: "
                    + color + ";\"");
            fmt.closeP();
        }
        return fmt.toString();
    }

}
