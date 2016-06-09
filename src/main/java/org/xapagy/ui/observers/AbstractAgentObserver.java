/*
   This file is part of the Xapagy project
   Created on: Oct 1, 2010
 
   org.xapagy.ui.observers.AbstractAgentObserver
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.ui.observers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;

/**
 * @author Ladislau Boloni
 * 
 */
public abstract class AbstractAgentObserver implements IAgentObserver {

    public enum TraceWhat {
        CHOICES_DYNAMIC, CHOICES_NATIVE, COMPACT, HLS_NEW_INSTANCES,
        VERBALIZATION
    }

    /**
     * 
     */
    private static final long serialVersionUID = 5565264273394620870L;
    protected Agent agent;
    protected boolean enabled = true;
    protected Set<DebugEventType> observeWhat = new HashSet<>();

    public AbstractAgentObserver() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.IAgentObserver#addObservationAt(org.xapagy.debug
     * .DebugEvent.DebugEventType)
     */
    @Override
    public void addObserveWhat(DebugEventType eventType) {
        observeWhat.add(eventType);
    }

    /**
     * Filters the observations
     */
    @Override
    public final void observe(DebugEvent event) throws IOException,
            InterruptedException {
        if (!enabled) {
            return;
        }
        if (!observeWhat.contains(event.getEventType())) {
            return;
        }
        observeInner(event);
    }

    /**
     * This is the function which needs to be implemented by the observers
     * 
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract void observeInner(DebugEvent event) throws IOException,
            InterruptedException;

    @Override
    public void removeObserveWhat(DebugEventType eventType) {
        observeWhat.remove(eventType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.IAgentObserver#setAgent(org.xapagy.model.Agent)
     */
    @Override
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
