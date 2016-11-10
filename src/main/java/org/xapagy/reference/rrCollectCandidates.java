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

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.InstanceClassifier;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.metaverbs.AbstractSaMvRelation;
import org.xapagy.set.EnergyColors;

/**
 * @author Ladislau Boloni
 * Created on: Feb 11, 2012
 */
public class rrCollectCandidates {

    /**
     * 
     * The different types
     * 
     */
    enum CandidateSource {
        INSTANCES_IN_SPECIFIC_SCENE, LIST_OF_SCENES
    };

    /**
     * Utility function for adding a candidate with a reference bias to a
     * collection of candidates. The new referenceBias will be the maximum
     * between them.
     * 
     * @param candidates
     * @param candidate
     * @param referenceBias
     */
    private static void addCandidateReference(List<rrCandidate> candidates,
            Instance candidate, double referenceBias, rrContext rrc) {
        for (rrCandidate cand : candidates) {
            if (cand.getInstance().equals(candidate)) {
                if (referenceBias > cand.getAssignedScore()) {
                    cand.setAssignedScore(referenceBias);
                    return;
                }
            }
        }
        // if we got here, the candidate was not found
        rrCandidate cand = new rrCandidate(candidate, rrc);
        cand.setAssignedScore(referenceBias);
        candidates.add(cand);
    }

    /**
     * Collect the candidates for a direct reference resolution: omnibus
     * function, basically calling the other functions based on the current
     * situation
     * 
     * @param agent
     * @param coRef
     * @param vi
     * @param part
     * @param scene
     *            the scene in which we will look up, providing that it is a
     *            scene lookup
     * @return a map of pairs of candidates and initial bias values
     */
    public static List<rrCandidate> collectCandidates(rrContext rrc) {
        List<rrCandidate> candidates = new ArrayList<>();
        CandidateSource cs =
                rrCollectCandidates.identifyCandidateSource(rrc.getAgent(),
                        rrc.getVerbsInVi(), rrc.getPartInVi(),
                        rrc.getCoDirect());
        switch (cs) {
        case LIST_OF_SCENES: {
            rrCollectCandidates.collectCandidatesListOfScenes(candidates,
                    rrc.getAgent(), rrc);
            return candidates;
        }
        case INSTANCES_IN_SPECIFIC_SCENE: {
            rrCollectCandidates.collectCandidatesInstancesInSpecificScene(
                    candidates, rrc.getAgent(), rrc.getScene(), rrc);
            return candidates;
        }
        }
        throw new Error("Should never reach here");
    }

    /**
     * Collects the candidates as the instances in a specific scene, with the
     * reference bias being the focus strenght
     * 
     * @param candidates
     * @param agent
     * @param scene
     * @param sceneStrenght
     */
    private static void collectCandidatesInstancesInSpecificScene(
            List<rrCandidate> candidates, Agent agent, Instance scene,
            rrContext rrc) {
        Focus fc = agent.getFocus();
        List<Instance> instancesInScene = scene.getSceneMembers();
        for (Instance candidate : instancesInScene) {
            double referenceBias = fc.getSalience(candidate, EnergyColors.FOCUS_INSTANCE);
            rrCollectCandidates.addCandidateReference(candidates, candidate,
                    referenceBias, rrc);
        }
    }

    /**
     * Collects the candidates as the list of all scenes. The current scene will
     * have a bias of 1.0 * focus value, the others 0.8 * focus value.
     * 
     * @param candidates
     *            - an existing <candidate, bias> map which will be updated
     * @param agent
     */
    static void collectCandidatesListOfScenes(List<rrCandidate> candidates,
            Agent agent, rrContext rrc) {
        Focus fc = agent.getFocus();
        for (Instance fi : fc.getSceneList(EnergyColors.FOCUS_INSTANCE)) {
            double fiValue = fc.getSalience(fi, EnergyColors.FOCUS_INSTANCE);
            if (fc.getCurrentScene().equals(fi)) {
                rrCollectCandidates.addCandidateReference(candidates, fi,
                        fiValue, rrc);
            } else {
                rrCollectCandidates.addCandidateReference(candidates, fi,
                        0.8 * fiValue, rrc);
            }
        }
    }

    /**
     * Returns a map of <candidate, bias> pairs. The candidates are instances
     * which are in a specified relation with the currentRoot instance.
     * 
     * TODO: In the current implementation, the bias is the focus strenght of
     * the candidate instances. This will need to be weighted by the strength of
     * the match between the voRelation and the actual relation which exists, as
     * well as the strenght of the relation which exists!!!
     * 
     * @param agent
     * @param currentRoot
     * @param voRelation
     * @return
     */
    public static List<rrCandidate> collectCandidatesRelational(Agent agent,
            Instance currentRoot, VerbOverlay voRelation, rrContext rrc) {
        List<rrCandidate> candidates = new ArrayList<>();
        Focus fc = agent.getFocus();
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (!ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
                continue;
            }
            if (vi.getSubject() != currentRoot) {
                continue;
            }
            if (!rrCollectCandidates.compatibleRelations(agent, vi.getVerbs(),
                    voRelation)) {
                continue;
            }
            rrCandidate cand = new rrCandidate(vi.getObject(), rrc);
            cand.setAssignedScore(fc.getSalience(vi.getObject(),
                    EnergyColors.FOCUS_VI));
            candidates.add(cand);
        }
        return candidates;
    }

    /**
     * Returns true if there is some commonality between the two relations
     * (minus meta-verbs)
     * 
     * FIXME: this should be a weight!
     * 
     * @param agent
     * @param vo1
     * @param vo2
     * @return
     */
    private static boolean compatibleRelations(Agent agent, VerbOverlay vo1,
            VerbOverlay vo2) {
        VerbOverlay temp1 =
                AbstractSaMvRelation.extractRelation(agent, vo1).getKey();
        VerbOverlay temp2 =
                AbstractSaMvRelation.extractRelation(agent, vo2).getKey();
        return temp1.overlapEnergy(temp2) > 0;
    }

    /**
     * Identifies the source from which the candidates are supposed to be
     * resolved. If depends on the verb (whether it explicitly indicates what it
     * expects), the position of the instance (subject or object) and the
     * reference CO.
     * 
     * @return
     */
    private static CandidateSource identifyCandidateSource(Agent agent,
            VerbOverlay vo, ViPart viPart, ConceptOverlay coRef) {
        // If we are the object of an inquit, the candidates are the scenes
        if (viPart.equals(ViPart.QuoteScene)) {
            return CandidateSource.LIST_OF_SCENES;
        }
        //
        // SubjectIsScene is a special verb which makes the subject refer to a
        // scene
        //
        if (Hardwired.contains(agent, vo, Hardwired.VR_SUBJECT_IS_SCENE)
                && viPart.equals(ViPart.Subject)) {
            return CandidateSource.LIST_OF_SCENES;
        }
        //
        // ObjectIsScene is a special verb which makes the object refer to a
        // scene
        //
        if (Hardwired.contains(agent, vo, Hardwired.VR_OBJECT_IS_SCENE)
                && viPart.equals(ViPart.Object)) {
            return CandidateSource.LIST_OF_SCENES;
        }
        //
        // If the reference CO specifies a scene, look for a scene
        //
        if (InstanceClassifier.decideSceneCo(coRef, agent)) {
            return CandidateSource.LIST_OF_SCENES;
        }
        // for everything else: instances in the scene
        return CandidateSource.INSTANCES_IN_SPECIFIC_SCENE;
    }

}
