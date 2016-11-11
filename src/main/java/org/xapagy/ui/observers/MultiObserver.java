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

/**
 * Allows us to put multiple observers in a single one
 * 
 * @author Ladislau Boloni
 * Created on: Sep 2, 2013
 */
public class MultiObserver implements IAgentObserver {

    private static final long serialVersionUID = 8666429968143606087L;
    public List<IAgentObserver> observers = new ArrayList<>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.IAgentObserver#addObserveWhat(org.xapagy.debug
     * .DebugEvent.DebugEventType)
     */
    @Override
    public void addObserveWhat(DebugEventType eventType) {
        // not relevant here, will be determined at the component level
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.AbstractAgentObserver#observe2(org.xapagy.debug
     * .DebugEvent)
     */
    @Override
    public void observe(DebugEvent event) throws IOException,
            InterruptedException {
        for (IAgentObserver observer : observers) {
            observer.observe(event);
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
        // not relevant here, will be determined at the component level
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.IAgentObserver#setAgent(org.xapagy.agents.Agent)
     */
    @Override
    public void setAgent(Agent agent) {
        // not relevant here, will be determined at the component level
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.observers.IAgentObserver#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        // not relevant here, will be determined at the component level
    }

}
