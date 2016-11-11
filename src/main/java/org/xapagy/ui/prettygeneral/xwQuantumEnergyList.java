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
package org.xapagy.ui.prettygeneral;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.agents.Focus;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.HtmlFormatter;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.util.SimpleEntryComparator;

/**
 * Functions for pretty-printing a quan
 * 
 * @author Ladislau Boloni
 * Created on: Nov 16, 2012
 */
public class xwQuantumEnergyList {

    /**
     * The goals of this is to ensure that every time we add this, it will
     * generate a new extensible H2
     */
    private static int staticCounter = 0;

    /**
     * Adds to the webpage an explanatory section about the Energy quantums
     * which had impacted a certain energy value. Should work for focus, memory
     * and shadow.
     * 
     * Normally, there is a compressed listing where the positive and negative
     * sources are listed.
     * 
     * All the quantums are listed in a collapsible section.
     * 
     * @param fmt
     * @param list
     */
    public static void xwEnergyQuantumList(IXwFormatter fmt, List<EnergyQuantum<?>> list) {
        // sort the energy quantums in the order of their application
        Collections.sort(list, new Comparator<EnergyQuantum<?>>() {

            @Override
            public int compare(EnergyQuantum<?> o1, EnergyQuantum<?> o2) {
                return Long.compare(o1.getApplicationOrder(),
                        o2.getApplicationOrder());
            }
        });
        // Collections.reverse(list);
        //
        // extract the values on a per source basis
        //
        Map<String, Double> values = new HashMap<>();
        for (EnergyQuantum<?> sq : list) {
            String label = sq.getSourceOriginator();
            Double d = values.get(label);
            if (d == null) {
                d = new Double(0);
                values.put(label, d);
            }
            double value =
                    d + sq.getEnergyAfterQuantum()
                            - sq.getEnergyBeforeQuantum();
            values.put(label, value);
        }
        List<SimpleEntry<String, Double>> contributions = new ArrayList<>();

        for (String label : values.keySet()) {
            contributions.add(new SimpleEntry<>(label, values.get(label)));
        }
        Collections.sort(contributions,
                new SimpleEntryComparator<String>());
        Collections.reverse(contributions);
        fmt.addLabelParagraph("Contributors:");
        for (SimpleEntry<String, Double> entry : contributions) {
            fmt.openP();
            double value = entry.getValue();
            fmt.add(Formatter.fmt(value) + " : " + entry.getKey());
            fmt.closeP();
        }
        //
        // list all the quantum objects sorted in time (collapsible)
        //
        HtmlFormatter fmt2 = new HtmlFormatter();
        fmt2.openTable("class=energyquantum");
        //
        // create the header
        //
        fmt2.openTR("style=background-color:white");
        // the time and identifier
        fmt2.openTD();
        fmt2.add("Time:");
        fmt2.closeTD();
        // the application order
        fmt2.openTD();
        fmt2.add("AppOrder:");
        fmt2.closeTD();
        // the change type and parameter
        fmt2.openTD();
        fmt2.add("Change:");
        fmt2.closeTD();
        // the timeSlice
        fmt2.openTD();
        fmt2.add("Timeslice:");
        fmt2.closeTD();
        // the source
        fmt2.openTD();
        fmt2.add("Source:");
        fmt2.closeTD();
        fmt2.openTD();
        fmt2.add("Energy after:");
        fmt2.closeTD();
        // end of columns
        fmt2.closeTR();

        for (EnergyQuantum<?> eq : list) {
            // determine the change and an appropriate color: blue for addition,
            // green for mult increase pink for mult decrease
            String change = "";
            String color = "";
            if (eq.getAdditiveChange() != EnergyQuantum.ADDITION_NEUTRAL) {
                change = "+" + Formatter.fmt(eq.getAdditiveChange());
                color = "LightBlue";
            } else {
                change = "*" + Formatter.fmt(eq.getMultiplicativeChange());
                if (eq.getMultiplicativeChange() > 1.0) {
                    color = "LightGreen";
                } else {
                    color = "LightPink";
                }
            }
            fmt2.openTR("style=background-color:" + color);
            // the time
            fmt2.openTD();
            String agentTime = Formatter.fmt(eq.getAgentTimeWhenApplied());
            fmt2.add(agentTime);
            fmt2.closeTD();
            // the application order
            fmt2.openTD();
            fmt2.add(eq.getApplicationOrder());
            fmt2.closeTD();
            // the change type and parameter
            fmt2.openTD();
            fmt2.add(change);
            fmt2.closeTD();
            // the time slice
            fmt2.openTD();
            fmt2.add(eq.getTimeSlice());
            fmt2.closeTD();
           // the source
            fmt2.openTD();
            fmt2.add(eq.getSource());
            fmt2.closeTD();
            // the colors
            // fmt2.openTD();
            // fmt2.add(sq.getEnergyColor().toString());
            // fmt2.closeTD();
            // before / after energy
            fmt2.openTD();
            fmt2.add(Formatter.fmt(eq.getEnergyAfterQuantum()));
            fmt2.closeTD();
            // end of columns
            fmt2.closeTR();
        }
        fmt2.closeTable();
        fmt.addExtensibleH2("idEnergyQuantumList" + staticCounter++,
                "Show all " + list.size() + " energy quantums",
                fmt2.toString(), false);
    }

    /**
     * Visualizes all the energy quantums of an instance or VI.
     * 
     * @param fmt
     * @param t
     * @param agent
     * @param query
     */
    public static <T extends XapagyComponent> void pwAllEnergyQuantums(
            IXwFormatter fmt, T t, Agent agent, RESTQuery query) {
        fmt.startEmbedX("Quantums");
        // Focus energies
        Focus fc = agent.getFocus();
        List<String> focusEnergies = null;
        List<String> memoryEnergies = null;
        if (t instanceof Instance) {
            focusEnergies = agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE);
            memoryEnergies = agent.getEnergyColors().getEnergies(EnergyColorType.AM_INSTANCE);
        } 
        if (t instanceof VerbInstance) {
            focusEnergies = agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI);
            memoryEnergies = agent.getEnergyColors().getEnergies(EnergyColorType.AM_VI);
        }
        for (String ec : focusEnergies) {
            fmt.addH3("Focus EnergyColor: " + ec);
            fmt.startEmbed();
            List<EnergyQuantum<?>> list = new ArrayList<>();
            if (t instanceof Instance) {
                list.addAll(fc.getEnergyQuantums((Instance) t, ec));
            }
            if (t instanceof VerbInstance) {
                list.addAll(fc.getEnergyQuantums((VerbInstance) t, ec));
            }
            xwEnergyQuantumList(fmt, list);
            fmt.endEmbed();
        }
        // Memory energies
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        for (String ec : memoryEnergies) {
            fmt.addH3("Memory EnergyColor: " + ec);
            fmt.indent();
            List<EnergyQuantum<?>> list = new ArrayList<>();
            if (t instanceof Instance) {
                list.addAll(am.getEnergyQuantums((Instance) t, ec));
            }
            if (t instanceof VerbInstance) {
                list.addAll(am.getEnergyQuantums((VerbInstance) t, ec));
            }
            xwEnergyQuantumList(fmt, list);
            fmt.deindent();
        }
        fmt.endEmbedX();
    }
}
