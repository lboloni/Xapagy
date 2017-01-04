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
package org.xapagy.storyline;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.smartprint.XapiPrint;
import org.xapagy.util.SimpleEntryComparator;

public class StoryLineReasoning {

	/**
	 * Second experiment for a renarration stuff. 
	 * 
	 * All the instances are created on-demand, but in the same scene.
	 * 
	 * @param agent
	 *            - the agent
	 * @param narrative
	 *            - the narrative, which is a collection of VIs selected from
	 *            the original story line
	 * @param sline
	 *            - the original story line, now likely in the AM and likely in
	 *            the shadows
	 * @param f2sInstanceMap
	 * @param ec
	 * @return
	 */
	public static List<VerbInstance> createRenarrate(Agent agent, List<VerbInstance> narrative, StoryLine sline) {
		List<VerbInstance> retval = new ArrayList<>();
		slrMapping mapping = new slrMapping(agent);
		// Let us go with the assumption that the current scene is the mapping for the first scene in the narrative.
		//VerbInstance svifirst = narrative.get(0);		
		//mapping.putSceneMap(agent.getFocus().getCurrentScene(), svifirst.getSubject().getScene());
		
		// iterate over the narrative
		for (VerbInstance svi : narrative) {
			// don't recall the relations, let the creation do it
			if (ViClassifier.decideViClass(ViClass.RELATION, svi, agent)) {
				// we do create the group member relations, cause they are different
				if (!Hardwired.contains(agent, svi.getVerbs(), Hardwired.VR_MEMBER_OF_GROUP)) {
					continue;					
				}
			}
			renarrateVi(agent, svi, mapping);
		}
		return retval;
	}

	
	/**
	 * Renarrates a particular instance from the shadows into the focus. It uses the 
	 * slrMapping to do instance mappings
	 * 
	 * @param agent
	 * @param svi
	 * @param mapping
	 * @return
	 */
	public static List<VerbInstance> renarrateVi(Agent agent, VerbInstance svi, slrMapping mapping) {
		List<VerbInstance> retval = new ArrayList<>();
		// check whether we have all the instances
		for (ViPart vip : ViStructureHelper.getAllowedInstanceParts(svi.getViType())) {
			Instance inst = (Instance) svi.getPart(vip);
			// check whether we have the regular instance, and if not, create it
			if (!inst.isScene()) {
				if (!mapping.getS2fInstanceMap().containsKey(inst)) {
					// create the instance
					List<VerbInstance> vis = renarrateInstance(agent, inst, mapping);
					retval.addAll(vis);
				}
			}
			// This is the case when we first encounter the reference to this scene as a direct 
			// part of the VI
			if (inst.isScene()) {
				if (!mapping.getS2fSceneMap().containsKey(inst)) {
					List<VerbInstance> vis = renarrateScene(agent, inst, mapping);
					retval.addAll(vis);
				}
			}			
		}
		// at this moment, we are supposed to have the instances 
		VerbInstance fvi = createFocusPair(agent, svi, mapping);
		if (fvi != null) {
			retval.add(fvi);
			agent.getLoop().proceedOneForcedStep(fvi, 1.0);
		}
		return retval;
	}
	
	
	/**
	 * Creates a focus instance corresponding to a shadow instance si
	 * 
	 * @param agent
	 * @param si
	 * @param mapping
	 * @return
	 */
	public static List<VerbInstance> renarrateInstance(Agent agent, Instance si, slrMapping mapping) {
		List<VerbInstance> retval = new ArrayList<>();
		VerbOverlay verbs = VerbOverlay.createVO(agent, Hardwired.VM_CREATE_INSTANCE);
		VerbInstance viTemplate = VerbInstance.createViTemplate(agent, ViType.S_ADJ, verbs);
		Instance scene = mapping.getS2fSceneMap().get(si.getScene());
		// if the scene is a different one, call it
		if (scene == null) {
			List<VerbInstance> vis = renarrateScene(agent, si.getScene(), mapping);
			retval.addAll(vis);
			scene = mapping.getS2fSceneMap().get(si.getScene());
		}
		viTemplate.setSubject(scene);
		ConceptOverlay co = new ConceptOverlay(agent);
		co.addOverlay(si.getConcepts());
		viTemplate.setAdjective(co);
		// now the template is complete, let us execute it
		agent.getLoop().proceedOneForcedStep(viTemplate, 1.0);
		retval.add(viTemplate);
		Instance fi = viTemplate.getCreatedInstance();
		mapping.putInstanceMap(fi, si);
		return retval;
	}
	
	
	
