/*
   This file is part of the Xapagy project
   Created on: Mar 5, 2016
 
   org.xapagy.debug.testUI
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.debug;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;

/**
 * 
 * A test for walking through the UI
 * 
 * @author Ladislau Boloni
 *
 */
public class testUI {

    // @Test
    public void test() {
        String description = "Debug the UI";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$Include 'P-FocusOnly'");
        r.exec("$CreateScene #first CloseOthers With Instances w_c_bai20 'Hector' #H, w_c_bai20 'Achilles' #A");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("$DebugHere");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
    }
}
