/*
   This file is part of the Xapagy project
   Created on: Dec 21, 2014
 
   org.xapagy.activity.focusmaintenance.testSaFcmInsertVi
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.agents.Focus;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;

/**
 * @author Ladislau Boloni
 *
 */
public class testSaFcmInsertVi {

    /**
     * This test tests that the insertion of a new VI adds FOCUS energy for
     * regular VIs and FOCUS_SUMMARIZATION for VIs marked as summarizations
     */
    @Test
    public void testInsertionViEnergy() {
        String testDescription = "The focus energy added at insertion";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.createAabConcepts();
        Focus fc = r.agent.getFocus();
        r.exec("$CreateScene #Reality CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles', w_c_bai22");
        //
        //  Regular VI
        //
        VerbInstance viRegular = r.exac("'Hector' / wa_v_av41 / 'Achilles'.");
        double valueFocus = fc.getEnergy(viRegular, EnergyColors.FOCUS_VI);
        double valueFocusSummarization =
                fc.getEnergy(viRegular, EnergyColors.FOCUS_SUMMARIZATION_VI);
        assertEquals("regular insert, EnergyColors.FOCUS", 1.0, valueFocus, 0.05);
        assertEquals("regular insert, EnergyColors.FOCUS_SUMMARIZATION", 0.0, valueFocusSummarization, 0.05);        
        //
        //  Summarization VI: note that we are not putting the action form, and we terminate
        //  with comma to avoit time
        //
        VerbInstance viSummarization =
                r.exac("'Hector' / ##Summarization wv_v_av41 / 'Achilles',");
        valueFocus = fc.getEnergy(viSummarization, EnergyColors.FOCUS_VI);
        valueFocusSummarization =
                fc.getEnergy(viSummarization, EnergyColors.FOCUS_SUMMARIZATION_VI);
        assertEquals("summarization insert, EnergyColors.FOCUS", 0.0, valueFocus, 0.05);
        assertEquals("summarization insert, EnergyColors.FOCUS_SUMMARIZATION", 1.0, valueFocusSummarization, 0.05);
        // just to make sure that the regular VI had not been affected
        valueFocus = fc.getEnergy(viRegular, EnergyColors.FOCUS_VI);
        valueFocusSummarization =
                fc.getEnergy(viRegular, EnergyColors.FOCUS_SUMMARIZATION_VI);
        assertEquals("regular VI after summarization, EnergyColor.FOCUS", 1.0, valueFocus, 0.05);
        assertEquals("regular VI after summarization, EnergyColor.FOCUS_SUMMARIZATION", 0.0, valueFocusSummarization, 0.05);        

        TestHelper.testDone();        
    }

}
