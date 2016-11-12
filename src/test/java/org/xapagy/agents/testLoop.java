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

import java.io.IOException;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * Created on: Sep 29, 2011
 */
public class testLoop {

    /**
     * Test for the reading component of the loop
     * 
     * @throws IOException
     */
    @Test
    public void testReading() throws IOException {
        String description = "Test for the reading component in the loop.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Loop l = r.agent.getLoop();
        l.addReading("$CreateScene #Reality CloseOthers With Instances w_c_bai20, w_c_bai21, w_c_bai22");
        l.addReading("The w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        r.tso.setTrace();
        l.proceed();
        TestHelper.testDone();
    }

    /**
     * Test for the schedule component of the loop
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSchedule() throws IOException, InterruptedException {
        String description = "Test for the evolution of the marking.";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Agent agent = r.agent;
        Loop l = r.agent.getLoop();
        l.addReading("$CreateScene #Reality CloseOthers With Instances w_c_bai20, w_c_bai21, w_c_bai22");
        for (int i = 0; i != 20; i++) {
            l.addReading("The w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        }
        l.addScheduled(agent.getTime() + 10.0, "A 'Zeus' / exists.");
        l.addScheduled(agent.getTime() + 14.0,
                "'Zeus' / wa_v_av41 / the w_c_bai22.");
        // l.addReading("A 'Zeus' / exists.");
        // l.addReading("'Zeus' / hits / the 'Hector'.");
        r.tso.setTrace();
        l.proceed();
        r.tso.ppd("ALL_FOCUS_INSTANCES");
        TestHelper.testDone();
    }
}
