/*
   This file is part of the Xapagy project
   Created on: Jun 1, 2011
 
   org.xapagy.xapi.testXapiProsody
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * 
 */
public class testXapiProsody {

    public static final double ACCURACY = 0.001;

    /**
     * Tests whether after the Xapi splitter you will have a correct way to
     * access the labels.
     * 
     */
    @Test
    public void testProsody() {
        String testDescription = "Tests various prosody models.";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.createAabConcepts();
        // two regular sentences
        double oldTime = r.agent.getTime();
        r.exec("A w_c_bai20 / exists.");
        r.exec("A w_c_bai21 / exists.");
        // TextUi.println(r.agent.getTime());
        Assert.assertTrue(Math.abs(r.agent.getTime() - oldTime - 4
                * XapiParser.PAUSE_PERIOD) < testXapiProsody.ACCURACY);
        // a comma sentence
        oldTime = r.agent.getTime();
        r.exec("A w_c_bai21 / exists,");
        Assert.assertTrue(Math.abs(r.agent.getTime() - oldTime
                - XapiParser.PAUSE_COMMA) < testXapiProsody.ACCURACY);
        // a semicolon sentence.
        oldTime = r.agent.getTime();
        r.exec("The w_c_bai21 / wa_v_av40 / the w_c_bai20;");
        Assert.assertTrue(Math.abs(r.agent.getTime() - oldTime
                - XapiParser.PAUSE_SEMICOLON) < testXapiProsody.ACCURACY);
        // a short pause (1 sec)
        oldTime = r.agent.getTime();
        r.exec("-");
        Assert.assertTrue(Math.abs(r.agent.getTime() - oldTime
                - XapiParser.PAUSE_1_DASH) < testXapiProsody.ACCURACY);
        // a medium pause (10 sec)
        oldTime = r.agent.getTime();
        r.exec("--");
        Assert.assertTrue(Math.abs(r.agent.getTime() - oldTime
                - XapiParser.PAUSE_2_DASH) < testXapiProsody.ACCURACY);
        // a long pause (100 sec)
        oldTime = r.agent.getTime();
        r.exec("---");
        Assert.assertTrue(Math.abs(r.agent.getTime() - oldTime
                - XapiParser.PAUSE_3_DASH) < testXapiProsody.ACCURACY);
        // a long pause (1000 sec)
        TestHelper.testDelayMarker();
        oldTime = r.agent.getTime();
        r.exec("----");
        Assert.assertTrue(Math.abs(r.agent.getTime() - oldTime
                - XapiParser.PAUSE_4_DASH) < testXapiProsody.ACCURACY);
        TestHelper.testDone();
    }
}
