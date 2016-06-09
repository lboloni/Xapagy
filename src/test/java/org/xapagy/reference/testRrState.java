/*
   This file is part of the Xapagy project
   Created on: Apr 15, 2014
 
   org.xapagy.reference.testRrState
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.reference;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.reference.rrState.rrPhase;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;

/**
 * Test the creation and printing of the rrState object
 * 
 * @author Ladislau Boloni
 * 
 */
public class testRrState {

    @Test
    public void test() throws XapiParserException {
        String description =
                "Test for the creation of a direct reference RrContext object.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        XapiParser xp = r.agent.getXapiParser();
        rrState rrs = null;
        @SuppressWarnings("unused")
        String text = null;
        //
        // no competition
        //
        rrs = rrState.createNoCompetitionResConf();
        Assert.assertSame("phase", rrPhase.WINNER, rrs.getPhase());
        text = rrs.toString();
        // TextUi.println(text);
        //
        // undetermined
        //
        rrs = rrState.createUndetermined();
        Assert.assertSame("phase", rrPhase.UNDETERMINED, rrs.getPhase());
        text = rrs.toString();
        // TextUi.println(text);
        //
        // calculated
        //
        ConceptOverlay coObject = xp.parseCo("w_c_bai20 w_c_bai21 w_c_bai22");
        ConceptOverlay coReference = xp.parseCo("w_c_bai20 w_not-c_bai21");
        rrs = rrState.createCalculated(coObject, coReference);
        Assert.assertSame("phase", rrPhase.UNDETERMINED, rrs.getPhase());
        Assert.assertEquals("overall score", -45.0, rrs.getOverallScore(), 0.1);
        text = rrs.toString();
        // TextUi.println(text);
        TestHelper.testDone();

    }

}
