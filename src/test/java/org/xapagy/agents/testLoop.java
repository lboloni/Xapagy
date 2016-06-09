/*
   This file is part of the Xapagy project
   Created on: Sep 29, 2011
 
   org.xapagy.agents.testLoop
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.io.IOException;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * 
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
        Runner r = ArtificialDomain.createAabConcepts();
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
        Runner r = ArtificialDomain.createAabConcepts();
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
