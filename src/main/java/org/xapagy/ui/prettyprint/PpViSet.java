/*
   This file is part of the Xapagy project
   Created on: Dec 26, 2011
 
   org.xapagy.ui.prettyprint.PiViSet
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.set.ViSet;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpViSet {
    /**
     * Fallback on the generic WeightedSet printing
     * 
     * @param is
     * @param agent
     * @return
     */
    public static String ppConcise(ViSet vis, Agent agent) {
        return PpWeightedSet.ppConcise(vis, agent);
    }

    /**
     * Fallback on the generic WeightedSet printing
     * 
     * @param is
     * @param agent
     * @return
     */
    public static String ppDetailed(ViSet vis, Agent agent) {
        return PpWeightedSet.ppDetailed(vis, agent);
    }
}
