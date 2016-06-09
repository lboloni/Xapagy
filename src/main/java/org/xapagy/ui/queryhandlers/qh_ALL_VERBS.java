package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.ConceptNamingConventions;
import org.xapagy.concepts.Verb;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_ALL_VERBS implements IQueryHandler {

    /**
     * Generates links to all the concepts in the concept database
     * 
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        AbstractConceptDB<Verb> cdb = agent.getVerbDB();
        fmt.addH2("Verbs: " + cdb.getAllConcepts().size(), "class=identifier");
        Map<Character, List<Verb>> map = new HashMap<>();
        // initialize the characterlist
        List<Character> characterList = new ArrayList<>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            characterList.add(letter);
        }
        // initialize the maps
        for (char letter : characterList) {
            map.put(letter, new ArrayList<Verb>());
        }
        // put all non-negative concepts in the maps
        for (Verb verb : cdb.getAllConcepts()) {
            if (AbstractConceptDB.isANegation(verb)) {
                continue;
            }
            char startsWith = ConceptNamingConventions.getVerbLetter(verb);
            List<Verb> list = map.get(startsWith);
            if (list == null) {
                TextUi.abort("generate can't find its place:" + verb);
            }
            list.add(verb);
        }
        // sort the maps alphabetically
        for (char letter : characterList) {
            List<Verb> list = map.get(letter);
            Collections.sort(list, new Comparator<Verb>() {

                @Override
                public int compare(Verb arg0, Verb arg1) {
                    String root0 =
                            ConceptNamingConventions.getTypeAndRoot(arg0)
                                    .getValue();
                    String root1 =
                            ConceptNamingConventions.getTypeAndRoot(arg1)
                                    .getValue();
                    return root0.compareTo(root1);
                }

            });
        }
        // ok, create the web page
        for (char letter : characterList) {
            List<Verb> list = map.get(letter);
            if (list.isEmpty()) {
                continue;
            }
            fmt.addH2("" + letter);
            for (Verb verb : list) {
                fmt.openP();
                PwQueryLinks.linkToVerb(fmt, agent, query, verb);
                if (!verb.getName().startsWith("\"")) {
                    fmt.add("-");
                    Verb notconcept =
                            cdb.getConcept(AbstractConceptDB
                                    .getNegationName(verb.getName()));
                    PwQueryLinks.linkToVerb(fmt, agent, query, notconcept);
                }
                fmt.closeP();
            }
        }
    }

}
