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

import java.util.HashMap;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;

/**
 * 
 * Text-mode visualization for the pre-succ relationships between a set of verbs
 * with the same prefix
 * 
 * @author Ladislau Boloni
 * Created on: Jan 2, 2011
 */
public class PpSuccession {
    /**
     * Visualize the focus strenght, the prec and succ values for a set of verbs
     * starting with the same prefix
     */
    public static String ppSuccessionOfVerbsOfIndex(Agent agent, String prefix) {
        Focus fc = agent.getFocus();
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        Formatter fmt = new Formatter();
        StringBuffer lineid = new StringBuffer();
        StringBuffer linefocus = new StringBuffer();
        Map<String, String> preMap = new HashMap<>();
        Map<String, String> sucMap = new HashMap<>();
        int count = 1;
        while (true) {
            String verbId = prefix + String.format("%03d", count);
            VerbInstance vi = am.getVerbInstance(verbId);
            if (vi == null) {
                break;
            }
            count++;
        }
        for (int i = 1; i != count; i++) {
            String verbId = prefix + String.format("%03d", i);
            VerbInstance vi = am.getVerbInstance(verbId);
            if (vi != null) {
                double val = fc.getSalience(vi, EnergyColors.FOCUS_VI);
                linefocus.append(Formatter.fmt(val) + " ");
                String suc =
                        PpSuccession.preOrSuccLine(agent, count, vi,
                                agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_PREDECESSOR));
                sucMap.put(verbId, suc);
                String pre =
                        PpSuccession.preOrSuccLine(agent, count, vi,
                                agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_SUCCESSOR));
                preMap.put(verbId, pre);
            } else {
                linefocus.append(" -    ");
            }
            lineid.append(verbId + "  ");
        }
        fmt.add(lineid);
        fmt.add(linefocus);
        for (int i = 1; i != count; i++) {
            String verbId = "v" + String.format("%03d", i);
            VerbInstance vi = am.getVerbInstance(verbId);
            if (vi != null) {
                fmt.add("Verb " + verbId);
                fmt.indent();
                fmt.add("    " + lineid);
                fmt.add("Pre:" + preMap.get(verbId));
                fmt.add("Suc:" + sucMap.get(verbId));
                fmt.deindent();
            }
        }
        return fmt.toString();
    }

    /**
     * Returns a line for the precedents or successors for the current VI out of
     * all the considered verb instances
     * 
     * @param agent
     * @param current
     * @param set
     * @return
     */
    private static String preOrSuccLine(Agent agent, int count,
            VerbInstance current, ViSet set) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        StringBuffer line = new StringBuffer();
        for (int i = 1; i != count; i++) {
            String verbId = "v" + String.format("%03d", i);
            VerbInstance vi = am.getVerbInstance(verbId);
            if (vi != null) {
                double val = set.value(vi);
                if (vi.equals(current)) {
                    line.append(" x    ");
                    continue;
                }
                if (val == 0.0) {
                    line.append(" -    ");
                    continue;
                }
                line.append(Formatter.fmt(val) + " ");
            } else {
                line.append(" -    ");
            }
        }
        return line.toString();
    }

}
