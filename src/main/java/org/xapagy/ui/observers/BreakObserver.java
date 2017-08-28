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
package org.xapagy.ui.observers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.TextUiHelper;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: May 27, 2012
 */
public class BreakObserver implements IAgentObserver {

    private static final String CHOICE_FIRST_AFTER_DA_CHUNK =
            "First event: after DA chunk";
    private static final String CHOICE_FIRST_AFTER_INSTANCE_RESOLUTION =
            "First event: after instance resolution";
    private static final String CHOICE_FIRST_AFTER_LOOP_ITEM_EXECUTION =
            "First event: after loop item execution";
    private static final String CHOICE_FIRST_AFTER_RECALL =
            "First event: after recall";
    private static final String CHOICE_FIRST_BEFORE_DA_STEP =
            "First event: before DA step";
    private static final String CHOICE_FIRST_BEFORE_LOOP_ITEM_EXECUTION =
            "First event: before the loop item execution";

    private static final String CHOICE_FIRST_RESOLVE_SURPRISE =
            "First event: resolve surprise";
    private static final String CHOICE_NO_STOP =
            "Do not stop any more, clear all breakpoints.";
    private static final long serialVersionUID = 5786369048058968937L;
    private Agent agent;
    private List<BreakCondition> breakConditions = new ArrayList<>();
    private List<DebugEvent> history = new ArrayList<>();
    private int historyLength = 100;
    private String lastChoice = null;
    /**
     * If set to false, the break can be controlled from the TextUi. If set to
     * true, it is controlled from the web ui
     */
    private boolean webBasedControl = true;

    /**
     * @return the inBreak
     */
    public boolean isInBreak() {
        return inBreak;
    }

    /**
     * If this is true, then we are at a break. This allows us in the web-based
     * steps to wait until the step had been executed
     */
    private boolean inBreak = false;

    /**
     * Constructor: start with a time observer
     */
    public BreakObserver(boolean startWithBreak) {
        if (startWithBreak) {
            BreakCondition bc = BreakCondition.generateTime(0);
            breakConditions.add(bc);
        }
    }

