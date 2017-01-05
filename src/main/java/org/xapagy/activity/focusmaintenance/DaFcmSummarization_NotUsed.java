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
package org.xapagy.activity.focusmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.links.Links;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.ViSet;
import org.xapagy.summarization.SummarizationHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * NOTE: January 5, 2017: This Da had been disconnected from PI-FocusHandling.xapi. 
 * While the neuromorphic Xapagy way is to do this with Da, in order to make 
 * progress faster, I will reimplement this as a single shot Sa, to be 
 * done after the VI is inserted.  
 * 
 * 
 * A focus DA which manages the transformation of the summarization energies
 * into regular focus energies. The general idea here is that a closed
 * summarization is like a regular VI.
 * 
 * Open summarizations carry FOCUS_SUMMARIZATION_VI energy.
 * 
 * When a summarization is closed, all its components are suddenly and in one
 * shot are being to transformed into FOCUS_VI.
 * 
 * If a summarization expired, its summarization energy will drop to zero, and
 * will be garbage collected without living a trace in the AM.
 * 
 * @author Ladislau Boloni
 * Created on: Dec 28, 2014
 */
public class DaFcmSummarization_NotUsed extends AbstractDaFocusIterator {

	private static final long serialVersionUID = -598675107488607177L;

	/**
	 * 
	 * @param agent
	 * @param name
	 */
	public DaFcmSummarization_NotUsed(Agent agent, String name) {
		super(agent, name);
	}

	/**
	 * 
	 * Apply for every VI
	 * 
	 * @param fvi
	 * @param time
	 */
	@Override
	protected void applyFocusVi(VerbInstance fvi, double time) {
		Links la = agent.getLinks();
		double summarizationEnergy = fc.getEnergy(fvi, EnergyColors.FOCUS_SUMMARIZATION_VI);
		if (summarizationEnergy == 0.0) {
			return;
		}
		//
		// if the summarization VI is closed, convert its energy
		// NOTE: this is a rather abrupt approach, better approaches might be
		// found
		//
		if (SummarizationHelper.isClosedSummarization(agent, fvi)) {
			double additiveChange = 1.0 * summarizationEnergy;
			EnergyQuantum<VerbInstance> eq = EnergyQuantum.createAdd(fvi, additiveChange, time, EnergyColors.FOCUS_VI,
					"DaFcmSummarization + FocusSummarization to Focus increase");
			fc.applyViEnergyQuantum(eq);
			double decay = 0.5;
			EnergyQuantum<VerbInstance> eqDecay = EnergyQuantum.createMult(fvi, decay, time,
					EnergyColors.FOCUS_SUMMARIZATION_VI, "DaFcmSummarization + FocusSummarization to Focus decay");
			fc.applyViEnergyQuantum(eqDecay);
		}
		//
		// if the summarization VI is open and all the links expired, remove the
		// summarization
		//
		//
		if (SummarizationHelper.isOpenSummarization(agent, fvi)) {
			boolean hasActiveLink = false;
			// begins
			ViSet begins = agent.getLinks().getLinksByLinkName(fvi, Hardwired.LINK_ELABORATION_BEGIN);
			ViSet bodies = null;
			for (VerbInstance vibegin : begins.getParticipants()) {
				if (fc.getEnergy(vibegin, EnergyColors.FOCUS_VI) > 0) {
					hasActiveLink = true;
					break;
				}
			}
			// if the begins are out, look for bodies
			if (!hasActiveLink) {
				bodies = agent.getLinks().getLinksByLinkName(fvi, Hardwired.LINK_ELABORATION_BODY);
				for (VerbInstance vibody : bodies.getParticipants()) {
					if (fc.getEnergy(vibody, EnergyColors.FOCUS_VI) > 0) {
						hasActiveLink = true;
						break;
					}
				}
			}
			//
			// If there is no active link, expire the unclosed summarization
			//
			if (!hasActiveLink) {
				TextUi.println("Expiring the summarization!!!");
				EnergyQuantum<VerbInstance> eqDecay = EnergyQuantum.createMult(fvi, 0.0, time,
						EnergyColors.FOCUS_SUMMARIZATION_VI, "DaFcmSummarization + expiring");
				fc.applyViEnergyQuantum(eqDecay);
				for (VerbInstance vibegin : begins.getParticipants()) {
					la.removeElaborationBegin(fvi, vibegin, "DaFcmSummarization");
				}
				for (VerbInstance vibody : bodies.getParticipants()) {
					la.removeElaborationBody(fvi, vibody, "DaFcmSummarization");
				}

			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.DiffusionActivity#extractParameters()
	 */
	@Override
	public void extractParameters() {
		// Nothing here for the time being
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters
	 * .IXwFormatter, int)
	 */
	@Override
	public void formatTo(IXwFormatter fmt, int detailLevel) {
		fmt.add("DaFcmSummarization");
		fmt.explanatoryNote("A focus DA which manages the transformation of the summarization energies "
				+ "into regular focus energies. The general idea here is that a closed "
				+ "summarization is like a regular VI. \n" +
				"Open summarizations carry FOCUS_SUMMARIZATION_VI energy. \n" +
				"When a summarization is closed, all its components are suddenly and in one "
				+ "shot are being to transformed into FOCUS_VI.\n" +
				"If a summarization expired, its summarization energy will drop to zero, and "
				+ "will be garbage collected without living a trace in the AM.");
	}

}
