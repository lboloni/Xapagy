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
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_ALL_HLS_NEW_INSTANCES implements IQueryHandler {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String redheader = "HlsNewInstance components";
        fmt.addH2(redheader, "class=identifier");
        // FIXME add the format here
        HeadlessComponents hlc = agent.getHeadlessComponents();
        for (HlsNewInstance hlsni : hlc.getHlsNewInstances()) {
            fmt.openP();
            PwQueryLinks.linkToHlsNewInstance(fmt, agent, query, hlsni);
            fmt.closeP();
        }
    }

}
