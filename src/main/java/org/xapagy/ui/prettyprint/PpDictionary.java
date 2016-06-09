/*
   This file is part of the Xapagy project
   Created on: Oct 10, 2011
 
   org.xapagy.ui.prettyprint.PpDictionary
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.xapagy.agents.Agent;
import org.xapagy.xapi.XapiDictionary;

/**
 * @author Ladislau Boloni
 * 
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
