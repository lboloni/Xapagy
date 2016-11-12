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
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.prettyprint.PrintDetail;

/**
 * @author Ladislau Boloni
 * Created on: Feb 23, 2012
 */
public class testHabituation {

    /**
     * Tests the habituation aspect: for a long time a certain type of nodes
     * perform certain type of actions
     * 
     * FIXME: once you have a history of more than 20, the shadows disappear
     * because they are too weak on their own
     * 
     * FIXME: also, I don't quite know what are we testing here. Probably once
     * we have features for selection from large repetitions, this can be
     * handled
     * 
     */
    @Test
    public void test() {
        String description = "test";
        TestHelper.testStart(description);
        // current history used: empty
        List<RecordedStory> history = new ArrayList<>();
        // the parameter we are going to test
        ProperNameGenerator sg = new ProperNameGenerator();
        int repetition = 100;
        // if you put 40, it will be too small... and the garbage collector will
        // pick it up...
        RecordedStory st = null;
        for (int i = 0; i != repetition; i++) {
            st = RsGenerator.generateReciprocal("w_c_bai20", "w_c_bai21",
                    Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av43"));
            st.addRandomPropernames(sg);
            history.add(st);
        }
        for (int i = 0; i != repetition; i++) {
            st = RsGenerator.generateReciprocal("w_c_bai20", "w_c_bai21",
                    Arrays.asList("wa_v_av41", "wa_v_av42", "wa_v_av45"));
            st.addRandomPropernames(sg);
            history.add(st);
        }
        // the new one
        RecordedStory stnew = RsGenerator.generateReciprocal("w_c_bai20",
                "w_c_bai21", Arrays.asList("wa_v_av41", "wa_v_av42"));
        stnew.addRandomPropernames(sg);

        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace();
        RsTestingUnit stu = new RsTestingUnit(
                new ABStory("$Include 'P-FocusOnly'"), history, null,
                new ABStory("$Include 'P-FocusAndShadows'"), stnew);

        stu.runHistory(r);
        stu.runFocusStory(r);
        // redirect the former printing to a formatter
        Formatter fmt = new Formatter();
        fmt.add("Shadow Instances");
        fmt.addIndented(
                r.tso.printNow("SHADOW_INSTANCES", PrintDetail.DTL_CONCISE));
        fmt.add("Shadow VIs");
        fmt.addIndented(r.tso.printNow("SHADOW_VIS", PrintDetail.DTL_CONCISE));
        fmt.add("Choices sorted by native");
        fmt.addIndented(
                r.tso.printNow("CHOICES_NATIVE", PrintDetail.DTL_CONCISE));
        TextUi.println(fmt);
        TestHelper.testIncomplete();
    }

}
