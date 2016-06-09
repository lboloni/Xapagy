package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_CONCEPT_OVERLAY {

    /**
     * Although there is no specific query for a concept overlay, there is a
     * need to pretty print it (for instance when inspecting the instance)
     * 
     * @param fmt
     * @param agent
     * @param concept
     * @param query
     */
    public static void generate(IXwFormatter fmt, Agent agent,
            ConceptOverlay co, RESTQuery query, boolean showLabels) {
        // fmt.addH3("Concept overlay");
        for (SimpleEntry<Concept, Double> entry : co
                .getSortedByExplicitEnergy()) {
            Concept concept = entry.getKey();
            fmt.openP();
            double value = entry.getValue();
            fmt.progressBar(value, 1.0);
            PwQueryLinks.linkToConcept(fmt, agent, query, concept);
            fmt.closeP();
        }
        if (showLabels) {
            qh_VERB_OVERLAY.addOverlayLabels(fmt, agent, co, query);
        }

    }

    /**
     * A compact way to characterize a query
     * 
     * @param fmt
     * @param agent
     * @param co
     * @param query
     */
    public static String pwCompact(IXwFormatter fmt, Agent agent,
            ConceptOverlay co, RESTQuery query) {
        fmt.add("[ ");
        for (SimpleEntry<Concept, Double> entry : co
                .getSortedByExplicitEnergy()) {
            Concept concept = entry.getKey();
            PwQueryLinks.linkToConcept(fmt, agent, query, concept);
        }
        fmt.add(" ]");
        return fmt.toString();
    }

}
