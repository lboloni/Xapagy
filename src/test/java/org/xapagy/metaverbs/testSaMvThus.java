/*
   This file is part of the Xapagy project
   Created on: Jan 18, 2012
 
   org.xapagy.metaverbs.testSaMvThus
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class testSaMvThus {

    public double pushoutValue(boolean useThus) {
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Focus fc = r.agent.getFocus();
        // r.tso.setTrace();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Hector' / has / a w_c_bai21.");
        VerbInstance vi1 = r.exac("'Hector' / gives / the w_c_bai21.");
        if (useThus) {
            r.exac("'Achilles' / thus receives / the w_c_bai21.");
        } else {
            r.exac("'Achilles' / receives / the w_c_bai21.");
        }
        return fc.getSalience(vi1, EnergyColors.FOCUS_VI);
    }

    /**
     * Tests impact of SaMvThus
     */
    @Test
    public void test() {
        String description = "SaMvThus - creating a coincidence group";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // r.tso.setTrace();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        r.exec("'Hector' / has / a w_c_bai21.");
        VerbInstance vi1 = r.exac("'Hector' / gives / the w_c_bai21.");
        VerbInstance vi2 =
                r.exac("'Achilles' / thus receives / the w_c_bai21.");
        r.ah.linkedBy(Hardwired.LINK_COINCIDENCE, vi1, vi2);
        TestHelper.testDone();
    }

    /**
     * Tests that the coincidence verb is connected to the leader with
     * coincidence links and the other links are in place The leader is
     * determined with labels
     */
    @Test
    public void testLabelBasedCoincidence() {
        String description =
                "SaMvThus - creating a coincidence group with labels";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        @SuppressWarnings("unused")
        VerbInstance virel = r.exac("'Hector' / wcr_vr_rel1 / 'Achilles'.");
        VerbInstance vi1 = r.exac("'Hector' / #A wa_v_av1 / a w_c_bai21.");
        VerbInstance viskip = r.exac("'Hector' / #B wa_v_av10 / 'Achilles'.");
        VerbInstance vi2 =
                r.exac("'Achilles' / #A thus wa_v_av2 / the w_c_bai21.");
        // r.exec("$DebugHere");
        // test the link mesh
        r.ah.linkedBy(Hardwired.LINK_COINCIDENCE, vi1, vi2);
        r.ah.linkedBy(Hardwired.LINK_COINCIDENCE, vi2, vi1);
        r.ah.notLinkedBy(Hardwired.LINK_COINCIDENCE, vi1, viskip);
        r.ah.linkedBy(Hardwired.LINK_SUCCESSOR, vi1, viskip);
        r.ah.linkedBy(Hardwired.LINK_SUCCESSOR, vi2, viskip);
        // r.ah.linkedBy(ViLinkDB.IR_CONTEXT, vi2, virel);
        TestHelper.testDone();
    }

    /**
     * This test verifies that the verbs marked with "thus" do not push out the
     * previous action verbs
     */
    @Test
    public void testPushOut() {
        String description = "SaMvThus - verifying the pushout";
        TestHelper.testStart(description);
        double valueWith = pushoutValue(true);
        double valueWithout = pushoutValue(false);
        Formatter fmt = new Formatter();
        fmt.is("with", valueWith);
        fmt.is("without", valueWithout);
        TextUi.println(fmt);
        Assert.assertTrue(valueWith > valueWithout);
        TestHelper.testDone();
    }
}
