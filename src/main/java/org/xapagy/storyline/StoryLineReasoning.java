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
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.smartprint.XapiPrint;
import org.xapagy.util.SimpleEntryComparator;
import org.xapagy.verbalize.VerbalizeVo;
import org.xapagy.verbalize.VrblzAdjective;

public class StoryLineReasoning {

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
	public static Map<Instance, Instance> getLikelyInstanceMapping(Agent agent, StoryLine fline, StoryLine sline,
			String ec) {
		Map<Instance, Instance> retval = new HashMap<>();
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
			retval.put(maxFi, maxSi);
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
	 * Performs a single story line prediction
	 * 
	 * This currently returns Xapi --- this should not normally be the case!!!
	 * 
	 * @param agent
	 * @param fline
	 *            - the current story line, at least partially in the focus
	 * @param sline
	 *            - the strongest shadow story line, based on which we are doing
	 *            the prediction
	 * @param ec
	 *            - the energy color based on which we are doing the prediction
	 *            (normally SHV_GENERIC)
	 * @return
	 */
	public static List<String> singleStoryLinePrediction(Agent agent, StoryLine fline, StoryLine sline, String ec) {
		int location = findPositionOfCurrentStoryLine(agent, fline, sline, ec);
		Map<Instance, Instance> mapping = getLikelyInstanceMapping(agent, fline, sline, ec);
		List<String> prediction = new ArrayList<>();
		if (location == -1) {
			TextUi.println("The current story is at an end");
			return prediction;
		}
		List<VerbInstance> vis = sline.getVis();
		for (int i = location; i < vis.size(); i++) {
			VerbInstance old = vis.get(i);
			switch (old.getViType()) {
			case QUOTE:
				prediction.add("Quote not supported yet");
				break;
			case S_ADJ: {
				Instance mapSubject = mapping.get(old.getSubject());
				if (mapSubject == null) {
					prediction.add("A " + SpInstance.spc(old.getSubject(), agent) + " / exists.");
					// create a fictional instance here, and add it to mapping
					// mapSubject = new Instance(identifier, scene, agent);
					// mapping.put(old.getSubject(), mapSubject);
				}
				String temp = SpInstance.spc(old.getSubject(), agent);
				temp += " / " + VerbalizeVo.verbalizeVerb(agent, old.getVerbs(), old);
				temp += " / " + VrblzAdjective.verbalizeAdjective(agent, old.getAdjective(), true);
				prediction.add(temp);
				break;
			}
			case S_V:
				prediction.add("SV not supported yet");
				break;
			case S_V_O:
				Instance mapSubject = mapping.get(old.getSubject());
				if (mapSubject == null) {
					prediction.add("A " + SpInstance.spc(old.getSubject(), agent) + " / exists.");
					// create a fictional instance here, and add it to mapping
					// mapSubject = new Instance(identifier, scene, agent);
					// mapping.put(old.getSubject(), mapSubject);
				}
				String temp = SpInstance.spc(mapSubject, agent);
				temp += " / " + VerbalizeVo.verbalizeVerb(agent, old.getVerbs(), old);
				Instance mapObject = mapping.get(old.getObject());
				if (mapObject == null) {
					prediction.add("A " + SpInstance.spc(old.getObject(), agent) + " / exists.");
					// create a fictional instance here, and add it to mapping
					// mapSubject = new Instance(identifier, scene, agent);
					// mapping.put(old.getSubject(), mapSubject);
				}
				temp += " / " + SpInstance.spc(mapObject, agent);
				prediction.add(temp);
				break;
			default:
				break;
			}
		}
		return prediction;
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
	public static VerbInstance createFocusPair(Agent agent, VerbInstance svi, Map<Instance, Instance> s2fInstanceMap) {
		ViType type = svi.getViType();
		VerbInstance retval = VerbInstance.createViTemplate(agent, svi.getViType(), svi.getVerbs());
		for (ViPart part : ViStructureHelper.getAllowedInstanceParts(type)) {
			Instance si = (Instance) svi.getPart(part);
			Instance fi = s2fInstanceMap.get(si);
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
			retval.setResolvedPart(part, fi);
		}
		// finally, if this is a quote, recurse
		if (type == ViType.QUOTE) {
			VerbInstance fqoute = createFocusPair(agent, svi.getQuote(), s2fInstanceMap);
		}
		return VerbInstance.createViFromResolvedTemplate(agent, retval);
	}

	/**
	 * Creates a prediction
	 * 
	 * @param agent
	 * @param fline
	 * @param sline
	 * @return
	 */
	public static List<VerbInstance> createPrediction(Agent agent, StoryLine fline, StoryLine sline,
			Map<Instance, Instance> f2sInstanceMap, String ec) {
		// create the s2fInstanceMap
		Map<Instance, Instance> s2fInstanceMap = new HashMap<>();
		for (Entry<Instance, Instance> entry : f2sInstanceMap.entrySet()) {
			s2fInstanceMap.put(entry.getValue(), entry.getKey());
		}

		int location = findPositionOfCurrentStoryLine(agent, fline, sline, ec);
		List<VerbInstance> prediction = new ArrayList<>();
		if (location == -1) {
			TextUi.println("The current story is at an end");
			return prediction;
		}
		List<VerbInstance> vis = sline.getVis();
		for (int i = location; i < vis.size(); i++) {
			VerbInstance svi = vis.get(i);
			VerbInstance fi = createFocusPair(agent, svi, s2fInstanceMap);
			if (fi != null) {
				prediction.add(fi);
			} 
			// maybe if it is null break???
		}
		return prediction;
	}

}
