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
package org.xapagy.activity.shadowmaintenance;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Overlay;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.concepts.operations.Coverage;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViSimilarityHelper;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.InstanceSet;
import org.xapagy.set.ViSet;

/**
 * Functions for looking up instances and verb instances in the autobiographical
 * memory by indexing them with concept overlays and verb overlays respectively.
 * These functions return InstanceSet and ViSet objects, respectively, where the weights 
 * in the sets represent the strength of the match. 
 * 
 * In general, the strength of the match represents not only the matching, but also the 
 * rarity of the match. Furthermore, some of the verbs that appear too often are ignored
 * in the match.
 * 
 * @author Ladislau Boloni
 * Created on: Feb 3, 2012
 */
public class AmLookup {

	// verbs which are ignored in the lookupVi, because they appear so often
	private static Set<String> ignoredVerbs = null;

	/**
	 * Returns the list of verbs ignored in shadows
	 * 
	 * @param agent
	 * @return
	 */
	static Set<String> getVerbsIgnoredInShadowing() {
		if (AmLookup.ignoredVerbs == null) {
			AmLookup.ignoredVerbs = new HashSet<>();
			// action verbs
			AmLookup.ignoredVerbs.add(Hardwired.VM_SUCCESSOR);
			AmLookup.ignoredVerbs.add(Hardwired.VM_ACTION_MARKER);
			// other metaverbs
			AmLookup.ignoredVerbs.add(Hardwired.VM_CHANGES);
			AmLookup.ignoredVerbs.add(Hardwired.VM_CREATE_RELATION);
			AmLookup.ignoredVerbs.add(Hardwired.VM_CREATE_ONE_SOURCE_RELATION);
			AmLookup.ignoredVerbs.add(Hardwired.VM_IS_A);
			AmLookup.ignoredVerbs.add(Hardwired.VM_NARRATE);
			AmLookup.ignoredVerbs.add(Hardwired.VM_RECALL);
			AmLookup.ignoredVerbs.add(Hardwired.VM_REMOVE_RELATION);
			AmLookup.ignoredVerbs.add(Hardwired.VM_SCENE_IS_ONLY);
			AmLookup.ignoredVerbs.add(Hardwired.VM_ACTS_LIKE);
			AmLookup.ignoredVerbs.add(Hardwired.V_DOES_NOTHING);
			AmLookup.ignoredVerbs.add(Hardwired.VM_THUS);
			// relation
			AmLookup.ignoredVerbs.add(Hardwired.VM_RELATION_MARKER);
			// instance creation: FIXME: removing this might create later
			// problems
			// as we might need this for the recall create new instances...
			AmLookup.ignoredVerbs.add(Hardwired.VM_CREATE_INSTANCE);
			// subject
			AmLookup.ignoredVerbs.add(Hardwired.VR_SUBJECT_IS_SCENE);
		}
		return AmLookup.ignoredVerbs;
	}

	/**
	 * New implementation (May 2014).
	 * 
	 * Goes concept-by-concept, looks up the overlapping concepts, and scales
	 * them with their match, with the participation in the fco, with the
	 * salience in the AM, and the rarity of the matches.
	 * 
	 * FIXME: this could be accelerated, by sorting the concepts by their
	 * rarity, and if there are sufficient matches based on the more salient
	 * concepts, then cut it off.
	 * 
	 * FIXME: this could be potentially changed to an energy based lookup.
	 * Furthermore, the frequency scaling is somewhat abrupt - maybe this can be
	 * passed through a sigmoid or something.
	 * 
	 * @param fco
	 *            - a concept overlay used to do the lookup
	 * @param ec
	 *            - the energy color
	 * 
	 * @return
	 */
	public static InstanceSet lookupCo(AutobiographicalMemory am, ConceptOverlay fco, String ec) {
		InstanceSet retval = new InstanceSet();
		for (SimpleEntry<Concept, Double> entry : fco.getList()) {
			Concept c = entry.getKey();
			double conceptWeight = entry.getValue();
			ConceptOverlay co = (ConceptOverlay) fco.newOverlay();
			co.addFullEnergy(c);
			Set<Instance> instances = am.getInstancesOverlappingWithCo(co);
			if (instances.size() == 0) {
				continue;
			}
			double frequencyScale = 1.0 / (double) instances.size();
			for (Instance inst : instances) {
				double memorySalience = am.getSalience(inst, EnergyColors.AM_INSTANCE);
				double match = Coverage.scoreCoverage(inst.getConcepts(), co);
				double value = conceptWeight * frequencyScale * match * memorySalience;
				retval.change(inst, value);
			}
		}
		return retval;
	}

