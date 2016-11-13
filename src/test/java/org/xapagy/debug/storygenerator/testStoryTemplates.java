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
 * Created on: Jul 7, 2011
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
