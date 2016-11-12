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
package org.xapagy.reference;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.InstanceClassifier;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.reference.rrException.RreType;
import org.xapagy.reference.rrState.rrJustification;
import org.xapagy.reference.rrState.rrPhase;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.smartprint.SpInstance;
import org.xapagy.verbalize.VerbalMemoryHelper;
import org.xapagy.xapi.reference.XrefVi;
import org.xapagy.xapi.reference.XrefWhInstance;

/**
 * 
 * This class contains code which helps in the resolution of the references
 * 
 * @author Ladislau Boloni
 * Created on: Dec 24, 2010
 */
public class ReferenceResolution {

    /**
     * Resolves a direct reference to a single, most likely resolution
     * 
     * @param rrc
     *            - the resolution context
     * @param allowNoResolution
     * @return
     * @throws rrException
     */
    public static SimpleEntry<Instance, rrState> resolveDirect(rrContext rrc,
            boolean allowNoResolution) throws rrException {
        SimpleEntry<Instance, rrState> retval = null;
        List<rrCandidate> candidates =
                rrCollectCandidates.collectCandidates(rrc);
        retval = ReferenceResolution.select(rrc, candidates, allowNoResolution);
        return retval;
    }

    /**
     * Create a VI from a XapiVi by resolving all the references.
     * 
     * @throws rrException
     * 
     */
    public static VerbInstance resolveFullVi(Agent agent, XrefVi xapiVi,
            VerbInstance inquit) throws rrException {
        VerbInstance verbInstance = agent.createVerbInstance(xapiVi.getViType(),
                xapiVi.getVerb().getVo());
        Instance scene = null;
        if (inquit != null) {
            scene = inquit.getQuoteScene();
            // TextUi.println("resolveViFromXapiVi: scene set to" +
            // PrettyPrint.ppDetailed(scene, agent));
        } else {
            scene = agent.getFocus().getCurrentScene();
        }
        //
        // All the VIs have a subject
        //

        switch (xapiVi.getViType()) {
        case S_V_O: {
            rrContext rrcSubject = rrContext.createFromXapiReference(agent,
                    xapiVi.getSubject(), verbInstance.getVerbs(),
                    ViPart.Subject, scene, inquit);
            Instance subjectInstance =
                    ReferenceResolution.resolveReference(rrcSubject).getKey();
            rrContext rrcObject = rrContext.createFromXapiReference(agent,
                    xapiVi.getObject(), verbInstance.getVerbs(), ViPart.Object,
                    scene, inquit);
            Instance objectInstance =
                    ReferenceResolution.resolveReference(rrcObject).getKey();
            //
            //
            // Unless this is a relation manipulation statement (when we can
            // allow a subject=object)
            // the subject cannot be the same as the object
            //
            //
            if (!ViClassifier.decideViClass(ViClass.RELATION_MANIPULATION,
                    verbInstance, agent)
                    && subjectInstance.equals(objectInstance)) {
                TextUi.println(
                        "SubjectInstance == ObjectInstance, should not happen - "
                                + PrettyPrint.ppDetailed(xapiVi, agent));
                TextUi.println("Subject resolved to"
                        + PrettyPrint.ppDetailed(subjectInstance, agent));
                TextUi.println("Object resolved to"
                        + PrettyPrint.ppDetailed(objectInstance, agent));
                // these are repeated here to see what it is doing
                subjectInstance = ReferenceResolution
                        .resolveReference(rrcSubject).getKey();
                objectInstance = ReferenceResolution.resolveReference(rrcObject)
                        .getKey();
                throw new Error("cannot handle sub = obj");
            }
            verbInstance.setSubject(subjectInstance);
            verbInstance.setObject(objectInstance);
            break;
        }
        case S_V: {
            rrContext rrcSubject = rrContext.createFromXapiReference(agent,
                    xapiVi.getSubject(), verbInstance.getVerbs(),
                    ViPart.Subject, scene, inquit);
            Instance subjectInstance =
                    ReferenceResolution.resolveReference(rrcSubject).getKey();
            verbInstance.setSubject(subjectInstance);
            break;
        }
        case S_ADJ: {
            rrContext rrcSubject = rrContext.createFromXapiReference(agent,
                    xapiVi.getSubject(), verbInstance.getVerbs(),
                    ViPart.Subject, scene, inquit);
            Instance subjectInstance =
                    ReferenceResolution.resolveReference(rrcSubject).getKey();
            verbInstance.setSubject(subjectInstance);
            verbInstance.setAdjective(xapiVi.getAdjective().getCo());
            break;
        }
        case QUOTE: {
            rrContext rrcSubject = rrContext.createFromXapiReference(agent,
                    xapiVi.getSubject(), verbInstance.getVerbs(),
                    ViPart.Subject, scene, inquit);
            Instance subjectInstance =
                    ReferenceResolution.resolveReference(rrcSubject).getKey();
            verbInstance.setSubject(subjectInstance);
            rrContext rrcQuoteScene = rrContext.createFromXapiReference(agent,
                    xapiVi.getQuoteScene(), verbInstance.getVerbs(),
                    ViPart.QuoteScene, scene, inquit);
            Instance quoteSceneInstance = ReferenceResolution
                    .resolveReference(rrcQuoteScene).getKey();
            verbInstance.setQuoteScene(quoteSceneInstance);
            // iterate down, and resolve the references at the quote
            VerbInstance quoteVerbInstance = ReferenceResolution.resolveFullVi(
                    agent, (XrefVi) xapiVi.getQuote(), verbInstance);
            verbInstance.setQuote(quoteVerbInstance);
        }
        }
        xapiVi.setResolutionConfidence(rrState.createNoCompetitionResConf());
        VerbalMemoryHelper.memorizeVerbalResolution(agent, scene, verbInstance,
                xapiVi);
        return verbInstance;
    }

