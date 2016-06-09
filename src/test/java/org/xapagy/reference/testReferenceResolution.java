/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2010
 
   org.xapagy.model.testReference
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.reference;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * Testing for references by parsing complete Xapi whole sentences.
 * 
 * Mostly, this test checks whether the resolution context is set up correctly -
 * for the actual resolution test see testResolutionWithRrContext
 * 
 * @author Ladislau Boloni
 * 
 */
@SuppressWarnings("unused")
@Deprecated
public class testReferenceResolution {

    /**
     * 
     * Test instance resolution based on concepts of the basic level of the
     * hierarchy
     * 
     */
    @Test
    public void testBasicLevelResolution() {
        String description =
                "Test instance resolution based on concepts of the basic level of the hierarchy";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        Instance instMan = r.exac("A w_c_bai20 / exists.").getSubject();
        Instance instWoman = r.exac("A w_c_bai21 / exists.").getSubject();
        // print the situation
        String temp = PrettyPrint.ppConcise(instMan, r.agent);
        // TextUi.println(temp);
        temp = PrettyPrint.ppConcise(instWoman, r.agent);
        // TextUi.println(temp);
        // print the situation
        VerbInstance vi =
                r.exec("The w_c_bai21 / wa_v_av40 / the w_c_bai20.").get(0);
        temp = PrettyPrint.ppConcise(vi, r.agent);
        // TextUi.println(temp);
        Assert.assertTrue(vi.getSubject().equals(instWoman));
        Assert.assertTrue(vi.getObject().equals(instMan));
        TestHelper.testDone();
    }

    /**
     * Test the group reference. The group should be created if it did not exist
     * 
     * FIXME: this is not the case currently!!!
     * 
     */
    @Test
    public void testGroup() {
        String description = "Reference in the quote (the dog must be Fido)";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.printOn = true;
        r.exec("$CreateScene #Scene1 CloseOthers With Instances w_c_bai20 'Jenny', w_c_bai21 'Billy', w_c_bai22 'Fluffy'");
        Instance instGroup =
                r.exec("The w_c_bai21 + the w_c_bai22 / wa_v_av40.").get(0)
                        .getSubject();
        String temp = PrettyPrint.ppDetailed(instGroup, r.agent);
        TextUi.println(temp);
        Instance instGroup2 =
                r.exec("The w_c_bai22 + the w_c_bai21 / wa_v_av40.").get(0)
                        .getSubject();
        temp = PrettyPrint.ppDetailed(instGroup2, r.agent);
        TextUi.println(temp);
        Assert.assertTrue(instGroup.equals(instGroup2));
        Instance instGroup3 =
                r.exec("The w_c_bai22 + the w_c_bai20 + the w_c_bai21 / wa_v_av40.")
                        .get(0).getSubject();
        temp = PrettyPrint.ppDetailed(instGroup3, r.agent);
        TextUi.println(temp);
        Assert.assertTrue(!instGroup.equals(instGroup3));
        TestHelper.testDone();
    }

    /**
     * 
     * Tests the resolution based on proper names
     * 
     */
    @Test
    public void testProperNameResolution() {
        String description = "Instance resolution based on proper names";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        Instance instHector =
                r.exec("A 'Hector' / exists.").get(1).getSubject();
        Instance instAchilles =
                r.exec("An 'Achilles' / exists.").get(1).getSubject();
        // print the situation
        String temp = PrettyPrint.ppConcise(instHector, r.agent);
        // TextUi.println(temp);
        temp = PrettyPrint.ppConcise(instAchilles, r.agent);
        // TextUi.println(temp);
        // print the situation
        VerbInstance vi = r.exec("'Achilles' / wa_v_av40 / 'Hector'.").get(0);
        temp = PrettyPrint.ppConcise(vi, r.agent);
        // TextUi.println(temp);
        Assert.assertTrue(vi.getSubject().equals(instAchilles));
        Assert.assertTrue(vi.getObject().equals(instHector));
        TestHelper.testDone();
    }

