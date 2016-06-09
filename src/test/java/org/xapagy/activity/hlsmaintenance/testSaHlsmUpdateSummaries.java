/*
   This file is part of the Xapagy project
   Created on: Nov 12, 2011
 
   org.xapagy.activity.testSaUpdateSummaries
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.parameters.Parameters;

/**
 * @author Ladislau Boloni
 * 
 */
public class testSaHlsmUpdateSummaries {

    /**
     * Tests the creation of the summaries
     */
    @Test
    public void testAlternative() {
        String description = "Create alternative summaries";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        Parameters p = r.agent.getParameters();
        p.set("A_HLSM", "G_GENERAL",
                "N_PROCESS_SUMMARIZATION_BLOCKS", true);
        // r.tso.setTrace();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.tso.ppd("FOCUS_STRUCTURED");
        TestHelper.testIncomplete();
    }

    /**
     * Tests the creation of the summaries
     */
    @Test
    public void testRepetitive() {
        String description = "SaUpdateSummaries - create repetitive summaries";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        // r.tso.setTrace();
        Parameters p = r.agent.getParameters();
        p.set("A_HLSM", "G_GENERAL",
                "N_PROCESS_SUMMARIZATION_BLOCKS", true);
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.tso.ppd("FOCUS_STRUCTURED");
        TestHelper.testIncomplete();
    }

}
