/*
   This file is part of the Xapagy project
   Created on: Aug 30, 2010
 
   org.xapagy.ui.observers.testToStringObserver
 
   Copyright (c) 2008-2010 Ladislau Boloni
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
 * 
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
