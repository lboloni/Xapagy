/*
   This file is part of the Xapagy project
   Created on: Nov 17, 2011
 
   org.xapagy.ui.prettyprint.PpResourceLeakyBucket
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.activity.ResourceLeakyBucket;
import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpResourceLeakyBucket {

    /**
     * Pretty prints the list of scenes in a concrete way
     * 
     * @param scenes
     * @param topLevel
     * @return
     */
    public static String ppConcise(ResourceLeakyBucket rlb, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("ResourceLeakyBucket:" + rlb.getName());
        fmt.is("total quantity", rlb.getParamTotalQuantity());
        fmt.is("max available", rlb.getParamMaxAvailable());
        fmt.is("pipe width", rlb.getParamPipeWidth());
        fmt.is("quantity available", rlb.getQuantityAvailable());
        fmt.is("quantity backup", rlb.getQuantityBackup());
        fmt.is("quantity used", rlb.getQuantityUsed());
        return fmt.toString();
    }

    public static String ppDetailed(ResourceLeakyBucket rlb, Agent agent) {
        return PpResourceLeakyBucket.ppConcise(rlb, agent);
    }
}
