/*
   This file is part of the Xapagy project
   Created on: Jun 2, 2011
 
   org.xapagy.Test
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy;

import org.junit.Assert;

import org.xapagy.ui.TextUi;

/**
 * Various utility functions for uniformizing the unit tests
 * 
 * Fixme: should have time count as well...
 * 
 * @author Ladislau Boloni
 * 
 */
public class TestHelper {
	/**
	 *  If set to true, then the tests go through their verbose modes 
	 */
	public static boolean verbose = false;
	/**
	 *  Default state of verbose: if set allows us to set individual tests to verbose 
	 */
	public static boolean defaultVerbose = false;
	
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String outputDirName = "output";

    /**
     * To be called whenever we expect a longer delay...
     */
    public static void testDelayMarker() {
        TextUi.println("--- long process, delay ok...");
    }

    /**
     * Called at the end of the test when it is done
     */
    public static void testDone() {
        TextUi.println("[done ok: " + TestHelper.whoCalledMe() + "]");
    }

    /**
     * 
     */
    public static void testIncomplete() {
        TextUi.println("[done - not self-tested: " + TestHelper.whoCalledMe()
                + "]");
    }

    /**
     * 
     */
    public static void testNotImplemented() {
        TextUi.errorPrint("Not implemented!");
        Assert.assertTrue("Not implemented", false);
    }

    /**
     * Called at the end of the test when it is done
     */
    public static void testStart(String label) {
    	verbose = defaultVerbose;
        TextUi.println("[test: " + TestHelper.whoCalledMe() + "]:\n\t" + label);
    }

    public static String whoCalledMe() {
        Throwable t = new Throwable();
        return t.getStackTrace()[2].toString();
    }
    
    /**
     *   Essentially a mini logging level - only print if set to verbose
     */
    public static void printIfVerbose(String text) {
    	if (verbose) {
    		TextUi.println(text);
    	}
    }
}
