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

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.reference.rrState;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.LatexFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * Test of the resolution confidence calculations when applied to COs
 * 
 * @author Ladislau Boloni
 * Created on: Jan 30, 2013
 */
public class testResolutionConfidence {

    public boolean doTest = true;
    /** used to number the examples in the latex table */
    public int lineCount = 1;

    /**
     * Run experiments about the referability between concept overlays
     */
    public SimpleEntry<String, String> resolutionConfidenceExperiments() {
        Runner r = ArtificialDomain.runnerArtificialDomain();
        Agent agent = r.agent;
        ConceptOverlay coTested = null;
        ConceptOverlay coReference = null;
        FormatTable ft = new FormatTable(20, 20, 10, 10, 10);
        LatexFormatter lf = new LatexFormatter();
        ft.header("coTested", "coReference", "sc.incompat", "sc.simil",
                "sc.overall");
        lf.beginTabular("|p{0.75cm}|p{5cm}|p{5cm}|p{1.5cm}|p{1.5cm}|p{1.5cm}|");
        lf.add("\\hline");
        lf.addTableLine("No.", "coTested", "coReference", "sc incompat",
                "sc simil", "sc overall");
        lf.add("\\hline");
        // one independent base attribute: itself
        coTested = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "c_bai1");
        tst(ft, lf, agent, coTested, coReference);
        // one independent base attribute: itself and negation
        coTested = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "not-c_bai1");
        tst(ft, lf, agent, coTested, coReference);
        // proper names
        coTested = ConceptOverlay.createCO(agent, "\"PN1\"");
        coReference = ConceptOverlay.createCO(agent, "\"PN1\"");
        tst(ft, lf, agent, coTested, coReference);
        // two different independent base attributes
        coTested = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "c_bai2");
        tst(ft, lf, agent, coTested, coReference);
        // two BAO
        coTested = ConceptOverlay.createCO(agent, "c_bao3_1");
        coReference = ConceptOverlay.createCO(agent, "c_bao3_2");
        tst(ft, lf, agent, coTested, coReference);
        // a BAI and a CMI
        coTested = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        tst(ft, lf, agent, coTested, coReference);
        // two CMIs of the same category
        coTested = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        coReference = ConceptOverlay.createCO(agent, "c_cat0_cmi2");
        tst(ft, lf, agent, coTested, coReference);
        // two CMIs of the same category, plus a common cmi
        coTested = ConceptOverlay.createCO(agent, "c_bai1", "c_cat0_cmi1");
        coReference = ConceptOverlay.createCO(agent, "c_bai1", "c_cat0_cmi2");
        tst(ft, lf, agent, coTested, coReference);
        // two CMOs of the same category
        coTested = ConceptOverlay.createCO(agent, "c_cot3_cmo1");
        coReference = ConceptOverlay.createCO(agent, "c_cot3_cmo2");
        tst(ft, lf, agent, coTested, coReference);
        // two CMOs of the same category, plus a common cmi
        coTested = ConceptOverlay.createCO(agent, "c_bai1", "c_cot3_cmo1");
        coReference = ConceptOverlay.createCO(agent, "c_bai1", "c_cot3_cmo2");
        tst(ft, lf, agent, coTested, coReference);
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
        SimpleEntry<String, String> val = resolutionConfidenceExperiments();
        TextUi.println(val.getKey());
    }

    /**
     * Calculates the various incompatibility values and adds it to a row
     * 
     * @param co
     * @param expected
     * @param concepts
     */
    public void tst(FormatTable ft, LatexFormatter lf, Agent agent,
            ConceptOverlay coTested, ConceptOverlay coReference) {
        rrState rc = rrState.createCalculated(coTested, coReference);
        ft.row(PrettyPrint.ppConcise(coTested, agent),
                PrettyPrint.ppConcise(coReference, agent),
                rc.getScoreIncompatibility(), rc.getScoreSimilarity(),
                rc.getOverallScore());
        lf.addTableLine("" + lineCount++,
                PrettyPrint.ppConcise(coTested, agent),
                PrettyPrint.ppConcise(coReference, agent),
                rc.getScoreIncompatibility(), rc.getScoreSimilarity(),
                rc.getOverallScore());
    }

}
