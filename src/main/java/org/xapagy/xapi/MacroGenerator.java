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
package org.xapagy.xapi;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.autobiography.ABStory;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Hardwired.SceneRelation;
import org.xapagy.instances.Instance;
import org.xapagy.ui.TextUi;

/**
 * Generates Xapi code corresponding to the expansions of the macro statements.
 * 
 * @author Ladislau Boloni
 * Created on: Feb 28, 2012
 */
public class MacroGenerator {

    /**
     * Generate a new scene, with a specific set of relations between the
     * current scene and the old scene
     * @param newSceneName
     *            - the label to be attached to the new scene
     * @param addSummary TODO
     * @param sceneRelationLabel
     *            - should be one of Hardwired.LABEL_SCENEREL_xxx
     * @return
     */
    public static final ABStory generateNewScene(Agent agent,
            String newSceneName, boolean makeCurrent, boolean makeOnly,
            boolean addSummary,
            String sceneRelationLabel, List<SimpleEntry<String, String>> participants) {
        String location = null;
        ABStory retval = new ABStory();
        String currentSceneName = "";
        // create an identifier for the current scene such that we can return to
        // it. First find a proper name, which is the old way
        Instance currentScene = agent.getFocus().getCurrentScene();
        for (SimpleEntry<Concept, Double> entry : currentScene.getConcepts()
                .getList()) {
            String conceptName = entry.getKey().getName();
            if (conceptName.startsWith("\"")) {
                currentSceneName = conceptName;
                break;
            }
        }
        // now add all the labels
        for (String label : currentScene.getConcepts().getLabels()) {
            currentSceneName += " " + label;
        }
        //
        // End of creation the current scene identifier
        //
        String tmp;
        // we need to do this for the often encountered situation that the new
        // scene has the same label as the previous one (eg. reality), so it
        // would
        // not be selected.
        if (makeOnly) {
            // tmp = "A scene \"UsedForCleanupOnly\" / is-only-scene.";
            tmp = "A scene #UsedForCleanupOnly / is-only-scene.";
            retval.add(tmp);
        }
        tmp = "A scene " + newSceneName + " / exists.";
        retval.add(tmp);

        SceneRelation sr =
                Hardwired.parseSceneRelationLabel(sceneRelationLabel);
        if (sr != SceneRelation.NONE) {
            if (makeOnly) {
                TextUi.abort("One cannot use a relation with an make scene Only. Make it only later.");
            }
            tmp =
                    "The scene " + currentSceneName + " / "
                            + Hardwired.getSceneRelationWord(sr)
                            + " / the scene " + newSceneName + ".";
            retval.add(tmp);
        }
        if (!participants.isEmpty() || makeOnly) {
            // switch to the new scene
            if (!makeOnly) {
                // tmp = "The scene " + newSceneName + " / is-current-scene.";
                tmp = "$ChangeScene " + newSceneName;
            } else {
                tmp = "The scene " + newSceneName + " / is-only-scene.";
            }
            retval.add(tmp);
            // create the new items and link them
            for (SimpleEntry<String, String> entry : participants) {
                String newItem = entry.getKey();
                if (newItem.startsWith("*")) {
                    newItem = newItem.substring("*".length());
                    location = newItem;
                }
                tmp = "A " + newItem + " / exists.";
                retval.add(tmp);
                // link back
                String linkBack = entry.getValue();
                if (linkBack != null) {
                    tmp =
                            "The " + newItem + " / is-identical / " + linkBack
                                    + " -- in -- " + "scene "
                                    + currentSceneName + ".";
                    retval.add(tmp);
                    // bidirectional identity?
                    tmp =
                            "The " + linkBack + " -- in -- " + "scene "
                                    + currentSceneName + " / is-identical / "
                                    + "the " + newItem + ".";
                    retval.add(tmp);
                }
            }
            //
            // Set the location
            //
            if (location != null) {
                tmp =
                        "Scene " + newSceneName
                                + " / current-location-is / the " + location
                                + ".";
                retval.add(tmp);
            }
            //
            // Make the created scene the current scene
            //
            if (!makeCurrent) {
                // switch to the old scene
                //tmp = "The scene " + currentSceneName + " / is-current-scene.";
                tmp = "$ChangeScene " + currentSceneName;
                retval.add(tmp);
            }
        }
        //
        //  If adding a summary is on, add a summary scene - won't change the current scene
        //
        if (addSummary) {            
            String summaryLabel = newSceneName + "_Summary";
            retval.add("Scene / clone-scene / scene " + summaryLabel  + ".");
            retval.add("Scene / has-view / scene " + summaryLabel + ".");
        }
        return retval;
    }
}
