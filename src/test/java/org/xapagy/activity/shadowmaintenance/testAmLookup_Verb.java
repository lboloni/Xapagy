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
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.LatexFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * Created on: Mar 2, 2013
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
