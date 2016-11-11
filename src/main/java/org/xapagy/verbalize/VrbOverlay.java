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
 * Created on: Oct 8, 2011
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