	/**
	 * Creates a focus scene corresponding to the shadow scene sscene
	 * FIXME: what about labels???
	 * 
	 * @param agent
	 * @param sscene
	 * @param mapping
	 * @return
	 */
	public static List<VerbInstance> renarrateScene(Agent agent, Instance sscene, slrMapping mapping) {
		List<VerbInstance> retval = new ArrayList<>();
		// create the scene
		String label = sscene.getConcepts().getLabels().get(0);
		String newlabel = "#renarrated_" + Formatter.fmt(agent.getTime()) + "_" + label.substring(1);
		VerbOverlay verbs = VerbOverlay.createVO(agent, Hardwired.VM_CREATE_INSTANCE);
		VerbInstance viTemplate = VerbInstance.createViTemplate(agent, ViType.S_ADJ, verbs);
		Instance scene = agent.getFocus().getCurrentScene();
		viTemplate.setSubject(scene);
		ConceptOverlay co = new ConceptOverlay(agent);
		co.addOverlay(sscene.getConcepts()); // this should include "scene"
		// FIXME: This creates a null pointer exception in 
		// at org.xapagy.ui.prettygraphviz.GraphVizHelper.formatIdentifier(GraphVizHelper.java:138)
		// at org.xapagy.ui.prettygraphviz.GvFormatter.openNode(GvFormatter.java:165)
		co.addFullLabel(newlabel, agent);
		// co.addFullLabel("#renarrate", agent);
		viTemplate.setAdjective(co);
		// now the template is complete, let us execute it
		agent.getLoop().proceedOneForcedStep(viTemplate, 1.0);
		retval.add(viTemplate);
		Instance fscene = viTemplate.getCreatedInstance();
		mapping.putSceneMap(fscene, sscene);				
		return retval;
	}
	
	
	
	/**
	 * First experiment for a renarration stuff. What it does at this moment, is
	 * that it creates all the instances in the current scene, then returns the
	 * action VIs
	 * 
	 * @param agent
	 *            - the agent
	 * @param narrative
	 *            - the narrative, which is a collection of VIs selected from
	 *            the original story line
	 * @param sline
	 *            - the original story line, now likely in the AM and likely in
	 *            the shadows
	 * @param f2sInstanceMap
	 * @param ec
	 * @return
	 */
	public static List<VerbInstance> createRenarrateInstancesFirst(Agent agent, List<VerbInstance> narrative,
			StoryLine sline) {
		List<VerbInstance> retval = new ArrayList<>();
		slrMapping mapping = new slrMapping(agent);
		// collect all the instances in the narrative
		Set<Instance> instances = new HashSet<>();
		for (VerbInstance svi : narrative) {
			for (ViPart vip : ViStructureHelper.getAllowedInstanceParts(svi.getViType())) {
				Instance inst = (Instance) svi.getPart(vip);
				if (!inst.isScene()) {
					instances.add(inst);
				}
			}
		}
		// now create all those instances and fill in the f2s and s2f instance
		// maps
		// assume that all these are going to be in the current scene
		for (Instance inst : instances) {
			VerbOverlay verbs = VerbOverlay.createVO(agent, Hardwired.VM_CREATE_INSTANCE);
			VerbInstance viTemplate = VerbInstance.createViTemplate(agent, ViType.S_ADJ, verbs);
			viTemplate.setSubject(agent.getFocus().getCurrentScene());
			ConceptOverlay co = new ConceptOverlay(agent);
			co.addOverlay(inst.getConcepts());
			viTemplate.setAdjective(co);
			// now the template is complete, let us execute it
			agent.getLoop().proceedOneForcedStep(viTemplate, 1.0);
			Instance fi = viTemplate.getCreatedInstance();
			mapping.putInstanceMap(fi, inst);
		}

		// now create the narrative
		for (VerbInstance svi : narrative) {
			VerbInstance fvi = createFocusPair(agent, svi, mapping);
			if (fvi != null) {
				retval.add(fvi);
				agent.getLoop().proceedOneForcedStep(fvi, 1.0);
			}
			// maybe if it is null break???
		}
		return retval;
	}

