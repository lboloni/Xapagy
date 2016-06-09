/*
   This file is part of the Xapagy project
   Created on: Feb 5, 2011
 
   org.xapagy.domain.basic.vbNarrate
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class SaMvNarrate extends AbstractSaMetaVerb {
    private static final long serialVersionUID = 4403880110309975733L;

    public SaMvNarrate(Agent agent) {
        super(agent, "SaMvNarrate", ViType.S_V);
    }

    /**
     * Removes the object as a composite
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        TextUi.println("Turning on narrate: for the time being, ignored.");
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvNarrate");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
    
}
