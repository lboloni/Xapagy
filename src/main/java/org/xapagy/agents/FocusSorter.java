/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2014
 
   org.xapagy.agents.FocusSorter
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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

}
