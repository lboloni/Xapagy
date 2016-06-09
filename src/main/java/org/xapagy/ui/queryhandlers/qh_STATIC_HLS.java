/*
   This file is part of the Xapagy project
   Created on: Sep 4, 2014
 
   org.xapagy.ui.prettyhtml.PwStaticHls
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.StaticFSLI;
import org.xapagy.headless_shadows.StaticHls;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.PwViTemplate;

/**
 * Functions which generate a visualization for a StaticHls
 * 
 * @author Ladislau Boloni
 *
 */
public class qh_STATIC_HLS implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        //
        // Identify the StaticHls we are referring too
        //
        String identifier = query.getAttribute(Q_ID);
        StaticHls shls = null;
        for (StaticHls temp : agent.getHeadlessComponents().getStaticHlss()) {
            if (temp.getIdentifier().equals(identifier)) {
                shls = temp;
                break;
            }
        }
        if (shls == null) {
            fmt.addErrorMessage("Could not find StaticHls with identifier "
                    + identifier);
            return;
        }
        //
        // Header
        //
        String redheader = "StaticHls " + shls.getIdentifier();
        fmt.addH2(redheader, "class=identifier");
        xwStaticHLS(shls, agent, query, fmt);
    }

    /**
     * 
     * @param shls
     * @param agent
     * @param query
     * @param fmt
     */
    public static void xwStaticHLS(StaticHls shls, Agent agent, RESTQuery query,
            IXwFormatter fmt) {
       fmt.is("Identifier", shls.getIdentifier());
       fmt.addBold("viInterpretation:");
       PwViTemplate.xwDetailed(fmt, shls.getViTemplate(), agent, query);
       fmt.addBold("Supports");
       fmt.indent();
       List<StaticFSLI> list = new ArrayList<>();
       list.addAll(shls.getSupports());
       Collections.sort(list);
       Collections.reverse(list);
       for(StaticFSLI sfsli: list) {
           fmt.openP();
           PwQueryLinks.linkToStaticFSLI(fmt, agent, query, sfsli);
           fmt.closeP();
       }
       fmt.deindent();
    }

    /**
     * @param agent
     * @param shls
     * @return
     */
    public static String pwConcise(Agent agent, StaticHls shls) {
        StringBuffer buf = new StringBuffer();
        buf.append("StaticHLS:");
        PwFormatter fmt = new PwFormatter();
        String template =
                PwViTemplate.pwConcise(fmt, shls.getViTemplate(), agent);
        buf.append(template);
        return buf.toString();
    }

}