	/**
	 * Selects VIs from a story line for re-narration. Returns a list of VIs
	 * which are from the original story line.
	 * 
	 * @param the
	 *            agent
	 * @param stl
	 *            - the story line we want to renarrate
	 * @param kind
	 *            - the filtering we want to apply for the renarration
	 * @return
	 */
	public static List<VerbInstance> selectForRenarration(Agent agent, StoryLine sline, String kind) {
		List<VerbInstance> vis = sline.getVis();
		List<VerbInstance> retval = new ArrayList<>();
		switch (kind) {
		case "all": {
			for (VerbInstance vi : vis) {
				retval.add(vi);
			}
			return retval;
		}
		case "0": {
			for (VerbInstance vi : vis) {
				if (vi.getSummarizationLevel() == 0)
					retval.add(vi);
			}
			return retval;
		}
		case "1": {
			for (VerbInstance vi : vis) {
				if (vi.getSummarizationLevel() == 1)
					retval.add(vi);
			}
			return retval;
		}
		case "2": {
			for (VerbInstance vi : vis) {
				if (vi.getSummarizationLevel() == 2)
					retval.add(vi);
			}
			return retval;
		}
		case "3": {
			for (VerbInstance vi : vis) {
				if (vi.getSummarizationLevel() >= 3)
					retval.add(vi);
			}
			return retval;
		}

		}
		return retval;
	}

	/**
	 * Returns the story lines associated with the last VI processed
	 * 
	 * @param agent
	 * @return
	 */
	public static StoryLine getCurrentStoryLine(Agent agent) {
		VerbInstance lastVi = agent.getLastVerbInstance();
		List<VerbInstance> vis = new ArrayList<>();
		vis.add(lastVi);
		List<StoryLine> stls = StoryLineReasoning.createStoryLines(agent, vis);
		StoryLine stl = stls.get(0); // normally, it cannot be more than one
		return stl;
	}

	/**
	 * Returns the story lines from the shadows which map to the specified
	 * in-focus story line. The resulting story lines are paired with a metric
	 * that shows their strength with regards to the current story line.
	 * 
	 * FIXME: the current implementation does not take into account the current
	 * storyline, only looks for the strongest shadows
	 * 
	 * @param agent
	 * @param fline
	 *            - the in-focus story line whose shadows we are looking at
	 * @param ec
	 *            - the energy color based on which we are making the matching,
	 *            normally SHV_GENERIC
	 * @return
	 */
	public static List<SimpleEntry<StoryLine, Double>> createShadowStoryLines(Agent agent, StoryLine fline, String ec) {
		Shadows sh = agent.getShadows();
		//
		// Extract all the story lines from the shadows
		//
		List<VerbInstance> vis = new ArrayList<>();
		for (VerbInstance vi : agent.getFocus().getViListAllEnergies()) {
			vis.addAll(sh.getMembers(vi, ec));
		}
		List<StoryLine> stlsShadows = StoryLineReasoning.createStoryLines(agent, vis);
		//
		// for all story lines, calculate a metric that looks at their energies
		//
		List<SimpleEntry<StoryLine, Double>> entries = new ArrayList<>();
		for (StoryLine stl : stlsShadows) {
			double metric = 0;
			for (VerbInstance vi : stl.getVis()) {
				ViSet viset = sh.getReverseShadow(vi, ec);
				List<SimpleEntry<VerbInstance, Double>> list = viset.getDecreasingStrengthList();
				if (!list.isEmpty()) {
					metric += list.get(0).getValue();
				}
			}
			SimpleEntry<StoryLine, Double> entry = new SimpleEntry<>(stl, metric);
			entries.add(entry);
		}
		// return the story line with the best metric
		Collections.sort(entries, new SimpleEntryComparator<StoryLine>());
		Collections.reverse(entries);
		return entries;
	}

