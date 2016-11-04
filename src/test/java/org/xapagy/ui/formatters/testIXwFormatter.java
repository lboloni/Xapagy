package org.xapagy.ui.formatters;

import org.junit.Test;
import org.xapagy.TestHelper;

/**
 * This class contains test for the iXwFormatter in its two incarnations, HTML and text
 * @author lboloni
 *
 */
public class testIXwFormatter {

	@Test
	public void testTwoFormatters() {
		TestHelper.testStart("Testing PwFormatter and TwFormatter compatibility");
		TestHelper.verbose = true;
		PwFormatter pw = new PwFormatter();
		TwFormatter tw = new TwFormatter();
		printStuff(pw);
		TestHelper.printIfVerbose("PwFormatter:\n" + pw.toString());
		printStuff(tw);
		TestHelper.printIfVerbose("TwFormatter:\n" + tw.toString());
		TestHelper.testDone();
	}
	
	private void printStuff(IXwFormatter xw) {
		printAdd(xw);
	}
	
	private void printAdd(IXwFormatter xw) {
		xw.add("Adding some stuff here");
		xw.add("And some more stuff here");
	}
}
