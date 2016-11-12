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
import org.xapagy.headless_shadows.StaticFSLI;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.PwViTemplate;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * Functions which generate a visualization for a StaticFSLI
 * 
 * @author Ladislau Boloni
 * Created on: Sep 4, 2014
 */
public class qh_STATIC_FSLI implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        //
        //  Identify the StaticFSLI we are referring too
        //
        String identifier = query.getAttribute(Q_ID);
        StaticFSLI sfsli = null;
        for(StaticFSLI temp: agent.getHeadlessComponents().getStaticFSLIs()) {
            if (temp.getIdentifier().equals(identifier)) {
                sfsli = temp;
                break;
            }
        }
        if (sfsli == null) {
            fmt.addErrorMessage("Could not find StaticFSLI with identifier " + identifier);
            return;
        }
        //
        // Header
        //
        String redheader = "StaticFLSI " + sfsli.getIdentifier();
        fmt.addH2(redheader, "class=identifier");
        xwStaticFSLI(sfsli, agent, query, fmt);
    }


    /**
     * Visualize the static fsli
     * @param sfsli
     * @param agent
     * @param query
     * @param fmt
     */
    public static void xwStaticFSLI(StaticFSLI sfsli, Agent agent, RESTQuery query,
            IXwFormatter fmt) {
        fmt.is("Identifier", sfsli.getIdentifier());
        fmt.addBold("viInterpretation:");
        PwViTemplate.xwDetailed(fmt, sfsli.getViInterpretation(), agent, query);
        fmt.is("viMemory", PwQueryLinks.linkToVi(fmt.getEmpty(), agent, query, sfsli.getViMemory()));
        fmt.is("totalSupport", sfsli.getTotalSupport());
        fmt.addBold("SOSP:");
        fmt.indent();
        qh_SOSP.xwSOSP(sfsli.getSosp(), agent, query, fmt);
        fmt.deindent();
    }
    

    /**
     * @param agent
     * @param sfsli
     * @return
     */
    public static String pwConcise(Agent agent, StaticFSLI sfsli) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("StaticFSLI:");
        PwFormatter fmt = new PwFormatter();
        String interpretation = PwViTemplate.pwConcise(fmt, sfsli.getViInterpretation(), agent);
        buffer.append(interpretation);
        buffer.append(" from ");
        buffer.append(XapiPrint.ppsViXapiForm(sfsli.getViMemory(), agent));
        return buffer.toString();
    }
    
    
}
