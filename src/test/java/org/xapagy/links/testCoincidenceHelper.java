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
package org.xapagy.links;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;

/**
 * @author lboloni Created on: Feb 25, 2017
 */
public class testCoincidenceHelper {

	@Test
	public void testGetVis() {
		String description = "Test CoincidenceHelper.getVis()";
		TestHelper.testStart(description);
		Runner r = ArtificialDomain.runnerArtificialAutobiography();
		r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
		VerbInstance vi1 = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
		VerbInstance vi2 = r.exac("'Achilles' / thus wa_v_av40 / 'Hector'.");
		Set<VerbInstance> vis = CoincidenceHelper.getVis(r.agent, vi1);
		assertTrue(vis.contains(vi1));
		assertTrue(vis.contains(vi2));
		TestHelper.testDone();
	}

	
	@Test
	public void testConnectToCoincidenceGroup() {
		String description = "Test CoincidenceHelper.connectToCoincidenceGroup()";
		TestHelper.testStart(description);
		Runner r = ArtificialDomain.runnerArtificialAutobiography();
		r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
		VerbInstance vi1 = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
		VerbInstance vi2 = r.exac("'Achilles' / thus wa_v_av40 / 'Hector'.");
		// add a new one
		VerbInstance vi3 = r.exac("'Ulysses' / wa_v_av40 / 'Hector'.");		
		Set<VerbInstance> vis = CoincidenceHelper.getVis(r.agent, vi1);
		assertTrue(vis.contains(vi1));
		assertTrue(vis.contains(vi2));
		// connect the third
		CoincidenceHelper.connectToCoincidenceGroup(r.agent, vi1, vi3, null);
		// extract from the 2 one 
		vis = CoincidenceHelper.getVis(r.agent, vi2);
		assertTrue(vis.contains(vi1));
		assertTrue(vis.contains(vi2));
		assertTrue(vis.contains(vi3));
		TestHelper.testDone();
	}


	@Test
	public void testGetScenes() {
		String description = "Test CoincidenceHelper.connectToCoincidenceGroup()";
		TestHelper.testStart(description);
		Runner r = ArtificialDomain.runnerArtificialAutobiography();
		r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
		VerbInstance vi1 = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
		VerbInstance vi2 = r.exac("'Achilles' / thus wa_v_av40 / 'Hector'.");
		Instance instHector = r.ref.InstanceByRef("'Hector'");
		Instance instAchilles = r.ref.InstanceByRef("'Achilles'");
		
		Set<Instance> instances = CoincidenceHelper.getInstances(r.agent, vi1, false);
		
		
		r.exec("$CreateScene #HomersEra With Instances 'Homer'");
		// add a new one
		VerbInstance vi3 = r.exac("'Ulysses' / wa_v_av40 / 'Hector'.");		
		Set<VerbInstance> vis = CoincidenceHelper.getVis(r.agent, vi1);
		assertTrue(vis.contains(vi1));
		assertTrue(vis.contains(vi2));
		// connect the third
		CoincidenceHelper.connectToCoincidenceGroup(r.agent, vi1, vi3, null);
		// extract from the 2 one 
		vis = CoincidenceHelper.getVis(r.agent, vi2);
		assertTrue(vis.contains(vi1));
		assertTrue(vis.contains(vi2));
		assertTrue(vis.contains(vi3));
		TestHelper.testDone();
	}

	
}
