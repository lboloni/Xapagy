/*
   This file is part of the Xapagy project
   Created on: Mar 12, 2014
 
   org.xapagy.verbalize.testVrbOverlay
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
