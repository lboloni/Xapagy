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
package org.xapagy.ui.prettyprint;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.util.SimpleEntryComparator;

/**
 * Prints the instance resolution choices
 * 
 * @author Ladislau Boloni
 * Created on: Jun 23, 2011
 */
public class PpInstanceResolutionChoices {

    /**
     * Describes the process of the resolution
     * 
     * @param objects
     * @param agent
     * @return
     */
    public static String pp(Object[] objects, Agent agent) {
        ConceptOverlay co = (ConceptOverlay) objects[0];
        String text = (String) objects[1];
        ViPart part = (ViPart) objects[2];
        Instance scene = (Instance) objects[3];
        Instance resolvedInstance = (Instance) objects[4];
        @SuppressWarnings("unchecked")
        Map<Instance, Double> possibles = (Map<Instance, Double>) objects[5];
        Formatter fmt = new Formatter();
        fmt.add("Resolving \"" + text + "\" "
                + PrettyPrint.ppConcise(co, agent) + " as " + part);
        fmt.indent();
        fmt.add("In scene:" + PrettyPrint.ppConcise(scene, agent));
        fmt.add("Resolved as:" + PrettyPrint.ppConcise(resolvedInstance, agent));
        List<SimpleEntry<Instance, Double>> listCandidates = new ArrayList<>();
        for (Instance candidate : possibles.keySet()) {
            listCandidates.add(new SimpleEntry<>(candidate, possibles
                    .get(candidate)));
        }
        Collections.sort(listCandidates, new SimpleEntryComparator<Instance>());
        Collections.reverse(listCandidates);
        fmt.add("Candidates:");
        fmt.indent();
        for (SimpleEntry<Instance, Double> entry : listCandidates) {
            fmt.addWithMarginNote(Formatter.fmt(entry.getValue()),
                    PrettyPrint.ppConcise(entry.getKey(), agent));
        }
        return fmt.toString();
    }

}
