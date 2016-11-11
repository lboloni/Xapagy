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
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.smartprint.SpInstance;

/**
 * @author Ladislau Boloni
 * Created on: Sep 16, 2010
 */
public class PpShadows {

    public static int maxPrint = 50;

    /**
     * Fall back on detailed
     * 
     * @param sf
     * @param agent
     * @return
     */
    public static String ppConcise(Shadows sf, Agent agent) {
        return PpShadows.ppDetailed(sf, agent);
    }

    /**
     * Prints both the instance shadows and the other ones
     * 
     * @param sf
     * @param agent
     * @return
     */
    public static String ppDetailed(Shadows sf, Agent agent) {
        return PpShadows.ppPrint(sf, agent, true, true);
    }

    /**
     * Prints the shadow of an instance
     * 
     * @param fi
     * @param sf
     * @param agent
     */
    public static String ppInstanceShadow(Instance fi, Shadows sf, Agent agent, String ec) {
        Formatter fmt = new Formatter();
        Focus fc = agent.getFocus();
        double focusValue = fc.getSalience(fi, EnergyColors.FOCUS_INSTANCE);
        fmt.addWithMarginNote(Formatter.fmt(focusValue),
                SpInstance.spc(fi, agent));
        fmt.indent();
        int count = 0;
        for (Instance si : sf.getMembers(fi, ec)) {
            count++;
            double valueRelative = sf.getSalience(fi, si, ec);
            double valueEnergy = sf.getSalience(fi, si, ec);
            String tmp = "";
            String spc = SpInstance.spc(si, agent);
            if (spc.length() < 40) {
                tmp += Formatter.padTo(spc, 40);
            } else {
                tmp += Formatter.padTo(spc, 60);
            }
            tmp += "  ";
            tmp += Formatter.padTo("R=" + Formatter.fmt(valueRelative), 10);
            tmp += Formatter.padTo("E=" + Formatter.fmt(valueEnergy), 10);
            fmt.add(tmp);
            if (count > PpShadows.maxPrint) {
                fmt.add("And "
                        + (sf.getMembers(fi, ec).size() - PpShadows.maxPrint)
                        + " more...");
                break;
            }
        }
        return fmt.toString();
    }

    /**
     * Iterating and then calling PpInstanceShadow and Pp
     * 
     * @param sf
     * @param detailLevel
     * @param agent
     * @return
     */
    public static String ppPrint(Shadows sf, Agent agent,
            boolean printInstances, boolean printVis) {
        Formatter fmt = new Formatter();
        Focus fc = agent.getFocus();
        //
        // instance shadows
        //
        if (printInstances) {
            fmt.separator("Instance shadows - EnergyColor.SHI_GENERIC");
            fmt.indent();
            for (Instance fi : fc.getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
                fmt.add(PpShadows.ppInstanceShadow(fi, sf, agent, EnergyColors.SHI_GENERIC));
            }
            fmt.deindent();
        }
        //
        // verb instance shadows
        //
        if (printVis) {
            fmt.separator("VI shadows - EnergyColor.SHV_GENERIC");
            fmt.indent();
            for (VerbInstance fvi : fc.getViList(EnergyColors.FOCUS_VI)) {
                fmt.add(PpShadows.ppViShadow(fvi, sf, agent, EnergyColors.SHV_GENERIC));
            }
            fmt.deindent();
        }
        return fmt.toString();
    }

    /**
     * @param fvi
     * @param sf
     * @param agent
     * @return
     */
    public static String ppViShadow(VerbInstance fvi, Shadows sf, Agent agent, String ec) {
        Formatter fmt = new Formatter();
        Focus fc = agent.getFocus();
        double focusValue = fc.getSalience(fvi, EnergyColors.FOCUS_VI);
        // how to do this??? verbalize
        fmt.addWithMarginNote(Formatter.fmt(focusValue), agent.getVerbalize()
                .verbalize(fvi) + " " + fvi.getIdentifier());
        fmt.indent();
        int count = 0;
        for (VerbInstance svi : sf.getMembers(fvi,ec)) {
            count++;
            double valueSalience = sf.getSalience(fvi, svi, ec);
            double valueEnergy = sf.getEnergy(fvi, svi, ec);
            String tmp = "";
            tmp +=
                    Formatter.padTo(agent.getVerbalize().verbalize(svi) + " "
                            + svi.getIdentifier(), 40);
            tmp += "  ";
            tmp += Formatter.padTo("S=" + Formatter.fmt(valueSalience), 10);
            tmp += Formatter.padTo("E=" + Formatter.fmt(valueEnergy), 10);
            fmt.add(tmp);
            if (count > PpShadows.maxPrint) {
                fmt.add("And "
                        + (sf.getMembers(fvi, ec).size() - PpShadows.maxPrint)
                        + " more...");
                break;
            }
        }
        return fmt.toString();
    }

}
