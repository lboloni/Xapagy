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
package org.xapagy.agents;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.script.ScriptException;

import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.exceptions.MalformedConceptOrVerbName;
import org.xapagy.exceptions.NoSuchConceptOrVerb;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.metaverbs.AbstractSaMetaVerb;
import org.xapagy.parameters.Parameters;
import org.xapagy.questions.QuestionHelper;
import org.xapagy.reference.ReferenceResolution;
import org.xapagy.reference.rrException;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.DecompositionHelper;
import org.xapagy.xapi.MacroParser;
import org.xapagy.xapi.XapiParserException;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiReferenceType;
import org.xapagy.xapi.reference.XrefStatement;
import org.xapagy.xapi.reference.XrefVi;
import org.xapagy.xapi.reference.XrefWait;

/**
 * Functions related to the execution of the VIs, etc. Mostly called from Loop
 * 
 * @author Ladislau Boloni
 * Created on: Aug 11, 2012
 */
public class Execute {

    /**
     * Execute the DAs associated with the passing of time (focus, shadows and hls). If the 
     * single step HLS are active, it is implementing them in one shot, at the end. 
     * 
     * Note that this call always performs a HLS step at the end.
     * 
     * @param agent
     * @param time
     *            the amount of time it had passed
     * 
     */
    public static void executeDiffusionActivities(Agent agent, double time) {
        Parameters p = agent.getParameters();
        boolean doSingleStepHls =
                p.getBoolean("A_HLSM",
                        "G_GENERAL",
                        "N_SINGLE_STEP_HLS");
        double timeStep =
                p.get("A_GENERAL", "G_GENERAL",
                        "N_TIME_STEP_OF_DAS");
        // to make sure that we do not do an extra step due to rounding errors
        double accumulatedTime = timeStep / 2;
        while (accumulatedTime < time) {
            agent.addTime(timeStep);
            accumulatedTime = accumulatedTime + timeStep;
            agent.notifyObservers(new DebugEvent(DebugEventType.BEFORE_DA_STEP));
            // call the diffusion activities
            agent.getDaComposite(Hardwired.DA_FOCUS_MAINTENANCE).apply(timeStep);
            agent.getDaComposite(Hardwired.DA_SHADOW_MAINTENANCE).apply(timeStep);
            if (!doSingleStepHls) {
                agent.getDaComposite(Hardwired.DA_HLS_MAINTENANCE).apply(timeStep);
            }
        }
        if (doSingleStepHls) {
            agent.getDaComposite(Hardwired.DA_HLS_MAINTENANCE).apply(timeStep);
        }
        agent.notifyObservers(new DebugEvent(DebugEventType.AFTER_DA_CHUNK));
    }

    /**
     * Performs the SAs associated with the insertion of a VI in the focus.
     * 
     * There is a trick here. When a verb is instantiated normally, the VM_SUCCESSOR verb is used to 
     * create the SUCCESSOR/PRED links and push out the other nodes. If this is instantiated internally,
     * an SA (I think normally SaMvInsertHlsLocation) will replace this, which inserts the VI at a location
     * instead of positioning it to the end.
     * 
     * 
     * @param vi - the newly created verb instance
     * @param successorReplacement - the verb which should be executed instead of the successor verb. This 
     *    is normally the SaMvInsertHml
     */
    public static void executeVIandSAs(Agent agent, VerbInstance vi,
            AbstractSaMetaVerb successorReplacement) {
        // adds the vi to the focus
        //SaFcmInsertVi saFocusInsertVi = new SaFcmInsertVi(agent, "FcmInsertVi");
        //saFocusInsertVi.apply(vi);
        agent.getSaComposite(Hardwired.SA_BEFORE_VI).apply(vi);
        //
        // Execute the SAs associated with the meta verbs,
        // only when the VI is not a question
        if (!QuestionHelper.isAQuestion(vi, agent)) {
            for (SimpleEntry<Verb, Double> entry : vi.getVerbs().getList()) {
                Verb verb = entry.getKey();
                // in this case, do not execute the successor
                if (successorReplacement != null
                        && verb.getName().equals(Hardwired.VM_SUCCESSOR)) {
                    // TextUi.println("Not executing common successor");
                    successorReplacement.apply(vi);
                    continue;
                } else {
                    verb.safeExecute(vi, agent);
                }
            }
        }
        // if vi is an answer to a question find it...
        //SaFcmFoundAnswer saFoundAnswer = new SaFcmFoundAnswer(agent, "FcmFoundAnswer");
        //saFoundAnswer.apply(vi);
        
        // update the expected shadow: this should be done at the
        // statement, the recall is handled differently? or is it?
        //SaHlsmExpectedVi saHlmsExpectedVi = new SaHlsmExpectedVi(agent, "HlmsExpectedVi");
        //saHlmsExpectedVi.apply(vi);
        
        agent.getSaComposite(Hardwired.SA_AFTER_VI).apply(vi);
        // FINALLY, if this is a quote, iterate down on the quote
        if (vi.getViType().equals(ViType.QUOTE)) {
            Execute.executeVIandSAs(agent, vi.getQuote(), null);
        }
    }

