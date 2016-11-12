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

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.links.Links;
import org.xapagy.links.LinkQuantum;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.HtmlFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * @author Ladislau Boloni
 * Created on: Mar 5, 2015
 */
public class qh_LINK implements IQueryHandler, IQueryAttributes {

    /**
     * Generate a web page for a Link type query
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        boolean recordLinkQuantum =
                agent.getParameters().getBoolean("A_DEBUG",
                        "G_GENERAL",
                        "N_RECORD_LINK_QUANTUMS");

        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        Links lapi = agent.getLinks();
        String viFromId = query.getAttribute(Q_ID);
        VerbInstance viFrom = am.getVerbInstance(viFromId);
        String viToId = query.getAttribute(Q_SECOND_ID);
        VerbInstance viTo = am.getVerbInstance(viToId);
        String linkName = query.getAttribute(Q_LINK_TYPE);
        
        String redheader = "Link of type " + linkName;
        fmt.addH2(redheader, "class=identifier");
        double linkValue = lapi.getLinkValueByLinkName(viFrom, viTo, linkName);
        fmt.openP();
        fmt.addBold("From: ");
        PwQueryLinks.linkToVi(fmt, agent, query, viFrom);
        fmt.closeP();
        fmt.openP();
        fmt.addBold("To:");
        PwQueryLinks.linkToVi(fmt, agent, query, viTo);
        fmt.closeP();
        fmt.openP();
        fmt.is("Link name", linkName);
        fmt.closeP();
        fmt.openP();
        fmt.is("Value", Formatter.fmt(linkValue));
        fmt.closeP();

        if (recordLinkQuantum) {
            fmt.addBold("Record link quantum turned on. This link was changed by the following quantums");
            //fmt.indent();
            //fmt.openPre();
            List<LinkQuantum> lqlist = new ArrayList<>();
            for (LinkQuantum lq : lapi.getQuantums()) {                
                if (lq.matches(linkName, viFrom, viTo)) {
                    lqlist.add(lq);
                }
                if (lq.matchesReverse(linkName, viTo, viFrom, agent.getLinks())) {
                    lqlist.add(lq);
                }
            }
            //fmt.closePre();
            //fmt.deindent();
            pwLinkQuantumList(fmt, lqlist);
        } else {
            fmt.addBold("Record link quantum turned off.");
        }
    }
    
    
    /**
     * Formats a list of link quantums into a colored table
     * 
     * @param fmt
     * @param list
     */
    private static void pwLinkQuantumList(HtmlFormatter fmt, List<LinkQuantum> list) {
        fmt.openTable("class=energyquantum");
        //
        // create the header
        //
        fmt.openTR("style=background-color:white");
        // the time and identifier
        fmt.openTD();
        fmt.add("Time:");
        fmt.closeTD();
        fmt.openTD();
        fmt.add("Value before:");
        fmt.closeTD();
        // the application order
        //fmt.openTD();
        //fmt.add("AppOrder:");
        //fmt.closeTD();
        // the change type and parameter
        fmt.openTD();
        fmt.add("Change:");
        fmt.closeTD();
        // the timeSlice
        //fmt.openTD();
        //fmt.add("Timeslice:");
        //fmt.closeTD();
        // the source
        fmt.openTD();
        fmt.add("Source:");
        fmt.closeTD();
        fmt.openTD();
        fmt.add("Value after:");
        fmt.closeTD();
        // end of columns
        fmt.closeTR();

        for (LinkQuantum lq : list) {
            // determine the change and an appropriate color: blue for addition,
            // green for mult increase pink for mult decrease
            String color = "";
            if (lq.getValueAfter() > lq.getValueBefore()) {
                color = "LightGreen";
            } else {
                color = "LightPink";
            }
            fmt.openTR("style=background-color:" + color);
            // the time
            fmt.openTD();
            String agentTime = Formatter.fmt(lq.getAgentTimeWhenApplied());
            fmt.add(agentTime);
            fmt.closeTD();
            // the application order
            //fmt.openTD();
            //fmt.add(eq.getApplicationOrder());
            //fmt.closeTD();
            // the change type and parameter
            // link value after quantum
            fmt.openTD();
            fmt.add(Formatter.fmt(lq.getValueBefore()));
            fmt.closeTD();
            // the change
            fmt.openTD();
            fmt.add(Formatter.fmt(lq.getDelta()));
            fmt.closeTD();
            // the time slice
            //fmt.openTD();
            //fmt.add(eq.getTimeSlice());
            //fmt.closeTD();
            // the source
            fmt.openTD();
            fmt.add(lq.getSource());
            fmt.closeTD();
            // the colors
            // fmt2.openTD();
            // fmt2.add(sq.getEnergyColor().toString());
            // fmt2.closeTD();
            // link value after quantum
            fmt.openTD();
            fmt.add(Formatter.fmt(lq.getValueAfter()));
            fmt.closeTD();
            // end of columns
            fmt.closeTR();
        }
        fmt.closeTable();
    }

    
    
}
