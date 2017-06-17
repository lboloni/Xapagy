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

import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.links.CoincidenceHelper;
import org.xapagy.summarization.SummarizationHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * This class implements the actions related to summarization for the newly
 * created VI.
 * 
 * @author Ladislau Boloni Created on Jan 13, 2017
 */
public class SaFcmNaiveSummarization extends SpikeActivity {

	private static final long serialVersionUID = 2592801386676711424L;

	public enum ViSuviRelation {
		Indifferent, Joins, Extends, Closes, Terminates, Opens
	};

	/**
	 * 
	 * @param agent
	 * @param name
	 * @param vi
	 */
	public SaFcmNaiveSummarization(Agent agent, String name) {
		super(agent, name);
	}

	@Override
	public void formatTo(IXwFormatter fmt, int detailLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyInner(VerbInstance vi) {
		// identify all the VIs that might possibly be suvi's for this one
		int sumlevel = vi.getSummarizationLevel();
		String ec = SummarizationHelper.getFocusEnergyColor(vi.getSummarizationLevel() + 1);
		List<VerbInstance> suvis = agent.getFocus().getViList(ec);
		// this number will count how many summarizations will the vi be part of
		int partof = 0;
		for (VerbInstance suvi : suvis) {
			if (!SummarizationHelper.isOpenSummarization(agent, suvi)) {
				continue;
			}
			ViSuviRelation action = visuviAction(vi, suvi);
			switch (action) {
			case Indifferent:
				break;
			case Joins:
			case Extends:
				partof++;
				break;
			case Closes:
				break;
			case Terminates:
				break;
			}
		}
		// If there is no summarization, we create an empty one
		if ((sumlevel) < 2 && (partof == 0)) {
			// create a new one
			VerbInstance newsummary = createSummarizationForVi(vi);
			agent.getLoop().proceedOneForcedStep(newsummary, 1.0);
		}

	}

	/**
	 * Creates a summarization step. For the time being it is maybe an empty
	 * one...
	 * 
	 * Later, it might be that already here we can create something interesting,
	 * such as the appropriate valence etc.
	 * 
	 * FIXME: not fully done yet!!!
	 * 
	 * @param vi
	 * @return
	 */
	public VerbInstance createSummarizationForVi(VerbInstance vi) {
		// create a verbs at the appropriate level
		VerbOverlay verbs = VerbOverlay.createVO(agent,
				Hardwired.V_SUMMARIZATION_ROOT + (vi.getSummarizationLevel() + 1), Hardwired.VM_ACTION_MARKER);
		VerbInstance viTemplate = VerbInstance.createViTemplate(agent, ViType.S_V, verbs);
		// the subject should actually be a group of the stuff
		Set<Instance> groupSet = new HashSet<>();
		switch(vi.getViType()) {
		case S_V:
			groupSet.add(vi.getSubject());
			break;
		case S_V_O:
			groupSet.add(vi.getSubject());
			groupSet.add(vi.getObject());
			break;
		case QUOTE:
			// summarizing quotes is a separate problem...
			groupSet.add(vi.getSubject());
			break;
		case S_ADJ:
			groupSet.add(vi.getSubject());
			break;
		}
		// we create a dedicated group here, because it will be extended
		Instance group = GroupHelper.createGroup(agent, groupSet, vi.getSubject().getScene());
		// but we will start by replicating the subject
		viTemplate.setSubject(group);
		return viTemplate;
	}

	/**
	 * Identify relationship between the vi and a particular candidate suvi. Take
	 * actions function of this relation
	 * 
	 * @param vi
	 * @param suvi
	 */
	public ViSuviRelation visuviAction(VerbInstance vi, VerbInstance suvi) {
		SimpleEntry<ViSuviRelation, VerbInstance> entry = identifyViSuviRelation(vi, suvi);
		ViSuviRelation rel = entry.getKey();
		switch (rel) {
		case Indifferent:
			// the vi and the suvi are indifferent to each other, do nothing
			return rel;
		case Joins:
			// they are compatible and they refer the the same things
			// The vi is added as part of the body of the suvi
			agent.getLinks().changeLinkByName(Hardwired.LINK_SUMMARIZATION_BODY, vi, suvi, 1.0, "visuviAction - Joins");
			return rel;
		case Extends:
			// It is going to get the link as elaboration
			VerbInstance suviex = entry.getValue();
			agent.getLoop().proceedOneForcedStep(suviex, 1.0);
			CoincidenceHelper.connectToCoincidenceGroup(agent, suvi, suviex, "SaFcmNaiveSummarization");
			agent.getLinks().changeLinkByName(Hardwired.LINK_SUMMARIZATION_BODY, vi, suviex, 1.0, "visuviAction - Extends");
			return rel;
		case Closes:
			// It adds the closed link, itself becoming part of the suvi
			agent.getLinks().changeLinkByName(Hardwired.LINK_SUMMARIZATION_CLOSE, vi, suvi, 1.0,
					"visuviAction - Closes");
			agent.getLinks().changeLinkByName(Hardwired.LINK_SUMMARIZATION_BODY, vi, suvi, 1.0,
					"visuviAction - Closes");
			return rel;
		case Terminates:
			// After this vi, the suvi will not be an open summary any more
			// but this vi will not be part of the suvi
			SummarizationHelper.forceSummarizationClosed(agent, suvi);
			return rel;
		}
		// should not get here
		throw new Error("Should not have gotten here " + rel);
	}

	/**
	 * Identifies the relationships between the vi and the suvi. Returns a tuple
	 * of the relation type, and in cases when appropriate, the new VI.
	 * 
	 * This is the function where most of the intelligence needs to go.
	 * 
	 * @param vi
	 * @param suvi
	 * @return
	 */
	public SimpleEntry<ViSuviRelation, VerbInstance> identifyViSuviRelation(VerbInstance vi, VerbInstance suvi) {
		//
		// if there is no shared scene, return indifferent
		//
		Set<Instance> viScenes = CoincidenceHelper.getScenes(agent, vi);
		Set<Instance> suviScenes = CoincidenceHelper.getScenes(agent, suvi);
		// calculate intersection in the viScenes, which will be modified here.
		viScenes.retainAll(suviScenes);
		if (viScenes.isEmpty()) {
			return new SimpleEntry<ViSuviRelation, VerbInstance>(ViSuviRelation.Indifferent, null);
		}
		//
		// if there is no shared instance, when unpacked, return indifferent
		//
		Set<Instance> viInstances = CoincidenceHelper.getInstances(agent, vi, true); 
		Set<Instance> suviInstances = CoincidenceHelper.getInstances(agent, suvi, true); 
		viInstances.retainAll(suviInstances);
		if (viInstances.isEmpty()) {
			return new SimpleEntry<ViSuviRelation, VerbInstance>(ViSuviRelation.Indifferent, null);
		}
		// FIXME: add here Joins

		
		// FIXME: add here Extend
		// otherwise return indifferent???
		TextUi.println("visuviAction called, return indiferent");
		return new SimpleEntry<ViSuviRelation, VerbInstance>(ViSuviRelation.Indifferent, null);
		
	}

	
	
	
	@Override
	public void extractParameters() {
		// for the time being, this SA has no parameters
	}

}
