/*
   This file is part of the Xapagy project
   Created on: Sep 14, 2011
 
   org.xapagy.ui.prettyprint.PpLoopItem
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;

/**
 * Pretty printing the loop item
 * 
 * @author Ladislau Boloni
 * 
 */
public class PpLoopItem {

    /**
     * Try to summarize in one line the essence of the loop item
     * 
     * @param li
     * @param agent
     * @return
     */
    public static String ppConcise(LoopItem li, Agent agent) {
        StringBuffer buf = new StringBuffer();
        // prefix: executed / not executed
        switch (li.getState()) {
        case EXECUTED:
            buf.append("X:" + Formatter.fmt(li.getExecutionTime()) + " ");
            break;
        case NOT_EXECUTED:
            break;
        }
        // /
        switch (li.getType()) {
        case EXTERNAL: {
            buf.append("External: ");
            break;
        }
        case INTERNAL: {
            buf.append("Internal: ");
            buf.append(PrettyPrint.ppConcise(li.getChoice(), agent));
            break;
        }
        case READING: {
            buf.append("Reading: " + li.getText());
            break;
        }
        }
        return buf.toString();
    }

    public static String ppDetailed(LoopItem li, Agent agent) {
        Formatter fmt = new Formatter();
        switch (li.getType()) {
        case EXTERNAL: {
            fmt.add("LoopItem - External");
            fmt.indent();
            fmt.is("scheduled time", li.getScheduledExecutionTime());
            break;
        }
        case INTERNAL: {
            fmt.add("LoopItem - Internal");
            fmt.indent();
            fmt.add(PrettyPrint.ppDetailed(li.getChoice(), agent));
            break;
        }
        case READING: {
            String header = "LoopItem - Reading";
            if (li.getFileName() != null) {
                header =
                        header + " (" + li.getFileName() + ":"
                                + li.getFileLineNo() + ")";
            } else {
                header = header + "(directly added)";
            }
            fmt.add(header);
            fmt.add(li.getText());
            fmt.indent();
            break;
        }
        }
        switch (li.getState()) {
        case EXECUTED:
            fmt.add("STATE: EXECUTED");
            fmt.is("Execution time", li.getExecutionTime());
            fmt.addIndented(PpListPartial.ppListPartial(
                    li.getExecutionResult(), agent, PrintDetail.DTL_CONCISE,
                    100));
            break;
        case NOT_EXECUTED:
            fmt.add("STATE: not executed");
            break;
        }
        return fmt.toString();
    }

}
