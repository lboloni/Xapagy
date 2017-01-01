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
package org.xapagy.introspect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Verb;
import org.xapagy.exceptions.MalformedConceptOrVerbName;
import org.xapagy.exceptions.NoSuchConceptOrVerb;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.questions.QuestionHelper;
import org.xapagy.reference.ReferenceResolution;
import org.xapagy.reference.rrException;
import org.xapagy.set.EnergyColors;
import org.xapagy.storyline.QuestionAnswering;
import org.xapagy.storyline.StoryLine;
import org.xapagy.storyline.StoryLineReasoning;
import org.xapagy.storyline.slrMapping;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.ui.smartprint.XapiPrint;
import org.xapagy.xapi.DecompositionHelper;
import org.xapagy.xapi.XapiParserException;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefVi;
import org.xapagy.xapi.reference.XapiReference.XapiReferenceType;

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
 * Created on: June 25, 2016
 */
public class Introspect {

	/**
	 * This kludge here allows us to store values in Javascript...
	 */
	public Map<String, Object> store = new HashMap<>();
	
	private Agent agent;

	public Introspect(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Re-narrate a specific story line
	 * @param sline
	 * @param kind
	 * 
	 */
	public void renarrate(StoryLine sline, String kind) {
		List<VerbInstance> svis = StoryLineReasoning.selectForRenarration(agent, sline, kind);
		List<VerbInstance> fvis = StoryLineReasoning.createRenarrate(agent, svis, sline);
	}
	
	
	/**
	 * Executes a VI in the agent.
	 * 
	 * @param vi
	 */
	public void execute(VerbInstance vi) {
		agent.getLoop().proceedOneForcedStep(vi, 1.0);
	}

	/**
	 * Verbalizes a story line in Xapi. Ideally, this should be a format which
	 * can be parsed back.
	 * 
	 * @param stl
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
	 * Verbalizes a list of verb instance
	 * 
	 * @param vis
	 * @return
	 */
	public String verbalize(List<VerbInstance> vis) {
		StringBuffer sb = new StringBuffer();
		for (VerbInstance vi : vis) {
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
		List<VerbInstance> viList = agent.getFocus()
				.getViList(EnergyColors.FOCUS_VI);
		return StoryLineReasoning.createStoryLines(agent, viList);
	}

	public slrMapping getLikelyInstanceMapping(StoryLine fline,
			StoryLine sline) {
		return StoryLineReasoning.getLikelyInstanceMapping(agent, fline, sline,
				EnergyColors.SHI_GENERIC);
	}

	public Map<VerbInstance, VerbInstance> getLikelyViMapping(StoryLine fline,
			StoryLine sline) {
		return StoryLineReasoning.getLikelyViMapping(agent, fline, sline,
				EnergyColors.SHV_GENERIC);
	}

	/**
	 * Returns a formatted string showing a focus instance to shadow instance
	 * map
	 * 
	 * @param map
	 * @return
	 */
//	public String formatInstanceMapping(Map<Instance, Instance> map) {
//		Formatter fmt = new Formatter();
//		for (Instance fi : map.keySet()) {
//			Instance si = map.get(fi);
//			if (si != null) {
//				fmt.is(SpInstance.spc(fi, agent), SpInstance.spc(si, agent));
//			} else {
//				fmt.is(SpInstance.spc(fi, agent), "<< no mapping found >>");
//			}
//		}
//		return fmt.toString();
//	}

	/**
	 * Answers an offline question. This happens without the time advancing in
	 * the agent, and of course.
	 * 
	 * Returns both a formatted, human readable answer string.
	 * 
	 * @param question
	 *            - the question in a string format
	 * @return
	 */
	public SimpleEntry<String, List<VerbInstance>> answerOfflineQuestion(
			String question) {
		VerbInstance theQuestion = parseStringToVi(question);
		SimpleEntry<String, List<VerbInstance>> answer = QuestionAnswering
				.answerQuestion(agent, theQuestion);
		TextUi.println(answer.getKey());
		return answer;
	}

	/**
	 * Forces Xapagy to answer a question posed online. Refers by a label to a
	 * question that had been posed inside the story, and returns the answers as
	 * VIs. These answers can then technically be inserted in the story.
	 * 
	 * @param label
	 *            - the label identifying the online question which should
	 *            already be in focus
	 * @return
	 */
	public SimpleEntry<String, List<VerbInstance>> answerOnlineQuestion(
			String label) {
		// identify the question VI
		VerbInstance theQuestion = null;
		for (VerbInstance vi : agent.getFocus().getViListAllEnergies()) {
			if (!QuestionHelper.isAQuestion(vi, agent)) {
				continue;
			}
			List<String> labels = vi.getVerbs().getLabels();
			String fullLabel = agent.getLabelSpaces().fullLabel(label);
			if (!labels.contains(fullLabel)) {
				continue;
			}
			theQuestion = vi;
			break;
		}
		if (theQuestion == null) {
			TextUi.errorPrint("There was no question to answer");
		}
		TextUi.println("Question: "
				+ XapiPrint.ppsViXapiForm(theQuestion, agent) + "?");
		SimpleEntry<String, List<VerbInstance>> answer = QuestionAnswering
				.answerQuestion(agent, theQuestion);
		TextUi.println(answer.getKey());
		return answer;
	}

	/**
	 * Parses a string to a VI. FIXME: this should share code with
	 * Execute.executeXapiText but that is too complicated at this moment.
	 * 
	 * @param xapi
	 * @return
	 */
	public VerbInstance parseStringToVi(String xapi) {
		XapiReference xst = null;
		try {
			xst = agent.getXapiParser().parseLine(xapi);
		} catch (XapiParserException e) {
			throw new Error(xapi + "\n" + e.getDiagnosis());
		} catch (MalformedConceptOrVerbName e) {
			throw new Error(xapi + "\n" + e.toString());
		} catch (NoSuchConceptOrVerb e) {
			throw new Error(xapi + "\n" + e.toString());
		}
		// we go with the assumption that xst is a pair
		List<XapiReference> atomicStatements = DecompositionHelper
				.decomposeStatementIntoVisAndWait(agent, (XrefStatement) xst);
		XapiReference statement = atomicStatements.get(0);
		if (!statement.getType().equals(XapiReferenceType.VI)) {
			throw new Error("Expected VI, found " + statement.getType());
		}
		VerbInstance vi = null;
		try {
			vi = ReferenceResolution.resolveFullVi(agent, (XrefVi) statement,
					null);
		} catch (rrException e) {
			throw new Error(e.toString());
		}
		return vi;
	}

	/**
	 * Returns a formatted string showing a focus VI to shadow VI map
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
	public List<SimpleEntry<StoryLine, Double>> createShadowStoryLines(
			StoryLine st) {
		return StoryLineReasoning.createShadowStoryLines(agent, st,
				EnergyColors.SHV_GENERIC);
	}

	/**
	 * Creates a list of predictions based on the most likely mapping...
	 * 
	 * @param fline
	 * @param sline
	 * @return
	 */
	public List<VerbInstance> createPrediction(StoryLine fline,
			StoryLine sline) {
		slrMapping slrm = getLikelyInstanceMapping(fline,
				sline);
		return StoryLineReasoning.createPrediction(agent, fline, sline,
				slrm, EnergyColors.SHV_GENERIC);
	}

	/**
	 * Prints the most likely completion of the current storyline in the Xapi form
	 */
	public String showCompletion() {
		List<VerbInstance> completion = StoryLineReasoning
				.createMostLikelyCompletion(agent);
		Formatter fmt = new Formatter();
		for (VerbInstance vi : completion) {
			fmt.add(XapiPrint.ppsViXapiForm(vi, agent));
		}
		return fmt.toString();
	}

	
	/**
	 * Prints the most likely prediction of the current storyline in the Xapi form
	 */
	public String showPrediction() {
		List<VerbInstance> completion = StoryLineReasoning
				.createMostLikelyPrediction(agent);
		Formatter fmt = new Formatter();
		for (VerbInstance vi : completion) {
			fmt.add(XapiPrint.ppsViXapiForm(vi, agent));
		}
		return fmt.toString();
	}

	
	
	/**
	 * For a given story line returns the set of VIs which are "in focus".
	 * 
	 * Probably at some moment we will need to make this more sophisticated, in
	 * the sense of keeping
	 * 
	 * @param line
	 * @return
	 */
	public List<VerbInstance> filterInFocus(StoryLine line) {
		List<VerbInstance> retval = new ArrayList<>();
		List<VerbInstance> focus = agent.getFocus().getViListAllEnergies();
		for (VerbInstance vi : line.getVis()) {
			if (focus.contains(vi)) {
				retval.add(vi);
			}
		}
		return retval;
	}

	/**
	 * Sets the verb impact on a subject... this is here temporarily
	 * @param verbName
	 * @param drive
	 * @param impactValue
	 */
	public void setVerbDriveImpactOnSubject(String verbName, String drive, double impactValue) {
		AbstractConceptDB<Verb> vdb = agent.getVerbDB(); 
		Verb verb = vdb.getConcept(verbName);
		verb.setDriveImpactOnSubject(drive, impactValue);
	}

	
	/**
	 * Sets the verb impact on a subject... this is here temporarily
	 * @param verbName
	 * @param drive
	 * @param impactValue
	 */
	public void setVerbDriveImpactOnObject(String verbName, String drive, double impactValue) {
		AbstractConceptDB<Verb> vdb = agent.getVerbDB(); 
		Verb verb = vdb.getConcept(verbName);
		verb.setDriveImpactOnObject(drive, impactValue);
	}


	/**
	 * Estimates the drive impacts of a list of VIs for the designated self
	 * 
	 * FIXME: this doesn't work in the current way for the energy quantum stuff...
	 * 
	 * @param vis
	 * @return
	 */
	public Map<String, Double> getDriveChanges(List<VerbInstance> vis) {
		Map<String, Double> retval = new HashMap<>();
		for(String drive: agent.getDrives().getDriveNames()) {
			retval.put(drive, 0.0);
		}
		for(VerbInstance vi: vis) {			
			// VI
			//EnergyQuantum<Instance> viChanges = agent.getDrives().getDriveChanges(vi, 1.0, 1.0);
			//for(Energy) {
			//	double val = retval.get(drive) + viChanges.get(drive);
			//	retval.put(drive, val);
			//}
			// assume 1 for time ...
			//Map<String, Double> viTime = agent.getDrives().getDriveChangesInTime();
			//for(String drive: viTime.keySet()) {
			//	double val = retval.get(drive) + viTime.get(drive);
			//	retval.put(drive, val);
			//}
		}
		return retval;
	}
	
}