    /**
     * 
     * The general purpose resolution dispatcher. The most important ones are
     * the direct, the relational and the group ones.
     * 
     * @param rrc
     *            - the context in which the reference must be done
     * @return pair of instance and rrState
     * @throws rrException
     */
    public static SimpleEntry<Instance, rrState> resolveReference(rrContext rrc)
            throws rrException {
        Agent agent = rrc.getAgent();
        SimpleEntry<Instance, rrState> entry = null;
        switch (rrc.getReferenceType()) {
        case REF_DIRECT: {
            if (rrc.isPronoun()) {
                entry = rrPronoun.resolvePronoun(rrc);
            } else {
                entry = ReferenceResolution.resolveDirect(rrc, false);
            }
            break;
        }
        case REF_RELATIONAL: {
            entry = rrRelational.resolveReferenceRelational(rrc);
            break;
        }
        case REF_GROUP: {
            entry = rrGroup.resolveReferenceGroup(rrc);
            break;
        }
        case WH_INSTANCE: {
            //TextUi.println(
            //        "Creating an instance for Wh: FIXME this should be a spike.");
            Instance instance = null;
            instance = agent.createInstance(rrc.getScene());
            // should be done from VerbalMemoryHelper
            // agent.getVerbalMemory().addReferredAs(instance, scene, vi,
            // part,
            // reference.getText());
            XrefWhInstance ref2 = (XrefWhInstance) rrc.getReference();
            instance.getConcepts().addOverlay(ref2.getCo());
            EnergyQuantum<Instance> eq = EnergyQuantum.createAdd(instance,
                    Focus.INITIAL_ENERGY_INSTANCE, EnergyColors.FOCUS_INSTANCE,
                    "ReferenceResolution");
            agent.getFocus().applyInstanceEnergyQuantum(eq);
            entry = new SimpleEntry<>(instance,
                    rrState.createNoCompetitionResConf());
            break;
        }
        default: {
            throw new Error(
                    "ReferenceResolution.resolveReference called with a reference of type "
                            + rrc.getReference().getType()
                            + ", it should not have been.");
        }
        }
        // this is the moment where the XapiReference acquires the resolution
        // confidence
        if (rrc.getReference() != null) {
            rrc.getReference().setResolutionConfidence(entry.getValue());
        }
        return entry;
    }

