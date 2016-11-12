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
package org.xapagy.concepts.operations;

import java.util.AbstractMap.SimpleEntry;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.LatexFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * Tests the similarity functions for overlays.
 * 
 * @author Ladislau Boloni
 * Created on: Jan 30, 2013
 */
public class testCoverageCo {

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
        ConceptOverlay coFocus = null;
        ConceptOverlay coShadow = null;
        FormatTable ft = new FormatTable(40, 40, 10, 10);
        ft.header("coFocus", "coShadow", "scoreCoverage", "decideSim");
        lf.beginTabular("|p{0.75cm}|p{5cm}|p{5cm}|p{2cm}|p{2cm}|");
        lf.add("\\hline");
        lf.addTableLine("No.", "coFocus", "coShadow", "scoreCoverage",
                "decideSim");
        lf.add("\\hline");
        //
        // Overlays of a single concept
        //
        // one independent base attribute: itself
        coFocus = ConceptOverlay.createCO(agent, "c_bai1");
        coShadow = ConceptOverlay.createCO(agent, "c_bai1");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // one independent base attribute: itself and negation
        coFocus = ConceptOverlay.createCO(agent, "c_bai1");
        coShadow = ConceptOverlay.createCO(agent, "not-c_bai1");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // proper names
        coFocus = ConceptOverlay.createCO(agent, "\"PN1\"");
        coShadow = ConceptOverlay.createCO(agent, "\"PN1\"");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // two different independent base attributes
        coFocus = ConceptOverlay.createCO(agent, "c_bai1");
        coShadow = ConceptOverlay.createCO(agent, "c_bai2");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // two BAO
        coFocus = ConceptOverlay.createCO(agent, "c_bao3_1");
        coShadow = ConceptOverlay.createCO(agent, "c_bao3_1");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // a BAI and a CMI
        coFocus = ConceptOverlay.createCO(agent, "c_bai1");
        coShadow = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // two CMIs of the same category
        coFocus = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        coShadow = ConceptOverlay.createCO(agent, "c_cat0_cmi2");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // two CMOs of the same category
        coFocus = ConceptOverlay.createCO(agent, "c_cot3_cmo1");
        coShadow = ConceptOverlay.createCO(agent, "c_cot3_cmo2");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        //
        // Overlays of two concepts
        //
        // one bai1 matching, plus another in the focus
        coFocus = ConceptOverlay.createCO(agent, "c_bai1", "c_bai2");
        coShadow = ConceptOverlay.createCO(agent, "c_bai1");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // one bai1 matching, plus another in the shadow
        coFocus = ConceptOverlay.createCO(agent, "c_bai1");
        coShadow = ConceptOverlay.createCO(agent, "c_bai1", "c_bai2");
        tst(ft, lf, agent, coFocus, coShadow, -1);

        // two CMIs of the same category, plus a common cmi
        coFocus = ConceptOverlay.createCO(agent, "c_bai1", "c_cat0_cmi1");
        coShadow = ConceptOverlay.createCO(agent, "c_bai1", "c_cat0_cmi2");
        tst(ft, lf, agent, coFocus, coShadow, -1);
        // two CMOs of the same category, plus a common cmi
        coFocus = ConceptOverlay.createCO(agent, "c_bai1", "c_cot3_cmo1");
        coShadow = ConceptOverlay.createCO(agent, "c_bai1", "c_cot3_cmo2");
        tst(ft, lf, agent, coFocus, coShadow, -1);
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
     * @param coFocus
     *            - the focus co - the first parameter for the metrics
     * @param coShadow
     *            - the shadow co - the second parameter for the metrics
     * @param expected
     *            - the expected value of the text row, if null, don't test
     */
    public void tst(FormatTable ft, LatexFormatter lf, Agent agent,
            ConceptOverlay coFocus, ConceptOverlay coShadow, double expected) {
        double similarity = Coverage.scoreCoverage(coShadow, coFocus);
        boolean decideSimilar = Coverage.decideSimilarity(coFocus, coShadow);
        ft.wrappedRow(PrettyPrint.ppConcise(coFocus, agent),
                PrettyPrint.ppConcise(coShadow, agent), similarity,
                decideSimilar);
        // if (expected != null) {
        // assertEquals(expected, textRow);
        // }
        lf.addTableLine("" + lineCount++,
                PrettyPrint.ppConcise(coFocus, agent),
                PrettyPrint.ppConcise(coShadow, agent), similarity,
                decideSimilar);
        // if the expected value is not -1, test for the expected similarity
        // value
        if (expected != -1.0 && doTest) {
            Assert.assertEquals(expected, similarity, 0.1);
        }
    }

}