	/**
	 * Returns the strongest shadow story line (based on which we are going to
	 * do a prediction normally
	 * 
	 * @param agent
	 * @param fline
	 *            - the in-focus story line whose shadows we are normally
	 *            looking at
	 * @param ec
	 * @return
	 */
	public static StoryLine getStrongestShadowStoryLine(Agent agent, StoryLine fline) {
		List<SimpleEntry<StoryLine, Double>> entries = createShadowStoryLines(agent, fline, EnergyColors.SHV_GENERIC);
		return entries.get(0).getKey();
	}

	/**
	 * Considers two story lines, one in the focus fstl and one in the shadow.
	 * For each instance in the story first story line, returns a sorted list of
	 * possible mappings to instances of the second storyline (based on the
	 * shadows). If it cannot find a mapping, this list will be empty.
	 * 
	 * There are several challenges with this approach.
	 * 
	 * <ul>
	 * <li>Expired components in longer interconnected components will not
	 * retain their mappings.
	 * <li>The strongest mappings might not be unique...
	 * </ul>
	 * 
	 * @param agent
	 * @param fline
	 *            - a storyline (normally, the one that is in the focus)
	 * @param sline
	 *            - a storyline (normally, one that is in the shadows)
	 * @param ec
	 *            - the energy color based on which the mapping is done -
	 *            normally SHI_GENERIC
	 * @return
	 */
	public static Map<Instance, List<SimpleEntry<Instance, Double>>> getInstanceMappingPossibilities(Agent agent,
			StoryLine fline, StoryLine sline, String ec) {
		Shadows sh = agent.getShadows();
		Map<Instance, List<SimpleEntry<Instance, Double>>> retval = new HashMap<>();
		for (Instance scene : fline.getScenes()) {
			for (Instance fi : scene.getSceneMembers()) {
				List<SimpleEntry<Instance, Double>> mappings = new ArrayList<>();
				retval.put(fi, mappings);
				List<Instance> members = sh.getMembers(fi, ec);
				for (Instance si : members) {
					if (sline.contains(si)) {
						double salience = sh.getSalience(fi, si, ec);
						SimpleEntry<Instance, Double> entry = new SimpleEntry<>(si, salience);
						mappings.add(entry);
					}
				}
			}
		}
		return retval;
	}

	/**
	 * Returns one likely instance mapping between in-focus storyline fstl and
	 * in-shadow storyline shtl. Uses getInstanceMappingPossibilities, and tries
	 * to extract the strongest unique mapping (i.e. there is no shadow instance
	 * that is attached to more than one current instance).
	 * 
	 * Note, by the way, that such non-unique mappings are not necessarily
	 * useless, we are just not considering them here.
	 * 
	 * @param agent
	 * @param fline
	 * @param sline
	 * @param ec
	 * @return
	 */
	public static slrMapping getLikelyInstanceMapping(Agent agent, StoryLine fline, StoryLine sline, String ec) {
		slrMapping retval = new slrMapping(agent);
		Map<Instance, List<SimpleEntry<Instance, Double>>> imp = getInstanceMappingPossibilities(agent, fline, sline,
				ec);
		while (!imp.isEmpty()) {
			// find the strongest not assigned one
			double max = -1.0;
			Instance maxFi = null;
			Instance maxSi = null;
			for (Instance fi : imp.keySet()) {
				List<SimpleEntry<Instance, Double>> list = imp.get(fi);
				Instance si = null;
				double val = 0;
				if (!list.isEmpty()) {
					si = list.get(0).getKey();
					val = list.get(0).getValue();
				}
				if (val > max) {
					max = val;
					maxFi = fi;
					maxSi = si;
				}
			}
			// ok, so we know the max, let us add it the retval, and clean all
			// the maxSi from the other lists
			retval.putInstanceMap(maxFi, maxSi);
			imp.remove(maxFi);
			for (Instance fi : imp.keySet()) {
				List<SimpleEntry<Instance, Double>> list = imp.get(fi);
				list.remove(maxSi);
			}
		}
		return retval;
	}

