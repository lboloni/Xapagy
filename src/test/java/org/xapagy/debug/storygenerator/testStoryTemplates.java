/*
   This file is part of the Xapagy project
   Created on: Jul 7, 2011
 
   org.xapagy.debug.testSGTemplates
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug.storygenerator;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.StoryTemplates.SceneSetting;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;

/**
 * A series of tests for the visual inspection of the stories generated in
 * StoryTemplates
 * 
 * @author Ladislau Boloni
 * 
 */
public class testStoryTemplates {

    // if true, the printing will actually happen
    public static boolean print = true;
    
    
    /**
     * Tests a simple reciprocal action
     */
    @Test
    public void testSimpleNewCharacter() {
        TestHelper.testStart("Generate simple new character");
        // Runner r = new Runner("Iliad");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        ABStory storyTemplate =
                StoryTemplates.templateNewCharacter(SceneSetting.NEW_ONLY, 2,
                        2, "S", "I", "A", null, null);
        String[] actions =
                { "wa_v_av20", "wa_v_av20", "wa_v_av20", "wa_v_av20" };
        ABStory story = new ABStory(storyTemplate);
        story.subs("S", r.properNameGenerator.generateProperNames(1));
        story.subs("I", r.properNameGenerator.generateProperNames(3));
        story.subs("A", actions);
        Formatter fmt = new Formatter();
        fmt.add(story);
        if (print) {
            TextUi.println(fmt);
        }
        TestHelper.testDone();
    }

    /**
     * Tests a simple reciprocal action
     */
    @Test
    public void testSimpleReciprocal() {
        TestHelper.testStart("Generate simple reciprocal");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // Runner r = new Runner("Iliad");
        // r.printOn = true;
        ABStory storyTemplate =
                StoryTemplates.templateNewInstancesReciprocalAction(
                        SceneSetting.NEW_ONLY, 4, "S", "I", "A", null);
        String[] actions =
                { "wa_v_av20", "wa_v_av20", "wa_v_av20", "wa_v_av20" };
        // String[] actions = { "hits", "strikes", "kicks", "cuts" };
        ABStory story = new ABStory(storyTemplate);
        story.subs("S", r.properNameGenerator.generateProperNames(1));
        story.subs("I", r.properNameGenerator.generateProperNames(2));
        story.subs("A", actions);
        Formatter fmt = new Formatter();
        fmt.add(story);
        if (print) {
            TextUi.println(fmt);
        }
        TestHelper.testDone();
    }

    
    /**
     * Tests a simple reciprocal action, with a long story (longer than 10)
     */
    @Test
    public void testLongStory() {
        TestHelper.testStart("Generate simple reciprocal");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // Runner r = new Runner("Iliad");
        // r.printOn = true;
        ABStory storyTemplate =
                StoryTemplates.templateNewInstancesReciprocalAction(
                        SceneSetting.NEW_ONLY, 12, "S", "I", "A", null);
        String[] actions =
                { "wa_v_av20", "wa_v_av21", "wa_v_av22", "wa_v_av23", 
                "wa_v_av20", "wa_v_av21", "wa_v_av22", "wa_v_av23",
                "wa_v_av20", "wa_v_av21", "wa_v_av22", "wa_v_av23",
                "wa_v_av20", "wa_v_av21", "wa_v_av22", "wa_v_av23"};
        // String[] actions = { "hits", "strikes", "kicks", "cuts" };
        ABStory story = new ABStory(storyTemplate);
        story.subs("S", r.properNameGenerator.generateProperNames(1));
        story.subs("I", r.properNameGenerator.generateProperNames(2));
        story.subs("A", actions);
        Formatter fmt = new Formatter();
        fmt.add(story);
        if (print) {
            TextUi.println(fmt);
        }
        TestHelper.testDone();
    }
    
}
