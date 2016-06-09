/*
   This file is part of the Xapagy project
   Created on: Jun 28, 2012
 
   org.xapagy.recall.FslComparator
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.util.Comparator;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * 
 */
public class FslComparator implements Comparator<FocusShadowLinked> {

    private Agent agent;

    public FslComparator(Agent agent) {
        this.agent = agent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(FocusShadowLinked arg0, FocusShadowLinked arg1) {
        return Double.compare(arg0.getTotalSupport(agent),
                arg1.getTotalSupport(agent));
    }

}
