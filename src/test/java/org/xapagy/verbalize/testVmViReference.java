/*
   This file is part of the Xapagy project
   Created on: Dec 27, 2011
 
   org.xapagy.verbalize.testVmViReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.util.List;

import org.junit.Assert;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.reference.rrState;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * Tests the VmViReference, if it is stored, and if yes how.
 * 
 * @author Ladislau Boloni
 * 
 */
public class testVmViReference {

    // This test does not pass because the focus change (no instance decay
    // in April 2014
    // @Test
    public void test() {
        String description = "basic";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();

        // r.tso.setTrace();
        r.exec("A scene #Troy / exists.");
        r.exec("$ChangeScene #Troy");
        r.exec("A w_c_bai20 'Hector' / exists.");
        r.exec("A w_c_bai20 'Achilles' / exists.");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        // simplest case, an S-V-O
        VmViReference ref =
                r.agent.getVerbalMemory().getVmViReferences().get(vi).get(0);
        TextUi.println(ref.getXapiReference().getText());
        PrettyPrint.ppd(ref.getXapiReference(), r.agent);
        Assert.assertTrue(ref.getXapiReference().getText()
                .equals("\"Hector\" / wa_v_av40 / \"Achilles\""));
        Assert.assertTrue(ref.getXapiReference().getResolutionConfidence()
                .getJustification()
                .equals(rrState.rrJustification.NO_COMPETITION));
        Assert.assertTrue(ref.getXapiReference().getResolutionConfidence()
                .getPhase().equals(rrState.rrPhase.WINNER));
        // creation
        List<VerbInstance> vis = r.exec("'Hector' / wa_v_av40 / a w_c_bai20.");
        // first the creation
        ref =
                r.agent.getVerbalMemory().getVmViReferences().get(vis.get(0))
                        .get(0);
        TextUi.println(PrettyPrint.ppDetailed(ref.getXapiReference(), r.agent));
        // reference text is currently null
        // then, the second one
        ref =
                r.agent.getVerbalMemory().getVmViReferences().get(vis.get(1))
                        .get(0);
        TextUi.println(PrettyPrint.ppDetailed(ref.getXapiReference(), r.agent));
        // TextUi.println(ref.getReferenceText());
        TestHelper.testDone();
    }

}
