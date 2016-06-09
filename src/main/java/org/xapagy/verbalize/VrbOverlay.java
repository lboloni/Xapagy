/*
   This file is part of the Xapagy project
   Created on: Oct 8, 2011
 
   org.xapagy.verbalize.VrbOverlay
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.concepts.operations.Coverage;
import org.xapagy.concepts.operations.Incompatibility;
import org.xapagy.util.SimpleEntryComparator;

/**
 * The verbalization of overlays
 * 
 * @author Ladislau Boloni
 * 
 */
public class VrbOverlay {

    /**
     * Returns a list of words matching the overlay, sorted with the better
     * match first
     * 
     * @param cr
     *            the overlay to be matched
     * @param forbiddenWords
     *            words which should not be considered
     * @param allowExtra
     *            - allows extra concepts in the words
     * 
     * @return
     */
    public static List<SimpleEntry<String, Double>> getWordsForConceptOverlay(
            Agent agent, ConceptOverlay cr, List<String> forbiddenWords,
            boolean allowExtra) {
        List<SimpleEntry<String, Double>> retval = new ArrayList<>();
        for (String word : agent.getXapiDictionary().getConceptWords()) {
            if (forbiddenWords.contains(word)) {
                continue;
            }
            ConceptOverlay testCr =
                    agent.getXapiDictionary().getCoForWord(word);
            //
            // checks whether the proposed words introduce extra concepts
            //
            if (!allowExtra) {
                ConceptOverlay sum = new ConceptOverlay(agent);
                sum.addOverlay(testCr);
                sum.addOverlay(cr, -10.0);
                if (sum.getTotalEnergy() > 0.0) {
                    continue;
                }
            }
            double match = cr.overlapEnergy(testCr);
            if (match > 0.0) {
                retval.add(new SimpleEntry<>(word, match));
            }
        }
        Comparator<SimpleEntry<String, Double>> comp =
                new SimpleEntryComparator<>();
        comp = Collections.reverseOrder(comp);
        Collections.sort(retval, comp);
        return retval;
    }

    /**
     * Returns a list of words matching the VO, sorted with the better match
     * first.
     * 
     * NOTES:
     * 
     * This function first scrapes the meta-verbs, so it will not work for them.
     * 
     * The function does not consider the implicit verbs forms in Xapi (eg
     * wa_v_...) this will need to be handled separately.
     * 
     * @return
     */
    public static List<SimpleEntry<String, Double>> getWordsForVerbOverlay(
            Agent agent, VerbOverlay vo) {
        // VerbOverlay scrapedVo = MetaVerbHelper.removeMetaVerbs(vo, agent);
        List<SimpleEntry<String, Double>> retval = new ArrayList<>();
        for (String word : agent.getXapiDictionary().getVerbWords()) {
            // Very specific thing: ignore these, because they are the same as
            // their non-negated
            // versions (negated meta-verbs)
            if (word.equals("not-changes") || word.equals("not-is-a")) {
                continue;
            }
            VerbOverlay testCr = agent.getXapiDictionary().getVoForWord(word);
            // double match = vo.overlapEnergy(testCr);
            double coverage = Coverage.scoreCoverage(vo, testCr);
            double incompatibility =
                    Incompatibility.scoreIncompatibility(vo, testCr);
            double match = coverage - incompatibility;
            if (match > 0.0) {
                retval.add(new SimpleEntry<>(word, match));
            }
        }
        Comparator<SimpleEntry<String, Double>> comp =
                new SimpleEntryComparator<>();
        comp = Collections.reverseOrder(comp);
        Collections.sort(retval, comp);
        return retval;
    }

}
