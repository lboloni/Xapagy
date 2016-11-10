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
 * Created on: Feb 7, 2012
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
