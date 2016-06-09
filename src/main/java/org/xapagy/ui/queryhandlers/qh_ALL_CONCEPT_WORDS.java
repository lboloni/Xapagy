package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.xapi.ConceptWord;
import org.xapagy.xapi.WordNamingConventions;
import org.xapagy.xapi.XapiDictionary;

public class qh_ALL_CONCEPT_WORDS implements IQueryHandler {

    /**
     * Applies the query for the collection of concept words
     * 
     * FIXME: implement it with sorting, filtering, cursor
     * 
     * @param query
     * @return
     */
    private static List<ConceptWord> applyQuery(Agent agent, RESTQuery query) {
        XapiDictionary xd = agent.getXapiDictionary();
        List<ConceptWord> retval = new ArrayList<>();
        retval.addAll(xd.getDbConceptWords().values());
        return retval;
    }

    
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        XapiDictionary xd = agent.getXapiDictionary();
        List<ConceptWord> list = qh_ALL_CONCEPT_WORDS.applyQuery(agent, query);
        fmt.addH2("Concept words: " + list.size(), "class=identifier");
        Map<Character, List<ConceptWord>> map = new HashMap<>();
        // initialize the characterlist
        List<Character> characterList = new ArrayList<>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            characterList.add(letter);
        }
        characterList.add('\"');
        // initialize the maps
        for (char letter : characterList) {
            map.put(letter, new ArrayList<ConceptWord>());
        }
        //
        // put all non-negative concepts in the maps
        for (ConceptWord conceptWord : list) {
            if (WordNamingConventions.isANegation(conceptWord)) {
                continue;
            }
            char startsWith =
                    WordNamingConventions.getConceptWordLetter(conceptWord);
            List<ConceptWord> letterList = map.get(startsWith);
            if (letterList == null) {
                TextUi.abort("generate can't find its place:" + conceptWord);
            }
            letterList.add(conceptWord);
        }
        // sort the maps alphabetically
        for (char letter : characterList) {
            List<ConceptWord> letterList = map.get(letter);
            Collections.sort(letterList, new Comparator<ConceptWord>() {

                @Override
                public int compare(ConceptWord arg0, ConceptWord arg1) {
                    String root0 = arg0.getTextFormat();
                    String root1 = arg1.getTextFormat();
                    return root0.compareTo(root1);
                }

            });
        }
        // ok, create the web page
        for (char letter : characterList) {
            List<ConceptWord> letterList = map.get(letter);
            if (letterList.isEmpty()) {
                continue;
            }
            if (letter == '\"') {
                fmt.addH2("Proper nouns");
            } else {
                fmt.addH2("" + letter);
            }
            for (ConceptWord conceptWord : letterList) {
                fmt.openP();
                PwQueryLinks.linkToConceptWord(fmt, agent, query, conceptWord);
                ConceptWord notCW = xd.getNegation(conceptWord);
                if (notCW != null) {
                    fmt.add("-");
                    PwQueryLinks.linkToConceptWord(fmt, agent, query, notCW);
                }
                fmt.closeP();
            }
        }
    }

}
