package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Loop;
import org.xapagy.agents.LoopItem;
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
        List<LoopItem> list = loop.getScheduled();
        List<LoopItem> toShow = new ArrayList<>();
        toShow.addAll(list.subList(location, Math.min(list.size(), size)));
        for (LoopItem li : toShow) {
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
        List<LoopItem> list = loop.getHistory();
        List<LoopItem> toShow = new ArrayList<>();
        toShow.addAll(list.subList(Math.max(0, list.size() - location - size),
                list.size() - location));
        for (LoopItem li : toShow) {
            fmt.openP();
            String color = "black";
            switch (li.getType()) {
            case READING:
                color = "black";
                break;
            case EXTERNAL:
                color = "yellow";
                break;
            case INTERNAL:
                color = "green";
                break;
            case FORCED:
                color = "red";
                break;
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
        List<LoopItem> list = loop.getReadings();
        List<LoopItem> toShow = new ArrayList<>();
        toShow.addAll(list.subList(location, Math.min(list.size(), size)));
        for (LoopItem li : toShow) {
            fmt.openP();
            String color = "black";
            PwQueryLinks.linkToLoopItem(fmt, agent, gq, li, "style=\"color: "
                    + color + ";\"");
            fmt.closeP();
        }
        return fmt.toString();
    }

}
