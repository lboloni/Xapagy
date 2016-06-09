/*
   This file is part of the Xapagy project
   Created on: Mar 2, 2013
 
   org.xapagy.activity.shadowmaintenance.testVerbAmLookup
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.latex.LatexFormatter;
import org.xapagy.ui.prettyprint.FormatTable;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * 
 */
public class testAmLookup_Verb {

    public boolean doTest = true;

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
    public SimpleEntry<String, String> lookupVoExperiments() {
        LatexFormatter lf = new LatexFormatter();
        Runner r = ArtificialDomain.createAabConcepts();
        Agent agent = r.agent;
        VerbOverlay voFocus = null;
        FormatTable ft = new FormatTable(50, 50);
        ft.header("voFocus", "voLookup Top5");
        lf.beginTabular("|p{5cm}|p{15cm}|");
        lf.add("\\hline");
        lf.addTableLine("voFocus", "voLookup Top5");
        lf.add("\\hline");
        //
        // Overlays of a single concept
        //
        // c_bai0
        voFocus = VerbOverlay.createVO(agent, "v_av1");
        tst(ft, lf, agent, voFocus, -1.0);
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
        SimpleEntry<String, String> retval = lookupVoExperiments();
        TextUi.println(retval.getKey());
    }

    /**
     * Looks up the coLookup values, and adds the first 5 to the row.
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
            VerbOverlay voFocus, double expectedTop) {
        ViSet lookup = AmLookup.lookupVo(agent.getAutobiographicalMemory(), voFocus, EnergyColors.AM_VI);
        List<String> tops = new ArrayList<>();
        double foundTop = -1;
        int count = 1;
        int maxCount = 5;
        int realCount = lookup.getParticipants().size();
        double totalWeight = lookup.getSum();
        double shownWeight = 0.0;
        for (SimpleEntry<VerbInstance, Double> entry : lookup
                .getDecreasingStrengthList()) {
            VerbInstance instance = entry.getKey();
            double value = entry.getValue();
            shownWeight += value;
            if (foundTop == -1) {
                foundTop = value;
            }
            tops.add("" + count + ": " + Formatter.fmt(value) + " - "
                    + PrettyPrint.ppConcise(instance.getVerbs(), agent));
            count++;
            if (count > maxCount) {
                tops.add("+ " + (realCount - count) + " VI's weighting "
                        + Formatter.fmt(totalWeight - shownWeight));
                break;
            }
        }
        if (tops.isEmpty()) {
            tops.add("--- nothing found---");
        }
        ft.internalSeparator();
        for (int i = 0; i != tops.size(); i++) {
            String line = tops.get(i);
            if (i == 0) {
                ft.wrappedRow(PrettyPrint.ppConcise(voFocus, agent), line);
                lf.addTableLine(PrettyPrint.ppConcise(voFocus, agent), line);
            } else {
                ft.wrappedRow("", line);
                lf.addTableLine("", line);
            }
            count++;
        }
        if (expectedTop != -1 && doTest) {
            Assert.assertEquals(expectedTop, foundTop, 0.01);
        }
    }
}
