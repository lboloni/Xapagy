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
import org.xapagy.xapi.VerbWord;
import org.xapagy.xapi.WordNamingConventions;
import org.xapagy.xapi.XapiDictionary;

public class qh_ALL_VERB_WORDS implements IQueryHandler {

    /**
     * Applies the query for the collection of concept words
     * 
     * FIXME: implement it with sorting, filtering, cursor
     * 
     * @param query
     * @return
     */
    private static List<VerbWord> applyQuery(Agent agent, RESTQuery query) {
        XapiDictionary xd = agent.getXapiDictionary();
        List<VerbWord> retval = new ArrayList<>();
        retval.addAll(xd.getDbVerbWords().values());
        // FIXME: sort, filter, cut
        return retval;
    }

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        XapiDictionary xd = agent.getXapiDictionary();
        List<VerbWord> list = qh_ALL_VERB_WORDS.applyQuery(agent, query);
        fmt.addH2("Verb words: " + list.size(), "class=identifier");
        Map<Character, List<VerbWord>> map = new HashMap<>();
        // initialize the characterlist
        List<Character> characterList = new ArrayList<>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            characterList.add(letter);
        }
        // initialize the maps
        for (char letter : characterList) {
            map.put(letter, new ArrayList<VerbWord>());
        }
        //
        // put all non-negative verbs in the maps
        for (VerbWord verbWord : list) {
            if (WordNamingConventions.isANegation(verbWord)) {
                continue;
            }
            char startsWith = WordNamingConventions.getVerbWordLetter(verbWord);
            List<VerbWord> letterList = map.get(startsWith);
            if (letterList == null) {
                TextUi.abort("generate can't find its place:" + verbWord);
            }
            letterList.add(verbWord);
        }
        // sort the maps alphabetically
        for (char letter : characterList) {
            List<VerbWord> letterList = map.get(letter);
            Collections.sort(letterList, new Comparator<VerbWord>() {

                @Override
                public int compare(VerbWord arg0, VerbWord arg1) {
                    String root0 = arg0.getTextFormat();
                    String root1 = arg1.getTextFormat();
                    return root0.compareTo(root1);
                }

            });
        }
        // ok, create the web page
        for (char letter : characterList) {
            List<VerbWord> letterList = map.get(letter);
            if (letterList.isEmpty()) {
                continue;
            }
            fmt.addH2("" + letter);
            for (VerbWord verbWord : letterList) {
                fmt.openP();
                PwQueryLinks.linkToVerbWord(fmt, agent, query, verbWord);
                VerbWord notVW = xd.getNegation(verbWord);
                if (notVW != null) {
                    fmt.add("-");
                    PwQueryLinks.linkToVerbWord(fmt, agent, query, notVW);
                }
                fmt.closeP();
            }
        }

        // for (VerbWord verbWord : list) {
        // fmt.openP();
        // PwLinks.linkToVerbWord(fmt, agent, query, verbWord);
        // fmt.closeP();
        // }
    }

}
