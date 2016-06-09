/*
   This file is part of the Xapagy project
   Created on: Nov 3, 2012
 
   org.xapagy.instances.SceneRelationHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.instances;

import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.ViStructureHelper.ViPart;

/**
 * Helper functions for handling the
 * 
 * @author Ladislau Boloni
 * 
 */
public class SceneRelationHelper {

    /**
     * Returns true if this scene is a fictional scene related to the reality
     * 
     * @param sceneFictional
     *            - the view which is the fictional future
     * @param sceneReality
     *            - the scene which is the reality (related to the fictional)
     * @return
     */
    public static boolean isFictionalScene(Agent agent,
            Instance sceneFictional, Instance sceneReality) {
        return RelationHelper.decideRelation(false, agent,
                Hardwired.VR_SCENE_FICTIONAL_FUTURE, sceneReality,
                sceneFictional);
    }

    /**
     * Returns true if this sceneSuccessor is a successor scene of
     * scenePredecessor
     * 
     * @param sceneSuccessor
     * @param scenePredecessor
     * @return
     */
    public static boolean isSuccessorScene(Agent agent,
            Instance sceneSuccessor, Instance scenePredecessor) {
        return RelationHelper
                .decideRelation(false, agent, Hardwired.VR_SCENE_SUCCESSION,
                        scenePredecessor, sceneSuccessor);
    }

    /**
     * Returns true if this sceneSuccessor is a successor scene of
     * scenePredecessor
     * 
     * @param sceneView
     *            - the view which is the view
     * @param sceneReality
     *            - the scene which is the scene viewed (the reality)
     * @return
     */
    public static boolean isViewScene(Agent agent, Instance sceneView,
            Instance sceneReality) {
        return RelationHelper.decideRelation(false, agent,
                Hardwired.VR_SCENE_VIEW, sceneReality, sceneView);
    }

    /**
     * Returns the scenes which have preceeded this one in a succession...
     * 
     * @return
     */
    public static Set<Instance> previousChainOfScenes(Agent agent,
            Instance rootScene) {
        int recursionCount = 3;
        return RelationHelper.getRecursiveRelationSpecificPart(agent,
                Hardwired.VR_SCENE_SUCCESSION, rootScene, ViPart.Object,
                ViPart.Subject, recursionCount);
    }
}
