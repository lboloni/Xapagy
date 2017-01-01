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
package org.xapagy.ui.prettygeneral;

import org.junit.Test;
import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.formatters.TwFormatter;

public class testXwInstance {

	protected PwFormatter pw;
	protected TwFormatter tw;
	protected StringBuffer tos;
	
	/**
	 * Testing if the different formatters create acceptable results. Trying it out on 
	 * PwFormatter for html, TwFormatter for text, and simple toString  
	 */
	@Test
	public void testFormatters() {
		TestHelper.testStart("Testing the formats created XwInstance");
		// create the formatters
		pw = new PwFormatter();
		tw = new TwFormatter();
		tos = new StringBuffer();
		// let us get some instances to print
		Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Reality CloseOthers With Instances 'Hector' #Hector, w_c_bai20 #referred, w_c_bai21");
        Instance instanceHector =
                r.ref.InstanceByLabel("#Hector");
        Instance scene =
                r.ref.SceneByLabel("#Reality");
        Instance instanceReferred =
                r.ref.InstanceByLabel("#referred");
        printSuperConcise(instanceHector, r);
        printConcise(instanceHector, r);
        printDetailed(instanceHector, r);
        
        //r.exec("The  w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        //r.exec("'Hector' / wa_v_av41 / the w_c_bai21.");
        TestHelper.verbose = true;
		TestHelper.printIfVerbose("PwFormatter:\n" + pw.toString());
		TestHelper.printIfVerbose("TwFormatter:\n" + tw.toString());
		TestHelper.printIfVerbose("toString output:\n" + tos.toString());		
		TestHelper.testDone();
	}
	
	/**
	 * Prints using superconcide on all three formatters
	 * @param instance
	 */
	private void printSuperConcise(Instance instance, Runner r) {
        xwInstance.xwSuperConcise(pw, instance, r.agent);
        xwInstance.xwSuperConcise(tw, instance, r.agent);
		tos.append(instance);
	}

	/**
	 * Prints using superconcide on all three formatters
	 * @param instance
	 */
	private void printConcise(Instance instance, Runner r) {
        xwInstance.xwConcise(pw, instance, r.agent);
        xwInstance.xwConcise(tw, instance, r.agent);
	}

	/**
	 * Prints using superconcide on all three formatters
	 * @param instance
	 */
	private void printDetailed(Instance instance, Runner r) {
        xwInstance.xwDetailed(pw, instance, r.agent);
        xwInstance.xwDetailed(tw, instance, r.agent);
	}

	
	
	private void printAdd(IXwFormatter xw) {
		xw.add("Adding some stuff here");
		xw.add("And some more stuff here");
	}
	
}