	/**
	 * Considers two story lines, one in the focus fstl and one in the shadow.
	 * For each VI in the story first story line, returns a sorted list of
	 * possible mappings to VIs of the second storyline (based on the shadows).
	 * If it cannot find a mapping, this list will be empty.
	 * 
	 * There are several challenges with this approach.
	 * 
	 * <ul>
	 * <li>Expired components in longer interconnected components will not
	 * retain their mappings.
	 * <li>The strongest mappings might not be unique...
	 * </ul>
	 * 
	 * @param agent
	 * @param fline
	 *            - a storyline (normally, the one that is in the focus)
	 * @param sline
	 *            - a storyline (normally, one that is in the shadows)
	 * @param ec
	 *            - the energy color based on which the mapping is done -
	 *            normally SHV_GENERIC
	 * @return
	 */
	public static Map<VerbInstance, List<SimpleEntry<VerbInstance, Double>>> getViMappingPossibilities(Agent agent,
			StoryLine fline, StoryLine sline, String ec) {
		Shadows sh = agent.getShadows();
		Set<VerbInstance> slineMembers = new HashSet<>(sline.getVis());
		Map<VerbInstance, List<SimpleEntry<VerbInstance, Double>>> retval = new HashMap<>();
		for (VerbInstance fvi : fline.getVis()) {
			List<SimpleEntry<VerbInstance, Double>> mappings = new ArrayList<>();
			retval.put(fvi, mappings);
			List<VerbInstance> members = sh.getMembers(fvi, ec);
			for (VerbInstance svi : members) {
				if (slineMembers.contains(svi)) {
					double salience = sh.getSalience(fvi, svi, ec);
					SimpleEntry<VerbInstance, Double> entry = new SimpleEntry<>(svi, salience);
					mappings.add(entry);
				}
			}
		}
		return retval;
	}

	/**
	 * Returns one likely instance mapping between in-focus storyline fstl and
	 * in-shadow storyline shtl. Uses getViMappingPossibilities, and tries to
	 * extract the strongest unique mapping (i.e. there is no shadow VI that is
	 * attached to more than one current VI).
	 * 
	 * Note, by the way, that such non-unique mappings are not necessarily
	 * useless, we are just not considering them here.
	 * 
	 * @param agent
	 * @param fline
	 * @param sline
	 * @param ec
	 * @return
	 */
	public static Map<VerbInstance, VerbInstance> getLikelyViMapping(Agent agent, StoryLine fline, StoryLine sline,
			String ec) {
		Map<VerbInstance, VerbInstance> retval = new HashMap<>();
		Map<VerbInstance, List<SimpleEntry<VerbInstance, Double>>> imp = getViMappingPossibilities(agent, fline, sline,
				ec);
		while (!imp.isEmpty()) {
			// find the strongest not assigned one
			double max = -1.0;
			VerbInstance maxFi = null;
			VerbInstance maxSi = null;
			for (VerbInstance fvi : imp.keySet()) {
				List<SimpleEntry<VerbInstance, Double>> list = imp.get(fvi);
				VerbInstance svi = null;
				double val = 0;
				if (!list.isEmpty()) {
					svi = list.get(0).getKey();
					val = list.get(0).getValue();
				}
				if (val > max) {
					max = val;
					maxFi = fvi;
					maxSi = svi;
				}
			}
			// ok, so we know the max, let us add it the retval, and clean all
			// the maxSi from the other lists
			retval.put(maxFi, maxSi);
			imp.remove(maxFi);
			for (VerbInstance fi : imp.keySet()) {
				List<SimpleEntry<VerbInstance, Double>> list = imp.get(fi);
				list.remove(maxSi);
			}
		}
		return retval;
	}

