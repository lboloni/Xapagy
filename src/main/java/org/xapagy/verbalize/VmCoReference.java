/*
   This file is part of the Xapagy project
   Created on: Nov 23, 2011
 
   org.xapagy.verbalize.VmCoReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.xapi.reference.XrefToCo;

/**
 * A textual reference at a given concept overlay
 * 
 * @author Ladislau Boloni
 * 
 */
public class VmCoReference extends AbstractVmReference {

    private static final long serialVersionUID = 8894737177636941715L;
    private ConceptOverlay coAtReference;
    private VerbInstance vi;
    private ViPart viPart;

    /**
     * @param co
     * @param vi
     * @param viPart
     * @param referenceText
     */
    public VmCoReference(Agent agent, ConceptOverlay co, VerbInstance vi,
            ViPart viPart, XrefToCo xapiReference) {
        super(co, agent.getTime(), xapiReference);
        this.vi = vi;
        this.viPart = viPart;
        // the coAtReference is created here
        coAtReference = (ConceptOverlay) co.newOverlay();
        coAtReference.addOverlay(co);
    }

    /**
     * @return the coAtReference
     */
    public ConceptOverlay getCoAtReference() {
        return coAtReference;
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

}
