/*
   This file is part of the Xapagy project
   Created on: Sep 27, 2011
 
   org.xapagy.ui.prettyprint.PpVerb
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpVerb {

    /**
     * Adds the words which refer to this concept
     * 
     * @param fmt
     * @param verb
     * @param agent
     * @param cdb
     */
    private static void
            addReferringWords(Formatter fmt, Verb verb, Agent agent) {
        // Add the area
        fmt.add("Referring words");
        fmt.indent();
        //
        // Adding the overlaps, decreasing order
        //
        List<String> words = agent.getXapiDictionary().getVerbWords();
        for (String word : words) {
            VerbOverlay voDirect = agent.getXapiDictionary().getVoForWord(word);
            VerbOverlay voImpact = new VerbOverlay(agent);
            voImpact.addOverlayImpacted(voDirect);
            if (voDirect.getEnergy(verb) > 0) {
                fmt.is(word, PrettyPrint.ppConcise(voDirect, agent));
            } else if (voImpact.getEnergy(verb) > 0) {
                fmt.is(".." + word, PrettyPrint.ppConcise(voDirect, agent));
            }
        }
        fmt.deindent();
    }

    public static String ppConcise(Verb verb, Agent agent) {
        return verb.getName();
    }

    /**
     * Detailed printing of a concept: includes area, and impacts
     * 
     * @param concept
     * @param agent
     * @return
     */
    public static String ppDetailed(Verb verb, Agent agent) {
        AbstractConceptDB<Verb> cdb = agent.getVerbDB();
        Formatter fmt = new Formatter();
        fmt.add("Verb: [" + verb.getName() + "]");
        fmt.add("Comment:" + verb.getDocumentation());
        PpConcept.addDetailsOfConcepts(fmt, verb, agent, cdb);
        fmt.indent();
        PpVerb.addReferringWords(fmt, verb, agent);
        fmt.deindent();
        return fmt.toString();
    }

}
