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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;

/**
 * Helper functions which sort list of instances and VIs based on their focus
 * participation (useful for visualization)
 * 
 * @author Ladislau Boloni
 * Created on: Apr 4, 2014
 */
public class FocusSorter {

    /**
     * Sorts a list of instances in the order of decreasing focus salience
     * 
     * @param list
     * @param agent
     */
    public static void sortInstancesDecreasingFocusSalience(
            List<Instance> list, final Agent agent) {
        Comparator<Instance> comp = new Comparator<Instance>() {
            @Override
            public int compare(Instance o1, Instance o2) {
                return Double.compare(
                        agent.getFocus().getSalience(o1, EnergyColors.FOCUS_INSTANCE),
                        agent.getFocus().getSalience(o2, EnergyColors.FOCUS_INSTANCE));
            }
        };
        Collections.sort(list, comp);
        Collections.reverse(list);
    }

    /**
     * Sorts a list of VIs in the order of decreasing focus salience
     * 
     * @param list
     * @param agent
     */
    public static void sortVisDecreasingFocusSalience(List<VerbInstance> list,
            final Agent agent) {
        Comparator<VerbInstance> comp = new Comparator<VerbInstance>() {
            @Override
            public int compare(VerbInstance o1, VerbInstance o2) {
                return Double.compare(
                        agent.getFocus().getSalience(o1, EnergyColors.FOCUS_VI),
                        agent.getFocus().getSalience(o2, EnergyColors.FOCUS_VI));
            }
        };
        Collections.sort(list, comp);
        Collections.reverse(list);
    }

    
    /**
     * Sorts a list of VIs in the order of decreasing focus salience
     * 
     * @param list
     * @param agent
     */
    public static void sortVisIncreasingCreationTime(List<VerbInstance> list,
            final Agent agent) {
        Comparator<VerbInstance> comp = new Comparator<VerbInstance>() {
            @Override
            public int compare(VerbInstance o1, VerbInstance o2) {
                return Double.compare(
                        o1.getCreationTime(),
                        o2.getCreationTime());
            }
        };
        Collections.sort(list, comp);
    }
}
