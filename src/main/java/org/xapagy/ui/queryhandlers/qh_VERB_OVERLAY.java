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
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Overlay;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_VERB_OVERLAY {
    /**
     * Adds a paragraph about the labels (vo or co)
     * 
     * @param fmt
     * @param agent
     * @param vo
     * @param query
     */
    public static String addOverlayLabels(IXwFormatter fmt, Agent agent,
            Overlay<?> vo, RESTQuery query) {
        List<String> labels = vo.getLabels();
        if (labels == null) {
            fmt.is("OverlayLabels", "<< none >>");
        } else {
            String tmp = "";
            for (String label : labels) {
                tmp = tmp + label + " ";
            }
            fmt.is("OverlayLabels", tmp);
        }
        return fmt.toString();
    }

    /**
     * Although there is no specific query for a verb overlay, there is a need
     * to pretty print it (for instance when inspecting a VI)
     * 
     * @param fmt
     * @param agent
     * @param concept
     * @param query
     */
    public static void generate(PwFormatter fmt, Agent agent, VerbOverlay vo,
            RESTQuery query, boolean showLabels) {
        for (SimpleEntry<Verb, Double> entry : vo.getSortedByExplicitEnergy()) {
            Verb verb = entry.getKey();
            fmt.openP();
            double value = entry.getValue();
            fmt.progressBar(value, 1.0);
            PwQueryLinks.linkToVerb(fmt, agent, query, verb);
            fmt.closeP();
        }
        if (showLabels) {
            qh_VERB_OVERLAY.addOverlayLabels(fmt, agent, vo, query);
        }
    }

    /**
     * Compact printing of the verb overlay, still with links to the verbs
     * 
     * @param fmt
     * @param agent
     * @param co
     * @param query
     */
    public static void xwCompact(IXwFormatter fmt, Agent agent,
            VerbOverlay vo, RESTQuery query) {
        fmt.add("[ ");
        for (SimpleEntry<Verb, Double> entry : vo.getSortedByExplicitEnergy()) {
            Verb verb = entry.getKey();
            PwQueryLinks.linkToVerb(fmt, agent, query, verb);
        }
        fmt.add(" ]");
    }

}