	/**
	 * Finds an index in the list of VIs in the shadow storyline that
	 * corresponds to the last VI of the in-focus storyline. This allows us to
	 * determine where we start the prediction.
	 * 
	 * FIXME: this is really naive at this moment, based on the shadow of the
	 * last VI
	 * 
	 * @param agent
	 * @param fline
	 *            - the current storyline, normally the one in the focus. The
	 *            assumption here is that this is a shorter, incomplete
	 *            storyline
	 * @param sline
	 *            -the shadow storyline, normally the one in the shadow (a
	 *            strong one)
	 * @param ec
	 *            - the energy color based on which the mapping is done -
	 *            probably SHV_GENERIC
	 * @return
	 */
	public static int findPositionOfCurrentStoryLine(Agent agent, StoryLine fline, StoryLine sline, String ec) {
		VerbInstance viLast = fline.getVis().get(fline.getVis().size() - 1);
		Shadows sh = agent.getShadows();
		List<VerbInstance> members = sh.getMembers(viLast, ec);
		Set<VerbInstance> slineMembers = new HashSet<>(sline.getVis());
		VerbInstance match = null;
		for (VerbInstance vi : members) {
			if (slineMembers.contains(vi)) {
				match = vi;
				break;
			}
		}
		int retval = -1;
		int count = 0;
		for (VerbInstance vi : sline.getVis()) {
			if (vi == match) {
				retval = count;
			}
			count++;
		}
		return retval;
	}

	/**
	 * Helper function for createStoryLines. Takes a set of scenes. Then for
	 * each of the scenes, adds to the set all the scenes which are related to
	 * it by sucession, fictional future or view relationship
	 * 
	 * @return
	 */
	private static Set<Instance> explodeRelatedScenes(Agent agent, Set<Instance> scenes) {
		Set<Instance> retval = new HashSet<>();
		retval.addAll(scenes);
		while (true) {
			int initSize = retval.size();
			Set<Instance> set = new HashSet<>(retval);
			for (Instance scene : retval) {
				Set<Instance> addOn = null;
				// add the succession ones
				addOn = RelationHelper.getRelationSpecificPart(agent, Hardwired.VR_SCENE_SUCCESSION, scene,
						ViPart.Object, ViPart.Subject);
				set.addAll(addOn);
				addOn = RelationHelper.getRelationSpecificPart(agent, Hardwired.VR_SCENE_SUCCESSION, scene,
						ViPart.Subject, ViPart.Object);
				set.addAll(addOn);
				// add the fictional-future
				addOn = RelationHelper.getRelationSpecificPart(agent, Hardwired.VR_SCENE_FICTIONAL_FUTURE, scene,
						ViPart.Object, ViPart.Subject);
				set.addAll(addOn);
				addOn = RelationHelper.getRelationSpecificPart(agent, Hardwired.VR_SCENE_FICTIONAL_FUTURE, scene,
						ViPart.Subject, ViPart.Object);
				set.addAll(addOn);
				// add the view
				addOn = RelationHelper.getRelationSpecificPart(agent, Hardwired.VR_SCENE_VIEW, scene, ViPart.Object,
						ViPart.Subject);
				set.addAll(addOn);
				addOn = RelationHelper.getRelationSpecificPart(agent, Hardwired.VR_SCENE_VIEW, scene, ViPart.Subject,
						ViPart.Object);
				set.addAll(addOn);
			}
			// iterate until we are not adding anything any more
			if (set.size() == initSize) {
				break;
			}
			retval = set;
		}
		return retval;
	}

