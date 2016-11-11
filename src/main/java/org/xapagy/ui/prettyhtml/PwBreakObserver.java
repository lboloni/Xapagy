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
