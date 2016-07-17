/*
   This file is part of the Xapagy project
   Created on: Dec 27, 2011
 
   org.xapagy.verbalize.testVmViReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;
import org.xapagy.xapi.reference.XapiReference.XapiReferenceType;

/**
 * 
 * Tests the VmViReference, if it is stored, and if yes how.
 * 
 * @author Ladislau Boloni
 * 
 */
public class testVmInstanceReference {

    /**
     * Testing the references to Hector
     */
    @Test
    public void test() {
        String description = "basic";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace();
        r.exec("A scene #Troy / exists.");
        r.exec("$ChangeScene #Troy");
        r.exec("A w_c_bai20 'Hector' / exists.");
        r.exec("A w_c_bai20 'Achilles' / exists.");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance instHector = vi.getSubject();
        // Instance instAchilles = vi.getObject();
        // reference by different word
        r.exec("'Hector' / is-a / w_c_bai21.");
        r.exec("The w_c_bai21 / wa_v_av40 / 'Achilles'.");
        // reference relational
        r.exec("'Achilles' / CreateRelation wv_vr_rel1 / 'Hector'.");
        r.tso.ppd("FOCUS_STRUCTURED");
        r.exec("The w_c_bai20 -- wv_vr_rel1 -- 'Achilles' / wa_v_av40 / 'Achilles'.");
        // references to Achilles
        // references to Hector
        TextUi.println("Hector");
        List<VmInstanceReference> list =
                r.agent.getVerbalMemory().getVmInstanceReferences()
                        .get(instHector);
        // for (VmInstanceReference ref : list) {
        // TextUi.println("    " + ref.getXapiReference().getText());
        // }
        // The first reference must be a direct reference, part of the creation
        XapiReference ref0 = list.get(0).getXapiReference();
        Assert.assertTrue(ref0.getType() == XapiReferenceType.REF_DIRECT);
        Assert.assertTrue(ref0.getPositionInParent() == XapiPositionInParent.SUBJECT);
        Assert.assertTrue(ref0.getParent().getPositionInParent() == XapiPositionInParent.IMPLICIT_CREATION_ACTIONPART);
        Assert.assertTrue(ref0.getText().equals("w_c_bai20 \"Hector\""));
        // PrettyPrint.ppd(ref0, r.agent);
        // The third reference is the one from the is-a trojan
        XapiReference ref3 = list.get(3).getXapiReference();
        PrettyPrint.ppd(ref3, r.agent);
        Assert.assertTrue(ref3.getType() == XapiReferenceType.REF_DIRECT);
        Assert.assertTrue(ref3.getPositionInParent() == XapiPositionInParent.SUBJECT);
        Assert.assertTrue(ref3.getParent().getPositionInParent() == XapiPositionInParent.ACTION);
        // The fifth reference is the relational one
        XapiReference ref5 = list.get(5).getXapiReference();
        Assert.assertTrue(ref5.getType() == XapiReferenceType.REF_RELATIONAL);
        Assert.assertTrue(ref5.getPositionInParent() == XapiPositionInParent.SUBJECT);
        Assert.assertTrue(ref5.getParent().getPositionInParent() == XapiPositionInParent.ACTION);
        PrettyPrint.ppd(ref5, r.agent);
        // now test the components of the relational reference
        // XrefRelational xrr = (XrefRelational) ref5;

        TestHelper.testDone();
    }
}