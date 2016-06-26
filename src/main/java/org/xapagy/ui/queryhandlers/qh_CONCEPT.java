package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.util.SimpleEntryComparator;

public class qh_CONCEPT implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        Concept concept = null;
        for (Concept concept2 : agent.getConceptDB().getAllConcepts()) {
            if (concept2.getIdentifier().equals(identifier)) {
                concept = concept2;
                break;
            }
        }
        int countHideable = 1;
        if (concept == null) {
            fmt.addPre("could not find the concept with the identifier "
                    + identifier);
            return;
        }
        AbstractConceptDB<Concept> cdb = agent.getConceptDB();
        //
        // Identifier block
        //
        String redheader =
                "Concept " + concept.getName() + " (" + concept.getIdentifier()
                        + ")";
        // check if it is a negation
        if (concept.getName().startsWith(Hardwired.CONCEPT_PREFIX_NEGATION)) {
            redheader += " negation";
        }
        //
        fmt.addH2(redheader, "class=identifier");
        fmt.addLabelParagraph("Documentation");
        fmt.explanatoryNote(concept.getDocumentation());
        fmt.is("area", cdb.getArea(concept));
        //
        // Adding the overlaps, decreasing order
        //
        Map<Concept, Double> overlaps = cdb.getOverlaps(concept);
        if (overlaps.isEmpty()) {
            fmt.addH3("Overlaps: None.");
        } else {
            fmt.addH3("Overlaps:");
            // sort them in decreasing order
            List<SimpleEntry<Concept, Double>> listOverlap = new ArrayList<>();
            for (Concept c : overlaps.keySet()) {
                listOverlap.add(new SimpleEntry<>(c, overlaps.get(c)));
            }
            Collections.sort(listOverlap, new SimpleEntryComparator<Concept>());
            Collections.reverse(listOverlap);
            for (SimpleEntry<Concept, Double> entry : listOverlap) {
                //fmt.openP();
                Concept conceptOverlap = entry.getKey();
                double valueOverlap = entry.getValue();
                PwFormatter fmt2 = new PwFormatter();
                fmt2.progressBar(valueOverlap, 1.0);
                PwQueryLinks.linkToConcept(fmt2, agent, query, conceptOverlap);
                fmt.is(fmt2.toString() + " overlaps", valueOverlap);
                //fmt.closeP();
            }
        }
        //
        // Adding the implications, decreasing order
        //
        Map<Concept, Double> impacts = cdb.getImpacts(concept);
        if (impacts == null) {
            fmt.addH3("Implications: None.");
        } else {
            fmt.addH3("Implications: ");
            // sort them in decreasing order
            List<SimpleEntry<Concept, Double>> listImpacts = new ArrayList<>();
            for (Concept c : impacts.keySet()) {
                listImpacts.add(new SimpleEntry<>(c, impacts.get(c)));
            }
            Collections.sort(listImpacts, new SimpleEntryComparator<Concept>());
            Collections.reverse(listImpacts);
            for (SimpleEntry<Concept, Double> entry : listImpacts) {
                //fmt.openP();
                Concept conceptImpact = entry.getKey();
                double valueImpact = entry.getValue();
                PwFormatter fmt2 = new PwFormatter();
                fmt2.progressBar(valueImpact, 1.0);
                PwQueryLinks.linkToConcept(fmt2, agent, query, conceptImpact);
                fmt.is(fmt2.toString(), valueImpact);
                //fmt.progressBar(valueImpact, 1.0);
                //PwQueryLinks.linkToConcept(fmt, agent, query, conceptImpact);
                //fmt.add(" impact = " + Formatter.fmt(valueImpact));
                //fmt.closeP();
            }
        }
        //
        // Adding the list of instances which have the concept
        //
        PwFormatter fmt2 = new PwFormatter("");
        qh_CONCEPT.pwReferringInstances(fmt2, concept, agent, query);
        fmt.addExtensibleH2("id" + countHideable++, "Referring instances",
                fmt2.toString(), true);

    }

    /**
     * Generates links to instances which overlap with this concept
     * 
     * @return
     */
    public static String pwReferringInstances(PwFormatter fmt, Concept concept,
            Agent agent, RESTQuery query) {
        fmt.explanatoryNote("The instances which have an attribute which overlaps with this concept.");
        ConceptOverlay co = new ConceptOverlay(agent);
        co.addFullEnergy(concept);
        Set<Instance> instances =
                agent.getAutobiographicalMemory()
                        .getInstancesOverlappingWithCo(co);
        for (Instance instance : instances) {
            fmt.openP();
            PwQueryLinks.linkToInstance(fmt, agent, query, instance);
            fmt.closeP();
        }
        return fmt.toString();
    }

}
