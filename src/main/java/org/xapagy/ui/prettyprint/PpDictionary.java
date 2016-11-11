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

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.xapagy.agents.Agent;
import org.xapagy.xapi.XapiDictionary;

/**
 * @author Ladislau Boloni
 * Created on: Oct 10, 2011
 */
public class PpDictionary {

    /**
     * Basically, falls down on pattern matching
     * 
     * @param word
     * @param pattern
     */
    private static boolean matchesPattern(String word, String pattern) {
        if (pattern == null) {
            return true;
        }
        return Pattern.matches(pattern, word);
    }

    public static String ppConcise(XapiDictionary dictionary, Agent agent) {
        return PpDictionary.ppDetailed(dictionary, agent);
    }

    /**
     * Detailed printing of a concept: includes area, and impacts
     * 
     * @param overlay
     * @param topLevel
     * @return
     */
    public static String ppDetailed(XapiDictionary dictionary, Agent agent) {
        return PpDictionary.ppQuery(dictionary, agent, PrintDetail.DTL_DETAIL,
                true, true, null);
    }

    /**
     * The overall printer for this.
     * 
     * @param dictionary
     * @param agent
     * @param detailLevel
     * @param printWords
     * @param printVerbs
     * @param pattern
     * @return
     */
    public static String ppQuery(XapiDictionary dictionary, Agent agent,
            PrintDetail detailLevel, boolean printWords, boolean printVerbs,
            String pattern) {
        Formatter fmt = new Formatter();
        if (printWords) {
            fmt.add("Concept words");
            fmt.indent();
            List<String> conceptwords = dictionary.getConceptWords();
            Collections.sort(conceptwords);
            for (String word : conceptwords) {
                String temp = word;
                if (!PpDictionary.matchesPattern(word, pattern)) {
                    continue;
                }
                if (word.startsWith("not-")) {
                    continue;
                }
                if (detailLevel == PrintDetail.DTL_DETAIL) {
                    temp =
                            temp
                                    + " = "
                                    + PrettyPrint.ppConcise(
                                            dictionary.getCoForWord(word),
                                            agent);
                }
                fmt.add(temp);
            }
            fmt.deindent();
        }
        if (printVerbs) {
            fmt.add("Verb words");
            fmt.indent();
            List<String> verbwords = dictionary.getVerbWords();
            Collections.sort(verbwords);
            for (String word : verbwords) {
                String temp = word;
                if (!PpDictionary.matchesPattern(word, pattern)) {
                    continue;
                }
                if (word.startsWith("not-")) {
                    continue;
                }
                if (detailLevel == PrintDetail.DTL_DETAIL) {
                    temp =
                            temp
                                    + " = "
                                    + PrettyPrint.ppConcise(
                                            dictionary.getVoForWord(word),
                                            agent);
                }
                fmt.add(temp);
            }
            fmt.deindent();
        }
        return fmt.toString();
    }
}
