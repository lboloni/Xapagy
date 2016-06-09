/*
   This file is part of the Xapagy project
   Created on: Mar 2, 2013
 
   org.xapagy.concepts.operations.testVerbIncompatibility
 
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
 * Tests the incompatibility between verbs. This has mostly applications in the
 * impact of relation creations (and potentially, in reference by relations)
 * 
 * @author Ladislau Boloni
 * 
 */
public class testIncompatibilityVo {

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
    public SimpleEntry<String, String> incompatibilityExperiments() {
        Runner r = ArtificialDomain.createAabConcepts();

        Agent agent = r.agent;
        VerbOverlay voSource = null;
        VerbOverlay voReference = null;
        LatexFormatter lf = new LatexFormatter();
        FormatTable ft = new FormatTable(20, 20, 20, 20, 20, 10);
        ft.header("voSource", "voReference", "negative impact energy",
                "over-impact energy", "incompatibility score", "decision");
        lf.beginTabular("|p{0.75cm}|p{4.5cm}|p{4.5cm}|p{1.5cm}|p{1.5cm}|p{1.5cm}|p{1cm}|");
        lf.add("\\hline");
        lf.addTableLine("No.", "coSource", "coReference",
                "negative impact energy", "over-impact energy", "score",
                "decision");
        lf.add("\\hline");

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
        testIncompatibilityVo.makeActionVO(agent, voSource);
        voReference = VerbOverlay.createVO(agent, "v_av0");
        testIncompatibilityVo.makeActionVO(agent, voReference);
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
        SimpleEntry<String, String> val = incompatibilityExperiments();
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
    public void
            tst(FormatTable ft, LatexFormatter lf, Agent agent,
                    VerbOverlay voSource, VerbOverlay voReference,
                    double expectedScore) {
        SimpleEntry<Double, Double> components =
                Incompatibility
                        .incompatibilityComponents(voSource, voReference);
        double score =
                Incompatibility.scoreIncompatibility(voSource, voReference);
        boolean decision =
                Incompatibility.decideIncompatibility(voSource, voReference);
        ft.wrappedRow(PrettyPrint.ppConcise(voSource, agent),
                PrettyPrint.ppConcise(voReference, agent), components.getKey(),
                components.getValue(), score, decision);
        lf.addTableLine("" + lineCount++,
                PrettyPrint.ppConcise(voSource, agent),
                PrettyPrint.ppConcise(voReference, agent), components.getKey(),
                components.getValue(), score, decision);
        if (expectedScore >= 0) {
            Assert.assertEquals("Incompatibility not as expected",
                    expectedScore, score, expectedScore * 0.03);
        }
    }

}
