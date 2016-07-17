/*
   This file is part of the Xapagy project
   Created on: Jun 27, 2014
 
   org.xapagy.debug.testViMatch
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.debug;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;

/**
 * @author Ladislau Boloni
 * 
 */
public class testViMatch {

    /**
     * Test if the ViMatch works on full VIs (not templates)
     */
    @Test
    public void test() {
        String description = "ViMatch for VIs";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        ViMatch vim = new ViMatch(r.agent);
        r.exec("$Include 'P-FocusOnly'");
        r.exec("$CreateScene #first CloseOthers With Instances w_c_bai20 'Hector' #H, w_c_bai20 'Achilles' #A");
        r.exec("$CreateScene #second With Instances w_c_bai25 'Jill', w_c_bai26 'Bill'");
        
        // S-V-O
        VerbInstance vi1 = r.exac("'Achilles' / wa_v_av40 / 'Hector'.");
        assertTrue(vim.match(vi1, null, "wa_v_av40"));
        assertFalse(vim.match(vi1, null, "wa_v_av41"));
        assertFalse(vim.match(vi1, ViType.S_ADJ, "wa_v_av40"));
        assertFalse(vim.match(vi1, ViType.S_V_O, "*", "wa_v_av40"));
        assertTrue(vim.match(vi1, ViType.S_V_O, "*", "wa_v_av40", "*"));
        assertTrue(vim.match(vi1, ViType.S_V_O, "\"Achilles\"", "wa_v_av40",
                "\"Hector\""));
        assertFalse(vim.match(vi1, ViType.S_V_O, "\"Achilles\"", "wa_v_av40",
                "\"Hector\"", "\"Hector\""));
        assertTrue(vim.match(vi1, ViType.S_V_O, "#A \"Achilles\"", "wa_v_av40",
                "#H \"Hector\""));
        assertFalse(vim.match(vi1, ViType.S_V_O, " #A #X \"Achilles\"",
                "wa_v_av40", "#H \"Hector\""));
        // S-V
        VerbInstance vi2 = r.exac("'Achilles' / #X wa_v_av41.");
        assertTrue(vim.match(vi2, null, "wa_v_av41"));
        assertTrue(vim.match(vi2, ViType.S_V, "wa_v_av41"));
        assertFalse(vim.match(vi2, ViType.S_ADJ, "wa_v_av41"));
        assertTrue(vim.match(vi2, ViType.S_V, "\"Achilles\"", "wa_v_av41"));
        assertTrue(vim.match(vi2, ViType.S_V, "\"Achilles\"", "#X wa_v_av41"));
        assertFalse(vim.match(vi2, ViType.S_V, "\"Achilles\"", "#Y wa_v_av41"));
        assertFalse(vim.match(vi2, ViType.S_V, "\"Achilles\"", "wa_v_av41",
                "\"Hector\""));
        // S-ADJ
        VerbInstance vi3 = r.exac("'Achilles' / is-a / #NEW w_c_bai21.");
        assertTrue(vim.match(vi3, null, "is-a"));
        assertTrue(vim.match(vi3, ViType.S_ADJ, "is-a"));
        assertFalse(vim.match(vi3, ViType.S_V_O, "is-a"));
        assertTrue(vim.match(vi3, null, "*", "is-a", "#NEW"));
        // this is a bit tricky: is the new label transferred???
        assertTrue(vim.match(vi3, null, "#NEW", "is-a", "*"));
        assertTrue(vim.match(vi3, null, "w_c_bai21", "is-a", "*"));
        // QUOTE
        VerbInstance vi4 = r.exac(
                "'Achilles' / says in #second // 'Jill' / wa_v_av43 / 'Bill'.");
        assertTrue(vim.match(vi4, null, "says"));
        assertTrue(vim.match(vi4, ViType.QUOTE, "says"));
        assertFalse(vim.match(vi4, ViType.S_V_O, "says"));
        assertTrue(vim.match(vi4, ViType.QUOTE, "*", "says", "*"));
        assertTrue(vim.match(vi4, ViType.QUOTE, "*", "says", "#second"));
        assertFalse(vim.match(vi4, ViType.QUOTE, "*", "says", "#first"));
        assertTrue(vim.match(vi4, ViType.QUOTE, "*", "says", "#second",
                "wa_v_av43"));
        assertFalse(vim.match(vi4, ViType.QUOTE, "*", "says", "#second",
                "wa_v_av44"));
        // test with the scene matching
        assertTrue(vim.match(vi1, "#first", null, "wa_v_av40"));
        assertFalse(vim.match(vi1, "#second", null, "wa_v_av40"));

    }

