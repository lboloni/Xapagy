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
import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;

/**
 * @author Ladislau Boloni
 * Created on: Oct 1, 2010
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
