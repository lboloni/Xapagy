/*
   This file is part of the Xapagy project
   Created on: Sep 4, 2014
 
   org.xapagy.ui.prettyhtml.PwAllSASPs
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.SOSP;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.parameters.Parameters;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * This class creates a page from which all currently active SASPs can be inspected
 * 
 * @author Ladislau Boloni
 *
 */
public class qh_ALL_SOSPS implements IQueryHandler {
    /**
     * If it is set to true, cut off those which are irrelevantly weak
     */
    public static boolean cutOffIrrelevant = false;

    
    /**
     * The main function, generates the page on the formatter.
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        //int idCount = 0;
        Parameters p = agent.getParameters();
        //
        // List of StaticFSLIs, grouped by the list of ViLinked
        //
        fmt.addH2("SOSP objects: grouped by shadow scene",
                "class=identifier");
        fmt.explanatoryNote("The SOSPs are grouped by the common shared shadow scene (i.e. story line).");

        //
        // List of FSLIs, sorted by support
        //
        if (!qh_ALL_STATIC_FSLIS.cutOffIrrelevant) {
            fmt.addH2("All currently active SOSP objects",
                    "class=identifier");
        } else {
            fmt.addH2(
                    "All SOSP objects with relevant strength (> "
                            + Formatter.fmt(p.get("A_HLSM",
                                    "G_GENERAL",
                                    "N_MINIMUM_FSL_STRENGTH"))
                            + ")", "class=identifier");
        }
        fmt.explanatoryNote("Number of SOSPs:" + agent.getHeadlessComponents().getSOSPs().size());
        // organize the SOSPs by scene
        Map<Instance, List<SOSP>> scenes = new HashMap<>();        
        for(SOSP sosp: agent.getHeadlessComponents().getSOSPs()) {
            List<SOSP> list = scenes.get(sosp.getShadowScene());
            if (list != null) {
                list.add(sosp);
            } else {
                list = new ArrayList<>();
                list.add(sosp);
                scenes.put(sosp.getShadowScene(), list);
            }
        }
        // create links to these on a scene by scene basis
        for(Instance scene: scenes.keySet()) {
            List<SOSP> list = scenes.get(scene);
            fmt.openH3();
            PwQueryLinks.linkToInstance(fmt, agent, query, scene);
            fmt.closeH3();
            fmt.indent();
            for(SOSP sosp: list) {
                fmt.openP();
                fmt.progressBar(sosp.getScore(), 1.0);
                PwQueryLinks.linkToSOSP(fmt, agent, query, sosp);
                fmt.closeP();
            }
            fmt.deindent();
            fmt.addP("");
        }
        
    }
}
