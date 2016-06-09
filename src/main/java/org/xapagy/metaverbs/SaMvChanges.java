/*
   This file is part of the Xapagy project
   Created on: Nov 17, 2012
 
   org.xapagy.metaverbs.SaMvChanges
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * SaMvChanges - creates a new instance, connects it with somatic identity to
 * the new one, copies the attributes and the relations of the old instance to
 * the new one.
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvChanges extends AbstractSaMvChange {

    private static final long serialVersionUID = 8058587414920394661L;

    /**
     * @param agent
     */
    public SaMvChanges(Agent agent) {
        super(agent, "SaMvChanges", true, true);
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvChanges");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
