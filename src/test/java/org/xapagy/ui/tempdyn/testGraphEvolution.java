/*
   This file is part of the Xapagy project
   Created on: May 14, 2014
 
   org.xapagy.ui.tempdyn.testGraphEvolution
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.ui.tempdyn;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.debug.Runner;
import org.xapagy.recall.testRecall;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;

/**
 * @author Ladislau Boloni
 *
 */
public class testGraphEvolution {

    @Test
    public void testGraphs() {
        String description = "Graph evolution";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.COMPACT);
        r.exec("$Include 'P-FocusOnly'");
        ABStory story = testRecall.createAchillesHector("#one", false, false, null);
        r.exec(story);
        r.exec("$Include 'P-All'");
        TemporalDynamicsObserver tdo = new TemporalDynamicsObserver();
        tdo.addObserveWhat(DebugEventType.BEFORE_DA_STEP);
        r.agent.addObserver("TemporalDynamicsObserver", tdo);
        ABStory story2 = testRecall.createAchillesHector("#two", false, false, null);
        story2.deleteLine(-1);
        TextUi.println(story2);
        r.exec(story2);
       
        // interactive visualization
        //tdInteractiveVisualization tdi =
        //        new tdInteractiveVisualization(tdo.getDatabase(), r.agent);
        //tdi.visualize();

        TestHelper.testDone();
    }
    
}
