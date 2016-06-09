/*
   This file is part of the Xapagy project
   Created on: Feb 19, 2016

   org.xapagy.agents.AgentDebugInfo

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.io.Serializable;

import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.storygenerator.RsTestingUnit;

/**
 * This class is a repository of various data that help in debugging a Xapagy
 * agent. The overall idea here is that the Xapagy agent should work just fine
 * without these.
 *
 * @author Ladislau Boloni
 *
 */
public class AgentDebugInfo implements Serializable {

    private static final long serialVersionUID = -6644423421509614209L;

    private Agent agent;
    private DebugEvent currentDebugEvent = null;
    private PerformanceMeter performanceMeter;
    /**
     * Used in debuggging, otherwise set to null
     */
    private RsTestingUnit rsTestingUnit = null;

    /**
     * @param agent
     */
    public AgentDebugInfo(Agent agent) {
        super();
        this.agent = agent;
    }

    /**
     * @return the currentDebugEvent
     */
    public DebugEvent getCurrentDebugEvent() {
        return currentDebugEvent;
    }

    /**
     * @return the performanceMeter
     */
    public PerformanceMeter getPerformanceMeter() {
        if (performanceMeter == null) {
            performanceMeter = new PerformanceMeter();
        }
        return performanceMeter;
    }

    public RsTestingUnit getRsTestingUnit() {
        return rsTestingUnit;
    }

    /**
     * @param currentDebugEvent
     *            the currentDebugEvent to set
     */
    public void setCurrentDebugEvent(DebugEvent currentDebugEvent) {
        this.currentDebugEvent = currentDebugEvent;
    }

    /**
     * Sets
     *
     * @param rsTestingUnit
     */
    public void setRsTestingUnit(RsTestingUnit rsTestingUnit) {
        this.rsTestingUnit = rsTestingUnit;
        agent.getParameters().set("A_DEBUG", "G_GENERAL",
                "N_RECORD_CHOICES_INTO_LOOPITEM", 1.0);
    }

}
