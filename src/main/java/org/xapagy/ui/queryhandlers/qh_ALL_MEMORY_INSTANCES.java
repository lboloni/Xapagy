package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_ALL_MEMORY_INSTANCES implements IQueryHandler, IQueryAttributes {

    public static final int CURSOR_SIZE = 30;

    
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        // determining the location in the cursor
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        List<String> instanceKeys = new ArrayList<>();
        instanceKeys.addAll(am.getInstanceIdentifiers());
        Collections.sort(instanceKeys);
        int cursorTotal = Integer.parseInt(query.getAttribute(Q_CURSOR_TOTAL));
        if (cursorTotal == 0) {
            query.setAttribute(Q_CURSOR_FROM, "0");
            query.setAttribute(Q_CURSOR_TO, "" + Math.min(CURSOR_SIZE, instanceKeys.size()));
            query.setAttribute(Q_CURSOR_SIZE, "" + CURSOR_SIZE);
            query.setAttribute(Q_CURSOR_TOTAL, "" + instanceKeys.size());
        } else {
            query.setAttribute(Q_CURSOR_TOTAL, "" + instanceKeys.size());
        }
        int from = Integer.parseInt(query.getAttribute(Q_CURSOR_FROM));
        int to = Integer.parseInt(query.getAttribute(Q_CURSOR_TO));        
        int total = Integer.parseInt(query.getAttribute(Q_CURSOR_TOTAL));
        // header        
        String redheader = "VI in the autobiographical memory from " +  from  + " to " + to + " out of" + total;
        fmt.addH2(redheader, "class=identifier");
        // explain the order in which the energies will be listed
        StringBuffer exp = new StringBuffer();
        exp.append("Energies listed: ");
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_INSTANCE)) {
            exp.append(ec.toString() + " ");
        }
        fmt.explanatoryNote(exp.toString());

        PwQueryLinks.cursorLinks(fmt, query);
        List<String> toshow = instanceKeys.subList(from, to);
        for (String key : toshow) {
            Instance instance = am.getInstance(key);
            fmt.openP();
            for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_INSTANCE)) {
                fmt.progressBarSlash(am.getSalience(instance, ec),
                        am.getEnergy(instance, ec));
            }
            PwQueryLinks.linkToInstance(fmt, agent, query, instance);
            fmt.closeP();
        }
    }

}
