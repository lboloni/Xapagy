/*
   This file is part of the Xapagy project
   Created on: Jan 24, 2011
 
   org.xapagy.model.ConceptOverlay
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.concepts;

import java.util.Arrays;
import java.util.Collection;

import org.xapagy.agents.Agent;
import org.xapagy.instances.XapagyComponent;

/**
 * @author Ladislau Boloni
 * 
 */
public class ConceptOverlay extends Overlay<Concept> implements XapagyComponent {

    private static final long serialVersionUID = 2115693869306850652L;

    /**
     * Creates a concept overlay from concept names
     * 
     * @param agent
     * @param conceptNames
     * @return
     */
    public static ConceptOverlay createCO(Agent agent,
            Collection<String> conceptNames) {
        ConceptOverlay ovr = new ConceptOverlay(agent);
        for (String conceptName : conceptNames) {
            Concept concept = agent.getConceptDB().getConcept(conceptName);
            if (concept == null) {
                throw new Error("Cannot find concept" + conceptName);
            }
            ovr.addFullEnergy(concept);
        }
        return ovr;
    }

    /**
     * Creates a concept overlay from concept names
     * 
     * @param agent
     * @param conceptNames
     * @return
     */
    public static ConceptOverlay createCO(Agent agent, String... conceptNames) {
        return ConceptOverlay.createCO(agent, Arrays.asList(conceptNames));
    }

    /**
     * Constructor from agent
     * 
     * @param agent
     */
    public ConceptOverlay(Agent agent) {
        super(agent.getConceptDB(), agent);
        identifier =
                agent.getIdentifierGenerator().getConceptOverlayIdentifier();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.concepts.Overlay#newOverlay()
     */
    @Override
    public Overlay<Concept> newOverlay() {
        return new ConceptOverlay(agent);
    }

}
