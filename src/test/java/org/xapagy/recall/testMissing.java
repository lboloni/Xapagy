/*
   This file is part of the Xapagy project
   Created on: Jul 1, 2011
 
   org.xapagy.recall.testMissing
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.recall;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.Xapagy;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.ProperNameGenerator;
import org.xapagy.debug.storygenerator.StoryTemplates;
import org.xapagy.debug.storygenerator.StoryTemplates.SceneSetting;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.util.DirUtil;

/**
 * @author Ladislau Boloni
 * 
 */
public class testMissing {

    public static String[] actions =
            { "wa_v_av40", "wa_v_av41", "wa_v_av42", "wa_v_av43" };
    public static File agentFile;
    // public static final String outputDirName = "output";
    public static ABStory storyTemplate =
            StoryTemplates.templateNewInstancesReciprocalAction(
                    SceneSetting.NEW_ONLY, 4, "S", "I", "A", null);

    /**
     * Testing the missing action in a pure recall setting
     * 
     * @throws IOException
     */
    @Test
    public void testMissingActionPureRecall() throws IOException {
        String description = "Missing action for pure recall.";
        TestHelper.testStart(description);
        String starterFile = ArtificialDomain.createAabConceptsFile();
        String dir = "output/testMissing/actionPureRecall/";
        String intermediateAgent = dir + "Intermediate.xa";
        String finalAgent = dir + "Final.xa";
        String warmupStoryFile = dir + "warmupStory.xapi";
        String finalStoryFile = dir + "finalStory.xapi";
        // create the warmup story
        ProperNameGenerator sg = new ProperNameGenerator();
        ABStory story = new ABStory(testMissing.storyTemplate);
        story.subs("S", sg.generateProperNames(1));
        story.subs("I", sg.generateProperNames(2));
        story.subs("A", testMissing.actions);
        story.isolate();
        story.saveTo(warmupStoryFile);
        Xapagy.main("P-FocusOnly", warmupStoryFile, "--input-agent",
                starterFile, "--output-agent", intermediateAgent);
        // now, the main story
        story = new ABStory(testMissing.storyTemplate);
        story.subs("S", "\"Troy\"");
        story.subs("I", "\"Hector\"", "\"Achilles\"");
        story.subs("A", testMissing.actions);
        story.deleteLine(-3);
        // story.insertAfter(3, "$DebugHere");
        story.saveTo(finalStoryFile);
        // story.isolate();
        // TextUi.println(story);
        Xapagy.main("P-All", finalStoryFile, "--input-agent",
                intermediateAgent, "--output-agent", finalAgent);
        // ability to load the final file and do tests on it
        Runner r = new Runner(new File(finalAgent));
        //r.exec("-");
        //r.exec("$DebugHere");
        //r.exec("-");
        //r.exec("-");
        //r.exec("-");
        r.ah.sequenceOfVerbsInRecentHistory(ChoiceType.MISSING_ACTION,
                "wv_v_av41");
        TestHelper.testDone();
    }

    /**
     * Testing the missing relation in a pure recall setting
     * 
     * @throws IOException
     */
    @Test
    public void testMissingInterpretation() throws IOException {
        String description = "Missing interpretation.";
        TestHelper.testStart(description);
        String starterFile = ArtificialDomain.createAabConceptsFile();
        String dir = "output/testMissing/interpretation/";
        DirUtil.guaranteeDirectory(dir);
        String intermediateAgent = dir + "Intermediate.xa";
        String finalAgent = dir + "Final.xa";
        String warmupStoryFile = dir + "warmupStory.xapi";
        String finalStoryFile = dir + "finalStory.xapi";
        ProperNameGenerator sg = new ProperNameGenerator();
        ABStory story = new ABStory(testMissing.storyTemplate);
        story.subs("S", sg.generateProperNames(1));
        // adding a relation
        // this is what was story.add(5, "The I1 / thus defeats / the I2.");
        story.insertAfter(4, "The I1 / thus wa_v_av44 / the I2.");
        // / story.insertAfter(4, "The I1 / wa_v_av44 / the I2.");
        // TextUi.println(PpStory.pp("story", story));
        // System.exit(2);
        story.subs("I", sg.generateProperNames(2));
        story.subs("A", testMissing.actions);
        story.isolate();
        story.saveTo(warmupStoryFile);
        // TextUi.println(story);
        Xapagy.main("--input-agent", starterFile, "--output-agent",
                intermediateAgent, "P-FocusOnly", warmupStoryFile);
        // now, for the real story
        story = new ABStory(testMissing.storyTemplate);
        story.subs("S", "\"Troy\"");
        story.subs("I", "\"Hector\"", "\"Achilles\"");
        story.subs("A", testMissing.actions);
        // story.insertAfter(3, "$DebugHere");
        story.saveTo(finalStoryFile);
        // story.isolate();
        // TextUi.println(story);
        Xapagy.main("P-All", finalStoryFile, "--input-agent",
                intermediateAgent, "--output-agent", finalAgent);
        Runner r = new Runner(new File(finalAgent));
        r.ah.sequenceOfVerbsInRecentHistory(ChoiceType.MISSING_ACTION,
                "wv_v_av44");
        TestHelper.testDone();
    }

    /**
     * Testing the missing relation in a pure recall setting
     * @throws IOException 
     */
    // @Test
    public void testMissingRelationPureRecall() throws IOException {
        String description = "Missing relation for pure recall.";
        TestHelper.testStart(description);
        String starterFile = ArtificialDomain.createAabConceptsFile();
        String dir = "output/testMissing/interpretation/";
        DirUtil.guaranteeDirectory(dir);
        String intermediateAgent = dir + "Intermediate.xa";
        String finalAgent = dir + "Final.xa";
        String warmupStoryFile = dir + "warmupStory.xapi";
        String finalStoryFile = dir + "finalStory.xapi";

        ProperNameGenerator sg = new ProperNameGenerator();
        ABStory story = new ABStory(testMissing.storyTemplate);
        story.subs("S", sg.generateProperNames(1));
        // adding a relation
        // was story.add(4, "The I1 / hates / the I2.");
        story.insertAfter(3, "The I1 / CreateRelation wv_vr_rel1 / the I2.");
        // TextUi.println(PpStory.pp("story", story));
        // System.exit(2);
        story.subs("I", sg.generateProperNames(2));
        story.subs("A", testMissing.actions);

        story.isolate();
        story.saveTo(warmupStoryFile);
        // TextUi.println(story);
        Xapagy.main("--input-agent", starterFile, "--output-agent",
                intermediateAgent, "P-FocusOnly", warmupStoryFile);

        // and now, for the real story
        story = new ABStory(testMissing.storyTemplate);
        story.subs("S", "\"Troy\"");
        story.subs("I", "\"Hector\"", "\"Achilles\"");
        story.subs("A", testMissing.actions);
        story.saveTo(finalStoryFile);

        Xapagy.main("P-All", finalStoryFile, "--input-agent",
                intermediateAgent, "--output-agent", finalAgent);
        Runner r = new Runner(new File(finalAgent));
        r.ah.sequenceOfVerbsInRecentHistory(ChoiceType.MISSING_RELATION,
                "wv_vr_rel1");
        TestHelper.testDone();
    }

}
