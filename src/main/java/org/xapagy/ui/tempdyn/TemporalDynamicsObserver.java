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
package org.xapagy.ui.tempdyn;

import java.io.IOException;

import org.xapagy.debug.DebugEvent;
import org.xapagy.ui.observers.AbstractAgentObserver;

/**
 * An observer which observes the temporal dynamics of the focus and shadow
 * components. The recorded values can be used to generate graphs etc.
 * 
 * @author Ladislau Boloni
 * Created on: Aug 4, 2013
 */
public class TemporalDynamicsObserver extends AbstractAgentObserver {

    private static final long serialVersionUID = -674145743682800757L;
    private tdDataBase database = new tdDataBase();

    public tdDataBase getDatabase() {
        return database;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.observers.AbstractAgentObserver#observe2(org.xapagy.debug
     * .DebugEvent)
     */
    @Override
    public void observeInner(DebugEvent event) throws IOException,
            InterruptedException {
        database.record(agent);
        //TextUi.println("TemporalDynamicsObserver: observing at time:"
        //        + Formatter.fmt(agent.getTime()));
    }

}
