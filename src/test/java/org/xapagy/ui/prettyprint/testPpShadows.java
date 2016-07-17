/*
   This file is part of the Xapagy project
   Created on: Nov 22, 2011
 
   org.xapagy.ui.prettyprint.testPpShadows
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * 
 */
public class testPpShadows {

    @Test
    public void testShadowInstances() {
        String description = "Printing of the shadow instances";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace();
        // the original observations
        r.printOn = true;
        r.exec("$CreateScene #N0 CloseOthers With Instances 'Billy', 'Johnny'");

        r.exec("'Billy' / wa_v_av40 / 'Johnny'.");
        r.exec("'Johnny' / wa_v_av40 / 'Billy'.");
        // r.tso.ppd(PrintWhat.FOCUS_STRUCTURED);
        r.exec("----");
        // the suggestion
        r.exec("$CreateScene #Conversation CloseOthers With Instances w_c_bai21 'Billy', w_c_bai21 'Johnny'");
        r.exec("$CreateScene #N1 Current With Instances 'Narrator'");

        r.exec("'Narrator' / says in #Conversation // 'Billy' / wa_v_av40 / 'Johnny'.");
        VerbInstance vi =
                r.exac("'Narrator' / says in #Conversation // 'Johnny' / wa_v_av40 / 'Billy'.");
        r.exec("'Narrator' / says in #Conversation // 'Johnny' / wa_v_av41 / 'Billy'.");
        PrettyPrint.ppd(vi, r.agent);
        PrettyPrint.ppd(vi.getQuote(), r.agent);
        // r.tso.ppc(PrintWhat.SHADOW_INSTANCES);
        r.exec("----");
        // the recall
        r.exec("$CreateScene #R1 CloseOthers With Instances w_c_bai21 'Billy', w_c_bai21 'Johnny'");
        r.exec("'Billy' / wa_v_av40 / 'Johnny'.");
        r.tso.ppc("SHADOW_INSTANCES");
        TestHelper.testIncomplete();
    }

}
