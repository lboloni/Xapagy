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
package org.xapagy.activity.shadowmaintenance;

import java.util.ArrayList;
import java.util.Arrays;

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
 * Created on: Nov 12, 2011
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