	/**
	 * Executes an internal or reading type LoopItem. This is whatever comes out
	 * of a single line of Xapi
	 * 
	 */
	public static void executeXapiText(Agent agent, AbstractXapiLoopItem item) {
		String line = item.getXapiText();
		//
		// if we are in script accumulation mode
		//
		if (agent.getLoop().isScriptAccumulationMode()) {
			if (line.startsWith("$EndScript") || line.startsWith("$}}")) {
				String script = agent.getLoop().stopScriptAccumulation();
				try {
					agent.getScriptEngine().eval(script);
				} catch (ScriptException e) {
					e.printStackTrace();
					throw new Error("Script exception!");
				}
				return;
			}
			// so it is not the end of the script
			agent.getLoop().addToAccumulatedScript(line);
			return;
		}
		//
		// if it is a one-line script
		//
		if (line.startsWith("!!")) {
			String scriptText = line.substring("!!".length());
			try {
				agent.getScriptEngine().eval(scriptText);
			} catch (ScriptException e) {
				e.printStackTrace();
				throw new Error("Script exception!");
			}
			return;
		}
		if (line.startsWith("$BeginScript") || line.startsWith("${{")) {
			agent.getLoop().startScriptAccumulation();
			return;
		}
	
		//
		// if it is a macro
		//
		if (line.startsWith(AbstractLoopItem.MACRO_PREFIX)) {
			MacroParser.executeMacro(line, agent);
			return;
		}
		//
		// if we got here, it is a regular sentence
		//
		XapiReference xst = null;
		try {
			xst = agent.getXapiParser().parseLine(line);
		} catch (XapiParserException e) {
			TextUi.println(line);
			throw new Error(item.formatException(e, e.getDiagnosis()));
		} catch (MalformedConceptOrVerbName e) {
			throw new Error(item.formatException(e, e.toString()));
		} catch (NoSuchConceptOrVerb e) {
			throw new Error(item.formatException(e, e.toString()));
		}
		// if it is a macro
		List<XapiReference> atomicStatements;
		// the scenes referenced by this loopitem
		Set<Instance> referencedScenes = new HashSet<>();
		if (xst.getType().equals(XapiReferenceType.WAIT)) {
			// if it is a wait statement, add directly
			atomicStatements = new ArrayList<>();
			atomicStatements.add(xst);
		} else {
			// if it is a statement, decompose
			atomicStatements = DecompositionHelper.decomposeStatementIntoVisAndWait(agent, (XrefStatement) xst);
		}
		List<VerbInstance> retval = new ArrayList<>();
		for (XapiReference statement : atomicStatements) {
			if (statement.getType().equals(XapiReferenceType.VI)) {
				VerbInstance vi = null;
				try {
					vi = ReferenceResolution.resolveFullVi(agent, (XrefVi) statement, null);
				} catch (rrException e) {
					throw new Error("Reference resolution exception at line:\n" + line + "\n" + e.toString());
				}
				// at this moment we have the VI !!!
				referencedScenes.addAll(vi.getReferencedScenes());
				executeVIandSAs(agent, vi, null);
				retval.add(vi);
				continue;
			}
			if (statement.getType().equals(XapiReferenceType.WAIT)) {
				executeDiffusionActivities(agent, ((XrefWait) statement).getTimeWait());
				continue;
			}
			throw new Error("The only ones allowed are XapiVi and XapiWait, this one is " + xst.getType());
		}
		//
		// Sets the interstitial energy for all the referenced scenes
		//
		for (Instance scene : referencedScenes) {
			scene.getSceneParameters().resetInterstitialEnergy();
		}
		item.getExecutionResult().addAll(retval);
	}



}
