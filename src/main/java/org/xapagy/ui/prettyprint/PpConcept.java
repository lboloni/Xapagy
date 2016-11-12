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
import org.xapagy.concepts.AbstractConcept;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.util.SimpleEntryComparator;

/**
 * 
 * Formatting a concept
 * 
 * @author Ladislau Boloni
 * Created on: Dec 29, 2010
 */
public class PpConcept {

    /**
     * Adds to the formatter the details of the concepts
     * 
     * @param fmt
     * @param concept
     * @param agent
     * @param cdb
     */
    public static <T extends AbstractConcept> void addDetailsOfConcepts(
            Formatter fmt, T concept, Agent agent, AbstractConceptDB<T> cdb) {
        // Add the area
        fmt.indent();
        fmt.is("Area", cdb.getArea(concept));
        //
        // Adding the overlaps, decreasing order
        //
        Map<T, Double> overlaps = cdb.getOverlaps(concept);
        if (overlaps.isEmpty()) {
            fmt.add("Overlaps: None.");
        } else {
            fmt.add("Overlaps:");
            fmt.indent();
            // sort them in decreasing order
            List<SimpleEntry<T, Double>> listOverlap = new ArrayList<>();
            for (T c : overlaps.keySet()) {
                listOverlap.add(new SimpleEntry<>(c, overlaps.get(c)));
            }
            Collections.sort(listOverlap, new SimpleEntryComparator<T>());
            Collections.reverse(listOverlap);
            for (SimpleEntry<T, Double> entry : listOverlap) {
                fmt.is(entry.getKey().getName(), entry.getValue());
            }
            fmt.deindent();
        }
        //
        // Adding the implications, decreasing order
        //
        Map<T, Double> impacts = cdb.getImpacts(concept);
        if (impacts == null) {
            fmt.add("Implications: None.");
        } else {
            fmt.add("Implications: ");
            fmt.indent();
            // sort them in decreasing order
            List<SimpleEntry<T, Double>> listImpacts = new ArrayList<>();
            for (T c : impacts.keySet()) {
                listImpacts.add(new SimpleEntry<>(c, impacts.get(c)));
            }
            Collections.sort(listImpacts, new SimpleEntryComparator<T>());
            Collections.reverse(listImpacts);
            for (SimpleEntry<T, Double> entry : listImpacts) {
                fmt.is(entry.getKey().getName(), entry.getValue());
            }
            fmt.deindent();
        }
        fmt.deindent();
    }

    /**
     * Adds the words which refer to this concept
     * 
     * @param fmt
     * @param concept
     * @param agent
     * @param cdb
     */
    private static void addReferringWords(Formatter fmt, Concept concept,
            Agent agent) {
        // Add the area
        fmt.add("Referring words");
        fmt.indent();
        //
        // Adding the overlaps, decreasing order
        //
        List<String> words = agent.getXapiDictionary().getConceptWords();
        for (String word : words) {
            ConceptOverlay coDirect =
                    agent.getXapiDictionary().getCoForWord(word);
            ConceptOverlay coImpact = new ConceptOverlay(agent);
            coImpact.addOverlayImpacted(coDirect);
            if (coDirect.getEnergy(concept) > 0) {
                fmt.is(word, PrettyPrint.ppConcise(coDirect, agent));
            } else if (coImpact.getEnergy(concept) > 0) {
                fmt.is(".." + word, PrettyPrint.ppConcise(coDirect, agent));
            }
        }
        fmt.deindent();
    }

    /**
     * Returns a list of the concepts which overlap with concept
     * 
     * This is a very expensive operation, and it should normally only be used
     * for listing and debugging purposes
     * 
     * @param concept
     * @return
     */
    public static List<SimpleEntry<Concept, Double>> getOverlaps(
            AbstractConceptDB<Concept> cdb, Concept concept) {
        List<SimpleEntry<Concept, Double>> retval = new ArrayList<>();
        for (Concept c : cdb.getAllConcepts()) {
            if (c.equals(concept)) {
                continue;
            }
            double overlap = cdb.getOverlap(concept, c);
            if (overlap != 0.0) {
                retval.add(new SimpleEntry<>(c, overlap));
            }
        }
        return retval;
    }

    /**
     * Very simple printing, only the name of the concept
     * 
     * @param concept
     * @param agent
     * @return
     */
    public static String ppConcise(Concept concept, Agent agent) {
        return concept.getName();
    }

    /**
     * Detailed printing of a concept: includes area, and impacts
     * 
     * @param concept
     * @param agent
     * @return
     */
    public static String ppDetailed(Concept concept, Agent agent) {
        AbstractConceptDB<Concept> cdb = agent.getConceptDB();
        Formatter fmt = new Formatter();
        fmt.add("Concept: [" + concept.getName() + "]");
        fmt.add("Comment:" + concept.getDocumentation());
        PpConcept.addDetailsOfConcepts(fmt, concept, agent, cdb);
        PpConcept.addReferringWords(fmt, concept, agent);
        return fmt.toString();
    }
}
