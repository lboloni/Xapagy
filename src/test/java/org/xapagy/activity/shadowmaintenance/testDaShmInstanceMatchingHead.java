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
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsGenerator;
import org.xapagy.debug.storygenerator.RsTemplates;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Jan 24, 2012
 */
public class testDaShmInstanceMatchingHead {

    /**
     * Two stories, two instances matching each other at the base level
     * 
     * Turning on only the decay and the instance matching head
     * 
     * Using the artificial domain
     */
    @Test
    public void test() {
        String description = "matching with various combinations";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        //
        // Create the parameters: history no shadowing or hls
        // Testing: only instance matching turned on
        //
        ABStory parFocus = new ABStory("$Include 'P-FocusOnly'");
        parFocus.add("$DiffusionActivity Append To Composite 'ShadowMaintenance' New DA With Name 'InstanceMatchingHead' Code 'org.xapagy.activity.shadowmaintenance.DaShmInstanceMatchingHead' Parameters 'scaleInstanceByAttribute' = '1.0', 'scaleInstanceInAction' = '0.0'");
        parFocus.add("$DiffusionActivity Append To Composite 'ShadowMaintenance' New DA With Name 'GarbageCollect' Code 'org.xapagy.activity.shadowmaintenance.DaShmGarbageCollect' Parameters 'beta' = '0.01', 'inflectionPopulation' = '10.0', 'safeEnergy' = '0.001'");
        // empty history
        List<RecordedStory> history = new ArrayList<>();
        //
        // Two simple reciprocal ones, using concepts and verbs which had never
        // been used before
        //
        RecordedStory rsShadow =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));
        RecordedStory rsFocus =
                RsGenerator.generateReciprocal("w_c_bai21", "w_c_bai22",
                        Arrays.asList("wa_v_av40", "wa_v_av41", "wa_v_av42"));

        RsTestingUnit rstu =
                new RsTestingUnit(new ABStory("$Include 'P-FocusOnly'"), history, rsShadow, parFocus,
                        rsFocus);
        rstu.runAll(r);
        TextUi.println("base level: "
                + rstu.getInstanceShadowSalience(RsTemplates.DIRECT, 0,
                        RsTemplates.DIRECT, 0, EnergyColors.SHI_GENERIC));
    }

}
