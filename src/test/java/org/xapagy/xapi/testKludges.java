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
package org.xapagy.xapi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.parameters.Parameters;

/**
 * @author Ladislau Boloni
 * Created on: Jan 1, 2015
 */
public class testKludges {

    
    @Test
    public void testChangeScene() {
        String description =
                "Test that the CreateScene indeed changes the current scene";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Parameters p = r.agent.getParameters();
        double oldValue = p.get("A_DEBUG","G_GENERAL", "N_RECORD_LINK_QUANTUMS");
        assertEquals(0.0, oldValue, 0.001);
        r.exec("$SetParameter A_DEBUG/G_GENERAL/N_RECORD_LINK_QUANTUMS = 1.0");
        double newValue =  p.get("A_DEBUG","G_GENERAL", "N_RECORD_LINK_QUANTUMS");
        assertEquals(1.0, newValue, 0.1);
        TestHelper.testDone();
    }    

    
}
