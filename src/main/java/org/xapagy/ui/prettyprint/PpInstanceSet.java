/*
   This file is part of the Xapagy project
   Created on: Dec 26, 2011
 
   org.xapagy.ui.prettyprint.PpInstanceSet
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.set.InstanceSet;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpInstanceSet {

    /**
     * Fallback on the generic WeightedSet printing
     * 
     * @param is
     * @param agent
     * @return
     */
    public static String ppConcise(InstanceSet is, Agent agent) {
        return PpWeightedSet.ppConcise(is, agent);
    }

    /**
     * Fallback on the generic WeightedSet printing
     * 
     * @param is
     * @param agent
     * @return
     */
    public static String ppDetailed(InstanceSet is, Agent agent) {
        return PpWeightedSet.ppDetailed(is, agent);
    }

}
