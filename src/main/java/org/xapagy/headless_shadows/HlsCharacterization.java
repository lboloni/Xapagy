/*
   This file is part of the Xapagy project
   Created on: Mar 6, 2011
 
   org.xapagy.story.RVIAElaboration
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A HLS which generates a non-action VI for adding attributes to an instance.
 * 
 * @author Ladislau Boloni
 * 
 */
public class HlsCharacterization implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4872464166154629632L;
    /**
     * The attributes added to the instance
     */
    private ConceptOverlay attributes;
    /**
     * The instance which is characterized
     */
    private Instance instance;

    /**
     * Creates a new instance characterization shadow, for adding the specificed
     * attributes to the instance.
     * 
     * @param instance
     * @param attributes
     */
    public HlsCharacterization(Instance instance, ConceptOverlay attributes) {
        this.instance = instance;
        this.attributes = attributes;
    }

    /**
     * @return the attributes
     */
    public ConceptOverlay getAttributes() {
        return attributes;
    }

    /**
     * @return the instance
     */
    public Instance getInstance() {
        return instance;
    }

    /**
     * Creates a verb instance which elaborates on a certain instance
     * 
     * @return
     */
    public VerbInstance instantiate(Agent agent) {
        VerbOverlay verbs = new VerbOverlay(agent);
        verbs.addFullEnergy(agent.getVerbDB().getConcept(Hardwired.VM_IS_A));
        VerbInstance vi = agent.createVerbInstance(ViType.S_ADJ, verbs);
        vi.setSubject(instance);
        vi.setAdjective(attributes);
        return vi;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }

}
