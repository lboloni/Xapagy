/*
   This file is part of the Xapagy project
   Created on: June 25, 2016
 
   org.xapagy.introspect.Introspect
 
   Copyright (c) 2008-2016 Ladislau Boloni
 */

package org.xapagy.introspect;

import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storyline.StoryLine;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * This class is the gathering point of a number of "low hanging fruit"
 * introspection models. They can be used by a user to conveniently extract
 * information from Xapagy agent, without going through the the complex internal
 * processes of story generation. For instance, once can get the "most likely"
 * continuation, the "most similar story", or a "most likely story from here".
 * 
 * Why these functions use the shadows / HLSs, they do not iterate xapagy
 * through the re-narration process.
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
	 * Test to see if is is working from Javascript.
	 * 
	 * @return
	 */
	public String getAgentName() {
		return agent.getName();
	}

	/**
	 * Verbalizing a the story line
	 * 
	 * @return
	 */
	private String verbalizeStoryLine(StoryLine stl) {
		StringBuffer sb = new StringBuffer();
		for (VerbInstance vi : stl.getVis()) {
			sb.append(XapiPrint.ppsViXapiForm(vi, agent) + "\n");
		}
		return sb.toString();
	}

	/**
	 * Gets the whole storyline of the current story and prints it out
	 * 
	 * @return
	 */
	public String getCurrentStory() {
		StoryLine stl = IntrospectHelper.getCurrentStoryLine(agent);
		return verbalizeStoryLine(stl);
	}

	/**
	 * Gets the whole storyline of the strongest shadow and prints it out
	 * -actually, what we would want here is to identify what are the strongest
	 * shadows of the whole thing...
	 */
	public String getStrongestShadowStory() {
		String ec = EnergyColors.SHV_GENERIC;
		StoryLine stl = IntrospectHelper.getCurrentStoryLine(agent);
		StringBuffer buf = new StringBuffer();
		StoryLine bestStoryLine = IntrospectHelper.getStrongestShadowStoryLine(agent, ec);
		if (bestStoryLine == null) {
			return "Could not find a storyline in the shadow.";
		}
		buf.append(verbalizeStoryLine(bestStoryLine));
		buf.append("Where the instances are mapped as follows:\n");
		Map<Instance, Instance> mapping = IntrospectHelper.getMapping(agent, stl, bestStoryLine,
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
		buf.append(IntrospectHelper.singleStoryLinePrediction(agent, stl, bestStoryLine, ec));
		return buf.toString();
	}
}
