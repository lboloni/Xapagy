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
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Jan 24, 2011
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
