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
package org.xapagy.xapi;

import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.httpserver.WebGui;
import org.xapagy.ui.observers.BreakCondition;
import org.xapagy.ui.observers.BreakObserver;
import org.xapagy.ui.observers.IAgentObserver;

/**
 * @author Ladislau Boloni
 * Created on: Dec 30, 2014
 */
public class Kludges {

    /**
     * Turns on the BreakObserver and ensures that the WebGui is running
     * 
     * @param agent
     */
    public static void kludgeDebugHere(Agent agent) {
        WebGui.startWebGui(agent);
        // find a breakobserver:
        BreakObserver bo = null;
        for (IAgentObserver observer : agent.getObservers()) {
            if (!(observer instanceof BreakObserver)) {
                continue;
            }
            bo = (BreakObserver) observer;
            break;
        }
        // if it is no breakobserver, create one
        if (bo == null) {
            bo = new BreakObserver(false);
            agent.addObserver("BreakObserver", bo);
        }
        BreakCondition bc =
                BreakCondition.generateCountedEvent(
                        DebugEventType.AFTER_LOOP_ITEM_EXECUTION, 1);
        bo.addBreakCondition(bc);
    }
   


}
