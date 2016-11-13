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
 * 
 * A series of tests for the binary relations defined in Incompatibility, when
 * applied to concept overlays CO.
 * 
 * @author Ladislau Boloni
 * Created on: Jan 29, 2013
 */
public class testIncompatibilityCo {

    public boolean doTest = true;
    /** used to number the examples in the latex table */
    public int lineCount = 1;

    /**
     * for the incompatibility between concepts
     */
    public SimpleEntry<String, String> incompatibilityExperiments() {
        Runner r = ArtificialDomain.runnerArtificialAutobiography();

        Agent agent = r.agent;
        ConceptOverlay coSource = null;
        ConceptOverlay coReference = null;
        LatexFormatter lf = new LatexFormatter();
        FormatTable ft = new FormatTable(20, 20, 20, 20, 20, 10);
        ft.header("ovrSrc", "ovrRef", "negative impact energy",
                "over-impact energy", "incompatibility score", "decision");
        lf.beginTabular("|p{0.75cm}|p{4.5cm}|p{4.5cm}|p{1.5cm}|p{1.5cm}|p{1.5cm}|p{1cm}|");
        lf.add("\\hline");
        lf.addTableLine("No.", "ovrSrc", "coRef", "negative impact energy",
                "over-impact energy", "score", "decision");
        lf.add("\\hline");

        //
        // Cases which happen normally
        //
        // one independent base attribute: itself
        coSource = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "c_bai1");
        tst(ft, lf, agent, coSource, coReference, 0);
        // one independent base attribute: itself and negation
        coSource = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "not-c_bai1");
        tst(ft, lf, agent, coSource, coReference, 1.0);
        // one independent base attribute: negation and itself
        coSource = ConceptOverlay.createCO(agent, "not-c_bai1");
        coReference = ConceptOverlay.createCO(agent, "c_bai1");
        tst(ft, null, agent, coSource, coReference, 1.0);
        // a proper name and its negation
        //coSource = ConceptOverlay.createCO(agent, "\"PN1\"");
        //coReference = ConceptOverlay.createCO(agent, "not-\"PN1\"");
        tst(ft, lf, agent, coSource, coReference, 1.0);
        // one independent base attribute: itself and negation, plus a bunch of
        // others in the source
        coSource =
                ConceptOverlay.createCO(agent, "c_bai1", "c_bai2", "c_bai3",
                        "c_bai4");
        coReference = ConceptOverlay.createCO(agent, "not-c_bai1");
        tst(ft, lf, agent, coSource, coReference, 1.0);
        // one independent base attribute: itself and negation, plus a bunch of
        // others in the reference
        coSource = ConceptOverlay.createCO(agent, "c_bai1");
        coReference =
                ConceptOverlay.createCO(agent, "not-c_bai1", "c_bai2",
                        "c_bai3", "c_bai4");
        tst(ft, lf, agent, coSource, coReference, 0.25);
        // two different independent base attributes
        coSource = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "c_bai2");
        tst(ft, lf, agent, coSource, coReference, 0.0);
        // two BAO
        coSource = ConceptOverlay.createCO(agent, "c_bao3_1");
        coReference = ConceptOverlay.createCO(agent, "c_bao3_2");
        tst(ft, lf, agent, coSource, coReference, 0.0);
        // a BAI and a CMI
        coSource = ConceptOverlay.createCO(agent, "c_bai1");
        coReference = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        tst(ft, lf, agent, coSource, coReference, 0.0);
        // two CMIs of the same category
        coSource = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        coReference = ConceptOverlay.createCO(agent, "c_cat0_cmi2");
        tst(ft, lf, agent, coSource, coReference, 0.5);
        // two CMIs of the same category of size 0.3 - the incompatibility is
        // smaller - this can be seen as a weaker category
        coSource = ConceptOverlay.createCO(agent, "c_catv3_cmiv1");
        coReference = ConceptOverlay.createCO(agent, "c_catv3_cmiv2");
        tst(ft, lf, agent, coSource, coReference, 0.231);
        // two CMIs of the same category of size 2.0 - the incompatibility is
        // larger - this can be seen as a stronger category
        coSource = ConceptOverlay.createCO(agent, "c_catv20_cmiv1");
        coReference = ConceptOverlay.createCO(agent, "c_catv20_cmiv2");
        tst(ft, lf, agent, coSource, coReference, 0.667);
        // two CMIs of the same category, plus a bunch of BAI in the source
        coSource =
                ConceptOverlay.createCO(agent, "c_cat0_cmi1", "c_bai2",
                        "c_bai3", "c_bai4");
        coReference = ConceptOverlay.createCO(agent, "c_cat0_cmi2");
        tst(ft, lf, agent, coSource, coReference, 0.5);
        // two different proper names
        //
        // FIXME: on May 2014, we set the proper names to be unit size
        //
        //coSource = ConceptOverlay.createCO(agent, "\"PN1\"");
        //coReference = ConceptOverlay.createCO(agent, "\"PN2\"");
        //tst(ft, lf, agent, coSource, coReference, 0.5); // was 0.909 when PN size 0.1
        // two CMIs of the same category, plus a bunch of BAI in the reference
        coSource = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        coReference =
                ConceptOverlay.createCO(agent, "c_cat0_cmi2", "c_bai2",
                        "c_bai3", "c_bai4");
        tst(ft, lf, agent, coSource, coReference, 0.2);
        // two CMIs of the same category, plus a common independent base
        // attribute
        coSource = ConceptOverlay.createCO(agent, "c_bai1", "c_cat0_cmi1");
        coReference = ConceptOverlay.createCO(agent, "c_bai1", "c_cat0_cmi2");
        tst(ft, lf, agent, coSource, coReference, 0.33);
        // two CMOs of the same category
        coSource = ConceptOverlay.createCO(agent, "c_cot3_cmo1");
        coReference = ConceptOverlay.createCO(agent, "c_cot3_cmo2");
        tst(ft, lf, agent, coSource, coReference, 0.35);
        // two CMOs of the same category, plus a common BAI
        coSource = ConceptOverlay.createCO(agent, "c_bai1", "c_cot3_cmo1");
        coReference = ConceptOverlay.createCO(agent, "c_bai1", "c_cot3_cmo2");
        tst(ft, lf, agent, coSource, coReference, 0.23);
        // what happens if the impact was resolved in the source?
        // two CMIs of the same category
        coSource = ConceptOverlay.createCO(agent, "c_cat0_cmi1");
        coReference = ConceptOverlay.createCO(agent, "c_cat0_cmi2");
        ConceptOverlay coSourceResolved =
                (ConceptOverlay) coSource.newOverlay();
        coSourceResolved.addOverlayImpacted(coSource);
        ConceptOverlay coReferenceResolved =
                (ConceptOverlay) coReference.newOverlay();
        coReferenceResolved.addOverlayImpacted(coReference);
        // try all the combinations - normally, the fact that the stuff is
        // resolved should not matter
        tst(ft, null, agent, coSource, coReference, 0.5);
        tst(ft, null, agent, coSourceResolved, coReference, 0.5);
        tst(ft, null, agent, coSource, coReferenceResolved, 0.5);
        tst(ft, null, agent, coSourceResolved, coReferenceResolved, 0.5);
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
     * @param lf
     *            - the LatexFormatter - for the paper
     * @param expected
     * @param concepts
     */
    public void tst(FormatTable ft, LatexFormatter lf, Agent agent,
            ConceptOverlay coSource, ConceptOverlay coReference,
            double expectedScore) {
        SimpleEntry<Double, Double> components =
                Incompatibility
                        .incompatibilityComponents(coSource, coReference);
        double score =
                Incompatibility.scoreIncompatibility(coSource, coReference);
        boolean decision =
                Incompatibility.decideIncompatibility(coSource, coReference);
        ft.wrappedRow(PrettyPrint.ppConcise(coSource, agent),
                PrettyPrint.ppConcise(coReference, agent), components.getKey(),
                components.getValue(), score, decision);
        if (lf != null) {
            lf.addTableLine("" + lineCount++,
                    PrettyPrint.ppConcise(coSource, agent),
                    PrettyPrint.ppConcise(coReference, agent),
                    components.getKey(), components.getValue(), score, decision);
        }
        if (expectedScore >= 0) {
            Assert.assertEquals("Incompatibility not as expected",
                    expectedScore, score, expectedScore * 0.03);
        }
    }

}
