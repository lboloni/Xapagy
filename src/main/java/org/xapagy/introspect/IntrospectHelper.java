package org.xapagy.introspect;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storyline.StoryLine;
import org.xapagy.debug.storyline.StoryLineRepository;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.util.SimpleEntryComparator;
import org.xapagy.verbalize.VerbalizeVo;
import org.xapagy.verbalize.VrblzAdjective;

/**
 * A number of calls helping the Introspect ones. These return internal
 * structures
 * 
 * @author lboloni
 *
 */
public class IntrospectHelper {

	/**
	 * Returns the current storyline of the Xapagy agent - note that this
	 * function tacitly assumes that there is a single story line...
	 * 
	 * @param agent
	 * @return
	 */
	public static StoryLine getCurrentStoryLine(Agent agent) {
		VerbInstance lastVi = agent.getLastVerbInstance();
		List<VerbInstance> vis = new ArrayList<>();
		vis.add(lastVi);
		List<StoryLine> stls = StoryLineRepository.createStoryLines(agent, vis);
		StoryLine stl = stls.get(0); // normally, it cannot be more than one
		return stl;
	}

	public static StoryLine getStrongestShadowStoryLine(Agent agent, StoryLine st, String ec) {
		List<SimpleEntry<StoryLine, Double>> entries = getShadowStoryLines(agent, st, ec);
		return entries.get(0).getKey();
	}

	/**
	 * Returns the story lines from the shadows which map to the specified
	 * current story line. The resulting story lines are annotated with a
	 * specific metric.
	 * 
	 * FIXME: the current implementation does not take into account the current
	 * storyline, only looks for the strongest shadows
	 * 
	 * @param agent
	 * @param st
	 *            - the
	 * @param ec
	 *            - normally SHV_GENERIC
	 * @return
	 */
	public static List<SimpleEntry<StoryLine, Double>> getShadowStoryLines(Agent agent, StoryLine st, String ec) {
		Shadows sh = agent.getShadows();
		//
		// Extract all the story lines from the shadows
		//
		List<VerbInstance> vis = new ArrayList<>();
		for (VerbInstance vi : agent.getFocus().getViListAllEnergies()) {
			vis.addAll(sh.getMembers(vi, ec));
		}
		List<StoryLine> stlsShadows = StoryLineRepository.createStoryLines(agent, vis);
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
	 * Returns a mapping between the instances based on their shadow levels.
	 * 
	 * FIXME: Note that this could be improved by taking into consideration
	 * identity relations
	 * 
	 * @param agent
	 * @param fstl
	 *            - a storyline (normally, the one that is in the focus)
	 * @param shtl
	 *            - a storyline (normally, one that is in the shadows)
	 * @param ec
	 *            - the energy color based on which the mapping is done
	 * @return
	 */
	public static Map<Instance, Instance> getMapping(Agent agent, StoryLine fstl, StoryLine shtl, String ec) {
		Shadows sh = agent.getShadows();
		Map<Instance, Instance> retval = new HashMap<>();
		for (Instance scene : fstl.getScenes()) {
			for (Instance fi : scene.getSceneMembers()) {
				Instance mapped = null;
				// find the strongest
				List<Instance> members = sh.getMembers(fi, ec);
				for (Instance inst : members) {
					if (shtl.contains(inst)) {
						mapped = inst;
						break;
					}
				}
				retval.put(fi, mapped);
			}
		}
		return retval;
	}

	/**
	 * Finds an index in the list of VIs in the second storyline that
	 * corresponds to the last VI of the current storyline. This allows us to
	 * determine where we start the prediction.
	 * 
	 * FIXME: this is really naive at this moment, based on the shadow of the
	 * last VI
	 * 
	 * @param agent
	 * @param stl
	 *            - the current storyline, normally the one in the focus. The
	 *            assumption here is that this is a shorter, incomplete
	 *            storyline
	 * @param shstl
	 *            -the shadow storyline, normally the one in the shadow (a
	 *            strong one)
	 * @param ec
	 *            - the energy color based on which the mapping is done -
	 *            probably SHV_GENERIC
	 * @return
	 */
	public static int findPositionOfCurrentStoryLine(Agent agent, StoryLine stl, StoryLine shstl, String ec) {
		VerbInstance viLast = stl.getVis().get(stl.getVis().size() - 1);
		Shadows sh = agent.getShadows();
		List<VerbInstance> members = sh.getMembers(viLast, ec);
		VerbInstance match = null;
		for (VerbInstance vi : members) {
			if (shstl.contains(vi)) {
				match = vi;
				break;
			}
		}
		int retval = -1;
		int count = 0;
		for (VerbInstance vi : shstl.getVis()) {
			if (vi == match) {
				retval = count;
			}
			count++;
		}
		return retval;
	}

	/**
	 * Performs a single story line prediction (returns Xapi...)
	 * 
	 * @param agent
	 * @param stl
	 *            - the current story line, at least partially in the focus
	 * @param shstl
	 *            - the strongest shadow story line, based on which we are doing
	 *            the prediction
	 * @param ec
	 *            - the energy color based on which we are doing the prediction
	 *            (normally SHV_GENERIC)
	 * @return
	 */
	public static List<String> singleStoryLinePrediction(Agent agent, StoryLine stl, StoryLine shstl, String ec) {
		int location = findPositionOfCurrentStoryLine(agent, stl, shstl, ec);
		Map<Instance, Instance> mapping = getMapping(agent, stl, shstl, ec);
		List<String> prediction = new ArrayList<>();
		if (location == -1) {
			TextUi.println("The current story is at an end");
			return prediction;
		}
		List<VerbInstance> vis = shstl.getVis();
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
}
