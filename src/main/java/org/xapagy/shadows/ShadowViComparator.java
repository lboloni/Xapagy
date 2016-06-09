/*
   This file is part of the Xapagy project
   Created on: Feb 7, 2012
 
   org.xapagy.shadows.ShadowViComparator
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.shadows;

import java.util.Comparator;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.shadows.ShadowInstanceComparator.SortedBy;

/**
 * Comparator which sorts shadows of VIs by ENERGY or SALIENCE of a specific
 * shadow color
 * 
 * @author Ladislau Boloni
 * 
 */
public class ShadowViComparator implements Comparator<VerbInstance> {
    private Agent agent;
    private String color;
    private VerbInstance fvi;
    private ShadowInstanceComparator.SortedBy sortedBy;

    /**
     * @param sortedBy
     * @param agent
     * @param fvi
     */
    public ShadowViComparator(SortedBy sortedBy, Agent agent, VerbInstance fvi,
            String color) {
        super();
        this.sortedBy = sortedBy;
        this.agent = agent;
        this.fvi = fvi;
        this.color = color;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(VerbInstance o1, VerbInstance o2) {
        Shadows sf = agent.getShadows();
        switch (sortedBy) {
        case ENERGY:
            return Double.compare(sf.getEnergy(fvi, o1, color),
                    sf.getEnergy(fvi, o2, color));
        case SALIENCE:
            return Double.compare(sf.getSalience(fvi, o1, color),
                    sf.getSalience(fvi, o2, color));
        }
        // should not happen
        throw new Error("Should not happen");
    }

}
