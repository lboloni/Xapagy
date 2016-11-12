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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.activity.DiffusionActivity;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.agents.PerformanceMeter;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.util.SimpleEntryComparator;

/**
 * @author Ladislau Boloni
 * Created on: Jul 14, 2011
 */
public class PpPerformanceMeter {

    public static String ppConcise(PerformanceMeter performanceMeter,
            Agent topLevel) {
        Formatter fmt = new Formatter();
        fmt.add("Performance values: DAs" + performanceMeter.getDas().size()
                + "total time (ms):" + performanceMeter.getDaTotalTime());
        return fmt.toString();
    }

    /**
     * Detailed printing
     * 
     * @param performanceMeter
     * @param agent
     * @return
     */
    public static String ppDetailed(PerformanceMeter performanceMeter,
            Agent agent) {
        Shadows sf = agent.getShadows();
        Focus fc = agent.getFocus();
        Formatter fmt = new Formatter();
        fmt.add("Performance values: ");
        fmt.indent();
        fmt.is("No. of DAs", performanceMeter.getDas().size());
        fmt.is("total time spend in DAs since reset (ms):",
                performanceMeter.getDaTimeSinceReset());
        long totalTime =
                System.currentTimeMillis() - performanceMeter.getTimeAtReset();
        fmt.is("total time since reset (ms):", totalTime);
        fmt.is("time outside DAs",
                totalTime - performanceMeter.getDaTimeSinceReset());
        List<SimpleEntry<DiffusionActivity, Double>> list = new ArrayList<>();
        for (DiffusionActivity da : performanceMeter.getDas()) {
            list.add(new SimpleEntry<>(da, (double) da.getTimeSpentSinceReset()));
        }
        Collections.sort(list, new SimpleEntryComparator<DiffusionActivity>());
        Collections.reverse(list);
        for (SimpleEntry<DiffusionActivity, Double> entry : list) {
            fmt.add(PpPerformanceMeter.ppDiffusionActivity(entry.getKey()));
        }
        //
        // print some agent statistics which gives us an idea of the problems
        //
        int countFocusInstances = fc.getInstanceList(EnergyColors.FOCUS_INSTANCE).size();
        int countFocusVIs = fc.getViList(EnergyColors.FOCUS_VI).size();
        int countShadowInstances = 0;
        int countShadowVIs = 0;
        for (Instance fi : fc.getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
            countShadowInstances += sf.getMembers(fi, EnergyColors.SHI_GENERIC).size();
        }
        for (VerbInstance fvi : fc.getViList(EnergyColors.FOCUS_VI)) {
            countShadowVIs += sf.getMembers(fvi, EnergyColors.SHV_GENERIC).size();
        }
        fmt.add("Agent statistics: ");
        fmt.indent();
        fmt.add("Focus: " + countFocusInstances + " instances, "
                + countFocusVIs + " VIs");
        fmt.add("Shadows: " + countShadowInstances + " instances, "
                + countShadowVIs + " VIs");
        fmt.add("");
        return fmt.toString();
    }

    /**
     * Prints out one single diffusion activity
     * 
     * @param da
     * @return
     */
    public static String ppDiffusionActivity(DiffusionActivity da) {
        Formatter fmt = new Formatter();
        fmt.add("DA: " + da.getClass().getName());
        // fmt.addIndented(da.getTimeSpent() + "ms /" + da.getCallCount()
        // + " calls");
        fmt.addIndented(da.getTimeSpentSinceReset() + "ms /"
                + da.getCallCountSinceReset() + " calls");
        //if (da instanceof DaHlsmSupport) {
        //    DaHlsmSupport dah = (DaHlsmSupport) da;
        //    fmt.indent();
        //}
        return fmt.toString();
    }
}
