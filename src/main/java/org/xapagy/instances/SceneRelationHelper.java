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
package org.xapagy.instances;

import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.ViStructureHelper.ViPart;

/**
 * Helper functions for handling the scene relations
 * 
 * @author Ladislau Boloni
 * Created on: Nov 3, 2012
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
