/*
   This file is part of the Xapagy project
   Created on: May 13, 2014
 
   org.xapagy.set.testInstanceSet
 
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
import org.xapagy.ui.prettyprint.Formatter;

/**
 * A variety of tests for the EnergySet object, and the way the energies are
 * changed through EnergyQuantums
 * 
 * @author Ladislau Boloni
 * 
 */
public class testEnergySet {

    @Test
    public void test() {
        String description = "EnergySet - adding energy";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        // Runner r = ArtificialDomain.createAabConcepts();
        Agent agent = new Agent("Xapagy");
        // create an external PairEnergySet
        EnergySet<Instance> es = new EnergySet<>(agent);
        // use instances as the basis: index 10, 11 will not be filled in
        List<Instance> instances = new ArrayList<>();
        for (int i = 0; i != 12; i++) {
            Instance inst = new Instance("i" + i, null, agent);
            instances.add(inst);
        }

        // add energy
        for (int i = 0; i != 10; i++) {
            double additiveChange = 0.2 * i;
            EnergyQuantum<Instance> eq =
                    EnergyQuantum.createAdd(instances.get(i), additiveChange,
                            EnergyColors.FOCUS_INSTANCE, "x");
            es.applyEnergyQuantum(eq);
        }
        //
        // now see the values
        //
        fmt.add("Initially created values");
        fmt.indent();
        for (int i = 0; i != 12; i++) {
            double value = es.valueEnergy(instances.get(i), EnergyColors.FOCUS_INSTANCE);
            fmt.is("i" + i, value);
            if (i == 0) {
                assertEquals("explicit set zero", 0.0, value, 0.0);
            }
            if (i == 11) {
                assertEquals("not set zero", 0.0, value, 0.0);
            }
            if (i == 6) {
                assertEquals("set to 1.2", 1.2, value, 0.0001);
            }
        }
        fmt.deindent();
        //
        // test for addition (on 6) adding 0.7 to 1.2 should be 1.9
        //
        double additiveChange = 0.7;
        Instance itest = instances.get(6);
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createAdd(itest, additiveChange,
                        EnergyColors.FOCUS_INSTANCE, "x");
        es.applyEnergyQuantum(eq);
        double value = es.valueEnergy(itest, EnergyColors.FOCUS_INSTANCE);
        fmt.is("i6 after adding 0.7", value);
        assertEquals("adding 07", 1.9, value, 0.0001);
        //
        // test for multiplication (on 6) should be 3.8
        //
        double multiplicativeChange = 2.0;
        eq =
                EnergyQuantum.createMult(itest, multiplicativeChange,
                        EnergyColors.FOCUS_INSTANCE, "x");
        es.applyEnergyQuantum(eq);
        value = es.valueEnergy(itest, EnergyColors.FOCUS_INSTANCE);
        fmt.is("i6 after multiplying with 2", value);
        assertEquals("multiply with 2", 3.8, value, 0.0001);
        //
        // test for additive with 10 time slices
        //
        for (int i = 0; i != 10; i++) {
            double timeSlice = 0.1;
            additiveChange = 0.8;
            eq =
                    EnergyQuantum.createAdd(itest, additiveChange, timeSlice,
                            EnergyColors.FOCUS_INSTANCE, "x");
            es.applyEnergyQuantum(eq);
        }
        value = es.valueEnergy(itest, EnergyColors.FOCUS_INSTANCE);
        fmt.is("i6 additive +0.8 10 slices of 0.1", value);
        assertEquals("additive +0.8 10 slices of 0.1", 4.6, value, 0.0001);
        //
        // test for multiplicative with 10 time slices
        //
        for (int i = 0; i != 10; i++) {
            double timeSlice = 0.1;
            multiplicativeChange = 2.0;
            eq =
                    EnergyQuantum.createMult(itest, multiplicativeChange,
                            timeSlice, EnergyColors.FOCUS_INSTANCE, "x");
            es.applyEnergyQuantum(eq);
        }
        value = es.valueEnergy(itest, EnergyColors.FOCUS_INSTANCE);
        fmt.is("i6 multiplicative *2.0 10 slices of 0.1", value);
        assertEquals("i6 multiplicative *2.0 10 slices of 0.1", 9.2, value,
                0.0001);
        //
        // print everything if necessary, and terminate the test
        //
        // TextUi.println(fmt.toString());
        TestHelper.testDone();

    }
}
