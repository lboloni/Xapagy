/*
   This file is part of the Xapagy project
   Created on: Jun 23, 2011
 
   org.xapagy.ui.prettyprint.PpResolutionChoices
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
