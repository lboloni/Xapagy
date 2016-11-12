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
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.reference.rrVoCompatibility;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.LatexFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * Created on: Aug 1, 2013
 */
public class testIncCovResVo {
    /**
     * Makes the verb overlay an action VO (must correspond to the definition
     * 
     * @param vo
     */
    public static void makeActionVO(Agent agent, VerbOverlay vo) {
        VerbOverlay voAddon =
                agent.getXapiDictionary().getVoForWord("ActionVerb");
        vo.addOverlay(voAddon);
    }

    public boolean doTest = true;

    /** used to number the examples in the latex table */
    public int lineCount = 1;

    /**
     * for the incompatibility between concepts
     */
    public SimpleEntry<String, String> experiments() {
        Runner r = ArtificialDomain.runnerArtificialAutobiography();

        Agent agent = r.agent;
        VerbOverlay voSource = null;
        VerbOverlay voReference = null;
        LatexFormatter lf = new LatexFormatter();
        FormatTable ft = new FormatTable(20, 20, 10, 10, 10, 10, 10);
        ft.header("ovrSrc", "ovrRef", "NIE", "OIE", "scINC", "scCOV", "RC");
        lf.beginTabular("|p{0.5cm}|p{4.5cm}|p{4.5cm}|p{0.75cm}|p{0.75cm}|p{0.75cm}|p{0.75cm}|p{1.25cm}|");
        lf.add("\\hline");
        lf.addTableLine("No.", "voSrc", "voRef", "NIE", "OIE", "incomp",
                "cover", "resconf");
        lf.add("\\hline");
        // r.exec("$DebugHere");
        
        //
        // Cases which happen normally
        //
        // the same independent action verb
        voSource = VerbOverlay.createVO(agent, "v_av0");
        voReference = VerbOverlay.createVO(agent, "v_av0");
        tst(ft, lf, agent, voSource, voReference, 0);
        // different independent action verbs
        voSource = VerbOverlay.createVO(agent, "v_av0");
        voReference = VerbOverlay.createVO(agent, "v_av1");
        tst(ft, lf, agent, voSource, voReference, 0);
        // an action verb and its negation
        voSource = VerbOverlay.createVO(agent, "v_av0");
        voReference = VerbOverlay.createVO(agent, "not-v_av0");
        tst(ft, lf, agent, voSource, voReference, 1.0);
        // same independent action verb, with action
        voSource = VerbOverlay.createVO(agent, "v_av0");
        testIncCovResVo.makeActionVO(agent, voSource);
        voReference = VerbOverlay.createVO(agent, "v_av0");
        testIncCovResVo.makeActionVO(agent, voReference);
        tst(ft, lf, agent, voSource, voReference, 0);

        // end of the table
        ft.endTable();
        lf.add("\\hline");
        lf.endTabular();
        return new SimpleEntry<>(ft.toString(), lf.toString());
    }

    /**
     * Runs
     */
    @Test
    public void test() {
        SimpleEntry<String, String> val = experiments();
        TextUi.println(val);
    }

    /**
     * Calculates the various incompatibility values, adds them to rows in text
     * and latex, and optionally does a junit test
     * 
     * @param ft
     *            the formatted table
     * @param expected
     * @param concepts
     */
    public void tst(FormatTable ft, LatexFormatter lf, Agent agent,
            VerbOverlay voSource, VerbOverlay voReference,
            double expectedIncompatibilityScore) {
        SimpleEntry<Double, Double> incompatibilityComponents =
                Incompatibility
                        .incompatibilityComponents(voSource, voReference);
        double NIE = incompatibilityComponents.getKey();
        double OIE = incompatibilityComponents.getValue();
        double scoreIncompability =
                Incompatibility.scoreIncompatibility(voSource, voReference);
        double scoreCoverage = Coverage.scoreCoverage(voSource, voReference);
        boolean isCompatible =
                rrVoCompatibility.areCompatible(voSource, voReference);
        ft.wrappedRow(PrettyPrint.ppConcise(voSource, agent),
                PrettyPrint.ppConcise(voReference, agent), NIE, OIE,
                scoreIncompability, scoreCoverage, isCompatible);
        if (lf != null) {
            lf.addTableLine("" + lineCount++,
                    PrettyPrint.ppConcise(voSource, agent),
                    PrettyPrint.ppConcise(voReference, agent), NIE, OIE,
                    scoreIncompability, scoreCoverage, isCompatible);
        }
        //
        // Now perform the unit test
        //
        if (expectedIncompatibilityScore >= 0) {
            Assert.assertEquals("Incompatibility not as expected",
                    expectedIncompatibilityScore, scoreIncompability,
                    expectedIncompatibilityScore * 0.03);
        }
    }

}
