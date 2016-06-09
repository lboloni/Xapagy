/*
   This file is part of the Xapagy project
   Created on: Dec 29, 2013
 
   org.xapagy.metaverbs.testSaMvSuccessor
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * Tests whether the successor verb creates the links as expected
 * 
 * @author Ladislau Boloni
 * 
 */
public class testSaMvSuccessor {

    /**
     * Tests whether the SaMvSuccessor verb creates the links indeed
     */
    @Test
    public void testLinkCreation() {
        String description =
                "SaMvSuccessor - test that the link Successor verb creates a successor link";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        VerbInstance vi1 = r.exac("'Hector' / wa_v_av1 / 'Achilles'.");
        VerbInstance vi2 = r.exac("'Achilles' / wa_v_av2 / 'Hector'.");
        // test the link mesh
        r.ah.linkedBy(Hardwired.LINK_SUCCESSOR, vi1, vi2);
        // r.ah.linkedBy(ViLinkDB.SUCCESSOR, vi2, viskip);
        // WebGui.startWebGui(r.agent);
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }

}
