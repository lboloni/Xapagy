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
package org.xapagy.ui.observers;

import java.io.IOException;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.debug.Runner;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;
import org.xapagy.ui.prettyprint.PrintDetail;
import org.xapagy.util.ClassResourceHelper;

/**
 * @author Ladislau Boloni
 * Created on: Aug 30, 2010
 */
public class testToStringObserver {

    /**
     * Set this to false to prevent the printing during unit tests
     */
    public static boolean printToOutput = false;

    /**
     * Tests the TSO logging to individual files
     * 
     */
    // disconnected, because of the thus - nothing to connect to stuff
    // @Test
    public void testIndividualFiles() {
        TestHelper.testStart("TSO logging to individual files");
        String storyFile = null;
        try {
            storyFile =
                    ClassResourceHelper.getResourceFile(this, "story.xst")
                            .getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setPrintToOutput(testToStringObserver.printToOutput); // turn to
                                                                    // true for
                                                                    // inspecting
        r.tso.addPrintWhat(DebugEventType.AFTER_LOOP_ITEM_EXECUTION,
                "SHADOW_INSTANCES", PrintDetail.DTL_DETAIL);
        r.tso.setPrintToTextFiles(true);
        r.execFile(storyFile, null);
        TestHelper.testDone();
    }

    /**
     * Test the TSO logging with surprise
     */
    // disconnected, because of the thus - nothing to connect to stuff
    // @Test
    public void testTraceChoices() {
        TestHelper.testStart("TSO: tracing with choices");
        String storyFile = "story.xst";
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.CHOICES_NATIVE);
        r.tso.setPrintToOutput(testToStringObserver.printToOutput);
        r.execFile(this, storyFile, null);
        r.exec("----");
        r.execFile(this, storyFile, null);
        TestHelper.testDone();
    }

    /**
     * Tests the tracing with verbalization
     * 
     */
    // disconnected, because of the thus - nothing to connect to stuff
    // @Test
    public void testTraceVerbalization() {
        TestHelper.testStart("TSO: Tracing with verbalization");
        String storyFile = "story.xst";
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.VERBALIZATION);
        r.tso.setPrintToOutput(testToStringObserver.printToOutput);
        r.execFile(this, storyFile, null);
        TestHelper.testDone();
    }

}
