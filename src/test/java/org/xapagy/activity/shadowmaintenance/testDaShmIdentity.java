/*
   This file is part of the Xapagy project
   Created on: Nov 12, 2011
 
   org.xapagy.activity.testDaShadowIdentity
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsGenerator;
import org.xapagy.debug.storygenerator.RsTemplates;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
 */
public class testDaShmIdentity {

    //@Test
    public void test() {
        String description = "test";
        TestHelper.testStart(description);
        // the parameter we are going to test
        ABStory p = new ABStory("$Include 'P-FocusAndShadows'");
        p.add("$DiffusionActivity Append To Composite 'ShadowMaintenance' New DA With Name 'Identity' Code 'org.xapagy.activity.shadowmaintenance.DaShmIdentity'");
        // two base level stuff
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        RecordedStory st =
                RsGenerator.generateNarratedReciprocal("w_c_bai23",
                        "w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av43"));
        st.addIdentity(RsTemplates.DIRECT, 0, RsTemplates.QUOTED, 0);
        // two base level stuff
        RecordedStory st2 =
                RsGenerator.generateNarratedReciprocal("w_c_bai23",
                        "w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av43"));
        st2.addIdentity(RsTemplates.DIRECT, 0, RsTemplates.QUOTED, 0);
        RsTestingUnit stu =
                new RsTestingUnit(p, new ArrayList<RecordedStory>(), st, p,
                        st2);
        stu.runAll(r);
        TextUi.println(stu);
        // r.tso.ppd(QueryType.SHADOW_INSTANCES);
    }
}
