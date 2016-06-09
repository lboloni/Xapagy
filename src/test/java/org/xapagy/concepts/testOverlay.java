/*
   This file is part of the Xapagy project
   Created on: Aug 23, 2010
 
   org.xapagy.model.testOverlay
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.concepts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;

/**
 * This test tests the combination of the overlays
 * 
 * @author Ladislau Boloni
 * 
 */
public class testOverlay {

    /**
     * Tests that dead replaces alive
     * 
     */
    @Test
    public void testImpact() {
        String description =
                "Test the impact-add of concepts (dead replaces alive)";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        ConceptOverlay ovr = new ConceptOverlay(r.agent);
        ovr.addFullEnergyImpacted(r.agent.getConceptDB().getConcept("c_bai10"));
        ovr.addFullEnergyImpacted(r.agent.getConceptDB().getConcept("c_bai11"));
        ovr.addFullEnergyImpacted(r.agent.getConceptDB().getConcept("not-c_bai11"));
        assertTrue(ovr.getEnergy(r.agent.getConceptDB().getConcept(
                "c_bai11")) == 0.0);
        TestHelper.testDone();
    }

    
    /**
     * Tests the same coverage function
     * 
     */
    @Test
    public void testSameCoverage() {
        String description =
                "Test the same coverage function";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        ConceptOverlay co1 = ConceptOverlay.createCO(r.agent, "c_bai1");
        ConceptOverlay co2 = ConceptOverlay.createCO(r.agent, "c_bai1");
        ConceptOverlay co3 = ConceptOverlay.createCO(r.agent, "c_bai1", "c_bai2");
        ConceptOverlay co4 = ConceptOverlay.createCO(r.agent, "c_bai2");
        assertTrue(co1.sameCoverage(co2));
        assertFalse(co1.sameCoverage(co3));
        assertFalse(co3.sameCoverage(co1));
        assertFalse(co1.sameCoverage(co4));
        TestHelper.testDone();
    }

    
    /**
     * Tests that the negative exhausts the overlay
     */
    @Test
    public void testNegationImpact() {
        String description =
                "Test the impact-add between overlays, with negation";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        ConceptOverlay ovr = ConceptOverlay.createCO(r.agent, "c_bai10");
        ConceptOverlay ovr2 = ConceptOverlay.createCO(r.agent, "not-c_bai10");
        ovr.addOverlayImpacted(ovr2);
        assertTrue(ovr.getEnergy(r.agent.getConceptDB().getConcept(
                "c_bai10")) == 0.0);
        TestHelper.testDone();
    }

    /**
     * Tests whether the toString and toStringDetailed do not crash
     * 
     */
    @SuppressWarnings("unused")
    @Test
    public void testPrettyPrint() {
        String description =
                "Test the prettyprint for overlays for not crashing";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        ConceptOverlay ovr =
                ConceptOverlay.createCO(r.agent, "c_bai10", "not-c_bai11");
        String ts = ovr.toString();
        String tsdCon = PrettyPrint.ppConcise(ovr, r.agent);
        String tsdDet = PrettyPrint.ppDetailed(ovr, r.agent);
        TestHelper.testDone();
    }
    
    /**
     * Tests the cover function    
     */
    @Test
    public void testCover() {
        String description =
                "Test the cover function for overlays";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        ConceptOverlay a = ConceptOverlay.createCO(r.agent, "c_bai10", "c_bai11", "c_bai12");        
        ConceptOverlay b = ConceptOverlay.createCO(r.agent, "c_bai10", "c_bai12");
        // a should cover b
        boolean a_covers_b = a.covers(b);
        assertTrue("a should cover b", a_covers_b);
        // b should not cover a
        boolean b_covers_a = b.covers(a);
        assertFalse("b should not cover a", b_covers_a);        
        TestHelper.testDone();
    }
    
    
    /**
     * Tests the cover function    
     * @throws XapiParserException 
     */
    @Test
    public void testCoverWithLabels() throws XapiParserException {
        String description =
                "Test the cover function for overlays";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        XapiParser xp = r.agent.getXapiParser();
        ConceptOverlay a = xp.parseCo("w_c_bai10 w_c_bai11 #one #two");        
        ConceptOverlay b = xp.parseCo("w_c_bai10 w_c_bai11 #one");
        // a should cover b
        boolean a_covers_b = a.coversWithLabels(b);
        assertTrue("a should cover b", a_covers_b);
        // b should not cover a
        boolean b_covers_a = b.coversWithLabels(a);
        assertFalse("b should not cover a", b_covers_a);        
        TestHelper.testDone();
    }
}
