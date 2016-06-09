/*
   This file is part of the Xapagy project
   Created on: Feb 5, 2012
 
   org.xapagy.activity.shadowmaintenance.testDaShmViMatchingHead
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.ProperNameGenerator;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsGenerator;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
 */
public class testDaShmViMatchingHead {

    /**
     * These are a series of tests for the instance matching head and vi
     * matching head enabled - they are using the artificial domain, but do not
     * refer to any bai's which had previous stories
     */
    @Test
    public void testIdenticalStory() {
        String description = "tests with story identical";
        TestHelper.testStart(description);
        // the parameter we are going to test
        Runner r = new Runner("Core");
        ABStory p = new ABStory("$Include 'P-FocusAndShadows'");
        //
        ProperNameGenerator sg = new ProperNameGenerator();
        // empty history
        List<RecordedStory> history = new ArrayList<>();

        //
        // STORY: identical, ACTORS: base level matching
        //
        RecordedStory st =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        RecordedStory st2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        r = ArtificialDomain.createAabConcepts();
        RsTestingUnit stu = new RsTestingUnit(p, history, st, p, st2);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: identical, ACTORS: base level matching");
        TextUi.println(stu);
        //
        // STORY: identical, ACTORS: base level and label matching
        //
        st =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        st.addRandomPropernames(sg);
        st2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        st2.addRandomPropernames(sg);
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st, p, st2);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: identical, ACTORS: base level and label matching");
        TextUi.println(stu);
        //
        // STORY: identical, ACTORS: base level identical, different proper
        // names
        //
        RecordedStory stp2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        stp2.addRandomPropernames(sg);
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st, p, stp2);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: identical,  ACTORS: base level identical, different labels");
        TextUi.println(stu);
        //
        // STORY: identical, ACTORS: base level different
        //
        RecordedStory st3 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        RecordedStory st4 =
                RsGenerator.generateReciprocal("w_c_bai25", "w_c_bai26",
                        Arrays.asList("wa_v_av40", "wa_v_av40", "wa_v_av40"));
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st3, p, st4);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: identical,  ACTORS: base level different");
        TextUi.println(stu);
        //
        // STORY: identical, ACTORS: base level opposing
        //
        st3 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        st4 =
                RsGenerator.generateReciprocal("w_c_bai22", "w_c_bai21",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st3, p, st4);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: identical,  ACTORS: base level opposing");
        TextUi.println(stu);
        //
        // STORY: identical with relations, ACTORS: base level matching
        // FIXME: the relations do not appear to improve the VI match, but they
        // improve the match of the instances??? maybe because of the
        // bidirectionality???
        // not good!!!
        //
        st3 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22", Arrays
                        .asList("CreateRelation wv_vr_rel1",
                                "CreateRelation wv_vr_rel2", "wa_v_av40",
                                "wa_v_av40", "wa_v_av40"));
        st4 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22", Arrays
                        .asList("CreateRelation wv_vr_rel1",
                                "CreateRelation wv_vr_rel2", "wa_v_av40",
                                "wa_v_av40", "wa_v_av40"));
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st3, p, st4);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: identical with relations, ACTORS: base level matching");
        TextUi.println(stu);
    }

    /**
     * 
     */
    @Test
    public void testNonIdenticalStory() {
        String description = "more tests, where the story is not identical";
        TestHelper.testStart(description);
        // current history used: empty
        ABStory p = new ABStory("$Include 'P-All-NoInternal'");
        // empty history
        List<RecordedStory> history = new ArrayList<>();
        //
        // STORY: identical, ACTORS: base level matching
        //
        RecordedStory st =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        RecordedStory st2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        Runner r = ArtificialDomain.createAabConcepts();
        RsTestingUnit stu = new RsTestingUnit(p, history, st, p, st2);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: identical, ACTORS: base level matching");
        TextUi.println(stu);
        //
        // STORY: 2-3 flipped, ACTORS: base level matching
        //
        st2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st, p, st2);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: 2-3 flipped, ACTORS: base level matching");
        TextUi.println(stu);
        //
        // STORY: 1-2-1-2-3, ACTORS: base level matching
        //
        st2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22", Arrays
                        .asList("wa_v_av40", "wa_v_av41", "wa_v_av40",
                                "wa_v_av41", "wa_v_av42"));
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st, p, st2);
        TextUi.printLabeledSeparator("STORY: 1-2-1-2-3, ACTORS: base level matching");
        TextUi.println(stu);
        //
        // STORY: r-r-1-2-3, ACTORS: base level matching
        //
        st2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22", Arrays
                        .asList("CreateRelation wv_vr_rel1",
                                "CreateRelation wv_vr_rel2", "wa_v_av40",
                                "wa_v_av41", "wa_v_av42"));
        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st, p, st2);
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: 1-2-1-2-3, ACTORS: base level matching");
        TextUi.println(stu);
        //
        // STORY: interstitial actions
        //
        st2 =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av42", "wa_v_av43"));
        ABStory postStory = new ABStory();
        postStory.add("The w_c_bai21 / is-a / w_c_bai25.");
        st2.addStory(postStory);

        r = ArtificialDomain.createAabConcepts();
        stu = new RsTestingUnit(p, history, st, p, st2);
        // Adding an extra story
        // stu.rsFocus.rsdsDirect.postStory.add("The w_c_bc1 / is-a / w_c_bc10.");
        r.tso.setTrace();
        stu.runAll(r);
        TextUi.printLabeledSeparator("STORY: extra action, ACTORS: base level matching");
        TextUi.println(stu);
    }

}
