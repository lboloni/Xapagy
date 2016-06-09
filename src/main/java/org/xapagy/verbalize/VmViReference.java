/*
   This file is part of the Xapagy project
   Created on: Nov 23, 2011
 
   org.xapagy.verbalize.VmViReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.xapi.reference.XapiReference;

/**
 * @author Ladislau Boloni
 * 
 */
public class VmViReference extends AbstractVmReference {
    private static final long serialVersionUID = 1271957828602094230L;
    private VerbInstance vi;

    /**
     * @param vi
     * @param referenceText
     */
    public VmViReference(Agent agent, VerbInstance vi,
            XapiReference xapiReference) {
        super(vi, agent.getTime(), xapiReference);
        this.vi = vi;
    }

    /**
     * @return the vi
     */
    public VerbInstance getVi() {
        return vi;
    }
}
