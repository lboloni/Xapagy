/*
   This file is part of the Xapagy project
   Created on: Apr 22, 2011
 
   org.xapagy.story.SpikeActivity
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;

/**
 * A spike activity describes an activity which happens instantanously, makes
 * changes in the Xapagy agent etc.
 * 
 * @author Ladislau Boloni
 * 
 */
public abstract class SpikeActivity extends Activity {

    private static final long serialVersionUID = 1962197695621001378L;

    /**
     * Default constructor, forces the agent and name on the others
     * 
     * @param agent
     * @param name
     */
    public SpikeActivity(Agent agent, String name) {
        super(agent, name);
    }

    /**
     * Final version, overwrite apply inner. The idea is that you have hooks
     * here for tracing
     */
    public final void apply(VerbInstance vi) {
        applyInner(vi);
    }

    /**
     * This is where inherited classes should put their component
     */
    public abstract void applyInner(VerbInstance vi);

}
