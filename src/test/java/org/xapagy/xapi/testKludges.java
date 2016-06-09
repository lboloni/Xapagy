/*
   This file is part of the Xapagy project
   Created on: Jan 1, 2015
 
   org.xapagy.xapi.testKludges
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.xapi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.parameters.Parameters;

/**
 * @author Ladislau Boloni
 *
 */
public class testKludges {

    
    @Test
    public void testChangeScene() {
        String description =
                "Test that the CreateScene indeed changes the current scene";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
        r.exec("$CreateScene #Current Current With Instances w_c_bai20 'Bill', w_c_bai21 'Jill'");
        /*VerbInstance vi1 = */ r.exac("'Bill' / wa_v_av1 / 'Jill'.");
        double time = r.agent.getTime();
        r.exec("$ChangeScene #Troy");
        assertEquals(time, r.agent.getTime(), 0.001);
        /* VerbInstance vi2 = */ r.exac("'Achilles' / wa_v_av2 / 'Hector'.");
        TestHelper.testDone();
    }    

    
    @Test
    public void testSetParameter() {
        String description =
                "Test that the SetParameter indeed sets the parameter";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        Parameters p = r.agent.getParameters();
        double oldValue = p.get("A_DEBUG","G_GENERAL", "N_RECORD_LINK_QUANTUMS");
        assertEquals(0.0, oldValue, 0.001);
        r.exec("$SetParameter A_DEBUG/G_GENERAL/N_RECORD_LINK_QUANTUMS = 1.0");
        double newValue =  p.get("A_DEBUG","G_GENERAL", "N_RECORD_LINK_QUANTUMS");
        assertEquals(1.0, newValue, 0.1);
        TestHelper.testDone();
    }    

    
}
