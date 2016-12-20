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
package org.xapagy.verbalize;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.util.SimpleEntryComparator;
import org.xapagy.verbalize.VrblzCandidateInstance.IVCType;

/**
 * Implements the verbalization of an instance
 * 
 * @author Ladislau Boloni Created on: Oct 6, 2011
 */
public class VrblzInstance {

	/**
	 * Generates a list of candidates for the verbalization of the instance
	 * 
	 * FIXME: extend this to other models
	 * 
	 * FIXME: how to handle multiple ones for the same CO
	 * 
	 * @param agent
	 * @param vi
	 * @param vip
	 * @return
	 */
	public static List<VrblzCandidateInstance> generateCandidates(Agent agent, VerbInstance vi, ViPart vip) {
		List<VrblzCandidateInstance> retval = new ArrayList<>();
		Instance instance = (Instance) vi.getPart(vip);
		ConceptOverlay co = instance.getConcepts();
		//
		// simple, one concept ones
		//
		for (SimpleEntry<Concept, Double> entry : co.getList()) {
			Concept concept = entry.getKey();
			ConceptOverlay ivcCo = new ConceptOverlay(agent);
			ivcCo.addFullEnergy(concept);
			for (String label : co.getLabels()) {
				ivcCo.addFullLabel(label, agent);
			}
			List<SimpleEntry<String, Double>> words = VrbOverlay.getWordsForConceptOverlay(agent, ivcCo,
					new ArrayList<String>(), true);
			for (SimpleEntry<String, Double> wordValue : words) {
				VrblzCandidateInstance ivc = new VrblzCandidateInstance(agent, IVCType.Simple, instance, ivcCo, vi, vip,
						wordValue.getKey(), wordValue.getValue());
				retval.add(ivc);
			}

		}
		return retval;
	}

	/**
	 * Score the candidate verbalization instances
	 * 
	 * FIXME this must be preference based
	 * 
	 * 
	 * @param agent
	 * @param vi
	 * @param vip
	 * @return
	 */
	private static double scoreVrblzCandidateInstance(VrblzCandidateInstance ivc, Agent agent, VerbInstance vi,
			ViPart vip) {
		//
		// Start with the score match
		//
		double retval = ivc.getScoreMatch();
		Instance instance = (Instance) vi.getPart(vip);
		// if it does not resolve this, it is zero
		if (!ivc.resolves(agent, instance.getScene())) {
			return Double.NEGATIVE_INFINITY;
		}
		// if you cannot verbalize it, don't consider it
		if (ivc.getVerbalization(agent) == null) {
			return Double.NEGATIVE_INFINITY;
		}
		//
		// add the 1 / total area to the score - favoring the references with
		// smaller area
		//
		double totalArea = 0;
		for (SimpleEntry<Concept, Double> entry : ivc.getCo().getList()) {
			totalArea += agent.getConceptDB().getArea(entry.getKey());
		}
		retval = retval + 1 / totalArea;
		//
		// penalize the ones which have negation in them
		//
		if (ivc.getVerbalization(agent).contains(Hardwired.CONCEPT_PREFIX_NEGATION)) {
			retval = retval / 2;
		}
		return retval;
	}

	/**
	 * Creates the verbalization of an instance in the context of a VI
	 * 
	 * Create a list of InstanceVerbalizationCandidates, score them, then pick
	 * the best
	 * 
	 * @param agent
	 * @param vi
	 *            - the VI in the context of which the verbalization will happen
	 * @param instance
	 *            - the instance to be verbalized
	 * @return
	 */
	public static String verbalizeInstance(Agent agent, VerbInstance vi, ViPart vip) {
		Instance instance = (Instance) vi.getPart(vip);
		// handle the case of a group, by verbalizing as if they would be alone
		// in that position
		if (GroupHelper.decideGroup(instance, agent)) {
			Set<Instance> members = GroupHelper.getMembersOfGroup(agent, instance);
			String tmp = "";
			if (!members.isEmpty()) {
				for (Instance member : members) {
					// FIXME: this is a particular case where this is not used
					// to create
					// a ViTemplate
					VerbInstance vx = VerbInstance.createViTemplateFromModel(vi);
					vx.setResolvedPart(vip, member);
					String vrblzMember = VrblzInstance.verbalizeInstance(agent, vx, vip);
					tmp += vrblzMember + " + ";
				}
				tmp = tmp.substring(0, tmp.length() - " + ".length());
			} else {
				tmp = "group-with-no-members";
			}
			return tmp;
		}
		List<VrblzCandidateInstance> ivcs = VrblzInstance.generateCandidates(agent, vi, vip);
		List<SimpleEntry<VrblzCandidateInstance, Double>> scoredIVCs = new ArrayList<>();
		for (VrblzCandidateInstance ivc : ivcs) {
			double score = VrblzInstance.scoreVrblzCandidateInstance(ivc, agent, vi, vip);
			if (score > 0) {
				scoredIVCs.add(new SimpleEntry<>(ivc, score));
			}
		}
		// verbalization failed - fail back to a full adjective dump.
		// FIXME in future versions fall back to relational etc.
		if (scoredIVCs.isEmpty()) {
			String retval = VrblzAdjective.verbalizeAdjective(agent, instance.getConcepts(), false);
			return retval;
		}
		Collections.sort(scoredIVCs, new SimpleEntryComparator<VrblzCandidateInstance>());
		Collections.reverse(scoredIVCs);
		VrblzCandidateInstance ivcChosen = scoredIVCs.get(0).getKey();
		return ivcChosen.getVerbalization(agent);
	}

	/**
	 * 
	 * Verbalizes a scene (eg in the qoute or other ones).
	 * 
	 * FIXME: right now I will make it fall back to verbalize instance, but this
	 * needs to be slightly different, for instance due to the resolution model.
	 * 
	 * @param agent
	 * @param vi
	 * @param obji
	 * @return
	 */
	public static String verbalizeScene(Agent agent, VerbInstance vi, ViPart vip) {
		return VrblzInstance.verbalizeInstance(agent, vi, vip);
	}
}
