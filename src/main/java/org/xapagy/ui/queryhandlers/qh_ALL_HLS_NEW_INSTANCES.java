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