	/**
	 * Extract a list of story lines from a set of VIs. The result will be at
	 * least one story line, but it might be multiple ones if the specified VIs
	 * are not part of the same story line.
	 * 
	 * @param agent
	 * @param vis
	 * @return
	 */
	public static List<StoryLine> createStoryLines(Agent agent, List<VerbInstance> vis) {
		List<StoryLine> retval = new ArrayList<>();
		List<VerbInstance> vis2 = new ArrayList<>(vis);
		while (!vis2.isEmpty()) {
			// initialize the components of the scene
			List<VerbInstance> stVis = new ArrayList<>();
			Set<Instance> stScenes = new HashSet<>();
			VerbInstance starter = vis2.get(0);
			vis2.remove(0);
			stVis.add(starter);
			stScenes.addAll(starter.getReferencedScenes());
			stScenes = explodeRelatedScenes(agent, stScenes);
			for (Iterator<VerbInstance> vit = vis2.iterator(); vit.hasNext();) {
				VerbInstance current = vit.next();
				Set<Instance> test = new HashSet<>(stScenes);
				test.retainAll(current.getReferencedScenes());
				if (test.isEmpty()) {
					continue;
				}
				// add the vi, and the
				stVis.add(current);
				stScenes.addAll(current.getReferencedScenes());
				stScenes = explodeRelatedScenes(agent, stScenes);
				vit.remove();
			}
			// create the story line from the scenes sorted by time
			List<Instance> scenes = new ArrayList<>(stScenes);
			Collections.sort(scenes, new Comparator<Instance>() {

				@Override
				public int compare(Instance o1, Instance o2) {
					return Double.compare(o1.getCreationTime(), o2.getCreationTime());
				}
			});
			StoryLine st = new StoryLine("storyline", scenes);
			retval.add(st);
		}
		return retval;
	}

	/**
	 * Creates a focus pair
	 * 
	 * @param agent
	 * @param svi
	 * @param s2fInstanceMap
	 * @return
	 */
	public static VerbInstance createFocusPair(Agent agent, VerbInstance svi, slrMapping mapping) {
		ViType type = svi.getViType();
		VerbInstance viTemplate = VerbInstance.createViTemplate(agent, svi.getViType(), svi.getVerbs());
		// resolve the instances
		for (ViPart part : ViStructureHelper.getAllowedInstanceParts(type)) {
			Instance si = (Instance) svi.getPart(part);
			Instance fi = null;
			if (!si.isScene()) {
				fi = mapping.getS2fInstanceMap().get(si);
			} else {
				fi = mapping.getS2fSceneMap().get(si);
			}
			if (fi == null) {
				// throw new Error("createFocusPair - cannot resolve what is in
				// the current focus for:"
				// + SpInstance.spc(si, agent));
				TextUi.println("When making prediction based on " + XapiPrint.ppsViXapiForm(svi, agent));
				TextUi.println("createFocusPair - cannot resolve what is in the current focus for:"
						+ SpInstance.spc(si, agent) + " this might be due to a change??");
				TextUi.println("Not generating prediction");
				return null;
			}
			viTemplate.setResolvedPart(part, fi);
		}
		// if this one is an adjective, copy over the adjective
		if (type.equals(ViType.S_ADJ)) {
			ConceptOverlay co = new ConceptOverlay(agent);
			co.addOverlay(svi.getAdjective());
			viTemplate.setResolvedPart(ViPart.Adjective, co);
		}
		// finally, if this is a quote, recurse
		if (type == ViType.QUOTE) {
			VerbInstance fqoute = createFocusPair(agent, svi.getQuote(), mapping);
			viTemplate.setResolvedPart(ViPart.Quote, fqoute);
		}
		return VerbInstance.createViFromResolvedTemplate(agent, viTemplate);
	}

