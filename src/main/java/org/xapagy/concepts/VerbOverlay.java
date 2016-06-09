/*
   This file is part of the Xapagy project
   Created on: Jan 24, 2011
 
   org.xapagy.model.VerbOverlay
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.concepts;

import java.util.Arrays;
import java.util.Collection;

import org.xapagy.agents.Agent;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
 */
public class VerbOverlay extends Overlay<Verb> implements XapagyComponent {

    private static final long serialVersionUID = 3498573418070441578L;

    /**
     * Creates an overlay from collection of verb names
     * 
     * @param agent
     * @param verbNames
     * @return
     */
    public static VerbOverlay
            createVO(Agent agent, Collection<String> verbNames) {
        VerbOverlay ovr = new VerbOverlay(agent);
        for (String verbName : verbNames) {
            Verb concept = agent.getVerbDB().getConcept(verbName);
            //if (concept == null) {
            //    throw new Error("Cannot find verb" + verbName);
            //}
            ovr.addFullEnergy(concept);
        }
        return ovr;
    }

    /**
     * Creates a verb overlay from verb names
     * 
     * @param agent
     * @param verbNames
     * @return
     */
    public static VerbOverlay createVO(Agent agent, String... verbNames) {
        return VerbOverlay.createVO(agent, Arrays.asList(verbNames));
    }

    public VerbOverlay(Agent agent) {
        super(agent.getVerbDB(), agent);
        identifier = agent.getIdentifierGenerator().getVerbOverlayIdentifier();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.concepts.Overlay#newOverlay()
     */
    @Override
    public Overlay<Verb> newOverlay() {
        if (agent == null) {
            TextUi.println("Agent is null, what the heck???");
        }
        return new VerbOverlay(agent);
    }

}
