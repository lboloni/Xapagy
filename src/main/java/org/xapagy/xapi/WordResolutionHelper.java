/*
   This file is part of the Xapagy project
   Created on: Feb 22, 2011
 
   org.xapagy.xapi.WordResolutionHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.util.Arrays;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptNamingConventions;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.learning.LearnProperNames;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
 */
public class WordResolutionHelper {

    /**
     * Resolves a list of words into a concept overlay.
     * 
     * Normally these words are supposed to be words defined in the Xapi
     * dictionary.
     * 
     * Right now, we are considering two other cases:
     * 
     * <ul>
     * <li>If the word is a proper noun, and is not in the dictionary, creates
     * an associated concept.
     * <li>If the word has a PREFIX_WORD prefix, treat the rest fo the word as a
     * direct access to a concept name
     * </ul>
     * 
     * @param agent
     * @param words
     *            - a list of words that are supposed to match to
     * 
     * @return
     * @throws XapiParserException - if it cannot resolve a word
     */
    public static ConceptOverlay resolveWordsToCo(Agent agent,
            List<String> words) throws XapiParserException {
        XapiDictionary dict = agent.getXapiDictionary();
        ConceptOverlay retval = new ConceptOverlay(agent);
        for (String word : words) {
            // skip the labels
            if (word.startsWith(XapiParser.CO_LABEL_PREFIX)) {
                continue;
            }
            ConceptOverlay co = dict.getCoForWord(word);
            //
            if (co == null
                    && word.startsWith(ConceptNamingConventions.PREFIX_WORD)) {
                String conceptName = word.substring(
                        ConceptNamingConventions.PREFIX_WORD.length());
                co = ConceptOverlay.createCO(agent, conceptName);
            }
            // if it still cannot be found, try to learn it
            if (co == null) {
                co = LearnProperNames.learnTheWord(agent, word);
            }
            // if we could not learn it, throw an exception
            if (co == null) {
                throw new XapiParserException("Could not resolve concept word: "
                        + word + " in the parsing of " + words.toString());
            }
            retval.addOverlay(co);
        }
        return retval;
    }

    /**
     * Syntactic sugar for resolveWordsToCo
     * 
     * @param agent
     * @param words
     * @return
     * @throws XapiParserException 
     */
    public static ConceptOverlay resolveWordsToCo(Agent agent,
            String... words) throws XapiParserException {
        return WordResolutionHelper.resolveWordsToCo(agent,
                Arrays.asList(words));
    }

    /**
     * Resolves a list of words into a verb overlay. Normally these words are
     * supposed to be words defined in the Xapi dictionary.
     * 
     * Right now, we are considering one other case:
     * 
     * <ul>
     * <li>If the word has a PREFIX_WORD prefix, treat the rest of the word as a
     * direct access to a verb name
     * </ul>
     * 
     * @param agent
     * @param words
     *            - a list of words
     * 
     * @return
     * @throws XapiParserException
     *             if a word cannot be found
     */
    public static VerbOverlay resolveWordsToVo(Agent agent, List<String> words)
            throws XapiParserException {
        XapiDictionary dict = agent.getXapiDictionary();
        VerbOverlay retval = new VerbOverlay(agent);
        for (String word : words) {
            // skip the labels
            if (word.startsWith(XapiParser.VO_LABEL_PREFIX)) {
                retval.addFullLabel(word, agent);
                continue;
            }
            VerbOverlay vo = null;
            // FIXME: why is this throwing an error?
            try {
                vo = dict.getVoForWord(word);
            } catch (Error e) {
                TextUi.println("Could not find Vo for " + word);
                vo = null;
            }
            if (vo == null) {
                //
                // if starts with PREFIX_WORD_VERB wv_ , direct access to the
                // verb
                //
                if (word.startsWith(
                        ConceptNamingConventions.PREFIX_WORD_VERB)) {
                    String verbName = word.substring(
                            ConceptNamingConventions.PREFIX_WORD_VERB.length());
                    vo = VerbOverlay.createVO(agent, verbName);
                }
                //
                // if starts with PREFIX_WORD_ACTION_VO wa_, direct access to
                // the
                // action VO
                //
                if (word.startsWith(
                        ConceptNamingConventions.PREFIX_WORD_ACTION_VO)) {
                    String verbName = word.substring(
                            ConceptNamingConventions.PREFIX_WORD_ACTION_VO
                                    .length());
                    vo = VerbOverlay.createVO(agent, verbName);
                    VerbOverlay voAction =
                            dict.getVoForWord(Hardwired.VO_ACTION_VERB);
                    vo.addOverlay(voAction);
                }
                //
                // if starts with PREFIX_WORD_CREATE_RELATION_VO wcr_
                //
                if (word.startsWith(
                        ConceptNamingConventions.PREFIX_WORD_CREATE_RELATION_VO)) {
                    String verbName = word.substring(
                            ConceptNamingConventions.PREFIX_WORD_CREATE_RELATION_VO
                                    .length());
                    vo = VerbOverlay.createVO(agent, verbName);
                    VerbOverlay voCreateRelation =
                            dict.getVoForWord(Hardwired.VO_CREATE_RELATION);
                    vo.addOverlay(voCreateRelation);
                }
                //
                // if starts with PREFIX_WORD_REMOVE_RELATION_VO wrr_
                //
                if (word.startsWith(
                        ConceptNamingConventions.PREFIX_WORD_REMOVE_RELATION_VO)) {
                    String verbName = word.substring(
                            ConceptNamingConventions.PREFIX_WORD_REMOVE_RELATION_VO
                                    .length());
                    vo = VerbOverlay.createVO(agent, verbName);
                    VerbOverlay voRemoveRelation =
                            dict.getVoForWord(Hardwired.VO_REMOVE_RELATION);
                    vo.addOverlay(voRemoveRelation);
                }
            }
            if (vo == null) {
                throw new XapiParserException("Could not resolve verb word: "
                        + word + " in the parsing of " + words.toString());
            }
            retval.addOverlay(vo);
        }
        return retval;
    }
}