    /**
     * Adds a break condition externally
     * 
     * @param bc
     */
    public void addBreakCondition(BreakCondition bc) {
        breakConditions.add(bc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.IAgentObserver#addObserveWhat(org.xapagy.debug
     * .DebugEvent.DebugEventType)
     */
    @Override
    public void addObserveWhat(DebugEventType eventType) {
        // nothing here
    }

    /**
     * @return the history
     */
    public List<DebugEvent> getHistory() {
        return history;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.observers.IAgentObserver#observe(org.xapagy.debug.
     * DebugEvent )
     */
    @Override
    public void observe(DebugEvent event)
            throws IOException, InterruptedException {
        // update the history and keep it the same length
        history.add(event);
        if (history.size() > historyLength) {
            history.remove(0);
        }
        // allows the system to make changes on the breakConditions from here
        List<BreakCondition> bcCopy = new ArrayList<>();
        bcCopy.addAll(breakConditions);
        for (BreakCondition bc : bcCopy) {
            if (bc.breakConditionSatisfied(event, agent)) {
                // the break condition had been satisfied, stop and allow
                // setting the next one
                setConditions(bc, event);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.IAgentObserver#removeObserveWhat(org.xapagy.debug
     * .DebugEvent.DebugEventType)
     */
    @Override
    public void removeObserveWhat(DebugEventType eventType) {
        // nothing here
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.IAgentObserver#setAgent(org.xapagy.agents.Agent)
     */
    @Override
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    /**
     * An interactive way to set the current break conditions
     * 
     * @param currentBreak
     * @param debugEvent
     */
    private void setConditions(BreakCondition currentBreak,
            DebugEvent debugEvent) {
        if (currentBreak != null) {
            breakConditions.remove(currentBreak);
        }
        if (!breakConditions.isEmpty()) {
            TextUi.print(TextUiHelper
                    .createLabeledSeparator("-Existing break conditions"));
            for (BreakCondition bc : breakConditions) {
                TextUi.println(bc);
            }
        }
        List<String> menuitems = new ArrayList<>();
        menuitems.add(BreakObserver.CHOICE_NO_STOP);
        menuitems.add(BreakObserver.CHOICE_FIRST_BEFORE_LOOP_ITEM_EXECUTION);
        menuitems.add(BreakObserver.CHOICE_FIRST_AFTER_LOOP_ITEM_EXECUTION);
        menuitems.add(BreakObserver.CHOICE_FIRST_AFTER_DA_CHUNK);
        menuitems.add(BreakObserver.CHOICE_FIRST_AFTER_INSTANCE_RESOLUTION);
        menuitems.add(BreakObserver.CHOICE_FIRST_AFTER_RECALL);
        menuitems.add(BreakObserver.CHOICE_FIRST_BEFORE_DA_STEP);
        menuitems.add(BreakObserver.CHOICE_FIRST_RESOLVE_SURPRISE);
        String defaultChoice =
                BreakObserver.CHOICE_FIRST_AFTER_LOOP_ITEM_EXECUTION;
        if (lastChoice != null) {
            defaultChoice = lastChoice;
        }
        //
        // This is where we can select between the TextUi or the other block!!!
        //
        String choice = null;
        if (webBasedControl) {
            // if we are in a web-based control, wait until the UI tells us that
            // we should proceed by calling notify on this
            TextUi.println("Stop, advance it from the web ui!");
            inBreak = true;
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            inBreak = false;
            TextUi.println("Ok, proceeding!");
            choice = defaultChoice;
        } else {
            choice = TextUi.menu(menuitems, defaultChoice,
                    "Choose where to stop next:");
        }
        switch (choice) {
        case CHOICE_FIRST_BEFORE_LOOP_ITEM_EXECUTION: {
            BreakCondition bc = BreakCondition.generateCountedEvent(
                    DebugEventType.BEFORE_LOOP_ITEM_EXECUTION, 1);
            breakConditions.add(bc);
            break;
        }
        case CHOICE_FIRST_AFTER_DA_CHUNK: {
            BreakCondition bc = BreakCondition
                    .generateCountedEvent(DebugEventType.AFTER_DA_CHUNK, 1);
            breakConditions.add(bc);
            break;
        }
        case CHOICE_FIRST_AFTER_INSTANCE_RESOLUTION: {
            BreakCondition bc = BreakCondition.generateCountedEvent(
                    DebugEventType.AFTER_INSTANCE_RESOLUTION, 1);
            breakConditions.add(bc);
            break;
        }
        case CHOICE_FIRST_AFTER_LOOP_ITEM_EXECUTION: {
            BreakCondition bc = BreakCondition.generateCountedEvent(
                    DebugEventType.AFTER_LOOP_ITEM_EXECUTION, 1);
            breakConditions.add(bc);
            break;
        }
        case CHOICE_FIRST_AFTER_RECALL: {
            BreakCondition bc = BreakCondition
                    .generateCountedEvent(DebugEventType.AFTER_RECALL, 1);
            breakConditions.add(bc);
            break;
        }
        case CHOICE_FIRST_BEFORE_DA_STEP: {
            BreakCondition bc = BreakCondition
                    .generateCountedEvent(DebugEventType.BEFORE_DA_STEP, 1);
            breakConditions.add(bc);
            break;
        }
        case CHOICE_FIRST_RESOLVE_SURPRISE: {
            BreakCondition bc = BreakCondition
                    .generateCountedEvent(DebugEventType.RESOLVE_SURPRISE, 1);
            breakConditions.add(bc);
            break;
        }
        case CHOICE_NO_STOP: {
            breakConditions.clear();
            break;
        }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.observers.IAgentObserver#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        // TODO Auto-generated method stub
    }
    
    /**
     * Allows us to print something intelligent during debugging
     */
    public String toString() {
    	Formatter fmt = new Formatter();
    	fmt.add("BreakObserver");
    	fmt.indent();
    	fmt.is("isInBreak", inBreak);
    	fmt.is("agent.getTime()", agent.getTime());
    	return fmt.toString();
    }
    
}
