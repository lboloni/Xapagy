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
package org.xapagy.xapi;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.metaverbs.MetaVerbHelper;

/**
 * 
 * A dictionary: maps XE Pidging words to participations of concepts and verbs
 * 
 * @author Ladislau Boloni
 * Created on: Aug 18, 2010
 */
public class XapiDictionary implements Serializable {

    private static final long serialVersionUID = -6371119142864228380L;
    private Agent agent;
    private Map<String, ConceptWord> dbConceptWords = new HashMap<>();

    private Map<String, VerbWord> dbVerbWords = new HashMap<>();

    public XapiDictionary(Agent agent) {
        this.agent = agent;
    }

    /**
     * Adds a new concept word
     * 
     * @param name
     * @param co
     * @param comment
     */
    public void addConceptWord(String name, ConceptOverlay co, String comment) {
        ConceptWord cw = new ConceptWord(agent, name, co);
        cw.setComment(comment);
        dbConceptWords.put(cw.getTextFormat(), cw);
    }

    /**
     * Adds a new verb word
     * 
     * @param name
     * @param verbParticipation
     */
    public void addVerbWord(String name, VerbOverlay vo, String comment) {
        VerbWord vw = new VerbWord(agent, name, vo);
        vw.setComment(comment);
        dbVerbWords.put(name, vw);
    }

    /**
     * Creates the negation for the word name
     * 
     * @param wordName
     */
    public void createNegationOfConceptWord(String wordName) {
        ConceptOverlay ovr = dbConceptWords.get(wordName).getCo();
        ConceptOverlay negationOvr = new ConceptOverlay(agent);
        for (SimpleEntry<Concept, Double> entry : ovr.getList()) {
            Concept c = entry.getKey();
            agent.getConceptDB();
            String negationName =
                    AbstractConceptDB.getNegationName(c.getName());
            Concept negation = agent.getConceptDB().getConcept(negationName);
            negationOvr.addSpecificEnergyImpacted(negation, entry.getValue());
        }
        String negationTextForm = Hardwired.CONCEPT_PREFIX_NEGATION + wordName;
        ConceptWord cwNegative =
                new ConceptWord(agent, negationTextForm, negationOvr);
        dbConceptWords.put(cwNegative.getTextFormat(), cwNegative);
    }

    /**
     * Creates the negation for the word name
     * 
     * @param wordName
     */
    public void createNegationOfVerbWord(String wordName) {
        VerbOverlay ovr = dbVerbWords.get(wordName).getVo();
        VerbOverlay negationOvr = new VerbOverlay(agent);
        for (SimpleEntry<Verb, Double> entry : ovr.getList()) {
            Verb c = entry.getKey();
            // meta verbs are not negated, other ones are
            if (MetaVerbHelper.isMetaVerb(c, agent)) {
                negationOvr.addSpecificEnergyImpacted(c, entry.getValue());
            } else {
                agent.getVerbDB();
                String negationName =
                        AbstractConceptDB.getNegationName(c.getName());
                Verb negation = agent.getVerbDB().getConcept(negationName);
                negationOvr.addSpecificEnergyImpacted(negation,
                        entry.getValue());
            }
        }
        String negationTextForm = Hardwired.CONCEPT_PREFIX_NEGATION + wordName;
        VerbWord vwNegation =
                new VerbWord(agent, negationTextForm, negationOvr);
        dbVerbWords.put(vwNegation.getTextFormat(), vwNegation);
    }

    /**
     * Returns the concept overlay associated with the word, or null there is
     * none
     * 
     * @param name
     * @return
     */
    public ConceptOverlay getCoForWord(String name) {
        ConceptOverlay retval = new ConceptOverlay(agent);
        ConceptWord cw = dbConceptWords.get(name);
        if (cw != null) {
            retval.addOverlay(cw.getCo());
            return retval;
        }
        return null;
    }

    /**
     * Returns all the concept words
     * 
     * @return
     */
    public List<String> getConceptWords() {
        List<String> retval = new ArrayList<>();
        retval.addAll(dbConceptWords.keySet());
        return retval;
    }

    /**
     * @return the dbConceptWords
     */
    public Map<String, ConceptWord> getDbConceptWords() {
        return dbConceptWords;
    }

    /**
     * @return the dbVerbWords
     */
    public Map<String, VerbWord> getDbVerbWords() {
        return dbVerbWords;
    }

    /**
     * Returns the negation of a concept word - it assumes it is not a negation
     * yet
     * 
     * @param cw
     * @return
     */
    public ConceptWord getNegation(ConceptWord cw) {
        String negationTextForm =
                Hardwired.CONCEPT_PREFIX_NEGATION + cw.getTextFormat();
        ConceptWord retval = dbConceptWords.get(negationTextForm);
        return retval;
    }

    /**
     * Returns the negation of a concept word - it assumes it is not a negation
     * yet
     * 
     * @param vw
     * @return
     */
    public VerbWord getNegation(VerbWord vw) {
        String negationTextForm =
                Hardwired.CONCEPT_PREFIX_NEGATION + vw.getTextFormat();
        VerbWord retval = dbVerbWords.get(negationTextForm);
        return retval;
    }

    /**
     * Returns all the verb words
     * 
     * @return
     */
    public List<String> getVerbWords() {
        List<String> retval = new ArrayList<>();
        retval.addAll(dbVerbWords.keySet());
        return retval;
    }

    /**
     * Returns the verb overlay associated with the word.
     * 
     * Currently it is not context dependent.
     * 
     * @param name
     * @return
     */
    public VerbOverlay getVoForWord(String name) {
        VerbOverlay retval = new VerbOverlay(agent);
        VerbWord vw = dbVerbWords.get(name);
        if (vw == null) {
            // TextUi.errorPrint("No dictionary entry for verb word: " + name);
            // throw new Error("No dictionary entry for verb word: " + name);
            return null;
        }
        retval.addOverlay(vw.getVo());
        return retval;
    }

}
