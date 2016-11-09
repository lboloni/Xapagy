/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
package org.xapagy.concepts;

import java.util.Arrays;
import java.util.Collection;

import org.xapagy.agents.Agent;
import org.xapagy.instances.XapagyComponent;

/**
 * @author Ladislau Boloni
 * Created on: Jan 24, 2011
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
