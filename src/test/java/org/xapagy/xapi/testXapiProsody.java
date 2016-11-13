/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
package org.xapagy.xapi;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * Created on: Jun 1, 2011
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
