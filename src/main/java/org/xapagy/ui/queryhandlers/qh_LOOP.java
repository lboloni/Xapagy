package org.xapagy.ui.queryhandlers;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Loop;
import org.xapagy.agents.liXapiScheduled;
import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_LOOP implements IQueryHandler {

    
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery gq, Session session) {
        int countHideable = 1;
        Loop loop = agent.getLoop();
        //
        // Identifier block
        //
        String redheader = "Loop";
        fmt.addH2(redheader, "class=identifier");
        if (loop.getInExecution() == null) {
            fmt.addLabelParagraph("No item is in execution.");
        } else {
            fmt.addLabelParagraph(
                    "Currently executing: ",
                    PwQueryLinks.linkToLoopItem(new PwFormatter(), agent, gq,
                            loop.getInExecution()));
        }
        fmt.addLabelParagraph("History: ", "" + loop.getHistory().size());
        fmt.addLabelParagraph("Next readings: ", "" + loop.getReadings().size());
        fmt.addLabelParagraph("Scheduled events: ", ""
                + loop.getScheduled().size());
        fmt.addLabelParagraph("Pending summaries: ", ""
                + loop.getSummaries().size());
        //
        // Scheduled readings
        //
        String tmp = qh_LOOP.listOfLoopItemLinks(agent, gq, loop.getReadings());
        fmt.addExtensibleH2("id" + countHideable++, "Scheduled readings: "
                + loop.getReadings().size(), tmp, true);
        //
        // Scheduled time items
        //
        tmp = qh_LOOP.listOfLiScheduled(agent, gq, loop.getScheduled());
        fmt.addExtensibleH2("id" + countHideable++, "Scheduled time items: "
                + loop.getScheduled().size(), tmp, true);
        //
        // Summaries
        //
        tmp = qh_LOOP.listOfLoopItemLinks(agent, gq, loop.getSummaries());
        fmt.addExtensibleH2("id" + countHideable++, "Pending summaries: "
                + loop.getSummaries().size(), tmp, true);
        //
        // History
        //
        tmp = qh_LOOP.listOfLoopItemLinks(agent, gq, loop.getHistory());
        fmt.addExtensibleH2("id" + countHideable++, "History: "
                + loop.getHistory().size(), tmp, true);
    }

    /**
     * Returns a list of link items
     * 
     * @param agent
     * @param gs
     * @param lis
     * @return
     */
    public static String listOfLoopItemLinks(Agent agent, RESTQuery gq,
            List<AbstractLoopItem> lis) {
        PwFormatter fmt = new PwFormatter("");
        if (lis.isEmpty()) {
            fmt.addP("No loop items in this list.");
        } else {
            for (AbstractLoopItem li : lis) {
                fmt.openP();
                PwQueryLinks.linkToLoopItem(fmt, agent, gq, li);
                fmt.closeP();
            }
        }
        return fmt.toString();
    }


    /**
     * Returns a list of link items
     * 
     * @param agent
     * @param gs
     * @param lis
     * @return
     */
    public static String listOfLiScheduled(Agent agent, RESTQuery gq,
            List<liXapiScheduled> lis) {
        PwFormatter fmt = new PwFormatter("");
        if (lis.isEmpty()) {
            fmt.addP("No loop items in this list.");
        } else {
            for (AbstractLoopItem li : lis) {
                fmt.openP();
                PwQueryLinks.linkToLoopItem(fmt, agent, gq, li);
                fmt.closeP();
            }
        }
        return fmt.toString();
    }

}
