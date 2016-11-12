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
package org.xapagy.agents;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;

/**
 * @author Ladislau Boloni
 * Created on: Feb 28, 2016
 */
public class testAgentLabelSpaces {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Tests whether the labels are correctly attached to the instances
     */
    @Test
    public void testFullLabel() {
        TestHelper.testStart("Tests whether LabelSpaces fillLabel does what it is supposed to do");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        AgentLabelSpaces als = r.agent.getLabelSpaces();
        String label = als.fullLabel("#A");
        assertEquals("#XNS_0.A", label);
        label = als.fullLabel("#My.Space.A");
        assertEquals("#My.Space.A", label);
        
    }

    
    
    /**
     * Tests whether the labels are correctly attached to the instances
     */
    @Test
    public void testInstanceAndViLabeling() {
        TestHelper.testStart("Testing the setting of label spaces");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #One CloseOthers With Instances w_c_bai20 #H 'Hector', w_c_bai21 'Achilles'");
        r.exec("'Achilles'/ #Label wa_v_av40 / #H 'Hector'.");
        r.exec("$Namespace Set 'My.Favorite.Space'");
        r.exec("'Achilles' #A / wa_v_av40 #Label / #H 'Hector'.");
        Instance h = r.ref.InstanceByLabel("#XNS_0.H");
        r.printOn = true;
        r.print(h);
    }

}
