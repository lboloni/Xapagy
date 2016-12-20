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
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.formatters.TwFormatter;

/**
 * @author lboloni
 * Created on: Dec 20, 2016
 */
public class testXwVi {

	
	@Test
	public void testTwoFormatters() {
		TestHelper.testStart("Testing the formatting of the Vi");
		PwFormatter pw = new PwFormatter();
		TwFormatter tw = new TwFormatter();
		StringBuffer tos = new StringBuffer();
		// let us get some instances to print
		Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Reality CloseOthers With Instances 'Hector' #Hector, w_c_bai20 #referred, w_c_bai21");
        r.exec("The  w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        VerbInstance vi = r.ref.ViByLastCreated();
        printConcise(pw, vi, r.agent);
        printConcise(tw, vi, r.agent);
        tos.append(vi.toString());
        
        //r.exec("'Hector' / wa_v_av41 / the w_c_bai21.");
        TestHelper.verbose = true;
		TestHelper.printIfVerbose("PwFormatter:\n" + pw.toString());
		TestHelper.printIfVerbose("TwFormatter:\n" + tw.toString());
		TestHelper.printIfVerbose("ToString:\n" + tos.toString());
		TestHelper.testDone();
	}
	
	/**
	 * Tests the printing of the
	 * @param xw
	 * @param instance
	 */
	private void printConcise(IXwFormatter xw, VerbInstance vi, Agent agent) {
		xwVerbInstance.xwConcise(xw, vi, agent);
	}
	
}
