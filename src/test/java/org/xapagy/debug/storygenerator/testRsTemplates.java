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

import java.util.Arrays;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A series of tests (and examples) which illustrate the way in which we can
 * create more complex RecordedStories
 * 
 * @author Ladislau Boloni
 * Created on: Feb 9, 2012
 */
public class testRsTemplates {

    public static boolean printIt = true;
    public static boolean runIt = true;

    /**
     * Example of complex quotations and identities
     * 
     * This is a good example in the setting up of the recorded story: it is 
     * as complicated as it gets.
     * 
     * FIXME: takes too long to run
     */
    // @Test
    public void testComplexQuotations() {
        String description = "Complex quotations and identities";
        TestHelper.testStart(description);
        RecordedStory rs =
                new RecordedStory(RsTemplates.DIRECT, RsTemplates.QUOTED,
                        "third");
        rs.getRsScene(RsTemplates.DIRECT).setInstanceLabels("w_c_bai1",
                "w_c_bai2");
        rs.getRsScene(RsTemplates.QUOTED).setInstanceLabels("w_c_bai3",
                "w_c_bai4", "w_c_bai5");
        rs.getRsScene("third").setInstanceLabels("w_c_bai1", "w_c_bai2");
        rs.addSceneRelation(RsTemplates.DIRECT, RsTemplates.QUOTED,
                Hardwired.LABEL_SCENEREL_FICTIONAL_FUTURE);
        rs.addSceneRelation(RsTemplates.DIRECT, "third",
                Hardwired.LABEL_SCENEREL_VIEW);
        rs.addIdentity(RsTemplates.DIRECT, 0, RsTemplates.QUOTED, 0);
        rs.addIdentity(RsTemplates.DIRECT, 1, RsTemplates.QUOTED, 1);
        rs.addIdentity(RsTemplates.DIRECT, 0, "third", 0);
        rs.addIdentity(RsTemplates.DIRECT, 1, "third", 1);

        // the relations, to and from, as
        ABStory absRelationsFrom =
                RsTemplates.generateOneWayAction("w_c_bai1", "w_c_bai2", Arrays
                        .asList("CreateRelation wv_vr_rel1",
                                "CreateRelation wv_vr_rel2"));
        ABStory absRelationsTo =
                RsTemplates.generateOneWayAction("w_c_bai2", "w_c_bai1", Arrays
                        .asList("CreateRelation wv_vr_rel3",
                                "CreateRelation wv_vr_rel4"));
        // the actions in the direct scene
        ABStory absActions =
                RsTemplates.generateReciprocalAction("w_c_bai1", "w_c_bai2",
                        Arrays.asList("wa_v_av1", "wa_v_av2", "wa_v_av3"));
        //
        // Creating a story narrated by w_c_bai1 in QUOTED
        //
        ABStory absEmbedded =
                StoryTemplates.templateReciprocalAction(3, "I", "V", 0);
        // absEmbedded.subs("I", rs.getRsdScene(RsTemplates.QUOTED)
        // .getInstanceLabels());
        RsTemplates.substituteInstances(absEmbedded, rs, RsTemplates.QUOTED, 0,
                1);
        absEmbedded.subs("V", "wa_v_av4", "wa_v_av4", "wa_v_av4");
        ABStory absNarrated =
                RsTemplates.generateNarratedStory(rs, "w_c_bai1",
                        RsTemplates.QUOTED, absEmbedded);
        //
        // Creating a story narrated by w_c_bai1 in third - but in the opposite
        // order
        //
        ABStory absEmbedded2 =
                StoryTemplates.templateReciprocalAction(3, "I", "V", 0);
        // absEmbedded2.subs("I", Arrays.asList("w_c_bai2", "w_c_bai1"));
        RsTemplates.substituteInstances(absEmbedded2, rs, "third", 1, 0);
        absEmbedded2.subs("V", "wa_v_av4", "wa_v_av4", "wa_v_av4");
        ABStory absNarrated2 =
                RsTemplates.generateNarratedStory(rs, "w_c_bai1", "third",
                        absEmbedded2);
        //
        // now, add all the stories to the recorded story
        //
        rs.addStory(absRelationsFrom);
        rs.addStory(absRelationsTo);
        rs.addStory(absActions);
        rs.addStory(absNarrated);
        rs.addStory(absNarrated2);
        Runner r =
                ArtificialDomain.doIt("Three scenes, various quotings", rs,
                        testRsTemplates.printIt, testRsTemplates.runIt);
        PrettyPrint.ppd(rs, r.agent);
        // WebGui.startWebGui(r.agent);
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }

    /**
     * One scene, two instances, connected with relations
     */
    @Test
    public void testReciprocalWithRelations() {
        String description = "Reciprocal with relations";
        TestHelper.testStart(description);
        RecordedStory rs = new RecordedStory(RsTemplates.DIRECT);
        rs.getRsScene(RsTemplates.DIRECT).setInstanceLabels("w_c_bai1",
                "w_c_bai2");
        // the relations, to and from, as
        ABStory absRelationsFrom =
                RsTemplates.generateOneWayAction("w_c_bai1", "w_c_bai2", Arrays
                        .asList("CreateRelation wv_vr_rel1",
                                "CreateRelation wv_vr_rel2"));
        ABStory absRelationsTo =
                RsTemplates.generateOneWayAction("w_c_bai2", "w_c_bai1", Arrays
                        .asList("CreateRelation wv_vr_rel3",
                                "CreateRelation wv_vr_rel4"));
        // the actions
        ABStory absActions =
                RsTemplates.generateReciprocalAction("w_c_bai1", "w_c_bai2",
                        Arrays.asList("wa_v_av1", "wa_v_av2", "wa_v_av3"));
        // now, add all the stories to the recorded story
        rs.addStory(absRelationsFrom);
        rs.addStory(absRelationsTo);
        rs.addStory(absActions);
        @SuppressWarnings("unused")
        Runner r =
                ArtificialDomain
                        .doIt("Two scenes, narration from the direct scene of a reciprocal story",
                                rs, testRsTemplates.printIt,
                                testRsTemplates.runIt);
        // WebGui.startWebGui(r.agent);
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }

}
