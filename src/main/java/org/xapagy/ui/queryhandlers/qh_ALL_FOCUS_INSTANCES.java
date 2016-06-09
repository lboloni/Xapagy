/*
   This file is part of the Xapagy project
   Created on: Apr 17, 2012
 
   org.xapagy.ui.prettyhtml.PwFocusInstances
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.QueryHelper;

/**
 * @author Ladislau Boloni
 * 
 */
public class qh_ALL_FOCUS_INSTANCES implements IQueryHandler, IQueryAttributes {

    /**
     * 
     * Generate the list of focus instances
     * 
     * @param agent
     * @param fc
     * @param udb
     * @param file
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery gq, Session session) {
        Focus fc = agent.getFocus();
        int countHideable = 1;
        //
        // List of instances organized by scenes
        //
        fmt.addH2("Focus instances", "class=identifier");
        // explain the order in which the energies will be listed
        StringBuffer exp = new StringBuffer();
        exp.append("Energies listed: ");
        for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
            exp.append(ec.toString() + " ");
         }        
        fmt.explanatoryNote(exp.toString());
        // list the scenes, and inside the scenes, all the member instances
        for (Instance scene : fc.getSceneListAllEnergies()) {
            List<Instance> members = scene.getSceneMembers();
            PwQueryLinks.linkToInstance(fmt, agent, gq, scene);
            fmt.indent();
            for (Instance inst : members) {
                fmt.openP();
                for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
                   fmt.progressBarSlash(fc.getSalience(inst, ec),
                        fc.getEnergy(inst, ec));
                }
                PwQueryLinks.linkToInstance(fmt, agent, gq, inst);
                fmt.closeP();
            }
            fmt.deindent();
        }

        //
        // Graphviz image
        //
        PwFormatter fmt2 = fmt.getEmpty();
        // Legend for the graphviz image
        exp = new StringBuffer();
        exp.append("Large rectangles: scenes. <br/>");
        exp.append("White nodes with black text: active instances<br>");
        exp.append("--- with gray text: inactive instances with zero focus presence.<br/>");
        exp.append("Dotted black lines: identity relations.<br/>");
        exp.append("Inter-scene arrows: black for succession, blue for fictional future, green for view<br/>");
        fmt2.explanatoryNote(exp.toString());
        // add an image
        RESTQuery gqImg = QueryHelper.copyWithEmptyCommand(gq);
        gqImg.setAttribute(Q_RESULT_TYPE, "JPG");
        fmt2.addImg("src=" + gqImg.toQuery());
        // add a download link for the same image PDF and EPS (as PDF does not
        // work on Win8 currently)
        fmt2.openP();
        RESTQuery gqPdf = QueryHelper.copyWithEmptyCommand(gq);
        gqPdf.setAttribute(Q_RESULT_TYPE, "PDF");
        PwQueryLinks.addLinkToQuery(fmt2, gqPdf, "download as pdf",
                PwFormatter.CLASS_BODYLINK);
        RESTQuery gqEps = QueryHelper.copyWithEmptyCommand(gq);
        gqEps.setAttribute(Q_RESULT_TYPE, "EPS");
        PwQueryLinks.addLinkToQuery(fmt2, gqEps, "download as eps",
                PwFormatter.CLASS_BODYLINK);
        fmt2.closeP();
        fmt.addExtensibleH2("id" + countHideable++, "GraphViz",
                fmt2.toString(), true);
    }

}
