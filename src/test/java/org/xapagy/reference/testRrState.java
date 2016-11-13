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
 * Created on: Apr 15, 2014
 */
public class testRrState {

    @Test
    public void test() throws XapiParserException {
        String description =
                "Test for the creation of a direct reference RrContext object.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
