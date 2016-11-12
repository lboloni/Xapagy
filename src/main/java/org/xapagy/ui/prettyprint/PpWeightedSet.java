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

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.WeightedSet;
import org.xapagy.ui.formatters.Formatter;

public class PpWeightedSet {

    /**
     * The detailed level of printing: falls back on detailed printing of the
     * keys
     * 
     * @param wset
     * @param agent
     * @return
     */
    public static <T> String pp(WeightedSet<T> wset, Agent agent,
            PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        boolean headerDone = false;
        for (SimpleEntry<T, Double> entry : wset.getDecreasingStrengthList()) {
            T key = entry.getKey();
            if (!headerDone) {
                headerDone = true;
                String typeString = "Weighted set";
                if (key instanceof VerbInstance) {
                    typeString = "VI set";
                }
                if (key instanceof Instance) {
                    typeString = "Instance set";
                }
                fmt.add(typeString + " (" + wset.getParticipants().size()
                        + " items " + Formatter.fmt(wset.getSum()) + " sum)\n");
                fmt.indent();

            }
            double value = wset.value(key);
            fmt.addWithMarginNote(Formatter.fmt(value) + "  ",
                    PrettyPrint.pp(key, agent, detailLevel));
        }
        fmt.indent();
        return fmt.toString();
    }

    /**
     * Concise printing
     * 
     * @param wset
     * @param agent
     * @return
     */
    public static <T> String ppConcise(WeightedSet<T> wset, Agent agent) {
        return PpWeightedSet.pp(wset, agent, PrintDetail.DTL_CONCISE);
    }

    /**
     * Concise printing
     * 
     * @param wset
     * @param agent
     * @return
     */
    public static <T> String ppDetailed(WeightedSet<T> wset, Agent agent) {
        return PpWeightedSet.pp(wset, agent, PrintDetail.DTL_DETAIL);
    }

}
