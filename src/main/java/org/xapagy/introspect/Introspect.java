/*
   This file is part of the Xapagy project
   Created on: June 25, 2016
 
   org.xapagy.introspect.Introspect
 
   Copyright (c) 2008-2016 Ladislau Boloni
 */

package org.xapagy.introspect;

import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.storyline.StoryLine;
import org.xapagy.storyline.StoryLineReasoning;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * 
 * 
 * This class is the gathering point of a number of "low hanging fruit"
 * introspection models. They can be used by a user to conveniently extract
 * information from Xapagy agent, without going through the the complex internal
 * processes of story generation. For instance, once can get the "most likely"
 * continuation, the "most similar story", or a "most likely story from here".
 * 
 * While these functions use the shadows / HLSs, they do not use the Xapagy
 * internal flow generation through the re-narration process.
 * 
 * These should be normally called from the Javascript embedded code from Xapi.
 * 
 * @author lboloni
 *
 */
public class Introspect {

	private Agent agent;

	public Introspect(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Verbalizes a story line in Xapi. Ideally, this should be a format which
	 * can be parsed back.
	 * 
	 * @return
	 */
	public String verbalize(StoryLine stl) {
		StringBuffer sb = new StringBuffer();
		for (VerbInstance vi : stl.getVis()) {
			sb.append(XapiPrint.ppsViXapiForm(vi, agent) + "\n");
		}
		return sb.toString();
	}

	/**
	 * Verbalizes a verb instance
	 * 
	 * @param vi
	 * @return
	 */
	public String verbalize(VerbInstance vi) {
		return XapiPrint.ppsViXapiForm(vi, agent) + "\n";
	}

	/**
	 * Verbalizes an instance
	 * 
	 * @param vi
	 * @return
	 */
	public String verbalize(Instance instance) {
		return SpInstance.spc(instance, agent);
	}

	/**
	 * Returns the current story line: this tacitly assumes that there is a
	 * single shadow story...
	 * 
	 * @return the current story line
	 */
	public StoryLine currentStoryLine() {
		return StoryLineReasoning.getCurrentStoryLine(agent);
	}

	/**
	 * Returns the current story line: this tacitly assumes that there is a
	 * single shadow story...
	 * 
	 * @return the current story line
	 */
	public List<StoryLine> inFocusStoryLines() {
		List<VerbInstance> viList = agent.getFocus().getViList(EnergyColors.FOCUS_VI);
		return StoryLineReasoning.createStoryLines(agent, viList);
	}

	/**
	 * Prints out the likely instance mapping between two story lines in a
	 * pleasant way
	 * 
	 * @return the current story line
	 */
	public String printLikelyInstanceMapping(StoryLine fstl, StoryLine shstl) {
		Formatter fmt = new Formatter();
		Map<Instance, Instance> map = StoryLineReasoning.getLikelyInstanceMapping(agent, fstl, shstl,
				EnergyColors.SHI_GENERIC);
		for (Instance fi : map.keySet()) {
			Instance si = map.get(fi);
			if (si != null) {
				fmt.is(SpInstance.spc(fi, agent), SpInstance.spc(si, agent));
			} else {
				fmt.is(SpInstance.spc(fi, agent), "<< no mapping found >>");
			}
		}
		return fmt.toString();
	}

	/**
	 * Returns the shadow story lines associated with the specified story line
	 * 
	 * @param st
	 * @return
	 */
	public List<SimpleEntry<StoryLine, Double>> createShadowStoryLines(StoryLine st) {
		return StoryLineReasoning.createShadowStoryLines(agent, st, EnergyColors.SHV_GENERIC);
	}

	/**
	 * Gets the whole storyline of the strongest shadow and prints it out
	 * -actually, what we would want here is to identify what are the strongest
	 * shadows of the whole thing...
	 */
	public String getStrongestShadowStory() {
		String ec = EnergyColors.SHV_GENERIC;
		StoryLine stl = StoryLineReasoning.getCurrentStoryLine(agent);
		StringBuffer buf = new StringBuffer();
		StoryLine bestStoryLine = StoryLineReasoning.getStrongestShadowStoryLine(agent, stl);
		if (bestStoryLine == null) {
			return "Could not find a storyline in the shadow.";
		}
		buf.append(verbalize(bestStoryLine));
		buf.append("Where the instances are mapped as follows:\n");
		Map<Instance, Instance> mapping = StoryLineReasoning.getLikelyInstanceMapping(agent, stl, bestStoryLine,
				EnergyColors.SHI_GENERIC);
		for (Instance from : mapping.keySet()) {
			Instance value = mapping.get(from);
			if (value != null) {
				buf.append(SpInstance.spc(from, agent) + " ==>" + SpInstance.spc(value, agent) + "\n");
			} else {
				buf.append(SpInstance.spc(from, agent) + " ==> <none>\n");
			}
		}
		//
		buf.append("And now for a prediction");
		buf.append(StoryLineReasoning.singleStoryLinePrediction(agent, stl, bestStoryLine, ec));
		return buf.toString();
	}
}
