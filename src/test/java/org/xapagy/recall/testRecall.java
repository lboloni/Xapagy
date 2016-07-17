/*
   This file is part of the Xapagy project
   Created on: Jun 22, 2011
 
   org.xapagy.recall.testRecallCase1PureRecall
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.recall;

import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;

/**
 * @author Ladislau Boloni
 * 
 */
public class testRecall {

    /**
     * Recall case 1: pure recall.
     * 
     * This test verifies whether the Xapagy agent can recall a story for which
     * it has a pure memory.
     */
    @Test
    public void testPureRecall() {
        String description = "Pure recall";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.COMPACT);
        r.exec("$Include 'P-FocusOnly'");
        ABStory story = createAchillesHector("#one", false, false, null);
        r.exec(story);
        r.exec("$Include 'P-All'");
        r.exec("$CreateScene #two CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        // r.debugMode();
        @SuppressWarnings("unused")
        List<VerbInstance> recalls = r.exec("Scene / recall narrate.");
        r.ah.sequenceOfVerbsInRecentHistory(ChoiceType.CONTINUATION, "wa_v_av41",
                "wa_v_av42", "wa_v_av43");
        TestHelper.testDone();
    }

    /**
     * Competitive recall: the agent had seen variant 1 3 times, variant 2 2
     * times, let us see what it is really recalling
     * 
     * FIXME: what it is recalling is a mashup
     */
    @Test
    public void testCompetitiveRecall() {
        String description = "Recall Case 2: competitive recall";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.COMPACT);
        r.exec("$Include 'P-FocusOnly'");
        ABStory story = null;
        story = createAchillesHector("#one", false, false, null);
        r.exec(story);
        story = createAchillesHector("#two", true, false, null);
        r.exec(story);
        story = createAchillesHector("#three", false, false, null);
        r.exec(story);
        story = createAchillesHector("#four", true, false, null);
        r.exec(story);
        story = createAchillesHector("#five", false, false, null);
        r.exec(story);
        r.exec("$Include 'P-All'");
        r.exec("$CreateScene #two CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        // r.debugMode();
        @SuppressWarnings("unused")
        List<VerbInstance> recalls = r.exec("Scene / recall narrate.");
        //r.ah.sequenceOfVerbsInRecentHistory(ChoiceType.CONTINUATION, "v_av41",
        //        "v_av42", "v_av43");
        TestHelper.testIncomplete();
    }

    /**
     * Self shadowing: shows how multiple recalls strengthen a recall
     * 
     */
    // @Test
    public void testSelfShadowing() {
        String description = "Self shadowing recall";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.COMPACT);
        r.exec("$Include 'P-FocusOnly'");
        ABStory story = null;
        story = createAchillesHector("#one", false, false, null);
        r.exec(story);
        // recall:
        for (int i = 0; i != 3; i++) {
            r.exec("$Include 'P-All'");
            r.exec("$CreateScene #two CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
            r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
            @SuppressWarnings("unused")
            List<VerbInstance> recalls = r.exec("Scene / recall narrate.");
        }

        // r.debugMode();
        //List<VerbInstance> recalls = r.exec("Scene / recall narrate.");
        //r.ah.sequenceOfVerbsInRecentHistory(ChoiceType.CONTINUATION, "v_av41",
         //       "v_av42", "v_av43");
        TestHelper.testIncomplete();
    }


    /**
     *  
     *  Context based recall
     * 
     */
    //@Test
    public void testContextBased() {
        String description = "Self shadowing recall";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.COMPACT);
        r.exec("$Include 'P-FocusOnly'");
        ABStory story = null;
        // variant one happens when context vr_rel1
        story = createAchillesHector("#one", false, false, "vr_rel1");
        r.exec(story);
        // variant two happens when context vr_rel2
        story = createAchillesHector("#one", true, false, "vr_rel2");
        r.exec(story);
        // now let us recall one or the other
        r.exec("$Include 'P-All'");
        r.exec("$CreateScene #two CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Achilles'/ wcr_vr_rel1 / 'Hector'.");
        // FIXME: with this one gets into an infinite loop, why?
        // r.exec("'Achilles'/ wcr_vr_rel2 / 'Hector'.");
        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        @SuppressWarnings("unused")
        List<VerbInstance> recalls = r.exec("Scene / recall narrate.");        
    }

    
    
    /**
     * Creates a story of achilles and hector in several variations
     * @param sceneLabel
     * @param variant
     * @param hasContext
     * @param contextRelation TODO
     * @return
     */
    public static ABStory createAchillesHector(String sceneLabel,
            boolean variant, boolean hasContext, String contextRelation) {
        ABStory story = new ABStory();
        story.add("$CreateScene " + sceneLabel
                + " CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        if (hasContext) {
            story.add("'Achilles'/ wcr_" + contextRelation + " / 'Hector'.");
        }
        story.add("'Achilles'/ wa_v_av40 / 'Hector'.");
        story.add("'Hector'/ wa_v_av41 / 'Achilles'.");
        story.add("'Achilles'/ wa_v_av42 / 'Hector'.");
        if (!variant) {
            story.add("'Hector'/ wa_v_av43 / 'Achilles'.");
        } else {
            story.add("'Hector'/ wa_v_av44 / 'Achilles'.");
            story.add("'Achilles'/ wa_v_av43 / 'Hector'.");
        }
        story.add("----");
        return story;
    }

}