    /**
     * 
     * Tests the resolution based on recentness
     * 
     */
    // This test does not pass because the focus change (no instance decay
    // in April 2014
    // @Test
    public void testRecentness() {
        String description = "Resolution based on recentness";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        // r.tso.addPrintWhat(DebugEventType.AFTER_INSTANCE_RESOLUTION,
        // PrintWhat.INSTANCE_RESOLUTION_CHOICES);
        // Instance instHector =
        // r.exec("A w_c_bai21 \"Hector\" / exists.").get(1).getSubject();
        Instance instHector =
                r.exac("A w_c_bai20 'Hector' / exists.").getSubject();
        // Instance instAchilles =
        // r.exec("A w_c_bai21 \"Achilles\" / exists.").get(1).getSubject();
        Instance instAchilles =
                r.exac("A w_c_bai20 'Achilles' / exists.").getSubject();
        VerbInstance vi = r.exac("The w_c_bai20 / is-a / w_c_bai21.");
        Assert.assertTrue(vi.getSubject().equals(instAchilles));
        TestHelper.testDone();
    }

    /**
     * Test for instance reference by label
     */
    @Test
    public void testReferenceByLabel() {
        String description = "Reference by label";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #R CloseOthers With Instances w_c_bai20 #A, w_c_bai20 #B");

        VerbInstance vi1 =
                r.exac("The w_c_bai20 #A / wa_v_av40 / the w_c_bai20 #B.");
        fmt.add("resolved for #A w_c_bai20");
        fmt.addIndented(PrettyPrint.ppDetailed(vi1.getSubject(), r.agent));
        fmt.add("resolved for #B w_c_bai20");
        fmt.addIndented(PrettyPrint.ppDetailed(vi1.getObject(), r.agent));
        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }

    /**
     * Test reference in the quote. The dog to which Jenny refers must be Fido!
     * 
     */
    @Test
    public void testReferenceQuote() {
        String description = "Reference in the quote (the dog must be Fido)";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.printOn = true;
        r.exec("$CreateScene #Scene1 CloseOthers With Instances w_c_bai20 'Jenny', w_c_bai20 'Fluffy'");
        r.exec("$CreateScene #Scene2 Current With Instances w_c_bai22 'Billy'");
        Instance instFido =
                r.exec("A w_c_bai21 'Fido' / exists.").get(1).getSubject();
        r.exec("$ChangeScene #Scene1");
        VerbInstance vi =
                r.eh.getAction(r
                        .exec("'Jenny' / says in #Scene2 // the w_c_bai22 / wa_v_av40 / the w_c_bai21."));
        Instance referredDog = vi.getQuote().getObject();
        String temp = PrettyPrint.ppDetailed(vi, r.agent);
        // TextUi.println(temp);
        Assert.assertTrue(referredDog.equals(instFido));
        TestHelper.testDone();
    }

    /**
     * Test for the relational reference
     */
    @Test
    public void testReferenceRelational() {
        String description = "Relational reference";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.printOn = false;
        r.exec("$CreateScene #Scene1 CloseOthers With Instances w_c_bai20 'Jenny', w_c_bai21 'Fluffy', w_c_bai22 'Billy'");
        Instance instFluffy =
                r.exac("The w_c_bai20 / has / the w_c_bai21.").getObject();
        r.exec("A w_c_bai21 'Muffy' / exists.");
        Instance instReferredDog =
                r.exac("The w_c_bai21  -- of -- the w_c_bai20 / wa_v_av40.")
                        .getSubject();
        String temp = PrettyPrint.ppDetailed(instReferredDog, r.agent);
        Assert.assertTrue(instFluffy.equals(instReferredDog));
        r.exec("A w_c_bai21 w_c_bai24 / exists.");
        r.exec("'Billy' / has / the w_c_bai24.");
        // r.debugMode();
        Instance instPedro =
                r.eh.getAction(
                        r.exec("The w_c_bai21 -- of -- 'Billy' / is-a / 'Pedro'."))
                        .getSubject();
        temp = PrettyPrint.ppDetailed(instPedro, r.agent);
        r.println(temp);
        r.ah.hasAttribute(instPedro, "\"Pedro\"");
        r.ah.hasAttribute(instPedro, "c_bai24");
        TestHelper.testDone();
    }

    /**
     * Test for instance reference by label
     */
    @Test
    public void testSceneReferenceByLabel() {
        String description = "Reference by label";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #R CloseOthers With Instances w_c_bai20");
        r.exec("$CreateScene #Story With Instances w_c_bai20, w_c_bai21");
        VerbInstance vi1 =
                r.exac("The w_c_bai20 / says in #Story // The w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        fmt.addIndented(PrettyPrint.ppDetailed(vi1.getQuoteScene(), r.agent));
        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }

}
