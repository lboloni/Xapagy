/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2013
 
   org.xapagy.ui.prettyhtml.PwRsTestingUnit
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyprint.PpRsTestingUnit;

/**
 * @author Ladislau Boloni
 * 
 */
public class qh_RS_TESTING_UNIT implements IQueryHandler {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery gq, Session session) {
        RsTestingUnit rtu = agent.getDebugInfo().getRsTestingUnit();
        String redheader = "RsTestingUnit";
        fmt.addH2(redheader, "class=identifier");
        fmt.addPre(PpRsTestingUnit.ppDetailed(rtu, agent));
    }
}
