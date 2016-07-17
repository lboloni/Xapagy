/*
   This file is part of the Xapagy project
   Created on: Feb 22, 2011
 
   org.xapagy.xapi.testWordResolutionHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * 
 */
public class testWordResolutionHelper {

    @Test
    public void testComposition() throws XapiParserException {
        String description = "Composition of words for an overlay";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        List<String> words = new ArrayList<>();
        words.add("w_c_bai20");
        words.add("w_c_bai21");
        ConceptOverlay co =
                WordResolutionHelper.resolveWordsToCo(r.agent, words);
        r.ah.coContains(co, "c_bai20");
        r.ah.coContains(co, "c_bai21");
        TestHelper.testDone();
    }

}
