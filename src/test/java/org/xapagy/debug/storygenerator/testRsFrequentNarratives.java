/*
   This file is part of the Xapagy project
   Created on: Jul 3, 2014
 
   org.xapagy.debug.storygenerator.testNarratives
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.debug.storygenerator;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.ui.TextUi;

/**
 * Trying out the different narratives from the RsFrequent narratives. How do they 
 * look like and whether they run
 * 
 * @author Ladislau Boloni
 *
 */
public class testRsFrequentNarratives {


    /**
     * Trying out the pure repetition model
     */
    @Test
    public void testForking() {
        String description = "Forking";
        TestHelper.testStart(description);
        RsTestingUnit rtu = RsFrequentNarratives.createForkSimple(5, 4);
        TextUi.println(rtu.getFullStory());
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        rtu.runAll(r);
        TestHelper.testDone();
    }

    
    /**
     * Trying out the pure repetition model
     */
    @Test
    public void testPureRepetition1() {
        String description = "PureRepetition1";
        TestHelper.testStart(description);
        RsTestingUnit rtu = RsFrequentNarratives.createPureRepetition(1);
        TextUi.println(rtu.getFullStory());
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        rtu.runAll(r);
        TestHelper.testDone();
    }

    
    /**
     * Trying out the pure repetition model
     */
    @Test
    public void testDirtyRepetition1() {
        String description = "DirtyRepetition1";
        TestHelper.testStart(description);
        RsTestingUnit rtu = RsFrequentNarratives.createDirtyRepetition(1);
        TextUi.println(rtu.getFullStory());
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        rtu.runAll(r);
        TestHelper.testDone();
    }

    
    /**
     * Trying out the approximate repetition with a framework where the memory is longer
     */
    @Test
    public void testApproximateRepetition_MemoryLonger() {
        String description = "ApproximateRepetition_MemoryLonger";
        TestHelper.testStart(description);
        RsTestingUnit rtu = RsFrequentNarratives.createApproximateRepetition(1, 2);
        TextUi.println(rtu.getFullStory());
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        rtu.runAll(r);
        TestHelper.testDone();
    }
    
}
