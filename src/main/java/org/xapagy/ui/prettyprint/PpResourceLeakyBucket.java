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

import org.xapagy.activity.ResourceLeakyBucket;
import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * Created on: Nov 17, 2011
 */
public class PpResourceLeakyBucket {

    /**
     * Pretty prints the list of scenes in a concrete way
     * 
     * @param scenes
     * @param topLevel
     * @return
     */
    public static String ppConcise(ResourceLeakyBucket rlb, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("ResourceLeakyBucket:" + rlb.getName());
        fmt.is("total quantity", rlb.getParamTotalQuantity());
        fmt.is("max available", rlb.getParamMaxAvailable());
        fmt.is("pipe width", rlb.getParamPipeWidth());
        fmt.is("quantity available", rlb.getQuantityAvailable());
        fmt.is("quantity backup", rlb.getQuantityBackup());
        fmt.is("quantity used", rlb.getQuantityUsed());
        return fmt.toString();
    }

    public static String ppDetailed(ResourceLeakyBucket rlb, Agent agent) {
        return PpResourceLeakyBucket.ppConcise(rlb, agent);
    }
}
