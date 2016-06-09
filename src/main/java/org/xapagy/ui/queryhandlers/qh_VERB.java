package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.util.SimpleEntryComparator;

public class qh_VERB implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        Verb verb = null;
        for (Verb verb2 : agent.getVerbDB().getAllConcepts()) {
            if (verb2.getIdentifier().equals(identifier)) {
                verb = verb2;
                break;
            }
        }
        int countHideable = 1;
        if (verb == null) {
            fmt.addPre("could not find the verb with the identifier "
                    + identifier);
            return;
        }
        AbstractConceptDB<Verb> cdb = agent.getVerbDB();
        //
        // Identifier block
        //
        String redheader =
                "Verb " + verb.getName() + " (" + verb.getIdentifier() + ")";
        // check if it is a negation
        if (verb.getName().startsWith(Hardwired.CONCEPT_PREFIX_NEGATION)) {
            redheader += " negation";
        }

        //
        // FIXME: add here if this is a category
        //
        fmt.addH2(redheader, "class=identifier");
        fmt.addLabelParagraph("Documentation");
        fmt.explanatoryNote(verb.getDocumentation());
        fmt.addH3("area = " + cdb.getArea(verb));
        if (verb.getSpike() == null) {
            fmt.addH3("No associated spike");
        } else {
            fmt.addH3("Associated spike:");
            fmt.addPre(verb.getSpike().toString());
        }
        //
        // Adding the overlaps, decreasing order
        //
        Map<Verb, Double> overlaps = cdb.getOverlaps(verb);
        if (overlaps.isEmpty()) {
            fmt.addH3("Overlaps: None.");
        } else {
            fmt.addH3("Overlaps:");
            // sort them in decreasing order
            List<SimpleEntry<Verb, Double>> listOverlap = new ArrayList<>();
            for (Verb v : overlaps.keySet()) {
                listOverlap.add(new SimpleEntry<>(v, overlaps.get(v)));
            }
            Collections.sort(listOverlap, new SimpleEntryComparator<Verb>());
            Collections.reverse(listOverlap);
            for (SimpleEntry<Verb, Double> entry : listOverlap) {
                fmt.openP();
                Verb verbOverlap = entry.getKey();
                double valueOverlap = entry.getValue();
                fmt.progressBar(valueOverlap, 1.0);
                PwQueryLinks.linkToVerb(fmt, agent, query, verbOverlap);
                fmt.add(" overlap = " + Formatter.fmt(valueOverlap));
                fmt.closeP();
            }
        }
        //
        // Adding the implications, decreasing order
        //
        Map<Verb, Double> impacts = cdb.getImpacts(verb);
        if (impacts == null) {
            fmt.addH3("Implications: None.");
        } else {
            fmt.addH3("Implications: ");
            // sort them in decreasing order
            List<SimpleEntry<Verb, Double>> listImpacts = new ArrayList<>();
            for (Verb c : impacts.keySet()) {
                listImpacts.add(new SimpleEntry<>(c, impacts.get(c)));
            }
            Collections.sort(listImpacts, new SimpleEntryComparator<Verb>());
            Collections.reverse(listImpacts);
            for (SimpleEntry<Verb, Double> entry : listImpacts) {
                fmt.openP();
                Verb conceptImpact = entry.getKey();
                double valueImpact = entry.getValue();
                fmt.progressBar(valueImpact, 1.0);
                PwQueryLinks.linkToVerb(fmt, agent, query, conceptImpact);
                fmt.add(" impact = " + Formatter.fmt(valueImpact));
                fmt.closeP();
            }
        }
        //
        // Adding the list of VIs which have the verb
        //
        PwFormatter fmt2 = new PwFormatter("");
        qh_VERB.pwReferringVis(fmt2, verb, agent, query);
        fmt.addExtensibleH2("id" + countHideable++, "Referring VIs",
                fmt2.toString(), true);
    }

    /**
     * Generates links to instances which overlap with this concept
     * 
     * @return
     */
    public static String pwReferringVis(PwFormatter fmt, Verb verb,
            Agent agent, RESTQuery query) {
        fmt.explanatoryNote("The verbs which have an vo which overlaps with this verb");
        VerbOverlay vo = new VerbOverlay(agent);
        vo.addFullEnergy(verb);
        Set<VerbInstance> vis =
                agent.getAutobiographicalMemory().getVisOverlappingWithVo(vo);
        for (VerbInstance vi : vis) {
            fmt.openP();
            PwQueryLinks.linkToVi(fmt, agent, query, vi);
            fmt.closeP();
        }
        return fmt.toString();
    }

}
