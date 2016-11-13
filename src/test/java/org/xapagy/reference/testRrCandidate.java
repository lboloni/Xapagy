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

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.XapiParserException;

/**
 * Tests the creation and printing of rrCandidate
 * 
 * @author Ladislau Boloni
 * Created on: Apr 15, 2014
 */
public class testRrCandidate {

    @Test
    public void test() throws XapiParserException {
        String description = "rrCandidate";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        //
        // a story
        //
        r.exec("A w_c_bai20 'Achilles' / exists.");
        r.exec("A w_c_bai20 'Hector' / exists.");
        VerbInstance vi = r.exac("'Achilles' / wa_v_av10 / 'Hector'.");
        Instance instHector = vi.getObject();
        rrContext rrc = testResolutionWithRrContext.createRefAsSubject(r, "w_c_bai20");
        rrCandidate rrcand = new rrCandidate(instHector, rrc);
        rrcand.setAssignedScore(0.99);
        String text = rrcand.toString();
        TextUi.println(text);
        TestHelper.testDone();

    }

}
