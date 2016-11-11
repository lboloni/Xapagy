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
package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.set.InstanceSet;

/**
 * @author Ladislau Boloni
 * Created on: Dec 26, 2011
 */
public class PpInstanceSet {

    /**
     * Fallback on the generic WeightedSet printing
     * 
     * @param is
     * @param agent
     * @return
     */
    public static String ppConcise(InstanceSet is, Agent agent) {
        return PpWeightedSet.ppConcise(is, agent);
    }

    /**
     * Fallback on the generic WeightedSet printing
     * 
     * @param is
     * @param agent
     * @return
     */
    public static String ppDetailed(InstanceSet is, Agent agent) {
        return PpWeightedSet.ppDetailed(is, agent);
    }

}