    /**
     * Selects from a list of candidates the ones which is most likely to be the
     * one referred to by the rrc, which must be a REF_DIRECT type
     * 
     * It returns the resolved instance, and a number which shows how far it is
     * from the next one
     * 
     * @param rrc
     *            - the reference context
     * @param candidates
     *            - the list of candidates, previously collected
     * @param allowNoResolution
     *            if true, it will not throw an error - useful for verbalize and
     *            probably later, also for passing over not quite understood
     *            stuff
     * 
     * @return
     * @throws rrException
     */
    public static SimpleEntry<Instance, rrState> select(rrContext rrc,
            List<rrCandidate> candidates, boolean allowNoResolution)
                    throws rrException {
        // First, try whether we can make a resolution based on the labels
        SimpleEntry<Instance, rrState> resolveByLabels =
                ReferenceResolution.selectByLabels(rrc, candidates);
        if (resolveByLabels != null) {
            return resolveByLabels;
        }
        // If the labels do not resolve it, try scoring by attributes
        List<SimpleEntry<Instance, rrState>> possibles = new ArrayList<>();
        for (rrCandidate cand : candidates) {
            double recencyScore = cand.getAssignedScore();
            Instance instance = cand.getInstance();
            rrState resconf = rrState.createCalculated(instance.getConcepts(),
                    rrc.getCoDirect());
            resconf.setScoreBias(recencyScore);
            // avoid getting a candidate where the similarity score is zero
            if (resconf.getScoreSimilarity() > 0.001) {
                possibles.add(new SimpleEntry<>(instance, resconf));
            }
        }
        // handling the case when none of them are correct
        if (possibles.isEmpty()) {
            //
            // if we are referring to a scene, return the current scene
            // this is a bit hackish, but this is the stuff for the
            // scene/create-instance/properties stuff
            //
            if (InstanceClassifier.decideSceneCo(rrc.getCoDirect(),
                    rrc.getAgent())) {
                rrState resconf = rrState.createNoCompetitionResConf();
                resconf.setPhase(rrPhase.WINNER);
                resconf.setJustification(rrJustification.CURRENT_SCENE);
                return new SimpleEntry<>(
                        rrc.getAgent().getFocus().getCurrentScene(), resconf);
            }
            if (allowNoResolution) {
                return null;
            } else {
                rrException rrex = new rrException(RreType.REF_SIMPLE);
                rrex.setExplanation(
                        "ReferenceResolution.resolveInstanceReferredByCoFromCandidates: \n"
                                + "Possibles Empty: Can not resolve reference:\n"
                                + PrettyPrint.ppConcise(rrc.getCoDirect(),
                                        rrc.getAgent())
                                + " in scene " + PrettyPrint.ppDetailed(
                                        rrc.getScene(), rrc.getAgent()));
                throw rrex;
            }
        }
        // we have only one possible resolution
        if (possibles.size() == 1) {
            SimpleEntry<Instance, rrState> retval = possibles.get(0);
            retval.getValue().setPhase(rrPhase.WINNER);
            retval.getValue().setJustification(rrJustification.ONLY_COMPATIBLE);
            return retval;
        }
        // multiple possible resolutions. We declare as winner, the one with
        // the highest overal score, the other ones are not winners...
        double highestScore = -1.0;
        SimpleEntry<Instance, rrState> retval = null;
        for (SimpleEntry<Instance, rrState> entry : possibles) {
            if (entry.getValue().getOverallScore() > highestScore) {
                highestScore = entry.getValue().getOverallScore();
                retval = entry;
            }
        }
        // now, mark them as winners and loosers
        for (SimpleEntry<Instance, rrState> entry : possibles) {
            if (entry == retval) {
                retval.getValue().setPhase(rrPhase.WINNER);
                retval.getValue()
                        .setJustification(rrJustification.MAX_COMPATIBLE);
            } else {
                entry.getValue().setPhase(rrPhase.NOT_WINNER);
            }
        }
        return retval;
    }

    /**
     * Resolves based on the labels: if there are labels in the coRef, finds
     * somebody from the candidates which has the same labels
     * 
     * @param agent
     * @param coRef
     * @param candidates
     * @return
     */
    private static SimpleEntry<Instance, rrState> selectByLabels(rrContext rrc,
            List<rrCandidate> candidates) {
        List<String> labels = rrc.getCoDirect().getLabels();
        if (labels.isEmpty()) {
            return null;
        }
        List<Instance> matching = new ArrayList<>();
        for (rrCandidate cand : candidates) {
            Instance candidate = cand.getInstance();
            boolean hasAll = true;
            for (String label : labels) {
                if (!candidate.getConcepts().getLabels().contains(label)) {
                    hasAll = false;
                    break;
                }
            }
            if (hasAll) {
                matching.add(candidate);
            }
        }
        if (matching.size() == 0) {
            return null;
        }
        if (matching.size() == 1) {
            SimpleEntry<Instance, rrState> retval = new SimpleEntry<>(
                    matching.get(0), rrState.createNoCompetitionResConf());
            return retval;
        }
        //
        // Create an extensive diagnosis here of why would this made 
        //
        Formatter fmt = new Formatter();
        fmt.add("ReferenceResolution.resolveByLabels - multiple matches, should not happen.");
        fmt.is("reference", rrc);
        fmt.add("matches:");
        fmt.indent();
        for(Instance inst: matching) {
            fmt.add(SpInstance.spc(inst, rrc.getAgent()));
        }
        TextUi.abort(
                fmt.toString());
        return null;
    }
}
