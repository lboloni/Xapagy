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
package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.PrintDetail;
import org.xapagy.util.SimpleEntryComparator;

public class qh_ALL_HLSS implements IQueryHandler {

    /**
     * Generating a single page
     * 
     * @param fmt
     * @param agent
     * @param query
     */    
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String redheader = "HLSs (sorted by support - shadow)";
        fmt.addH2(redheader, "class=identifier");
        // add the format here
        qh_ALL_HLSS.pwListHlss(fmt, agent, PrintDetail.DTL_DETAIL, false, query);
    }

    /**
     * Creates a list of Hlss. They will be sorted by (all support types -
     * shadow support)
     * 
     * @param fmt
     * @param agent
     * @param detailLevel
     * @param printAll
     *            - if true prints them all, if false only above the
     *            daHlsSupport_minimumFslStrength
     * @param query
     */
    public static void pwListHlss(PwFormatter fmt, Agent agent,
            PrintDetail detailLevel, boolean printAll, RESTQuery query) {
        HeadlessComponents hlc = agent.getHeadlessComponents();
        int notShown = 0;
        double minimumStrength = 0;
        //
        // creates a sorting order
        //
        List<SimpleEntry<Hls, Double>> hlsList = new ArrayList<>();
        for (Hls hls : hlc.getHlss()) {
            // calculates the total support weight of a HLS
            double value = 0;
            for (FslInterpretation fsli : hls.getSupports()) {
                if (fsli.getFsl().getFslType() != FslType.IN_SHADOW) {
                    value += fsli.getTotalSupport(agent);
                } else {
                    value -= fsli.getTotalSupport(agent);
                }
            }
            if (printAll || value >= minimumStrength) {
                hlsList.add(new SimpleEntry<>(hls, value));
            } else {
                notShown++;
            }
        }
        StringBuffer buf = new StringBuffer();
        buf.append("HLSs sorted by the sum weight of their supports. ");
        buf.append("Showing " + hlsList.size() + " HLSs. ");
        if (notShown > 0) {
            buf.append("Not showing " + notShown + " HLSs weaker than "
                    + Formatter.fmt(minimumStrength));
        }
        fmt.explanatoryNote(buf.toString());
        Collections.sort(hlsList, new SimpleEntryComparator<Hls>());
        Collections.reverse(hlsList);
        if (hlsList.isEmpty()) {
            return;
        }
        double maxValue = hlsList.get(0).getValue();
        if (maxValue < 0) {
            maxValue = 1.0;
        }
        for (SimpleEntry<Hls, Double> entry : hlsList) {
            Hls hls = entry.getKey();
            double value = entry.getValue();
            fmt.openP();
            if (value >= 0.0) {
                fmt.progressBar(value, maxValue);
            } else {
                fmt.progressBar(value, -10.0);
            }
            PwQueryLinks.linkToHls(fmt, agent, query, hls);
            fmt.closeP();
        }
    }
}
