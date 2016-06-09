/*
   This file is part of the Xapagy project
   Created on: Apr 12, 2014
 
   org.xapagy.reference.testRrContext
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.reference;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;
import org.xapagy.xapi.reference.XapiReference.XapiReferenceType;

/**
 * Test the creation and the printing of the RrContext object (not the
 * resolution)
 * 
 * @author Ladislau Boloni
 * 
 */
public class testRrContext {

    /**
     * Test for the direct explicit building of the RrContext object.
     * @throws XapiParserException 
     */
    @Test
    public void testDirectExplicitBuild() throws XapiParserException {
        String description =
                "Test for the creation of a direct reference RrContext object.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        XapiParser xp = r.agent.getXapiParser();
        // r.exec("A w_c_bai20 'Achilles' / exists.");
        // r.exec("A w_c_bai20 'Hector' / exists.");
        Instance scene = r.agent.getFocus().getCurrentScene();
        ViPart partInVi = ViPart.Subject;
        ConceptOverlay coDirect = xp.parseCo("w_c_bai20");
        VerbOverlay verbsInVi = xp.parseVo("wa_v_av10");
        VerbInstance viInquitParent = null;
        //
        // now, create the rrContext
        //
        rrContext rrc =
                rrContext.createDirectReference(r.agent, coDirect, verbsInVi,
                        partInVi, scene, viInquitParent);
        //
        // Testing: whether the printing fails, and basic sanity test
        //
        // String text = rrc.toString();
        // TextUi.println(text);
        r.ah.coContains(rrc.getCoDirect(), "c_bai20");
        Assert.assertSame("Reference type is not the same, it should be",
                rrc.getReferenceType(), XapiReferenceType.REF_DIRECT);
        TestHelper.testDone();
    }

    /**
     * Test the creation of the rrContext object a XapiReference (which is
     * obtained from the parser)
     * @throws XapiParserException 
     */
    @Test
    public void testXapiBuild() throws XapiParserException {
        String description =
                "Test for the creation of a direct reference RrContext object.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        XapiParser xp = r.agent.getXapiParser();
        Instance scene = r.agent.getFocus().getCurrentScene();
        ViPart partInVi = ViPart.Subject;
        VerbOverlay verbsInVi = xp.parseVo("wa_v_av10");
        VerbInstance viInquitParent = null;
        XapiReference reference;
        @SuppressWarnings("unused")
        String text;
        rrContext rrc;
        //
        // direct reference
        //
        reference =
                xp.parseNounPhrase("w_c_bai20", XapiPositionInParent.SUBJECT);
        rrc =
                rrContext.createFromXapiReference(r.agent, reference,
                        verbsInVi, partInVi, scene, viInquitParent);
        text = rrc.toString();
        Assert.assertSame("Reference type incorrect",
                XapiReferenceType.REF_DIRECT, rrc.getReferenceType());
        r.ah.coContains(rrc.getCoDirect(), "c_bai20");
        // TextUi.println(text);
        //
        // group reference
        //
        reference =
                xp.parseNounPhrase("w_c_bai20 + w_c_bai21",
                        XapiPositionInParent.SUBJECT);
        rrc =
                rrContext.createFromXapiReference(r.agent, reference,
                        verbsInVi, partInVi, scene, viInquitParent);
        text = rrc.toString();
        Assert.assertSame("Reference type incorrect",
                XapiReferenceType.REF_GROUP, rrc.getReferenceType());
        Assert.assertSame("Two group members", 2, rrc.getGroupMembers().size());
        r.ah.coContains(rrc.getGroupMembers().get(1).getCoDirect(), "c_bai21");
        // TextUi.println(text);
        //
        // relational
        //
        reference =
                xp.parseNounPhrase("w_c_bai20 -- of -- w_c_bai21",
                        XapiPositionInParent.SUBJECT);
        rrc =
                rrContext.createFromXapiReference(r.agent, reference,
                        verbsInVi, partInVi, scene, viInquitParent);
        text = rrc.toString();
        // TextUi.println(text);
        Assert.assertSame("Reference type incorrect",
                XapiReferenceType.REF_RELATIONAL, rrc.getReferenceType());
        Assert.assertSame("Two COs", 2, rrc.getRelationCOs().size());
        Assert.assertSame("One relation", 1, rrc.getRelationChain().size());
        r.ah.coContains(rrc.getRelationCOs().get(1), "c_bai21");
        r.ah.voContains(rrc.getRelationChain().get(0), "vr_legally_owns");
        TestHelper.testDone();
    }
}
