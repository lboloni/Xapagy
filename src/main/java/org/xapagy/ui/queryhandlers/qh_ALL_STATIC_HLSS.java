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
import org.xapagy.headless_shadows.StaticHls;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * This class creates a page from which all the currently active StaticHls-s can
 * be inspected.
 * 
 * @author Ladislau Boloni
 * Created on: Sep 4, 2014
 */
public class qh_ALL_STATIC_HLSS implements IQueryHandler {
    /**
     * If it is set to true, cut off those which are irrelevantly weak
     */
    public static boolean cutOffIrrelevant = false;

    /**
     * The main function, generates the page on the formatter.
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        fmt.addH2("StaticHls objects", "class=identifier");
        fmt.explanatoryNote("Number of StaticHLSs:"
                + agent.getHeadlessComponents().getStaticHlss().size());
        for (StaticHls shls : agent.getHeadlessComponents().getStaticHlss()) {
            fmt.openP();
            fmt.progressBar(shls.getSupportSalience(), 1.0);
            PwQueryLinks.linkToStaticHls(fmt, agent, query, shls);
            fmt.closeP();
        }
    }
}
