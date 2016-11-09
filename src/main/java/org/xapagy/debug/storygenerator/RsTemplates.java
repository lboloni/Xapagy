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
package org.xapagy.debug.storygenerator;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.autobiography.ABStory;

/**
 * A number of functions used to help the generation of more complex stories for
 * the RecordedStory objects.
 * 
 * @author Ladislau Boloni
 * Created on: Feb 9, 2012
 */
public class RsTemplates {

    /**
     * The label typically used in a recorded story for the direct scene
     * ("reality")
     */
    public static final String DIRECT = "direct";
    /**
     * The label typically used in recorded story for the quoted scene
     */
    public static final String QUOTED = "quoted";

    /**
     * Generate a story which is narrated by the narrator and happens in the
     * scene labeled with the quoted string
     * 
     */
    public static ABStory generateNarratedStory(RecordedStory rs,
            String narrator, String sceneQuoted, ABStory storyEmbedded) {
        RsScene rsdsQuoted = rs.getRsScene(sceneQuoted);
        ABStory retval = new ABStory();
        for (String line : storyEmbedded.getLines()) {
            String newLine = "The N1 / says in S1" + " // " + line;
            retval.add(newLine);
        }
        retval.subs("S", rsdsQuoted.getLabelScene());
        retval.subs("N", narrator);
        return retval;
    }

    /**
     * Generates the story taking part in the direct scene.
     * 
     * Currently it only allows for two instances, which perform a list of S-V-O
     * actions in which the two instances perform actions alternating.
     * 
     */
    public static ABStory generateOneWayAction(String instance1,
            String instance2, List<String> actionVerbs) {
        ABStory retval = new ABStory();
        List<String> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        retval =
                StoryTemplates.templateOneWayAction(actionVerbs.size(), "I",
                        "V", 0);
        retval.subs("I", instances);
        retval.subs("V", actionVerbs);
        return retval;
    }

    /**
     * Generates the story taking part in the direct scene.
     * 
     * Currently it only allows for two instances, which perform a list of S-V-O
     * actions in which the two instances perform actions alternating.
     * 
     * @param instance1 - the way in which all the references to the first instance is done
     * @param instance2 - the way in which all the reference to the second instances are done
     * @param actionVerbs - the list of action verbs which will happen
     * 
     */
    public static ABStory generateReciprocalAction(String instance1,
            String instance2, List<String> actionVerbs) {
        ABStory retval = new ABStory();
        List<String> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);
        retval =
                StoryTemplates.templateReciprocalAction(actionVerbs.size(),
                        "I", "V", 0);
        retval.subs("I", instances);
        retval.subs("V", actionVerbs);
        return retval;
    }

    /**
     * Replaces the instances I1... in a story, with instance labels indexed in
     * the story
     * 
     * @param abs
     * @param rs
     * @param sceneLabel
     * @param instanceNumbers
     */
    public static void substituteInstances(ABStory abs, RecordedStory rs,
            String sceneLabel, int... instanceNumbers) {
        RsScene rsds = rs.getRsScene(sceneLabel);
        List<String> subsValue = new ArrayList<>();
        for (int i : instanceNumbers) {
            subsValue.add(rsds.getInstanceLabels().get(i));
        }
        abs.subs("I", subsValue);
    }

}
