package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.prettyprint.PrintDetail;

public class qh_PERFORMANCE implements IQueryHandler {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String pp =
                PrettyPrint.pp(agent.getDebugInfo().getPerformanceMeter(), agent,
                        PrintDetail.DTL_DETAIL);
        // reset the performance meter
        // agent.getPerformanceMeter().timeCountReset();
        fmt.addPre(pp);
    }

}
