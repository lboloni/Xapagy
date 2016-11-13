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
import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * Created on: Mar 12, 2014
 */
public class testVrbOverlay {

    /**
     * Prints the verbs, the VO created and the candidate verbalizations
     */
    private static String printCandidates(Agent agent, List<String> verblist,
            VerbOverlay vo, List<SimpleEntry<String, Double>> verbalizations) {
        Formatter fmt = new Formatter();
        fmt.add("The verb list");
        fmt.addIndented(verblist);
        fmt.add("VerbOverlay");
        fmt.addIndented(PrettyPrint.ppConcise(vo, agent));
        fmt.add("Candidate verbalizations");
        fmt.indent();
        for (SimpleEntry<String, Double> candidate : verbalizations) {
            fmt.is(PrettyPrint.ppConcise(candidate.getKey(), agent),
                    candidate.getValue());
        }
        return fmt.toString();
    }

    @Test
    public void testWordsForVerbOverlay() {
        String description = "basic";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        List<String> verbList = new ArrayList<>();
        verbList.add("v_does_nothing");
        // verbList.add("v_av10");
        verbList.add("vm_successor");
        verbList.add("vm_action_marker");
        VerbOverlay vo = VerbOverlay.createVO(r.agent, verbList);
        List<SimpleEntry<String, Double>> wordsForVerbOverlay =
                VrbOverlay.getWordsForVerbOverlay(r.agent, vo);
        String text =
                printCandidates(r.agent, verbList, vo, wordsForVerbOverlay);
        TextUi.println(text);
        TestHelper.testDone();

    }

}
