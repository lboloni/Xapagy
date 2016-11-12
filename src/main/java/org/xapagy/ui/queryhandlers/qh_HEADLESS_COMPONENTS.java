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

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.QueryHelper;

public class qh_HEADLESS_COMPONENTS implements IQueryHandler, IQueryAttributes {

    
    /**
     * Generate the page for headless components (just a set of links)
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery gq, Session session) {
        HeadlessComponents hlc = agent.getHeadlessComponents();
        //
        // the red header
        //
        StringBuffer buffer = new StringBuffer();
        buffer.append("Headless components");
        fmt.addH2(buffer.toString(), "class=identifier");
        //
        // Now some links to the general purpose ones
        //
        RESTQuery newq = null;
        fmt.openUL();
        //
        // choice, sorted by strength
        //
        int countChoices = hlc.getChoices().keySet().size();
        fmt.openLI();
        newq = QueryHelper.copyWithEmptyCommand(gq);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_CHOICES");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_DEPENDENT_SCORE");
        PwQueryLinks.addLinkToQuery(fmt, newq,
                "Choices, sorted by native strength (" + countChoices + ")",
                PwFormatter.CLASS_BODYLINK);
        fmt.closeLI();
        //
        // choice, sorted by dynamic score
        //
        fmt.openLI();
        newq = QueryHelper.copyWithEmptyCommand(gq);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_CHOICES");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_MOOD_SCORE");
        PwQueryLinks.addLinkToQuery(fmt, newq, "Choices, sorted by dynamic score ("
                + countChoices + ")", PwFormatter.CLASS_BODYLINK);
        fmt.closeLI();
        //
        // all HlsSupported's
        //
        int countHlsSupporteds = hlc.getHlss().size();
        fmt.openLI();
        newq = QueryHelper.copyWithEmptyCommand(gq);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_HLSS");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_CONTINUATION");
        PwQueryLinks.addLinkToQuery(fmt, newq, "HlsSupported components ("
                + countHlsSupporteds + ")", PwFormatter.CLASS_BODYLINK);
        fmt.closeLI();
        //
        // all HlsNewInstance's
        //
        int countHlsNewInstances = hlc.getHlsNewInstances().size();
        fmt.openLI();
        newq = QueryHelper.copyWithEmptyCommand(gq);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_HLS_NEW_INSTANCES");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_STRENGTH");
        PwQueryLinks.addLinkToQuery(fmt, newq, "HlsNewInstance components ("
                + countHlsNewInstances + ")", PwFormatter.CLASS_BODYLINK);
        fmt.closeLI();
        //
        // all FSLs
        //
        int countFSLs = hlc.getFsls().size();
        fmt.openLI();
        newq = QueryHelper.copyWithEmptyCommand(gq);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_FSLS");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_STRENGTH");
        PwQueryLinks.addLinkToQuery(fmt, newq, "FocusShadowLinkedVis components ("
                + countFSLs + ")", PwFormatter.CLASS_BODYLINK);
        fmt.closeLI();
        //
        // all FSLIs
        //
        int countFSLIs = hlc.getFslis().size();
        fmt.openLI();
        newq = QueryHelper.copyWithEmptyCommand(gq);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_FSLIS");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_STRENGTH");
        PwQueryLinks.addLinkToQuery(fmt, newq, "FslInterpretation components ("
                + countFSLIs + ")", PwFormatter.CLASS_BODYLINK);
        fmt.closeLI();
        // closing the list of different breakouts
        fmt.closeUL();
    }

}
