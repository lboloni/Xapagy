/*
   This file is part of the Xapagy project
   Created on: Sep 4, 2014
 
   org.xapagy.ui.prettyhtml.PwAllStaticHlss
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 *
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
