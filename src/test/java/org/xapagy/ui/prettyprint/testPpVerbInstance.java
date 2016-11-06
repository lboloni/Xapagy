/*
   This file is part of the Xapagy project
   Created on: Nov 2, 2011
 
   org.xapagy.ui.prettyprint.testPpVerbInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwVerbInstance;

/**
 * Tests the functionality of the ppVerbInstance printing.
 * 
 * @author Ladislau Boloni
 * 
 */
public class testPpVerbInstance {

    @SuppressWarnings("unused")
    @Test
    public void testSimple() {
        String description = "PpsFocus - printing a summary";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        // r.tso.trace();
        // r.tso.setCompactTraceIncludingVerbalization(true);
        r.exec("$CreateScene #Scene CloseOthers AddSummary With Instances w_c_bai21 'Billy', w_c_bai20 'Jenny'");

        VerbInstance vi1 =
                r.exac("The w_c_bai21 / CreateRelation wv_vr_rel1 / the w_c_bai20.");
        VerbInstance vi2 = r.exac("The w_c_bai21 / wa_v_av40 / the w_c_bai20.");
        VerbInstance viA = r.exac("The w_c_bai20 / wa_v_av41 / the w_c_bai21.");
        VerbInstance viQ = r.exac("Wh / wa_v_av41 / the w_c_bai21?");
        VerbInstance vi4 = r.exac("The w_c_bai21 + the w_c_bai20 / wa_v_av42.");
        // printing w_c_bai21 + w_c_bai20 laughs, no summary here
        TextUi.println(xwVerbInstance.xwDetailed(new TwFormatter(), vi4, r.agent));
        TextUi.println(xwVerbInstance.xwDetailed(new TwFormatter(), viQ, r.agent));
        TextUi.println(xwVerbInstance.xwDetailed(new TwFormatter(), viA, r.agent));
        TestHelper.testIncomplete();
    }

}
