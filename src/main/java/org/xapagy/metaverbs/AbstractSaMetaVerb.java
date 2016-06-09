/*
   This file is part of the Xapagy project
   Created on: May 20, 2011
 
   org.xapagy.domain.basic.SAMetaVerb
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * An SA associated with a specific type of verbs. Triggered when a meta-verb is
 * entered into the focus.
 * 
 * Current additional functionality is only an additional check that it is
 * applied to the right kind of VI.
 * 
 * @author Ladislau Boloni
 * 
 */
public abstract class AbstractSaMetaVerb extends SpikeActivity {

    private static final long serialVersionUID = -100545172467514753L;
    private ViType viType;

    /**
     * Creates a metaverb SA.
     * 
     * @param agent
     * @param name
     * @param viType
     */
    public AbstractSaMetaVerb(Agent agent, String name, ViType viType) {
        super(agent, name);
        this.viType = viType;
    }

    /**
     * @return the ViType
     */
    public ViType getViType() {
        return viType;
    }

}
