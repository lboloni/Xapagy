package org.xapagy.ui.prettyhtml;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.HtmlFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.observers.BreakObserver;
import org.xapagy.ui.observers.IAgentObserver;

public class PwBreakObserver {

    /**
     * Generate a small mark about where are we
     * 
     * @param fmt
     * @param agent
     * @param concept
     * @param query
     */
    public static void generate(HtmlFormatter fmt, Agent agent, RESTQuery query) {
        BreakObserver bo = null;
        for (IAgentObserver observer : agent.getObservers()) {
            if (observer instanceof BreakObserver) {
                bo = (BreakObserver) observer;
                break;
            }
        }
        if (bo == null) {
            return;
        }
        List<DebugEvent> list = bo.getHistory();
        list = list.subList(Math.max(0, list.size() - 12), list.size());
        StringBuffer buff = new StringBuffer();
        for (DebugEvent de : list) {
            switch (de.getEventType()) {
            case BEFORE_LOOP_ITEM_EXECUTION: {
                buff.append("<");
                break;
            }
            case AFTER_DA_CHUNK: {
                buff.append(".");
                break;
            }
            case AFTER_INSTANCE_RESOLUTION: {
                buff.append("i");
                break;
            }
            case AFTER_LOOP_ITEM_EXECUTION: {
                buff.append(">");
                break;
            }
            case AFTER_RECALL: {
                buff.append("R");
                break;
            }
            case BEFORE_DA_STEP: {
                buff.append(">");
                break;
            }
            case RESOLVE_SURPRISE: {
                buff.append("s");
                break;
            }
            }
        }
        fmt.addP(buff.toString(), "class=" + PwFormatter.CLASS_NAVBAR);
    }

}
