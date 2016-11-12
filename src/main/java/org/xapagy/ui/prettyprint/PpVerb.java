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

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Sep 27, 2011
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
