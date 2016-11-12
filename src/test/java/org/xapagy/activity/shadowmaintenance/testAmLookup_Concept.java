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
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.InstanceSet;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.LatexFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * Created on: Feb 21, 2013
 */
public class testAmLookup_Concept {

    public boolean doTest = true;

    private Agent agent;
    private AutobiographicalMemory am;
    private int id = 0;

    /**
     * Initializes an autobiographical memory (separate from that of the agent)
     * 
     * Creates a series of instances in this AM.
     * 
     */
    public void createAM() {
        am = new AutobiographicalMemory(agent);
        // 1 c_bai20
        addInstance("c_bai20");
        // 2 c_bai21
        addInstance("c_bai21");
        addInstance("c_bai21");
        // 10 c_bai22
        for (int i = 0; i != 10; i++) {
            addInstance("c_bai22");
        }
        // 100 c_bai23
        for (int i = 0; i != 100; i++) {
            addInstance("c_bai23");
        }
        // 1 Hector
        addInstance("\"PN1\"");
        // 9 c_bai24, 29 c_bai25, 1 both
        for (int i = 0; i != 9; i++) {
            addInstance("c_bai24");
        }
        for (int i = 0; i != 29; i++) {
            addInstance("c_bai25");
        }
        addInstance("c_bai24", "c_bai25");
        // three unique ones
        addInstance("c_bai26", "c_bai27", "c_bai28");
        
    }

    /**
     * 
     * Artificially adds an autobiographical.
     * 
     * @param am
     * @param concepts
     * @return
     */
    public Instance addInstance(String... concepts) {
        Instance instance = new Instance("id" + id++, null, agent);
        for (String concept : concepts) {
            instance.getConcepts().addFullEnergy(
                    agent.getConceptDB().getConcept(concept));
        }
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createAdd(instance, 1.0, EnergyColors.AM_INSTANCE, "x");
        am.applyInstanceEnergyQuantum(eq);
        return instance;
    }

    /**
     * 
     * 
     * @param doTest
     *            - if set to true, it performs validation tests and throws
     *            failure. If set to false, if just generates the output
     * 
     * @return a pair of the results formatted as simple text and latex
     * 
     */
    public SimpleEntry<String, String> lookupCoExperiments() {
        LatexFormatter lf = new LatexFormatter();
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        agent = r.agent;
        createAM();
        ConceptOverlay coFocus = null;
        FormatTable ft = new FormatTable(50, 50);
        ft.header("coFocus", "coLookup Top5");
        lf.beginTabular("|p{5cm}|p{15cm}|");
        lf.add("\\hline");
        lf.addTableLine("coFocus", "coLookup Top5");
        lf.add("\\hline");
        //
        // Overlays of a single concept
        //
        // c_bai20 - a single one like this
        coFocus = ConceptOverlay.createCO(agent, "c_bai20");
        tst(ft, lf, coFocus, 0.462);
        // c_bai21 - two like this
        coFocus = ConceptOverlay.createCO(agent, "c_bai21");
        tst(ft, lf, coFocus, 0.231);
        // c_bai22 - 10 like this
        coFocus = ConceptOverlay.createCO(agent, "c_bai22");
        tst(ft, lf, coFocus, 0.046);
        // c_bai23 - 100 like this
        coFocus = ConceptOverlay.createCO(agent, "c_bai23");
        tst(ft, lf, coFocus, 0.005);
        // 'PN1' - 1 like this - currently, proper names are the same as base concepts
        coFocus = ConceptOverlay.createCO(agent, "\"PN1\"");
        tst(ft, lf, coFocus, 0.462);       //
        // Overlays of two concepts
        // there are 9 with this and 1 c_bai24 + c_bai25 - just like if would be 10 like this 
        coFocus = ConceptOverlay.createCO(agent, "c_bai24");
        tst(ft, lf, coFocus, 0.046);
        // there are 1 with this combination - that one gets more
        coFocus = ConceptOverlay.createCO(agent, "c_bai24", "c_bai25");
        tst(ft, lf, coFocus, 0.062, 0.046);
        // none with c_bai29, 1 with c_bai20 - doesn't care about c_bai29 in the reference
        coFocus = ConceptOverlay.createCO(agent, "c_bai20", "c_bai29");
        tst(ft, lf, coFocus, 0.462);
        // 1 with c_bai20, 10 with c_bai22
        coFocus = ConceptOverlay.createCO(agent, "c_bai20", "c_bai22");
        tst(ft, lf, coFocus, 0.462, 0.046);
        // there is no match with the extra - but it does not care
        coFocus = ConceptOverlay.createCO(agent, "c_bai24", "c_bai25", "c_bai29");
        tst(ft, lf, coFocus, 0.062, 0.046);
        // there are many matches with the extra - doesn't care with the top
        coFocus = ConceptOverlay.createCO(agent, "c_bai24", "c_bai25", "c_bai23");
        tst(ft, lf, coFocus, 0.062, 0.046);
        // three unique ones - maxes out at 1.0
        coFocus = ConceptOverlay.createCO(agent, "c_bai26", "c_bai27", "c_bai28");
        tst(ft, lf, coFocus, 1.0);
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
        SimpleEntry<String, String> retval = lookupCoExperiments();
        TextUi.println(retval.getKey());
    }

    /**
     * Looks up the coLookup values, and adds the first 5 to the row.
     * 
     * @param ft
     *            - the text table to which we add the value
     * @param lf
     *            - the latex formatter to which we add the values in a row
     * @param co
     *            - the co which we plan to look up
     * @param expectedTop
     *            - the expected value of the top shadow
     */
    public void tst(FormatTable ft, LatexFormatter lf, ConceptOverlay co,
            double... expectedTop) {
        InstanceSet lookup = AmLookup.lookupCo(am, co, EnergyColors.AM_INSTANCE);
        List<String> tops = new ArrayList<>();
        int maxCount = 5;
        // it collects the top values, which will be then compared with the
        // expectedTop
        double foundTop[] = new double[maxCount];
        int count = 1;
        int realCount = lookup.getParticipants().size();
        double totalWeight = lookup.getSum();
        double shownWeight = 0.0;
        for (SimpleEntry<Instance, Double> entry : lookup
                .getDecreasingStrengthList()) {
            Instance instance = entry.getKey();
            double value = entry.getValue();
            shownWeight += value;
            foundTop[count - 1] = value;
            tops.add("" + count + ": " + Formatter.fmt(value) + " - "
                    + PrettyPrint.ppConcise(instance.getConcepts(), agent));
            count++;
            if (count > maxCount) {
                tops.add("+ " + (realCount - count) + " inst's weighting "
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
                ft.wrappedRow(PrettyPrint.ppConcise(co, agent), line);
                lf.addTableLine(PrettyPrint.ppConcise(co, agent), line);
            } else {
                ft.wrappedRow("", line);
                lf.addTableLine("", line);
            }
            count++;
        }
        if (doTest) {
            for (int i = 0; i != expectedTop.length; i++) {
                Assert.assertEquals("" + (i + 1) + "th strongest match",
                        expectedTop[i], foundTop[i], 0.01);
            }
        }
    }

}
