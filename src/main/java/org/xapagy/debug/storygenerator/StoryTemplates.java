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

import org.xapagy.autobiography.ABStory;

/**
 * Generate story templates, using prefixed values, which then need to be filled
 * in.
 * 
 * @author Ladislau Boloni
 * Created on: Jun 18, 2011
 */
public class StoryTemplates {

    /**
     * This variable type describes what kind of scene setting to be created
     * 
     * @author Ladislau Boloni
     * 
     */
    public enum SceneSetting {
        EXISTING_CURRENT, NEW_CURRENT, NEW_ONLY, NONE
    };

    /**
     * Adds a characterization, only called from inside here
     * 
     * @param name
     * @param words
     * @return
     */
    public static ABStory templateCharacterization(String name, String[] words) {
        ABStory story = new ABStory();
        if (words == null) {
            return story;
        }
        for (String word : words) {
            String line = name + "/ is-a /" + word + ".";
            story.add(line);
        }
        return story;
    }

    /**
     * Generates a story which creates a new scene, two agents and a series of
     * reciprocal defaultActions. Then it introduces a new agent, and a series
     * of reciprocal defaultActions between 2 and 3
     * 
     * @param rounds
     * @return
     */
    public static ABStory templateNewCharacter(SceneSetting sceneSetting,
            int rounds1, int rounds2, String scenePrefix,
            String instancePrefix, String actionPrefix, ABStory descriptors1,
            ABStory descriptors2) {
        ABStory story = new ABStory();
        story.add(StoryTemplates.templateSceneSetting(sceneSetting,
                scenePrefix, 1));
        story.add("A " + instancePrefix + "1 / exists.");
        story.add("A " + instancePrefix + "2 / exists.");
        if (descriptors1 != null) {
            story.add(descriptors1);
        }
        story.add(StoryTemplates.templateReciprocalAction(rounds1,
                instancePrefix, actionPrefix, 0));
        // now the new agent comes in
        story.add("A " + instancePrefix + "3 / exists.");
        if (descriptors2 != null) {
            story.add(descriptors2);
        }
        ABStory recip23 =
                StoryTemplates.templateReciprocalAction(rounds2, "tmp",
                        actionPrefix, rounds1);
        recip23.subs("tmp", instancePrefix + "2", instancePrefix + "3");
        story.add(recip23);
        return story;
    }

    /**
     * Generates a story which creates a new scene, two agents, and an action
     * 
     * @param rounds
     * @return
     */
    public static ABStory templateNewInstancesReciprocalAction(
            SceneSetting sceneSetting, int rounds, String scenePrefix,
            String instancePrefix, String actionPrefix, ABStory descriptors) {
        ABStory story = new ABStory();
        story.add(StoryTemplates.templateSceneSetting(sceneSetting,
                scenePrefix, 1));
        story.add("A " + instancePrefix + "1 / exists.");
        story.add("A " + instancePrefix + "2 / exists.");
        if (descriptors != null) {
            story.add(descriptors);
        }
        story.add(StoryTemplates.templateReciprocalAction(rounds,
                instancePrefix, actionPrefix, 0));
        return story;
    }

    /**
     * A series of defaultActions from instancePrefix1 to instancePrefix2
     * 
     * @param rounds
     * @param instancePrefix
     * @param actionPrefix
     * @param startAction
     * @return
     */
    public static ABStory templateOneWayAction(int rounds,
            String instancePrefix, String actionPrefix, int startAction) {
        ABStory story = new ABStory();
        for (int i = startAction; i != startAction + rounds; i++) {
            String subject = "The " + instancePrefix + "1";
            String object = "the " + instancePrefix + "2";
            story.add(subject + " / " + actionPrefix + (i + 1) + " / " + object
                    + ".");
        }
        return story;
    }

    /**
     * Generate a series of reciprocal defaultActions between existing instances
     * instancePrefix1 and instancePrefix2
     * 
     * @param rounds
     *            the number of rounds
     * @param instancePrefix
     * @param actionPrefix
     * @param startAction
     *            - the index at which the defaultActions prefixes start
     * @return
     */
    public static ABStory templateReciprocalAction(int rounds,
            String instancePrefix, String actionPrefix, int startAction) {
        ABStory story = new ABStory();
        for (int i = startAction; i != startAction + rounds; i++) {
            String subject;
            String object;
            if (i % 2 == 1) {
                subject = "The " + instancePrefix + "1";
                object = "the " + instancePrefix + "2";
            } else {
                subject = "The " + instancePrefix + "2";
                object = "the " + instancePrefix + "1";
            }
            story.add(subject + " / " + actionPrefix + (i + 1) + " / " + object
                    + ".");
        }
        return story;
    }

    /**
     * Creates a template for creating or changing a scene
     * 
     * @param sceneSetting
     * @param scenePrefix
     * @param existingSceneName
     * @return
     */
    private static ABStory templateSceneSetting(SceneSetting sceneSetting,
            String scenePrefix, int sceneIndex, String... existingSceneNames) {
        ABStory story = new ABStory();
        switch (sceneSetting) {
        case NONE:
            return story;
        case NEW_ONLY: {
            story.add("A scene " + scenePrefix + sceneIndex + " / exists.");
            story.add(scenePrefix + sceneIndex + " / is-only-scene.");
            return story;
        }
        case NEW_CURRENT: {
            story.add("A scene " + scenePrefix + sceneIndex + " / exists.");
            story.add("$ChangeScene " + scenePrefix + sceneIndex);
            return story;
        }
        case EXISTING_CURRENT: {
            story.add("$ChangeScene " + existingSceneNames[0]);
            return story;
        }
        }
        throw new Error("One should never get here.");
    }

}
