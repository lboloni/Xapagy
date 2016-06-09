/*
   This file is part of the Xapagy project
   Created on: Aug 4, 2013
 
   org.xapagy.ui.tempdyn.TemporalDynamicsObserver
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import java.io.IOException;

import org.xapagy.debug.DebugEvent;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.observers.AbstractAgentObserver;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * An observer which observes the temporal dynamics of the focus and shadow
 * components. The recorded values can be used to generate graphs etc.
 * 
 * @author Ladislau Boloni
 * 
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
        TextUi.println("TemporalDynamicsObserver: observing at time:"
                + Formatter.fmt(agent.getTime()));
    }

}
