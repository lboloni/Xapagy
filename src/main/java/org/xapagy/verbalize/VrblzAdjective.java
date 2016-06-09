/*
   This file is part of the Xapagy project
   Created on: Oct 11, 2011
 
   org.xapagy.verbalize.VrbAdjective
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
 */
public class VrblzAdjective {

    /**
     * Goes to the verbal memory, if it finds an exact match, returns it.
     * 
     * @param agent
     * @param co
     * @return
     */
    public static String
            getVerbalMemoryAdjective(Agent agent, ConceptOverlay co) {
        for (VmCoReference vmco : agent.getVerbalMemory().getVmCoReferences()) {
            ConceptOverlay coReference = vmco.getCoAtReference();
            double overlapEnergy = coReference.overlapEnergy(co);
            if (overlapEnergy == co.getTotalEnergy()
                    && overlapEnergy == coReference.getTotalEnergy()) {
                return vmco.getXapiReference().getText();
            }
        }
        return null;
    }

    /**
     * Returns a single word for a concept
     * 
     * @param concept
     * @param forbiddedWords
     * @return
     */
    public static String getWordForConcept(Agent agent, Concept concept,
            List<String> forbiddenWords) {
        ConceptOverlay co = new ConceptOverlay(agent);
        co.addFullEnergyImpacted(concept);
        List<SimpleEntry<String, Double>> list =
                VrbOverlay.getWordsForConceptOverlay(agent, co, forbiddenWords,
                        false);
        if (list.isEmpty()) {
            TextUi.println("Could not find a word to express:" + concept);
            return null;
        }
        return list.get(0).getKey();
    }

    /**
     * Creates a verbalization of a concept overlay (a set of adjectives).
     * 
     * This particular implementation is separation based - it will return a
     * list of items, it will not rely on groups - even if there was a group.
     * 
     * FIXME: a final solution for this would go back and forth between the
     * generated list of words and the concept overlay, until we get the best
     * approximation
     * 
     * TODO: Test for this TODO: Not finding the right word, etc. ...
     * 
     * @param ovr
     * @return
     */
    public static List<String> getWordListForConceptOverlay(Agent agent,
            ConceptOverlay ovr) {
        List<String> wordList = new ArrayList<>();
        for (SimpleEntry<Concept, Double> entry : ovr
                .getSortedByExplicitEnergy()) {
            String newWord =
                    VrblzAdjective.getWordForConcept(agent, entry.getKey(),
                            wordList);
            // skip what we cannot express
            if (newWord != null) {
                wordList.add(newWord);
            }
        }
        return wordList;
    }

    /**
     * Returns as a string the verbalization of an adjective
     * 
     * This particular implementation is separation based - it will return a
     * list of items, it will not rely on groups - even if there was a group.
     * 
     * @param co
     * @param isAdjective
     *            - if it is not an adjective, it will add "the"
     * @return
     */
    public static String verbalizeAdjective(Agent agent, ConceptOverlay co,
            boolean isAdjective) {
        // try the verbal memory for an exact match
        String retval = VrblzAdjective.getVerbalMemoryAdjective(agent, co);
        if (retval != null) {
            return retval;
        }
        // fall back on the verbalization from scratch
        StringBuffer buffer = new StringBuffer();
        List<String> wordList =
                VrblzAdjective.getWordListForConceptOverlay(agent, co);
        boolean hasProperNoun = false;
        for (String word : wordList) {
            if (word.startsWith("\"")) {
                hasProperNoun = true;
            }
            buffer.append(word + " ");
        }
        // remove the last space
        if (buffer.length() > 0) {
            buffer.delete(buffer.length() - 1, buffer.length());
        }
        retval = buffer.toString();
        if (!isAdjective && !hasProperNoun) {
            retval = "the " + retval;
        }
        return retval;
    }

}
