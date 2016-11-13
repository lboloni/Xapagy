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
 * Created on: May 13, 2014
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
