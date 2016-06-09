/*
   This file is part of the Xapagy project
   Created on: May 21, 2011
 
   org.xapagy.xapi.testXapiSplitter
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.util.AbstractMap.SimpleEntry;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefVi;

/**
 * @author Ladislau Boloni
 * 
 */
public class testImplicitCreationHelper {

    /**
     * Tests whether the XapiSplitter correctly counts the number of instance
     * creations in Xapi sentences.
     * @throws XapiParserException 
     * 
     */
    @Test
    public void testCount() throws XapiParserException {
        String testDescription =
                "The XapiSplitter correctly counts the number of instance creations in Xapi sentences.";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.createAabConcepts();
        XrefStatement entry =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "The w_c_bai20 / wa_v_av40 / the thing.");
        int count = ImplicitCreationHelper.countCreations(entry);
        Assert.assertTrue(count == 0);
        entry =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "A w_c_bai20 / wa_v_av40 / the thing.");
        count = ImplicitCreationHelper.countCreations(entry);
        Assert.assertTrue(count == 1);
        entry =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "A w_c_bai20 / wa_v_av40 / a thing.");
        count = ImplicitCreationHelper.countCreations(entry);
        Assert.assertTrue(count == 2);
        entry =
                (XrefStatement) r.agent
                        .getXapiParser()
                        .parseLine(
                                "\"Hector\" / says in scene // the w_c_bai20 / wa_v_av40 / the thing.");
        count = ImplicitCreationHelper.countCreations(entry);
        Assert.assertTrue(count == 0);
        entry =
                (XrefStatement) r.agent
                        .getXapiParser()
                        .parseLine(
                                "A \"Hector\" / says in scene // the w_c_bai20 / wa_v_av40 / the thing.");
        count = ImplicitCreationHelper.countCreations(entry);
        Assert.assertTrue(count == 1);
        entry =
                (XrefStatement) r.agent
                        .getXapiParser()
                        .parseLine(
                                "\"Hector\" / says in scene // a w_c_bai20 / wa_v_av40 / the thing.");
        count = ImplicitCreationHelper.countCreations(entry);
        Assert.assertTrue(count == 1);
        // TextUi.println("Count = " + count + " for " + entry.getValue());
        TestHelper.testDone();
    }

    /**
     * This is not working correctly! - the non-quoted one misses the middle
     * @throws XapiParserException 
     */
    @Test
    public void testQuotedQuote() throws XapiParserException {
        String testDescription =
                "The XapiSplitter correctly splits sentences into creation and reference part.";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.createAabConcepts();
        XrefStatement statement =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "The w_c_bai20 / says in scene #A //"
                                + "The w_c_bai21 / says in scene #B // "
                                + "a 'Hector' / wa_v_av40 / the thing.");
        PrettyPrint.ppd(statement, r.agent);
        SimpleEntry<XrefVi, XrefVi> pair =
                ImplicitCreationHelper.splitCreation(r.agent, statement);
        PrettyPrint.ppd(pair.getKey(), r.agent);
        PrettyPrint.ppd(pair.getValue(), r.agent);
        TestHelper.testDone();

    }

    /**
     * Tests whether the XapiSplitter correctly splits sentences into creation
     * and reference part.
     * @throws XapiParserException 
     * 
     */
    @Test
    public void testSplitting() throws XapiParserException {
        String testDescription =
                "The XapiSplitter correctly splits sentences into creation and reference part.";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.createAabConcepts();
        // new subject, one level
        XrefStatement entry =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "A w_c_bai20 / wa_v_av40 / the thing.");
        SimpleEntry<XrefVi, XrefVi> pair =
                ImplicitCreationHelper.splitCreation(r.agent, entry);
        XrefVi creationStatement = pair.getKey();
        // check whether the creation statement is correctly created
        r.ah.voContains(creationStatement.getVerb().getVo(),
                Hardwired.VM_CREATE_INSTANCE);
        r.ah.coContains(creationStatement.getAdjective().getCo(), "c_bai20");

        // new object, one level
        entry =
                (XrefStatement) r.agent.getXapiParser().parseLine(
                        "The w_c_bai20 / wa_v_av40 / a thing.");
        pair = ImplicitCreationHelper.splitCreation(r.agent, entry);
        creationStatement = pair.getKey();
        r.ah.voContains(creationStatement.getVerb().getVo(),
                Hardwired.VM_CREATE_INSTANCE);
        r.ah.coContains(creationStatement.getAdjective().getCo(),
                Hardwired.C_THING);

        // new subject, in the inquit
        entry =
                (XrefStatement) r.agent
                        .getXapiParser()
                        .parseLine(
                                "A w_c_bai20 / says in scene #A // The w_c_bai21 / wa_v_av40 / the thing.");
        pair = ImplicitCreationHelper.splitCreation(r.agent, entry);
        creationStatement = pair.getKey();
        r.ah.voContains(creationStatement.getVerb().getVo(),
                Hardwired.VM_CREATE_INSTANCE);
        r.ah.coContains(creationStatement.getAdjective().getCo(), "c_bai20");

        // new subject in the quote
        entry =
                (XrefStatement) r.agent
                        .getXapiParser()
                        .parseLine(
                                "The w_c_bai20 / says in scene #A // A w_c_bai21 / wa_v_av40 / the thing.");
        pair = ImplicitCreationHelper.splitCreation(r.agent, entry);
        creationStatement = (XrefVi) pair.getKey().getQuote();
        r.ah.voContains(creationStatement.getVerb().getVo(),
                Hardwired.VM_CREATE_INSTANCE);
        r.ah.coContains(creationStatement.getAdjective().getCo(), "c_bai21");

        // new subject in qouted qoute
        entry =
                (XrefStatement) r.agent
                        .getXapiParser()
                        .parseLine(
                                "The w_c_bai20 / says in scene #A // The w_c_bai21 / says in scene #B // A 'Hector' / wa_v_av40 / the thing.");
        pair = ImplicitCreationHelper.splitCreation(r.agent, entry);
        PrettyPrint.ppd(pair.getKey(), r.agent);
        PrettyPrint.ppd(pair.getValue(), r.agent);

        TestHelper.testDone();
    }

}
