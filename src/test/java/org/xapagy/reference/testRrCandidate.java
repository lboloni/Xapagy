/*
   This file is part of the Xapagy project
   Created on: Apr 15, 2014
 
   org.xapagy.reference.testRrCandidate
 
   Copyright (c) 2008-2013 Ladislau Boloni
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
 * 
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
