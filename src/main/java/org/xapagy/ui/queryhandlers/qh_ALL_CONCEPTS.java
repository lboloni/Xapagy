package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptNamingConventions;
import org.xapagy.exceptions.NoSuchConceptOrVerb;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_ALL_CONCEPTS implements IQueryHandler {

    /**
     * Generates links to all the concepts in the concept database
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        AbstractConceptDB<Concept> cdb = agent.getConceptDB();
        fmt.addH2("Concepts: " + cdb.getAllConcepts().size(),
                "class=identifier");
        Map<Character, List<Concept>> map = new HashMap<>();
        // initialize the characterlist
        List<Character> characterList = new ArrayList<>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            characterList.add(letter);
        }
        characterList.add('\"');
        // initialize the maps
        for (char letter : characterList) {
            map.put(letter, new ArrayList<Concept>());
        }
        // put all non-negative concepts in the maps
        for (Concept concept : cdb.getAllConcepts()) {
            if (AbstractConceptDB.isANegation(concept)) {
                continue;
            }
            char startsWith =
                    ConceptNamingConventions.getConceptLetter(concept);
            List<Concept> list = map.get(startsWith);
            if (list == null) {
                TextUi.abort("generate can't find its place:" + concept);
            }
            list.add(concept);
        }
        // sort the maps alphabetically
        for (char letter : characterList) {
            List<Concept> list = map.get(letter);
            Collections.sort(list, new Comparator<Concept>() {

                @Override
                public int compare(Concept arg0, Concept arg1) {
                    String root0 = ConceptNamingConventions.getTypeAndRoot(arg0)
                            .getValue();
                    String root1 = ConceptNamingConventions.getTypeAndRoot(arg1)
                            .getValue();
                    return root0.compareTo(root1);
                    // return arg0.getName().compareTo(arg1.getName());
                }

            });
        }
        // ok, create the web page
        for (char letter : characterList) {
            List<Concept> list = map.get(letter);
            if (list.isEmpty()) {
                continue;
            }
            fmt.addH2("" + letter);
            for (Concept concept : list) {
                fmt.openP();
                PwQueryLinks.linkToConcept(fmt, agent, query, concept);
                // check to see if there is a negation
                if (!concept.getName().startsWith("\"")) {
                    try {
                        Concept notconcept = cdb.getConcept(AbstractConceptDB
                                .getNegationName(concept.getName()));
                        fmt.add("-");
                        PwQueryLinks.linkToConcept(fmt, agent, query,
                                notconcept);
                    } catch (NoSuchConceptOrVerb nscv) {
                        fmt.add("- has no negation");
                    }
                }
                fmt.closeP();
            }
        }
        // old implementation
        /*
         * for (Concept concept : cdb.getAllConcepts()) { fmt.openP();
         * PwLinks.linkToConcept(fmt, agent, query, concept); fmt.closeP(); }
         */

    }
}
