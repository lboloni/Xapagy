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
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptNamingConventions;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.metaverbs.AbstractSaMvRelation;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwVerbOverlay;
import org.xapagy.xapi.XapiParserException;

/**
 * Functionality for verbalizing a VO
 * 
 * @author Ladislau Boloni
 * Created on: Oct 3, 2014
 */
public class VerbalizeVo {

    /**
     * Creates a verbalized form of the verb, should be suitable to Xapi
     * reprocessing. We need to pass on the full verb instance as well, because
     * some of the decisions are based on the full VI.
     *
     * @param agent
     * @param vo
     *            - the verb overlay which needs to be verbalized
     * @param vi
     *            - the vi in which it takes place
     * @return
     */
    public static String verbalizeVerb(Agent agent, VerbOverlay vo,
            VerbInstance vi) {
        //
        // Special case: relation. Relations can not be parsed, as they only
        // appear as create-relation etc.
        //
        if (ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
            // FIXME!!!
            String retval = "rel: ";
            retval += xwVerbOverlay.ppRelationLabel(vo, agent);
            return retval;
        }
        //
        // Case: relation-manipulation
        //
        if (ViClassifier.decideViClass(ViClass.RELATION_MANIPULATION, vi,
                agent)) {
            SimpleEntry<VerbOverlay, VerbOverlay> split =
                    AbstractSaMvRelation.extractRelation(agent, vo);
            VerbOverlay voRelations = split.getKey();
            VerbOverlay voResidue = split.getValue();
            // print the relations
            String retval = "";
            String relation = xwVerbOverlay.ppRelationLabel(voRelations, agent);
            boolean hasThus = false;
            for (SimpleEntry<Verb, Double> entry : voResidue.getList()) {
                String verbName = entry.getKey().getName();
                switch (verbName) {
                case Hardwired.VM_ACTION_MARKER:
                case Hardwired.VM_SUCCESSOR:
                    // these are expected to be in a RELATION_MANIPULATION class
                    break;
                case Hardwired.VM_CREATE_RELATION:
                    retval += Hardwired.VO_CREATE_RELATION + " ";
                    break;
                case Hardwired.VM_CREATE_ONE_SOURCE_RELATION:
                    retval += Hardwired.VO_CREATE_ONE_SOURCE_RELATION + " ";
                    break;
                case Hardwired.VM_REMOVE_RELATION:
                    retval += Hardwired.VO_REMOVE_RELATION + " ";
                    break;
                case Hardwired.VM_THUS:
                    hasThus = true;
                    break;
                default:
                    retval += ConceptNamingConventions.PREFIX_WORD_VERB
                            + verbName;
                    break;
                }
            }
            if (hasThus) {
                retval = Hardwired.VO_THUS + " " + retval;
            }
            retval += relation;
            return retval;
        }
        //
        // Case: action
        //
        if (ViClassifier.decideViClass(ViClass.ACTION, vi, agent)) {
            String retval = "";
            boolean hasThus = Hardwired.contains(agent, vo, Hardwired.VM_THUS);
            VerbOverlay root = Hardwired.scrapeVerbsFromVO(agent, vo,
                    Hardwired.VM_ACTION_MARKER, Hardwired.VM_SUCCESSOR,
                    Hardwired.VM_THUS);
            List<SimpleEntry<String, Double>> list =
                    VrbOverlay.getWordsForVerbOverlay(agent, root);
            if (!list.isEmpty()) {
                retval = list.get(0).getKey();
            } else {
                // if empty, fall back on Xapi direct access tricks
                boolean first = true;
                for (SimpleEntry<Verb, Double> entry : root.getList()) {
                    if (first) {
                        retval += ConceptNamingConventions.PREFIX_WORD_ACTION_VO
                                + entry.getKey().getName();
                        first = false;
                    } else {
                        retval +=
                                " " + ConceptNamingConventions.PREFIX_WORD_VERB
                                        + entry.getKey().getName();
                    }
                }
            }
            if (hasThus) {
                retval += " thus";
            }
            return retval;
        }
        //
        // Dealing with some of the specific cases
        //
        if (VerbalizeVo.testXapiForm(agent, vo, "create-instance")) {
            return "create-instance";
        }
        if (VerbalizeVo.testXapiForm(agent, vo, "is-only-scene")) {
            return "is-only-scene";
        }
        VerbOverlay voDoesNothing =
                VerbOverlay.createVO(agent, "v_does_nothing");
        if (vo.sameCoverage(voDoesNothing)) {
            return "w_v_does_nothing";
        }
        //
        // If we got here, we don't know what kind of verb is this
        //
        TextUi.errorPrint(
                "Verbalizing the verb of a which doesn't seem to be of any known type");
        List<SimpleEntry<String, Double>> list =
                VrbOverlay.getWordsForVerbOverlay(agent, vo);
        if (list.isEmpty()) {
            return "[verbalize failed for ] "
                    + xwVerbOverlay.xwDetailed(new TwFormatter(), vo, agent);
        }
        return list.get(0).getKey();
    }

    /**
     * This function allows us to test whether a given vo has the same coverage
     * as the specified VO
     * 
     * @return
     */
    public static boolean testXapiForm(Agent agent, VerbOverlay vo,
            String xapiString) {
        VerbOverlay test = null;
        try {
            test = agent.getXapiParser().parseVo(xapiString);
        } catch (XapiParserException e) {
            TextUi.abort("Should not happen here: " + e.toString());
        }
        if (test.sameCoverage(vo)) {
            return true;
        }
        return false;
    }

}
