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

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.TextUiHelper;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygraphviz.GvParameters;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.QueryHelper;

/**
 * @author Ladislau Boloni Created on: Apr 17, 2012
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
        //
        // energy label: explain the order in which the energies will be listed
        //
        //
        // energy label: explain the order in which the energies will be listed
        //
//        String overheadLabel = "";
//        for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
//            String ectext = ec.toString();
//            ectext = ectext.substring("FOCUS_".length());
//            ectext = ectext.replaceAll("SUMMARIZATION", "SUM");
//            // padding to width
//            overheadLabel += TextUiHelper.padTo(ectext, 15);
//         }        
		// list the scenes, and inside the scenes, all the member instances
		for (Instance scene : fc.getSceneListAllEnergies()) {
			List<Instance> members = scene.getSceneMembers();
			PwQueryLinks.linkToInstance(fmt, agent, gq, scene);
			fmt.indent();
			//fmt.add("<pre>" + overheadLabel + "</pre>");
			fmt.add(EnergyLabels.labelsFocusInstance(agent));
			for (Instance inst : members) {
				fmt.openP();
				for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
					fmt.progressBarSlash(fc.getSalience(inst, ec), fc.getEnergy(inst, ec));
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
		// add an image
		RESTQuery gqImg = QueryHelper.copyWithEmptyCommand(gq);
		gqImg.setAttribute(Q_RESULT_TYPE, "JPG");
		fmt2.addImg("src=" + gqImg.toQuery());
		// add a download link for the same image PDF and EPS (as PDF does not
		// work on Win8 currently)
		fmt2.openP();
		RESTQuery gqPdf = QueryHelper.copyWithEmptyCommand(gq);
		gqPdf.setAttribute(Q_RESULT_TYPE, "PDF");
		PwQueryLinks.addLinkToQuery(fmt2, gqPdf, "download as pdf", PwFormatter.CLASS_BODYLINK);
		RESTQuery gqEps = QueryHelper.copyWithEmptyCommand(gq);
		gqEps.setAttribute(Q_RESULT_TYPE, "EPS");
		PwQueryLinks.addLinkToQuery(fmt2, gqEps, "download as eps", PwFormatter.CLASS_BODYLINK);
		fmt2.closeP();
		IXwFormatter xw = new PwFormatter();
		GvParameters gvp = new GvParameters();
		gvp.describeInstanceLegend(xw);
		fmt2.explanatoryNote(xw.toString());
		fmt.addExtensibleH2("id" + countHideable++, "GraphViz", fmt2.toString(), true);
	}

}
