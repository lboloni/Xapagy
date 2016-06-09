/*
   This file is part of the Xapagy project
   Created on: Dec 28, 2014
 
   org.xapagy.activity.focusmaintenance.testDaFcmSummarization
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;

/**
 * @author Ladislau Boloni
 *
 */
public class testDaFcmSummarization {

    /**
     * Test that closing a summarization decrease it
     */
    @Test
    public void testClosing() {
        String description =
                "Closing of the summarization transforms the FOCUS_SUMMARIZATION energy to FOCUS energy";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        Focus fc = r.agent.getFocus();
        AutobiographicalMemory am = r.agent.getAutobiographicalMemory();
        r.exec("$CreateScene #One CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        VerbInstance viSum =
                r.exac("'Achilles' / ##Summarization wv_v_av43 / 'Hector'.");
        r.exec("$CreateLink 'Summarization_Begin' { 'wa_v_av40' } ==> { 'wv_v_av43' }");
        r.exec("'Hector'/ #act wa_v_av41 / 'Achilles'.");
        r.exec("$CreateLink 'Summarization_Body' { 'wa_v_av41' } ==> { 'wv_v_av43' }");
        r.exec("'Achilles'/ #act wa_v_av42 / 'Hector'.");
        //
        // at this point the summary has only focus summarization energy...
        //
        double energyFocus = fc.getEnergy(viSum, EnergyColors.FOCUS_VI);
        double energyFocusSummarization =
                fc.getEnergy(viSum, EnergyColors.FOCUS_SUMMARIZATION_VI);
        double energyAm = am.getEnergy(viSum, EnergyColors.AM_VI);
        assertEquals(0.0, energyFocus, 0.0);
        assertEquals(1.0, energyFocusSummarization, 0.1);
        assertEquals(0.0, energyAm, 0.0);
        // now create the closing link
        r.exec("$CreateLink 'Summarization_Close' { 'wa_v_av42' } ==> { 'wv_v_av43' }");
        // some non-summarized actions
        r.exec("'Achilles'/ #act wa_v_av44 / 'Hector'.");
        r.exec("'Hector'/ #act wa_v_av45 / 'Achilles'.");
        //
        // at this point the summary energy had gone away and was transformed
        // into focus energy
        //
        energyFocus = fc.getEnergy(viSum, EnergyColors.FOCUS_VI);
        energyFocusSummarization =
                fc.getEnergy(viSum, EnergyColors.FOCUS_SUMMARIZATION_VI);
        energyAm = am.getEnergy(viSum, EnergyColors.AM_VI);        
        assertTrue(energyFocus > 0.5);
        assertTrue(energyFocusSummarization < 0.5);
        assertTrue(energyAm > 0.3);
        TestHelper.testDone();
    }

    /**
     * Test that the expiration of the members of the summarization without
     * closing returns it
     */
    @Test
    public void testExpiration() {
        String description =
                "The expiration of begin and body leads to the removal of the summarization with no record";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        Focus fc = r.agent.getFocus();
        AutobiographicalMemory am = r.agent.getAutobiographicalMemory();

        r.exec("$CreateScene #One CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
        VerbInstance viBegin = r.exac("'Achilles'/ wa_v_av40 / 'Hector'.");
        VerbInstance viSum =
                r.exac("'Achilles' / ##Summarization wv_v_av43 / 'Hector'.");
        r.exec("$CreateLink 'Summarization_Begin' { 'wa_v_av40' } ==> { 'wv_v_av43' }");
        VerbInstance viBody = r.exac("'Hector'/ #act wa_v_av41 / 'Achilles'.");
        r.exec("$CreateLink 'Summarization_Body' { 'wa_v_av41' } ==> { 'wv_v_av43' }");
        r.exec("'Achilles'/ #act wa_v_av42 / 'Hector'.");
        //
        //  Verify that the links are present
        //
        r.ah.linkedBy(Hardwired.LINK_SUMMARIZATION_BEGIN, viBegin, viSum);
        r.ah.linkedBy(Hardwired.LINK_SUMMARIZATION_BODY, viBody, viSum);
        
        // no closing happens, instead a number of other actions will happen
        // which push out these ones.
        for (int i = 0; i != 10; i++) {
            r.exec("'Achilles'/ #act wa_v_av44 / 'Hector'.");
            r.exec("'Hector'/ #act wa_v_av45 / 'Achilles'.");
        }
        //
        // at this point the viSummarize has no energy anywhere
        //
        double energyFocus = fc.getEnergy(viSum, EnergyColors.FOCUS_VI);
        double energyFocusSummarization =
                fc.getEnergy(viSum, EnergyColors.FOCUS_SUMMARIZATION_VI);
        double energyAm = am.getEnergy(viSum, EnergyColors.AM_VI);        
        assertEquals(0.0, energyAm, 0.0);
        assertEquals(0.0, energyFocus, 0.0);
        assertEquals(0.0, energyFocusSummarization, 0.0);
        //
        // furthermore, the links which had been there, are now gone!
        //
        r.ah.notLinkedBy(Hardwired.LINK_SUMMARIZATION_BEGIN, viBegin, viSum);
        r.ah.notLinkedBy(Hardwired.LINK_SUMMARIZATION_BODY, viBody, viSum);

        
        TestHelper.testDone();
    }

}
