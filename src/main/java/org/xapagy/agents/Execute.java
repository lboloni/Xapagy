/*
   This file is part of the Xapagy project
   Created on: Aug 11, 2012
 
   org.xapagy.agents.Execute
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.debug.DebugEvent;
import org.xapagy.debug.DebugEvent.DebugEventType;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.metaverbs.AbstractSaMetaVerb;
import org.xapagy.parameters.Parameters;
import org.xapagy.questions.QuestionHelper;

/**
 * Functions related to the execution of the VIs, etc. Mostly called from Loop
 * 
 * @author Ladislau Boloni
 * 
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
    public static void executeSpikeActivities(Agent agent, VerbInstance vi,
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
            Execute.executeSpikeActivities(agent, vi.getQuote(), null);
        }
    }



}