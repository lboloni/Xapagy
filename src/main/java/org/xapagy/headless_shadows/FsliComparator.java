/*
   This file is part of the Xapagy project
   Created on: Jun 25, 2012
 
   org.xapagy.recall.FsliComparator
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.util.Comparator;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * 
 */
public class FsliComparator implements Comparator<FslInterpretation> {

    private Agent agent;

    public FsliComparator(Agent agent) {
        this.agent = agent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(FslInterpretation arg0, FslInterpretation arg1) {
        return Double.compare(arg0.getTotalSupport(agent),
                arg1.getTotalSupport(agent));
    }

}
