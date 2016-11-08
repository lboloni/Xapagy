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
package org.xapagy;

import org.junit.Assert;

import org.xapagy.ui.TextUi;

/**
 * Various utility functions for uniformizing the unit tests
 * 
 * Fixme: should have time count as well...
 * 
 * @author Ladislau Boloni
 * Created on: Jun 2, 2011
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
