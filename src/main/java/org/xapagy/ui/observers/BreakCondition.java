/*
   This file is part of the Xapagy project
   Created on: May 27, 2012

   org.xapagy.ui.observers.BreakCondition

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.observers;

import org.xapagy.agents.Agent;
import org.xapagy.agents.LoopItem;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * This class represents the break condition in the BreakObserver
 *
 * @author Ladislau Boloni
 *
 */
public class BreakCondition {

    public enum BreakConditionType {
        COUNTED_EVENT, TIME, XAPI_STRING
    };

    /**
     * Factory method for generating a counted event break condition
     *
     * @return
     */
    public static BreakCondition
            generateCountedEvent(DebugEventType debugEventType, int initCount) {
        BreakCondition retval = new BreakCondition();
        retval.breakConditionType = BreakConditionType.COUNTED_EVENT;
        retval.debugEventType = debugEventType;
        retval.initCount = initCount;
        retval.currentCount = 0;
        return retval;
    }

    /**
     * Factory method for generating a time event break condition
     *
     * @return
     */
    public static BreakCondition generateString(String string) {
        BreakCondition retval = new BreakCondition();
        retval.breakConditionType = BreakConditionType.XAPI_STRING;
        retval.xapiString = string;
        return retval;
    }

    /**
     * Factory method for generating a time event break condition
     *
     * @param time
     * @return
     */
    public static BreakCondition generateTime(double time) {
        BreakCondition retval = new BreakCondition();
        retval.breakConditionType = BreakConditionType.TIME;
        retval.time = time;
        return retval;
    }

    private BreakConditionType breakConditionType;
    private int currentCount;
    private DebugEventType debugEventType;
    private int initCount;
    private double time;
    private String xapiString;

    /**
     * Verifies if this particular break condition had been satisfied
     *
     * @param event
     * @param agent
     * @return
     */
    public boolean breakConditionSatisfied(DebugEvent event, Agent agent) {
        switch (breakConditionType) {
        case COUNTED_EVENT: {
            if (event.getEventType() != debugEventType) {
                return false;
            }
            currentCount++;
            if (currentCount >= initCount) {
                TextUi.println("\nStopped at counted event" + event.toString()
                        + "time: " + agent.getTime() + "loopItem "
                        + agent.getLoop().getInExecution().toString());
                return true;
            }
            return false;
        }
        case TIME: {
            if (agent.getTime() >= time) {
                return true;
            } else {
                return false;
            }
        }
        case XAPI_STRING: {
            LoopItem li = agent.getLoop().getInExecution();
            if (li == null) {
                return false;
            }
            String pp = PrettyPrint.ppConcise(li, agent);
            return pp.contains(xapiString);
        }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BreakCondition [breakConditionType=" + breakConditionType
                + ", currentCount=" + currentCount + ", debugEventType="
                + debugEventType + ", initCount=" + initCount + ", time=" + time
                + ", xapiString=" + xapiString + "]";
    }

}