/*
   This file is part of the Xapagy project
   Created on: Jan 21, 2016
 
   org.xapagy.ui.queryhandlers.qh_DAS
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import org.xapagy.activity.DaComposite;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;

/**
 * @author Ladislau Boloni
 *
 */
public class qh_DAS implements IQueryHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.prettyhtml.IQueryHandler#generate(org.xapagy.ui.formatters.
     * PwFormatter, org.xapagy.agents.Agent, org.xapagy.httpserver.RESTQuery,
     * org.xapagy.httpserver.Session)
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        // TODO Auto-generated method stub
        //
        // Identifier block
        //
        String redheader = "DA's active in the agent";
        fmt.addH2(redheader, "class=identifier");
        generateComposite(fmt, agent, Hardwired.DA_FOCUS_MAINTENANCE);
        generateComposite(fmt, agent, Hardwired.DA_SHADOW_MAINTENANCE);
        generateComposite(fmt, agent, Hardwired.DA_HLS_MAINTENANCE);
    }

    
    /**
     * Format the values for a composite DA
     * @param fmtBig
     * @param agent
     * @param composite
     */
    private void generateComposite(PwFormatter fmtBig, Agent agent, String composite) {
        PwFormatter fmt = new PwFormatter();
        DaComposite daComposite = agent.getDaComposite(composite);
        fmt.addH3("CompositeDA: " + composite);
        //fmt.openPre();
        //fmt.add(daGroup.toString());
        //fmt.closePre();
        daComposite.formatTo(fmt, 10);
        //TextUi.println(fmt.toString());
        // only for debugging
        fmtBig.add(fmt);
    }
    
}
