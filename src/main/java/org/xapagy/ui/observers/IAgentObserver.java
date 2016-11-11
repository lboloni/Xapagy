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
import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;

/**
 * The interface which needs to be implemented by the observers
 * 
 * @author Lotzi Boloni
 * 
 */
public interface IAgentObserver extends Serializable {
    void addObserveWhat(DebugEventType eventType);

    void observe(DebugEvent event) throws IOException, InterruptedException;

    void removeObserveWhat(DebugEventType eventType);

    void setAgent(Agent agent);

    void setEnabled(boolean enabled);
}
