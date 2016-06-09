package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.parameters.Parameters;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwParameters;
import org.xapagy.ui.prettyhtml.IQueryHandler;

public class qh_PARAMETERS implements IQueryHandler {

    
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        Parameters p = agent.getParameters();
        String redheader = "Parameters prepack = NO NAME";
        fmt.addH2(redheader, "class=identifier");
        xwParameters.xwDetailed(fmt, p);
    }
}
