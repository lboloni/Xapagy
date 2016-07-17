/*
   This file is part of the Xapagy project
   Created on: Jun 18, 2011
 
   org.xapagy.debug.testSG2
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug.storygenerator;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * 
 */
@SuppressWarnings("unused")
public class testStoryGenerator {

    @Test
    public void testNameGenerators() {
        String description =
                "The concept name, proper name and verb name generators of the the story generator";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        for (int i = 0; i != 5; i++) {
            String temp = r.properNameGenerator.generateProperName();
            // TextUi.println(temp);
        }
        TestHelper.testDone();
    }

}
