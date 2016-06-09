/*
   This file is part of the Xapagy project
   Created on: Nov 21, 2012
 
   org.xapagy.metaverbs.testSaMvForget
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * 
 */
public class testSaMvForget {

    /**
     * Test for the SaMvForget verb which removes somebody from the focus
     */
    @Test
    public void test() {
        String description = "SaMvForget";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        // r.tso.setTrace();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles', w_c_bai20 'Ulysses'");
        VerbInstance vi = r.exac("'Ulysses' / wa_v_av40 / 'Hector'.");
        Instance instUlysses = vi.getSubject();
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Ulysses' / forget.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.ah.isNotInFocus(instUlysses);
        TestHelper.testDone();
    }

}
