/*
   This file is part of the Xapagy project
   Created on: Mar 2, 2013
 
   org.xapagy.concepts.operations.testVerbSimilarity
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.concepts.operations;

import java.util.AbstractMap.SimpleEntry;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.latex.LatexFormatter;
import org.xapagy.ui.prettyprint.FormatTable;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * Tests the similarity functions for verbs. We are especially interested here
 * on how different relation verbs and action verbs are similar to each other.
 * 
 * 
 * @author Ladislau Boloni
 * 
 */
public class testCoverageVo {

    public boolean doTest = true;
    /** used to number the examples in the latex table */
    public int lineCount = 1;

    /**
     * Performs a series of tests for the similarity metrics (scoreSimilarity,
     * scoreSpecific and decideSimilar) on an artificial domain.
     * 
     * The validation tests are not implemented yet.
     * 
     * @param doTest
     *            - if set to true, it performs validation tests and throws
     *            failure. If set to false, if just generates the output
     * 
     * @return a pair of the results formatted as simple text and latex
     * 
     */
    public SimpleEntry<String, String> coverageExperiments() {
        LatexFormatter lf = new LatexFormatter();
        Runner r = ArtificialDomain.runnerArtificialDomain();
        Agent agent = r.agent;
        VerbOverlay voFocus = null;
        VerbOverlay voShadow = null;
        FormatTable ft = new FormatTable(40, 40, 10, 10);
        ft.header("voFocus", "voShadow", "scoreSimilarity", "decideSim");
        lf.beginTabular("|p{0.75cm}|p{5cm}|p{5cm}|p{2cm}|p{2cm}|");
        lf.add("\\hline");
        lf.addTableLine("No.", "voFocus", "voShadow", "scoreCoverage",
                "decideSim");
        lf.add("\\hline");
        //
        // Overlays of a single concept
        //
        // one independent base attribute: itself
        voFocus = VerbOverlay.createVO(agent, "v_av0");
        voShadow = VerbOverlay.createVO(agent, "v_av0");
        tst(ft, lf, agent, voFocus, voShadow, -1);
        // end of the table
        lf.add("\\hline");
        ft.endTable();
        lf.endTabular();
        return new SimpleEntry<>(ft.toString(), lf.toString());
    }

    /**
     * Runs
     */
    @Test
    public void test() {
        SimpleEntry<String, String> retval = coverageExperiments();
        TextUi.println(retval.getKey());
    }

    /**
     * Calculates the various similarity values and adds them to a row
     * 
     * @param ft
     *            - the text table to which we add the value
     * @param lf
     *            - the latex formatter to which we add the values in a row
     * @param voFocus
     *            - the focus co - the first parameter for the metrics
     * @param voShadow
     *            - the shadow co - the second parameter for the metrics
     * @param expected
     *            - the expected value of the text row, if null, don't test
     */
    public void tst(FormatTable ft, LatexFormatter lf, Agent agent,
            VerbOverlay voFocus, VerbOverlay voShadow, double expected) {
        double similarity = Coverage.scoreCoverage(voShadow, voFocus);
        boolean decideSimilar = Coverage.decideSimilarity(voFocus, voShadow);
        ft.wrappedRow(PrettyPrint.ppConcise(voFocus, agent),
                PrettyPrint.ppConcise(voShadow, agent), similarity,
                decideSimilar);
        // if (expected != null) {
        // assertEquals(expected, textRow);
        // }
        lf.addTableLine("" + lineCount++,
                PrettyPrint.ppConcise(voFocus, agent),
                PrettyPrint.ppConcise(voShadow, agent), similarity,
                decideSimilar);
        // if the expected value is not -1, test for the expected similarity
        // value
        if (expected != -1.0 && doTest) {
            Assert.assertEquals(expected, similarity, 0.1);
        }
    }

}
