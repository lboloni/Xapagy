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

import org.xapagy.agents.Agent;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.TextUiHelper;

/**
 * This class has helper functions that creates labels aligned with the progress
 * bars
 * 
 * FIXME: this is probably better done with some kind of variation of the progress bar itself 
 * 
 * @author lboloni Created on: Jan 3, 2017
 */
public class EnergyLabels {

	
	/**
	 * Creates a label for the focus VI energies
	 * @param agent
	 * @return
	 */
	public static String labelsFocusVi(Agent agent) {
		String overheadLabel = "";
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
			String ectext = ec.toString();
			ectext = ectext.substring("FOCUS_".length());
			ectext = ectext.replaceAll("SUMMARIZATION", "SUM");
			// padding to width
			overheadLabel += TextUiHelper.padTo(ectext, 15);
		}
		return "<pre>" + overheadLabel + "</pre>";
	}

	/**
	 * Creates a label for the shadow VI energies
	 * @param agent
	 * @return
	 */
	public static String labelsShadowVi(Agent agent) {
		String overheadLabel = "";
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
			String ectext = ec.toString();
			ectext = ectext.substring("SVH_".length());
			// padding to width
			overheadLabel += TextUiHelper.padTo(ectext, 15);
		}
		return "<pre>" + overheadLabel + "</pre>";
	}
	

	/**
	 * Creates a label for the shadow VI energies
	 * @param agent
	 * @return
	 */
	public static String labelsShadowInstance(Agent agent) {
		String overheadLabel = "";
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
			String ectext = ec.toString();
			ectext = ectext.substring("SVI_".length());
			// padding to width
			overheadLabel += TextUiHelper.padTo(ectext, 15);
		}
		return "<pre>" + overheadLabel + "</pre>";
	}

	
	/**
	 * Creates a label for the focus energies
	 * @param agent
	 * @return
	 */
	public static String labelsFocusInstance(Agent agent) {
        String overheadLabel = "";
        for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
            String ectext = ec.toString();
            ectext = ectext.substring("FOCUS_".length());
            ectext = ectext.replaceAll("SUMMARIZATION", "SUM");
            // padding to width
            overheadLabel += TextUiHelper.padTo(ectext, 15);
         }   
		return "<pre>" + overheadLabel + "</pre>";
	}
	
	
	
}