	/**
	 * Utility function for creating the most likely prediction
	 * 
	 * @param agent
	 * @return
	 */
	public static List<VerbInstance> createMostLikelyPrediction(Agent agent) {
		StoryLine fline = getCurrentStoryLine(agent);
		StoryLine sline = createShadowStoryLines(agent, fline, EnergyColors.SHV_GENERIC).get(0).getKey();
		slrMapping slrm = getLikelyInstanceMapping(agent, fline, sline, EnergyColors.SHI_GENERIC);
		return StoryLineReasoning.createPrediction(agent, fline, sline, slrm, EnergyColors.SHV_GENERIC);
	}

	/**
	 * Utility function for creating the most likely completion
	 * 
	 * Returns a list of verbs that contains both the current storyline as well
	 * as the completion VIs inserted in the appropriate locations
	 * 
	 * @param agent
	 * @return
	 */
	public static List<VerbInstance> createMostLikelyCompletion(Agent agent) {
		StoryLine fline = getCurrentStoryLine(agent);
		StoryLine sline = createShadowStoryLines(agent, fline, EnergyColors.SHV_GENERIC).get(0).getKey();
		slrMapping slrm = getLikelyInstanceMapping(agent, fline, sline, EnergyColors.SHI_GENERIC);
		return StoryLineReasoning.createCompletion(agent, fline, sline, slrm, EnergyColors.SHV_GENERIC);
	}

	/**
	 * Creates a prediction for a given story and sline and instance map. This
	 * can be then executed using liViBased loop items.
	 * 
	 * @param agent
	 * @param fline
	 * @param sline
	 * @param f2sInstanceMap
	 * @param ec
	 * @return
	 */
	public static List<VerbInstance> createPrediction(Agent agent, StoryLine fline, StoryLine sline, slrMapping mapping,
			String ec) {
		int location = findPositionOfCurrentStoryLine(agent, fline, sline, ec);
		List<VerbInstance> prediction = new ArrayList<>();
		if (location == -1) {
			TextUi.println("The current story is at an end");
			return prediction;
		}
		List<VerbInstance> vis = sline.getVis();
		for (int i = location; i < vis.size(); i++) {
			VerbInstance svi = vis.get(i);
			VerbInstance fi = createFocusPair(agent, svi, mapping);
			if (fi != null) {
				prediction.add(fi);
			}
			// maybe if it is null break???
		}
		return prediction;
	}

	/**
	 * Creates a completion for a given story and sline and instance map. This
	 * will contain both the new and old VIs.
	 * 
	 * Note that this only does completion based on the passed storyline and
	 * instance map.
	 * 
	 * @param agent
	 * @param fline
	 * @param sline
	 * @param f2sInstanceMap
	 * @param ec
	 * @return
	 */
	public static List<VerbInstance> createCompletion(Agent agent, StoryLine fline, StoryLine sline, slrMapping mapping,
			String ec) {
		// create the reverse maps: s2fInstanceMap and s2fViMap
		Map<VerbInstance, VerbInstance> f2sViMap = getLikelyViMapping(agent, fline, sline, ec);
		Map<VerbInstance, VerbInstance> s2fViMap = new HashMap<>();
		for (Entry<VerbInstance, VerbInstance> entry : f2sViMap.entrySet()) {
			s2fViMap.put(entry.getValue(), entry.getKey());
		}
		//
		int location = findPositionOfCurrentStoryLine(agent, fline, sline, ec);
		List<VerbInstance> completion = new ArrayList<>();
		if (location == -1) {
			TextUi.println("The current story is at an end");
			return completion;
		}
		List<VerbInstance> vis = sline.getVis();
		for (int i = 0; i < location; i++) {
			VerbInstance svi = vis.get(i);
			VerbInstance fi = s2fViMap.get(svi);
			if (fi == null) {
				fi = createFocusPair(agent, svi, mapping);
			}
			completion.add(fi);
			// maybe if it is null break???
		}
		return completion;
	}

}
