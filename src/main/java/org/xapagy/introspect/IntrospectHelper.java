package org.xapagy.introspect;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
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
	 * Returns the current storyline of the Xapagy agent
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

	/**
	 * Returns the strongest story line from the shadows
	 * 
	 * 
	 * @param agent
	 * @param ec
	 * @return
	 */
	public static StoryLine getStrongestShadowStoryLine(Agent agent, String ec) {
		List<VerbInstance> vis = new ArrayList<>();
		VerbInstance lastVi = agent.getLastVerbInstance();
		vis.add(lastVi);
		// List<StoryLine> stls = StoryLineRepository.createStoryLines(agent,
		// vis);
		// StoryLine stl = stls.get(0); // normally, it cannot be more than one
		// get all the shadow story lines
		vis = new ArrayList<>();
		Shadows sh = agent.getShadows();
		// extract all the storylines in the shadows
		for (VerbInstance vi : agent.getFocus().getViListAllEnergies()) {
			vis.addAll(sh.getMembers(vi, ec));
		}
		List<StoryLine> stlsShadows = StoryLineRepository.createStoryLines(agent, vis);
		double bestMetric = -1.0;
		StoryLine bestStoryLine = null;
		// for all story lines, sum up the shadow VI energy: for the strongest
		// ones, in salience steps
		for (StoryLine stl : stlsShadows) {
			double metric = 0;
			for (VerbInstance vi : stl.getVis()) {
				ViSet viset = sh.getReverseShadow(vi, ec);
				List<SimpleEntry<VerbInstance, Double>> list = viset.getDecreasingStrengthList();
				if (!list.isEmpty()) {
					metric += list.get(0).getValue();
				}
			}
			// is this the best storyline???
			if (metric > bestMetric) {
				bestMetric = metric;
				bestStoryLine = stl;
			}
		}
		return bestStoryLine;
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
	 * Performs a single story line prediction
	 * @param agent
	 * @param stl
	 * @param shstl
	 * @param ec
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
		for(int i = location; i < vis.size(); i++) {
			VerbInstance old = vis.get(i);
			switch(old.getViType()) {
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
				temp += " / " + VrblzAdjective.verbalizeAdjective(agent,
                        old.getAdjective(), true);
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
