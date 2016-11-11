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
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;

/**
 * @author Ladislau Boloni
 * Created on: Aug 31, 2010
 */
public class PpFocus {

    /**
     * Pretty print in a concise way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppConcise(Focus focus, Agent topLevel) {
        Formatter fmt = new Formatter();
        fmt.add("Focus:");
        fmt.indent();
        fmt.is("Instances:", focus.getInstanceList(EnergyColors.FOCUS_INSTANCE).size());
        // fmt.is("Instances sum:", focus.getInstances().getSum());
        fmt.is("Verb instances:", focus.getViList(EnergyColors.FOCUS_VI).size());
        // fmt.is("Verb instances sum:", focus.getVerbInstances().getSum());
        return fmt.toString();
    }

    /**
     * Pretty print in a detailed way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppDetailed(Focus focus, Agent topLevel) {
        return PpFocus.ppPrint(focus, topLevel, PrintDetail.DTL_DETAIL);
    }

    /**
     * Prints the focus in a pleasing way. Prints the instances in the order of
     * their strength, while the verb-instances in the other way to treat
     * 
     * Currently, it ignores the detail level and prints everything my way
     * 
     * @param focus
     * @param detailLevel
     * @param agent
     * @return
     */
    private static String ppPrint(Focus focus, Agent agent,
            PrintDetail detailLevel) {
        Focus fc = agent.getFocus();
        Formatter fmt = new Formatter();
        fmt.separator("Instances");
        for (Instance fi : fc.getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
            String text = PrettyPrint.pp(fi, agent, detailLevel);
            double value = fc.getSalience(fi, EnergyColors.FOCUS_INSTANCE);
            fmt.addWithMarginNote(Formatter.fmt(value), text);
        }
        fmt.separator("Verb instances");
        for (VerbInstance fvi : fc.getViList(EnergyColors.FOCUS_VI)) {
            String text = PrettyPrint.pp(fvi, agent, detailLevel);
            double value = fc.getSalience(fvi, EnergyColors.FOCUS_VI);
            fmt.addWithMarginNote(Formatter.fmt(value), text);
        }
        return fmt.toString();
    }

}
