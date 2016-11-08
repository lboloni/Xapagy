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
 * Created on: May 14, 2014
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
