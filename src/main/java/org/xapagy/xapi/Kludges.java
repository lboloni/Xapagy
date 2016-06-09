/*
   This file is part of the Xapagy project
   Created on: Dec 30, 2014
 
   org.xapagy.xapi.Kludges
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 *
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
