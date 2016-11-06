/*
   This file is part of the Xapagy project
   Created on: May 13, 2014
 
   org.xapagy.set.testSharpening
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.set;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class testSharpening {

    /**
     */
    @Test
    public void test() {
        String description = "The sharpening algorithm";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        // Runner r = ArtificialDomain.createAabConcepts();
        Agent agent = new Agent("Xapagy");
        // create an external PairEnergySet
        PairEnergySet<Instance> pes = new PairEnergySet<>(agent);
        // use instances as the basis
        List<Instance> instances = new ArrayList<>();
        for (int i = 0; i != 10; i++) {
            Instance inst = new Instance("i" + i, null, agent);
            instances.add(inst);
        }
        Instance instF = new Instance("if", null, agent);
        // now add energy to those instances
        // we are using EnergyColor.FOCUS, just to make sure we are not
        // dependent on shadows
        for (int i = 0; i != 10; i++) {
            double additiveChange = i * 0.2;
            EnergyQuantum<Instance> eq =
                    EnergyQuantum.createAdd(instF, instances.get(i),
                            additiveChange, EnergyQuantum.TIMESLICE_ONE,
                            EnergyColors.FOCUS_INSTANCE, "x");
             pes.applyEnergyQuantum(eq);
        }
        // print the values
        fmt.add("Initial energy values:");
        for (int i = 0; i != 10; i++) {
            double energy =
                    pes.valueEnergy(instF, instances.get(i), EnergyColors.FOCUS_INSTANCE);
            fmt.is("i" + i, energy);
        }
        // now apply the sharpener
        List<SimpleEntry<Instance, Instance>> list = new ArrayList<>();
        for (int i = 0; i != 7; i++) {
            SimpleEntry<Instance, Instance> entry =
                    new SimpleEntry<>(instF, instances.get(i));
            list.add(entry);
        }
        double sigma = 2.0;
        List<EnergyQuantum<Instance>> eqs =
                Sharpening.sharpen(pes, list, EnergyColors.FOCUS_INSTANCE, sigma, 1.0);
        for (EnergyQuantum<Instance> eq : eqs) {
            pes.applyEnergyQuantum(eq);
        }
        // now print again
        fmt.add("Sharpened energy values for 0-7 for value " + sigma);
        for (int i = 0; i != 10; i++) {
            double energy =
                    pes.valueEnergy(instF, instances.get(i), EnergyColors.FOCUS_INSTANCE);
            fmt.is("i" + i, energy);
        }
        TestHelper.printIfVerbose(fmt.toString());
        TestHelper.testDone();

    }
}
