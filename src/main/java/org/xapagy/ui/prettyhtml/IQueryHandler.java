/*
   This file is part of the Xapagy project
   Created on: Mar 5, 2015
 
   org.xapagy.ui.prettyhtml.IQueryHandler
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;

/**
 * General interface for Xapagy query handlers. They generate the bottom part of the 
 * page and write them in the page.
 * 
 * @author Ladislau Boloni
 *
 */
public interface IQueryHandler {
     void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session);
}
