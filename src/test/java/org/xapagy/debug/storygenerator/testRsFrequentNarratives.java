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
 * Created on: Jul 3, 2014
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
