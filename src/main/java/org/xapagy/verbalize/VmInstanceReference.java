/*
   This file is part of the Xapagy project
   Created on: Nov 23, 2011
 
   org.xapagy.verbalize.VmInstanceReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.xapi.reference.XrefToInstance;

/**
 * Describes a reference done
 * 
 * @author Ladislau Boloni
 * 
 */
public class VmInstanceReference extends AbstractVmReference {

    private static final long serialVersionUID = 6873206070330661309L;
    private ConceptOverlay coAtReference;
    private Instance instance;
    private Instance sceneFrom;
    private VerbInstance vi;
    private ViPart viPart;

    /**
     * Creates a VmInstanceReference
     * 
     * @param instance
     * @param sceneFrom
     * @param vi
     * @param viPart
     * @param referenceText
     */
    public VmInstanceReference(Agent agent, Instance instance,
            Instance sceneFrom, VerbInstance vi, ViPart viPart,
            XrefToInstance xapiReference) {
        super(instance, agent.getTime(), xapiReference);
        this.instance = instance;
        this.sceneFrom = sceneFrom;
        this.vi = vi;
        this.viPart = viPart;
        // the coAtReference is created here
        coAtReference = (ConceptOverlay) instance.getConcepts().newOverlay();
        coAtReference.addOverlay(instance.getConcepts());
    }

    /**
     * @return the coAtReference
     */
    public ConceptOverlay getCoAtReference() {
        return coAtReference;
    }

    /**
     * @return the instance
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * @return the sceneFrom
     */
    public Instance getSceneFrom() {
        return sceneFrom;
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
