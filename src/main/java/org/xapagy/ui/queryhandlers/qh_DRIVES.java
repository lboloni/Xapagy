package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwDrives;
import org.xapagy.ui.prettyhtml.IQueryHandler;

public class qh_DRIVES  implements IQueryHandler{

   @Override
   public void generate(PwFormatter pw, Agent agent, RESTQuery query,
           Session session) {
       String redheader = "Drives";
       pw.addH2(redheader, "class=identifier");
       xwDrives.xwDetailed(pw, agent.getDrives(), agent);
   }

	
}
