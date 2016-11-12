/*
   This file is part of the Xapagy project
   Created on: May 13, 2014
 
   org.xapagy.set.testPairEnergySet
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.set;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;

/**
 * A variety of tests for the PairEnergySet objects.
 * 
 * @author Ladislau Boloni
 * 
 */
public class testPairEnergySet {

    @Test
    public void test() {
        String description = "PairEnergySet";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        // Runner r = ArtificialDomain.createAabConcepts();
        Agent agent = new Agent("Xapagy");
        // create an external PairEnergySet
        PairEnergySet<Instance> es = new PairEnergySet<>(agent);
        // use instances as the basis: index 10, 11 will not be filled in
        List<Instance> instances = new ArrayList<>();
        List<Instance> firstInstances = new ArrayList<>();
        for (int i = 0; i != 12; i++) {
            Instance inst = new Instance("i" + i, null, agent);
            instances.add(inst);
        }
        for (int i = 0; i != 12; i++) {
            Instance inst = new Instance("fi" + i, null, agent);
            firstInstances.add(inst);
        }

        // add energy
        for (int i = 0; i != 10; i++) {
            for (int j = 0; j != 10; j++) {
                double additiveChange = 0.2 * i + 100.0 * j;
                EnergyQuantum<Instance> eq =
                        EnergyQuantum.createAdd(firstInstances.get(i),
                                instances.get(j), additiveChange,
                                EnergyQuantum.TIMESLICE_ONE, EnergyColors.FOCUS_INSTANCE,
                                "x");
                // new EnergyQuantum<Instance>(firstInstances.get(i),
                // instances.get(j), additiveChange,
                // EnergyQuantum.MULTIPLIER_NEUTRAL,
                // EnergyQuantum.TIMESLICE_ONE, EnergyQuantum.STRENGTH_FULL,
                // "x",
                // EnergyColor.FOCUS);
                es.applyEnergyQuantum(eq);
            }
        }

        //
        // now see the values
        //
        fmt.add("Initially created values");
        fmt.indent();
        double value =
                es.valueEnergy(firstInstances.get(5), instances.get(7),
                        EnergyColors.FOCUS_INSTANCE);
        fmt.is("fi5, i7", value);
        assertEquals("explicit set zero", 701.0, value, 0.0);
        value =
                es.valueEnergy(firstInstances.get(7), instances.get(5),
                        EnergyColors.FOCUS_INSTANCE);
        fmt.is("fi7, i5", value);
        assertEquals("explicit set zero", 501.4, value, 0.0);
        fmt.deindent();
        //
        // print everything if necessary, and terminate the test
        //
        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }
}
