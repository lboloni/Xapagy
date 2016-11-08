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
import org.xapagy.agents.Agent;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.formatters.TwFormatter;

public class testXwInstance {

	@Test
	public void testTwoFormatters() {
		TestHelper.testStart("Testing PwFormatter and TwFormatter compatibility");
		PwFormatter pw = new PwFormatter();
		TwFormatter tw = new TwFormatter();
		// let us get some instances to print
		Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Reality CloseOthers With Instances 'Hector' #Hector, w_c_bai20 #referred, w_c_bai21");
        Instance instanceHector =
                r.ref.InstanceByLabel("#Hector");
        Instance scene =
                r.ref.SceneByLabel("#Reality");
        Instance instanceReferred =
                r.ref.InstanceByLabel("#referred");
        r.exec("The  w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        r.exec("'Hector' / wa_v_av41 / the w_c_bai21.");
        
		printSuperConcise(tw, instanceHector, r.agent);

        
		TestHelper.printIfVerbose("PwFormatter:\n" + pw.toString());
		TestHelper.printIfVerbose("TwFormatter:\n" + tw.toString());
		TestHelper.testDone();
	}
	
	/**
	 * Tests the printing of the superconcise printing
	 * @param xw
	 * @param instance
	 */
	private void printSuperConcise(IXwFormatter xw, Instance instance, Agent agent) {
		xwInstance.xwConcise(xw, instance, agent);
	}
	
	private void printAdd(IXwFormatter xw) {
		xw.add("Adding some stuff here");
		xw.add("And some more stuff here");
	}
	
}
