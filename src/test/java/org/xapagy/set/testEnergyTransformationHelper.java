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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Nov 5, 2014
 */
public class testEnergyTransformationHelper {
    @Test
    public void test() {
        String description = "EnergyTransformationHelper";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        // Runner r = ArtificialDomain.createAabConcepts();
        Agent agent = new Agent("Xapagy");
        Shadows shadows = agent.getShadows();
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
            for (int j = 0; j != 1; j++) {
                double additiveChange = 0.2 * i + 100.0 * j;
                EnergyQuantum<Instance> eq =
                        EnergyQuantum.createAdd(firstInstances.get(i),
                                instances.get(j), additiveChange,
                                EnergyQuantum.TIMESLICE_ONE,
                                EnergyColors.SHI_RELATION, "x");
                shadows.applyInstanceEnergyQuantum(eq);
            }
        }
        print(fmt, agent, firstInstances, instances);
        //
        // now create the energy transformation quantums
        //
        for (int k = 0; k != 10; k++) {
            fmt.add("after timeslot k=" + k);
            for (int i = 0; i != 10; i++) {
                for (int j = 0; j != 1; j++) {
                    SimpleEntry<EnergyQuantum<Instance>, EnergyQuantum<Instance>> pair =
                            EnergyTransformationHelper.createS2E(agent,
                                    firstInstances.get(i), instances.get(j),
                                    0.9, 1.0, EnergyQuantum.TIMESLICE_ONE / 10.0,
                                    EnergyColors.SHI_RELATION,
                                    EnergyColors.SHI_GENERIC, "x");
                    shadows.applyInstanceEnergyQuantum(pair.getKey());
                    shadows.applyInstanceEnergyQuantum(pair.getValue());
                }
            }
            print(fmt, agent, firstInstances, instances);
        }
        //
        // print everything if necessary, and terminate the test
        //
        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }

    /**
     * Create a table of the relation and generic energies and add them
     * 
     * @param fmt
     * @param es
     * @param firstInstances
     * @param instances
     */
    private static void print(Formatter fmt, Agent agent,
            List<Instance> firstInstances, List<Instance> instances) {
        Shadows shadows = agent.getShadows();
        FormatTable ft = new FormatTable(40, 10, 10, 10, 10);
        ft.header("values", "e-relation", "s-relation", "e-generic",
                "s-generic");
        for (int i = 0; i != 10; i++) {
            for (int j = 0; j != 1; j++) {
                double energyShiRelation =
                        shadows.getEnergy(firstInstances.get(i),
                                instances.get(j), EnergyColors.SHI_RELATION);
                double salienceShiRelation =
                        shadows.getSalience(firstInstances.get(i),
                                instances.get(j), EnergyColors.SHI_RELATION);
                double energyShiGeneric =
                        shadows.getEnergy(firstInstances.get(i),
                                instances.get(j), EnergyColors.SHI_GENERIC);
                double salienceShiGeneric =
                        shadows.getSalience(firstInstances.get(i),
                                instances.get(j), EnergyColors.SHI_GENERIC);
                String label = "i" + i + "->" + "j";
                ft.row(label, energyShiRelation, salienceShiRelation,
                        energyShiGeneric, salienceShiGeneric);
            }
        }
        ft.endTable();
        fmt.add(ft.toString());
    }
}
