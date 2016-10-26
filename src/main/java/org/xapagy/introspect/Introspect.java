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
import org.xapagy.questions.QuestionHelper;
import org.xapagy.set.EnergyColors;
import org.xapagy.storyline.QuestionAnswering;
import org.xapagy.storyline.StoryLine;
import org.xapagy.storyline.StoryLineReasoning;
import org.xapagy.ui.TextUi;
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
	 * Executes a VI in the agent. 
	 * @param vi
	 */
	public void enactVi(VerbInstance vi) {
		agent.getLoop().proceedOneForcedStep(vi, 1.0);
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

	public Map<Instance, Instance> getLikelyInstanceMapping(StoryLine fline, StoryLine sline) {
		return StoryLineReasoning.getLikelyInstanceMapping(agent, fline, sline, EnergyColors.SHI_GENERIC);
	}

	public Map<VerbInstance, VerbInstance> getLikelyViMapping(StoryLine fline, StoryLine sline) {
		return StoryLineReasoning.getLikelyViMapping(agent, fline, sline, EnergyColors.SHV_GENERIC);
	}
	
	/**
	 * Returns a formatted string showing a focus instance to shadow instance
	 * map
	 * 
	 * @param map
	 * @return
	 */
	public String formatInstanceMapping(Map<Instance, Instance> map) {
		Formatter fmt = new Formatter();
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
	 * This is the entry point to the Xapagy question answering system. Takes a 
	 * question that had been posed, and returns a set of VIs that answer it
	 * 
	 * @param label - the label from the focus
	 * @return
	 */
	public List<VerbInstance> answerQuestion(String label) {
		// identify the question VI
		VerbInstance theQuestion = null;
		for(VerbInstance vi: agent.getFocus().getViListAllEnergies()) {
			if (!QuestionHelper.isAQuestion(vi, agent)) {
				continue;
			}
			if (!vi.getVerbs().getLabels().contains(label)) {
				continue;
			}			
		}
		if (theQuestion == null) {
			TextUi.errorPrint("There was no question to answer");
		}
		TextUi.println(XapiPrint.ppsViXapiForm(theQuestion, agent));
		return QuestionAnswering.answerQuestion(theQuestion);
	}
	
	
	/**
	 * Returns a formatted string showing a focus VI to shadow VI
	 * map
	 * 
	 * @param map
	 * @return
	 */
	public String formatViMapping(Map<VerbInstance, VerbInstance> map) {
		Formatter fmt = new Formatter();
		for (VerbInstance fvi : map.keySet()) {
			VerbInstance svi = map.get(fvi);
			if (svi != null) {
				fmt.add(XapiPrint.ppsViXapiForm(fvi, agent));
				fmt.addIndented("---> " + XapiPrint.ppsViXapiForm(svi, agent));
			} else {
				fmt.add(XapiPrint.ppsViXapiForm(fvi, agent));
				fmt.addIndented("---> << no match found >>");
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
	 * Creates a list of predictions based on the most likely mapping... 
	 * 
	 * @param fline
	 * @param sline
	 * @return
	 */
	public List<VerbInstance> createPrediction(StoryLine fline, StoryLine sline) {
		Map<Instance, Instance> instanceMap = getLikelyInstanceMapping(fline, sline);
		return StoryLineReasoning.createPrediction(agent, fline, sline, instanceMap, EnergyColors.SHV_GENERIC);
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
