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
package org.xapagy.metaverbs;

import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.agents.FocusSorter;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.SceneHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.links.CoincidenceHelper;
import org.xapagy.links.Links;
import org.xapagy.summarization.SummarizationHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Spike activity for the "thus" verb. Used for the creation of coincidence
 * groups. Coincidence groups are cross-connected with coincidence links, and
 * for all purposes serve as a single VI.
 * 
 * @author Ladislau Boloni Created on: Jan 18, 2012
 */
public class SaMvThus extends AbstractSaMetaVerb {

	private static final long serialVersionUID = -7114446061249114025L;

	/**
	 * 
	 * @param agent
	 */
	public SaMvThus(Agent agent) {
		super(agent, "SaMvThus", null);
	}

	/**
	 * The functionality here is that it identifies
	 */
	@Override
	public void applyInner(VerbInstance verbInstance) {
		VerbInstance viLeader = resolveCoincidenceLeader(verbInstance);
		if (viLeader == null) {
			TextUi.errorPrint("SaMvThus: coincidence, could not resolve the leader!!!");
		}
		CoincidenceHelper.connectToCoincidenceGroup(agent, viLeader, verbInstance, "SaMvThus");
	}

	/**
	 * Resolves the coincidence leader, the vi to which the verb instance will
	 * cling.
	 */
	private VerbInstance resolveCoincidenceLeader(VerbInstance verbInstance) throws Error {
		List<String> labels = verbInstance.getVerbs().getLabels();
		// depending whether the VI has verb labels or not, different way to
		// choose the summarization
		if (labels.isEmpty()) {
			return resolveCoincidenceLeaderStrongest(verbInstance);
		} else {
			return resolveCoincidenceLeaderLabelBased(verbInstance, labels);
		}
	}

	/**
	 * Resolves the hook to be the strongest VI in the focus which has the label
	 * specified in the VI. Strongest here means the one with the appropriate
	 * focus energy color for summarizations etc.
	 * 
	 * @param verbInstance - the vi we are linking
	 * @param labels - the extracted labels based on which we are doing the linking
	 * @return
	 * @throws Error
	 */
	private VerbInstance resolveCoincidenceLeaderLabelBased(VerbInstance verbInstance, List<String> labels)
			throws Error {
		String ec = SummarizationHelper.getFocusEnergyColor(verbInstance);
		List<VerbInstance> vis = agent.getReferenceAPI().VisByLabels(labels, ec);
		if (vis.isEmpty()) {
			throw new Error("Using thus but nothing with labels: " + labels + " to connect to.");
		}
		for (VerbInstance vi : vis) {
			if (vi.equals(verbInstance)) {
				continue;
			}
			return vi;
		}
		throw new Error("Using thus but nothing with labels: " + labels + " to connect to.");
	}

	/**
	 * Resolves the coincidence leader to the the strongest one in terms of the appropriate
	 * energy color. 
	 *
	 * @param verbInstance
	 * @return
	 * @throws Error
	 */
	private VerbInstance resolveCoincidenceLeaderStrongest(VerbInstance verbInstance) throws Error {
		VerbInstance viHook = null;
		Focus fc = agent.getFocus();
		String ec = SummarizationHelper.getFocusEnergyColor(verbInstance);
		double maxVal = 0;
		List<VerbInstance> list = fc.getViList(ec);
		FocusSorter.sortVisDecreasingCreationTime(list, agent);
		for (VerbInstance fvi : list) {
			if (fvi == verbInstance) {
				continue;
			}
			// if the two vi's do not share a scene, skip
			Set<Instance> fviScenes =  SceneHelper.extractScenes(fvi, false);
			Set<Instance> viScenes = SceneHelper.extractScenes(verbInstance, false); 
			fviScenes.retainAll(viScenes);
			if (fviScenes.isEmpty()) {
				continue;
			}
			if (fvi.getSummarizationLevel() != verbInstance.getSummarizationLevel()) {
				continue;
			}
			if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
				double fviValue = fc.getSalience(fvi, ec);
				if (fviValue > maxVal) {
					viHook = fvi;
					maxVal = fviValue;
				}
			}
		}
		if (viHook == null) {
			throw new Error("Using thus but nothing to connect to!!!");
		}
		return viHook;
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
		fmt.add("SaMvThus");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.Activity#extractParameters()
	 */
	public void extractParameters() {
	}
}
