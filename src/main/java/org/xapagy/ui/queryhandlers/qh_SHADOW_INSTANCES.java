package org.xapagy.ui.queryhandlers;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_SHADOW_INSTANCES implements IQueryHandler {

    public static final int maxShadows = 5;


    /**
     * Generates a web page listing shadows for all the instances in the focus
     * @param fmt
     * @param agent
     * @param query
     * @param wgpg
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {        
        Focus fc = agent.getFocus();
        String redheader =
                "Focus instances with the " + qh_SHADOW_INSTANCES.maxShadows
                        + " strongest shadows";
        fmt.addH2(redheader, "class=identifier");
        for (Instance scene : fc.getSceneListAllEnergies()) {
            List<Instance> members = scene.getSceneMembers();
            PwQueryLinks.linkToStoryLineColor(fmt, agent, scene, session.colorCodeRepository);
            PwQueryLinks.linkToInstance(fmt, agent, query, scene);
            fmt.indent();
            for (Instance inst : members) {
    			fmt.add(EnergyLabels.labelsFocusInstance(agent));
                fmt.openP();
                for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
                    fmt.progressBarSlash(fc.getSalience(inst, ec),
                         fc.getEnergy(inst, ec));
                 }
                PwQueryLinks.linkToStoryLineColor(fmt, agent, inst, session.colorCodeRepository);
                PwQueryLinks.linkToInstance(fmt, agent, query, inst);
                fmt.startEmbed();
                // print strongest shadows
                fmt.add(qh_INSTANCE.pwInstanceShadow(inst, agent, query,
                        qh_SHADOW_INSTANCES.maxShadows, session.colorCodeRepository, false));
                fmt.endEmbed();
                fmt.closeP();
            }
            fmt.deindent();
        }
    }
}