    /**
     * Test if the ViMatch works on templates
     * @throws XapiParserException 
     */
    @Test
    public void testTemplates() throws XapiParserException {
        String description = "ViMatch for ViTemplates";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        ViMatch vim = new ViMatch(r.agent);
        XapiParser xp = r.agent.getXapiParser();
        r.exec("$Include 'P-FocusOnly'");
        r.exec("$CreateScene #first CloseOthers With Instances w_c_bai20 'Hector' #H, w_c_bai20 'Achilles' #A");
        r.exec("$CreateScene #second With Instances w_c_bai25 'Jill', w_c_bai26 'Bill'");        
        // S-V-O
        @SuppressWarnings("unused")
        VerbInstance vi = r.exac("'Achilles' / wa_v_av40 / 'Hector'.");
        // Instance instAchilles = vi.getSubject();
        // Instance instHector = vi.getObject();
        VerbOverlay verbs = xp.parseVo("wa_v_av10");
        VerbInstance vit = VerbInstance.createViTemplate(ViType.S_V_O, verbs);
        assertTrue(vim.match(vit, null, "wa_v_av10"));
        assertTrue(vim.match(vit, null, "MISSING", "wa_v_av10", "MISSING"));
        assertFalse(vim.match(vit, null, "NEW:", "wa_v_av10", "MISSING"));
        // set the subject to new:
        ConceptOverlay co = xp.parseCo("w_c_bai10");
        vit.setNewPart(ViPart.Subject, co);
        assertTrue(vim.match(vit, null, "NEW:", "wa_v_av10", "MISSING"));
        assertTrue(
                vim.match(vit, null, "NEW: w_c_bai10", "wa_v_av10", "MISSING"));
        assertFalse(
                vim.match(vit, null, "NEW: w_c_bai11", "wa_v_av10", "MISSING"));
    }

    /**
     * Test if the ViMatch filter works
     */
    @Test
    public void testFilter() {
        String description = "ViMatch";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        ViMatch vim = new ViMatch(r.agent);
        r.exec("$Include 'P-FocusOnly'");
        // r.exec("$SetParameter A_DEBUG/G_GENERAL/N_RECORD_FOCUS_MEMORY_QUANTUMS=1.0");
        r.exec("$CreateScene #first CloseOthers With Instances w_c_bai20 'Hector' #H, w_c_bai20 'Achilles' #A");
        r.exec("$CreateScene #second With Instances w_c_bai25 'Jill', w_c_bai26 'Bill'");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Hector' / wa_v_av41 / 'Achilles'.");
        r.exec("'Achilles' / #X wa_v_av41.");
        r.exec("'Achilles' / is-a / #NEW w_c_bai21.");
        r.exec("'Achilles' / says in #second // 'Jill' / wa_v_av43 / 'Bill'.");
        // r.exec("$DebugHere");

        List<VerbInstance> viListAllEnergies =
                r.agent.getFocus().getViListAllEnergies();
        List<VerbInstance> results =
                vim.select(viListAllEnergies, ViType.S_V, "*");
        // for(VerbInstance vi: results) {
        // PrettyPrint.ppc(vi, r.agent);
        // }
        assertEquals(1, results.size());
        results = vim.select(viListAllEnergies, ViType.S_V, "wa_v_av41");
        assertEquals(1, results.size());
        results = vim.select(viListAllEnergies, null, "wa_v_av41");
        assertEquals(2, results.size());
    }

}
