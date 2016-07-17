/*
   This file is part of the Xapagy project
   Created on: Feb 28, 2016
 
   org.xapagy.agents.testAgentLabelSpaces
 
   Copyright (c) 2008-2013 Ladislau Boloni
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
 *
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
