/*
   This file is part of the Xapagy project
   Created on: Nov 17, 2012
 
   org.xapagy.metaverbs.SaMvTransforms
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Transforms: for radical transformations. Does not transform the attributes
 * and it does
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvTransforms extends AbstractSaMvChange {

    /**
     * 
     */
    private static final long serialVersionUID = 987036919230544307L;

    /**
     * @param agent
     */
    public SaMvTransforms(Agent agent) {
        super(agent, "SaMvTransforms", false, false);
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvTransforms");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
