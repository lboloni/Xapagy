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
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwVerb;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.util.SimpleEntryComparator;

public class qh_VERB implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter pw, Agent agent, RESTQuery query, Session session) {
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
            pw.addPre("could not find the verb with the identifier "
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
        pw.addH2(redheader, "class=identifier");
        // documentation
        pw.addLabelParagraph("Documentation");
        pw.explanatoryNote(verb.getDocumentation());
        // area
        pw.addH3("area = " + cdb.getArea(verb));
        // associated spike activity
        if (verb.getSpike() == null) {
            pw.addH3("No associated spike activity");
        } else {
            pw.addH3("Associated spike activity:");
            pw.addPre(verb.getSpike().toString());
        }
        // Drives
        xwVerb.xwDetailedDriveImpacts(pw, verb, agent);
        
        //
        // Adding the overlaps, decreasing order
        //
        Map<Verb, Double> overlaps = cdb.getOverlaps(verb);
        if (overlaps.isEmpty()) {
            pw.addH3("Overlaps: None.");
        } else {
            pw.addH3("Overlaps:");
            // sort them in decreasing order
            List<SimpleEntry<Verb, Double>> listOverlap = new ArrayList<>();
            for (Verb v : overlaps.keySet()) {
                listOverlap.add(new SimpleEntry<>(v, overlaps.get(v)));
            }
            Collections.sort(listOverlap, new SimpleEntryComparator<Verb>());
            Collections.reverse(listOverlap);
            for (SimpleEntry<Verb, Double> entry : listOverlap) {
                pw.openP();
                Verb verbOverlap = entry.getKey();
                double valueOverlap = entry.getValue();
                pw.progressBar(valueOverlap, 1.0);
                PwQueryLinks.linkToVerb(pw, agent, query, verbOverlap);
                pw.add(" overlap = " + Formatter.fmt(valueOverlap));
                pw.closeP();
            }
        }
        //
        // Adding the implacts, decreasing order
        //
        Map<Verb, Double> impacts = cdb.getImpacts(verb);
        if (impacts == null) {
            pw.addH3("Implications: None.");
        } else {
            pw.addH3("Implications: ");
            // sort them in decreasing order
            List<SimpleEntry<Verb, Double>> listImpacts = new ArrayList<>();
            for (Verb c : impacts.keySet()) {
                listImpacts.add(new SimpleEntry<>(c, impacts.get(c)));
            }
            Collections.sort(listImpacts, new SimpleEntryComparator<Verb>());
            Collections.reverse(listImpacts);
            for (SimpleEntry<Verb, Double> entry : listImpacts) {
                pw.openP();
                Verb conceptImpact = entry.getKey();
                double valueImpact = entry.getValue();
                pw.progressBar(valueImpact, 1.0);
                PwQueryLinks.linkToVerb(pw, agent, query, conceptImpact);
                pw.add(" impact = " + Formatter.fmt(valueImpact));
                pw.closeP();
            }
        }
        //
        // Adding the list of VIs which have the verb
        //
        PwFormatter fmt2 = new PwFormatter("");
        qh_VERB.pwReferringVis(fmt2, verb, agent, query);
        pw.addExtensibleH2("id" + countHideable++, "Referring VIs",
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