	/**
	 * Look up a weighted set of VIs whose verbs match the specified verb
	 * overlay.
	 * 
	 * <ul>
	 * <li>the fvo is separated into its constituent verbs
	 * <li>each verb will add its contribution separately to the resulting
	 * weighted set
	 * <li>the contribution of each verb is scaled 1 / frequency in the AM of
	 * the verb
	 * <li>the match is based on the coverage score - decreases if the
	 * </ul>
	 * 
	 * FIXME: verbs which appear too often (eg. 1000 times) will basically
	 * return trivially small values here. How are we going to handle this???
	 * 
	 * FIXME: see also lookupCo for the same problem
	 * 
	 * @param am
	 * @param fvo
	 *            - the verb overlay - normally coming from a VI in the focus
	 * @param ec
	 *            - the energy type in the autobiograhical memory (currently
	 *            only AM)
	 * @return
	 */
	static ViSet lookupVo(AutobiographicalMemory am, VerbOverlay fvo, String ec) {
		ViSet retval = new ViSet();
		for (SimpleEntry<Verb, Double> entry : fvo.getList()) {
			Verb v = entry.getKey();
			double verbWeight = entry.getValue();
			VerbOverlay voOneVerb = (VerbOverlay) fvo.newOverlay();
			voOneVerb.addFullEnergy(v);
			Set<VerbInstance> vis = am.getVisOverlappingWithVo(voOneVerb);
			if (vis.size() == 0) {
				continue;
			}
			double frequencyScale = 1.0 / (double) vis.size();
			for (VerbInstance vi : vis) {
				// FIXME: the problem here is that the memory salience of most
				// verb instances
				// is weak, so some of the tests fail
				double memorySalienceReal = am.getSalience(vi, ec);
				// TextUi.println("Memory salience real:" +
				// Formatter.fmt(memorySalienceReal));
				double memorySalience = memorySalienceReal;
				// this is basically how much of the voOneVerb is present in
				double match = Coverage.scoreCoverage(vi.getVerbs(), voOneVerb);
				double value = verbWeight * frequencyScale * match * memorySalience;
				retval.change(vi, value);
			}
		}
		return retval;
	}

	/**
	 * Looks up VIs matching a certain VI from the autobiographical memory.
	 * 
	 * Makes a VO lookup on the verbs with the metaverbs removed. Then, removes
	 * those VIs that are not compatible.
	 * 
	 * @param agent
	 * @param fvi
	 * @param ec
	 *            - the energy color
	 * @return
	 */
	public static ViSet lookupVi(Agent agent, VerbInstance fvi, String ec) {
		ViSet retval = new ViSet();
		VerbOverlay vo = fvi.getVerbs();
		VerbOverlay scraper = VerbOverlay.createVO(agent, AmLookup.getVerbsIgnoredInShadowing());
		VerbOverlay voScraped = Overlay.scrape(vo, scraper);
		if (voScraped.getList().isEmpty()) {
			return retval;
		}
		ViSet match = AmLookup.lookupVo(agent.getAutobiographicalMemory(), voScraped, ec);
		for (VerbInstance vi : match.getParticipants()) {
			if (!ViSimilarityHelper.isCompatible(agent, fvi, vi)) {
				continue;
			}
			retval.changeTo(vi, match.value(vi));
		}
		return retval;
	}
}
