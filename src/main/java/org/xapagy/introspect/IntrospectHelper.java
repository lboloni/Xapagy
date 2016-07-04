package org.xapagy.introspect;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storyline.StoryLine;
import org.xapagy.debug.storyline.StoryLineRepository;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;

/**
 * A number of calls helping the Introspect ones. These return internal structures
 * 
 * @author lboloni
 *
 */
public class IntrospectHelper {

	/**
	 * Returns the current storyline of the Xapagy agent
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
	
	public static StoryLine getStrongestShadowStoryLine(Agent agent, String ec) {
		List<VerbInstance> vis = new ArrayList<>();
		VerbInstance lastVi = agent.getLastVerbInstance();
		vis.add(lastVi);
		//List<StoryLine> stls = StoryLineRepository.createStoryLines(agent, vis);
		//StoryLine stl = stls.get(0); // normally, it cannot be more than one
		// get all the shadow story lines
		vis = new ArrayList<>();
		Shadows sh = agent.getShadows();
		// extract all the storylines in the shadows
		for(VerbInstance vi: agent.getFocus().getViListAllEnergies()) {
			vis.addAll(sh.getMembers(vi, ec));
		}
		List<StoryLine> stlsShadows = StoryLineRepository.createStoryLines(agent, vis);
		double bestMetric = -1.0;
		StoryLine bestStoryLine = null;
		// for all story lines, sum up the shadow VI energy: for the strongest ones, in salience steps
		for(StoryLine stl: stlsShadows) {
			double metric = 0;
			for(VerbInstance vi: stl.getVis()) {
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
	 * FIXME: Note that this could be improved by taking into consideration identity relations
	 * 
	 * @param agent 
	 * @param fstl - a storyline (normally, the one that is in the focus)
	 * @param shtl - a storyline (normally, one that is in the shadows)
	 * @param ec - the energy color based on which the mapping is done
	 * @return
	 */
	public static List<SimpleEntry<Instance, Instance>> getMapping(Agent agent, StoryLine fstl, StoryLine shtl, String ec) {
		Shadows sh = agent.getShadows();
		List<SimpleEntry<Instance, Instance>> retval = new ArrayList<>();
		for(Instance scene: fstl.getScenes()) {
			for(Instance fi: scene.getSceneMembers()) {
				SimpleEntry<Instance, Instance> rv = null;
				// find the strongest
				List<Instance> members = sh.getMembers(fi, ec);
				for(Instance inst: members) {
					if (shtl.contains(inst)) {
						rv = new SimpleEntry<Instance, Instance>(fi, inst);
						break;
					}
				}
				if (rv == null) {
					rv = new SimpleEntry<Instance, Instance>(fi, null);					
				}
				retval.add(rv);
			}
		}
		return retval;
	}
}
