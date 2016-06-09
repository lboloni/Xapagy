/*
   This file is part of the Xapagy project
   Created on: Apr 22, 2011
 
   org.xapagy.story.activity.DiffusionActivity
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity;

import org.xapagy.agents.Agent;

/**
 * A diffusion activity in Xapagy represents a change in the system that occurs
 * over continuous time scale (e.g. spreading activation). This would be
 * normally described through a differential equation, but in this code it will
 * be a repeated call over small timeslices.
 * 
 * DAs should be stateless and they should not access external parts of the agent.
 * 
 * @author Ladislau Boloni
 * 
 */
public abstract class DiffusionActivity extends Activity {

    private static final long serialVersionUID = -4025625223236088540L;
    private int callCount;
    private int callCountSinceReset;
    private long timeSpent;
    private long timeSpentSinceReset;
    /**
     * Constructor: creates the DA and registers it with the performance meter
     * in the agent.
     * 
     * FIXME: with the move to the dynamic creation of the DAs, this is not a
     * good idea any more.
     * 
     * @param agent
     * @throws Exception 
     */
    public DiffusionActivity(Agent agent, String name) {
        super(agent, name);
        agent.getDebugInfo().getPerformanceMeter().registerDA(this);
    }

    
    /**
     * The outer call. Will allow us to put hooks. If the strenght is zero, do
     * not call applyInner
     * 
     * @param timeSlice
     */
    public void apply(double timeSlice) {
        callCount++;
        callCountSinceReset++;
        long startTime = System.currentTimeMillis();
        applyInner(timeSlice);
        // resources.usageUnlock();
        long endTime = System.currentTimeMillis();
        timeSpent = timeSpent + endTime - startTime;
        timeSpentSinceReset = timeSpentSinceReset + endTime - startTime;
    }

    /**
     * The general purpose apply function inner call, this is what the
     * inheriting implementations must implement
     * 
     * @param agent
     * @param timeSlice
     *            the time slot the diffusion activity had been running
     */
    protected abstract void applyInner(double timeSlice);

    /**
     * @return the callCount
     */
    public int getCallCount() {
        return callCount;
    }

    /**
     * @return the callCountSinceReset
     */
    public int getCallCountSinceReset() {
        return callCountSinceReset;
    }

    /**
     * @return the timeSpent
     */
    public long getTimeSpent() {
        return timeSpent;
    }

    /**
     * @return the timeSpentSinceReset
     */
    public long getTimeSpentSinceReset() {
        return timeSpentSinceReset;
    }

    public void timeCountReset() {
        callCountSinceReset = 0;
        timeSpentSinceReset = 0;
    }

}
