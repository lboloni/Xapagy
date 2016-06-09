/*
   This file is part of the Xapagy project
   Created on: Oct 9, 2011

   org.xapagy.learning.LearnProperNames

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.learning;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptDataBaseHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;

/**
 * @author Ladislau Boloni
 *
 */
public class LearnProperNames {

    /**
     * Creates a new proper noun
     *
     * @param agent
     * @param word
     */
    public static ConceptOverlay addProperNounConcept(Agent agent, String word) {
        if (!LearnProperNames.isProperNoun(word)) {
            throw new Error("Proper nouns start with \" - this is " + word);
        }
        ConceptDataBaseHelper<Concept> cdh =
                new ConceptDataBaseHelper<>(agent.getConceptDB(),
                        ConceptDataBaseHelper.ContentType.TYPE_CONCEPT, agent);
        double areaOfProperNames =
                agent.getParameters().get("A_GENERAL", "G_GENERAL",
                        "N_AREA_OF_PROPER_NAMES");
        Concept concept = cdh.createWithArea(word, areaOfProperNames);
        Concept conPropername =
                agent.getConceptDB().getConcept(Hardwired.CC_PROPERNAME);
        cdh.makeImpact(word, conPropername.getName(), agent.getConceptDB()
                .getArea(conPropername) / agent.getConceptDB().getArea(concept));
        ConceptOverlay co = new ConceptOverlay(agent);
        co.addFullEnergy(concept);
        agent.getXapiDictionary().addConceptWord(word, co, "comment: ");
        // PrettyPrint.ppd(co, agent);
        return co;
    }

    /**
     * The Xapagy definition of a proper noun is one which starts with an
     * apostrophe
     *
     * @param name
     * @return
     */
    public static boolean isProperNoun(String name) {
        return name.startsWith("\"");
    }

    /**
     * Learning a new word, only working for proper names currently
     *
     * @param agent
     * @param name
     * @return
     */
    public static ConceptOverlay learnTheWord(Agent agent, String name) {
        ConceptOverlay retval = new ConceptOverlay(agent);
        if (name.startsWith("\"")) {
            ConceptOverlay co =
                    LearnProperNames.addProperNounConcept(agent, name);
            retval.addOverlay(co, 1.0);
            return retval;
        }
        //TextUi.errorPrint("learning new words not implemented yet: " + name);
        //throw new Error("could not find Xapi word '" + name + "'");
        return null;
    }

}
