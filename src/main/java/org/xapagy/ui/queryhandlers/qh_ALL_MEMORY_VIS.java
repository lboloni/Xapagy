package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_ALL_MEMORY_VIS implements IQueryHandler, IQueryAttributes {

    public static final int CURSOR_SIZE = 30;
    
    /**
     * Generate a web page listing 30 VIs from the autobiographical memory on a moving cursor
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        // start by determining the cursor
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        List<String> viKeys = new ArrayList<>();
        viKeys.addAll(am.getViIdentifiers());
        Collections.sort(viKeys);
        
        int cursorTotal = Integer.parseInt(query.getAttribute(Q_CURSOR_TOTAL));
        if (cursorTotal == 0) {
            query.setAttribute(Q_CURSOR_FROM, "0");
            query.setAttribute(Q_CURSOR_TO, "" + Math.min(CURSOR_SIZE, viKeys.size()));
            query.setAttribute(Q_CURSOR_SIZE, "" + CURSOR_SIZE);
            query.setAttribute(Q_CURSOR_TOTAL, "" + viKeys.size());
        } else {
            query.setAttribute(Q_CURSOR_TOTAL, "" + viKeys.size());
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
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_VI)) {
            exp.append(ec.toString() + " ");
        }
        fmt.explanatoryNote(exp.toString());
        
        
        PwQueryLinks.cursorLinks(fmt, query);
        List<String> toshow = viKeys.subList(from, to);
        for (String key : toshow) {
            VerbInstance vi = am.getVerbInstance(key);
            fmt.openP();
            for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_VI)) {
                fmt.progressBarSlash(am.getSalience(vi, ec),
                        am.getEnergy(vi, ec));
            }
            PwQueryLinks.linkToVi(fmt, agent, query, vi);
            fmt.closeP();
        }
    }

}
