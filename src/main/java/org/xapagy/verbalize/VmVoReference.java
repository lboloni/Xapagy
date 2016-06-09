/*
   This file is part of the Xapagy project
   Created on: Nov 23, 2011
 
   org.xapagy.verbalize.VmVoReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.xapi.reference.XapiReference;

/**
 * @author Ladislau Boloni
 * 
 */
public class VmVoReference extends AbstractVmReference {
    private static final long serialVersionUID = 3484151953731984140L;
    private VerbInstance vi;
    private ViPart viPart;
    private VerbOverlay voAtReference;

    /**
     * @param voAtReference
     * @param vi
     * @param viPart
     * @param referenceText
     */
    public VmVoReference(Agent agent, VerbOverlay vo, VerbInstance vi,
            ViPart viPart, XapiReference xapiReference) {
        super(vo, agent.getTime(), xapiReference);
        this.vi = vi;
        this.viPart = viPart;
        // the voAtReference is created here
        voAtReference = (VerbOverlay) vo.newOverlay();
        voAtReference.addOverlay(vo);

    }

    /**
     * @return the vi
     */
    public VerbInstance getVi() {
        return vi;
    }

    /**
     * @return the viPart
     */
    public ViPart getViPart() {
        return viPart;
    }

    /**
     * @return the voAtReference
     */
    public VerbOverlay getVoAtReference() {
        return voAtReference;
    }

}
