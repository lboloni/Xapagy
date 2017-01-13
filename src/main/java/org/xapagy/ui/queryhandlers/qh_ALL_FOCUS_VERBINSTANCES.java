/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.agents.FocusSorter;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.formatters.HtmlFormatter;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygraphviz.GvParameters;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.QueryHelper;
import org.xapagy.ui.smartprint.SpFocus;

public class qh_ALL_FOCUS_VERBINSTANCES implements IQueryHandler, IQueryAttributes {


    /**
     * Generates a webpage documenting the VIs in the focus. 
     * @param fmt
     * @param agent
     * @param gq
     * @param session
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery gq, Session session) {
        int countId = 0;
        Focus fc = agent.getFocus();
        fmt.addH2("Focus VIs", "class=identifier");
        // this set allows us to track if something did not fit in any of the categories
        // and print it separately at the end
        Set<VerbInstance> alreadyPrinted = new HashSet<>();
        // get all the VIs of any energy and split them into categories
        List<VerbInstance> listVis = fc.getViListAllEnergies();
        Map<ViClass, List<VerbInstance>> visSplitByClass =
                ViClassifier.splitByClass(listVis, agent);
        //
        // Action VIs
        //
        fmt.addH2("Action VIs");
        List<VerbInstance> actionList =
                new ArrayList<>(visSplitByClass.get(ViClass.ACTION));
        // FocusSorter.sortVisDecreasingFocusSalience(actionList, agent);
        fmt.add(EnergyLabels.labelsFocusVi(agent));
        FocusSorter.sortVisIncreasingCreationTime(actionList, agent);
        for (VerbInstance vi : actionList) {
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            PwQueryLinks.linkToVi(fmt, agent, gq, vi);
            fmt.closeP();
            alreadyPrinted.add(vi);
        }
        //
        // Relation
        //
        fmt.addH2("Relations");
        List<VerbInstance> relationList =
                new ArrayList<>(visSplitByClass.get(ViClass.RELATION));
        FocusSorter.sortVisDecreasingFocusSalience(relationList, agent);
        for (VerbInstance vi : relationList) {
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            PwQueryLinks.linkToVi(fmt, agent, gq, vi);
            fmt.closeP();
            alreadyPrinted.add(vi);
        }
        //
        // Relation creation
        //
        fmt.addH2("Relation manipulation");
        List<VerbInstance> relationManipulationList =
                new ArrayList<>(visSplitByClass.get(ViClass.RELATION_MANIPULATION));
        FocusSorter.sortVisDecreasingFocusSalience(relationManipulationList,
                agent);
        for (VerbInstance vi : relationManipulationList) {
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            PwQueryLinks.linkToVi(fmt, agent, gq, vi);
            fmt.closeP();
            alreadyPrinted.add(vi);
        }
        //
        // Everything else in the focus
        //
        boolean missedSomething = false;
        List<VerbInstance> missedList = new ArrayList<>(fc.getViListAllEnergies());
        FocusSorter.sortVisDecreasingFocusSalience(missedList, agent);
        for (VerbInstance vi : missedList) {
            if (alreadyPrinted.contains(vi)) {
                continue;
            }
            if (!missedSomething) {
                missedSomething = true;
                fmt.addH2("Everything else which did not appear above");
            }
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            PwQueryLinks.linkToVi(fmt, agent, gq, vi);
            fmt.closeP();
        }
		//
		// Graphviz image
		//
		PwFormatter fmt2 = fmt.getEmpty();
        RESTQuery gqImg = QueryHelper.copyWithEmptyCommand(gq);
        gqImg.setAttribute(Q_RESULT_TYPE, "JPG");
        fmt2.addImg("src=" + gqImg.toQuery()); // was "width=90%"

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
        //
        // Legend for the graphviz image
        //
		IXwFormatter xw = new PwFormatter();
		GvParameters gvp = new GvParameters();
		gvp.describeVILegend(xw);
        fmt2.explanatoryNote(xw.toString());

        fmt.addExtensibleH2("id" + countId++, "GraphViz", fmt2.toString(), true);
        //
        // Raw printing of structure
        //
        String actionVis = SpFocus.ppsActionVis(fc, agent);
        fmt.addExtensibleH2("id" + countId++, "Action VIs prettyprint",
                HtmlFormatter.getPreBlock(actionVis), false);
    }
}
