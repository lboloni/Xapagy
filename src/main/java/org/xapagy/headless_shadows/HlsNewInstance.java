/*
   This file is part of the Xapagy project
   Created on: Mar 7, 2012
 
   org.xapagy.recall.HlsNewInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.TextUi;

/**
 * A headless shadow encapsulating the decision to create a new instance, in a
 * certain scene
 * 
 * @author Ladislau Boloni
 * 
 */
public class HlsNewInstance implements XapagyComponent, Serializable {

    private static final long serialVersionUID = -8318392561884119989L;
    /**
     * The attributes with which the instance must be created
     */
    private ConceptOverlay attributes;
    private String identifier;
    /**
     * True if this Hls had been instantiated and the specific instance resolved
     */
    private boolean resolved = false;
    /**
     * The instance to which the HlsNewInstance was resolved
     */
    private Instance resolvedInstance = null;

    /**
     * The scene in which the instance must be created
     */
    private Instance scene;

    /**
     * The supports for the creation of the given instance: the HlsSupported and
     * the vi part which it will fill
     */
    private List<SimpleEntry<Hls, ViPart>> supports = new ArrayList<>();

    /**
     * Creates a HlsNewInstance for a specific scene and collection of
     * attributes.
     * 
     * @param scene
     * @param attributes
     */
    public HlsNewInstance(Agent agent, Instance scene, ConceptOverlay attributes) {
        super();
        this.identifier =
                agent.getIdentifierGenerator().getHlsNewInstanceIdentifier();
        this.scene = scene;
        this.attributes = attributes;
    }

    /**
     * @return the attributes
     */
    public ConceptOverlay getAttributes() {
        return attributes;
    }

    /**
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return the resolvedInstance
     */
    public Instance getResolvedInstance() {
        return resolvedInstance;
    }

    /**
     * @return the scene
     */
    public Instance getScene() {
        return scene;
    }

    /**
     * @return the supports
     */
    public List<SimpleEntry<Hls, ViPart>> getSupports() {
        return supports;
    }

    /**
     * Generates a VI corresponding to this HLS.
     * 
     * @return
     */
    public VerbInstance instantiate(Agent agent) {
        // VerbOverlay verbs = new VerbOverlay(agent);
        // verbs.addFullEnergy(agent, agent.getVerbDB().getConcept(
        // Hardwired.VM_CREATE_INSTANCE));
        VerbOverlay verbs =
                VerbOverlay.createVO(agent, Hardwired.VM_CREATE_INSTANCE);
        VerbInstance retval = agent.createVerbInstance(ViType.S_ADJ, verbs);
        retval.setSubject(scene);
        retval.setAdjective(attributes);
        return retval;
    }

    /**
     * @return the resolved
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * Resolving for all the motivations with instance
     * 
     * @param instance
     */
    public void resolve(Instance instance) {
        TextUi.print("HlsNewInstance: resolve - resolving for all the  with instance"
                + instance + "\n");
        resolved = true;
        resolvedInstance = instance;
        for (SimpleEntry<Hls, ViPart> entry : supports) {
            Hls hls = entry.getKey();
            ViPart part = entry.getValue();
            hls.resolveDependency(instance, part, this);
        }
    }

    /**
     * Sets the mo
     * 
     * @param hls
     * @param part
     */
    public void setMotivation(Hls hls, ViPart part) {
        for (SimpleEntry<Hls, ViPart> entry : supports) {
            if (entry.getKey().equals(hls) && entry.getValue().equals(part)) {
                // nothing to be done here
                return;
            }
        }
        // if we are here, we need to add the new motivation
        SimpleEntry<Hls, ViPart> entry = new SimpleEntry<>(hls, part);
        supports.add(entry);
    }
}
