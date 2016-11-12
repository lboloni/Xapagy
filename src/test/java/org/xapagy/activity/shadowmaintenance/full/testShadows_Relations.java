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
package org.xapagy.activity.shadowmaintenance.full;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.InstanceMatchFilter;
import org.xapagy.debug.InstanceShadowAccessFilter;
import org.xapagy.debug.InstanceShadowEvolutionMatrix;
import org.xapagy.debug.Runner;
import org.xapagy.debug.storygenerator.RsFrequentNarratives;
import org.xapagy.debug.storygenerator.RsTemplates;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Oct 3, 2014
 */
public class testShadows_Relations {
    /**
     *  This is suppose to be a test for situations where there are relations in 
     *  the 
     */
    // @Test
    public void testDefault() {
        TestHelper.testStart("testShadows_Pure");
        RsTestingUnit rtu = RsFrequentNarratives.createPureRepetition(1);
        TextUi.println(rtu.getFullStory());
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        rtu.runHistory(r);
        rtu.runShadowStory(r);
        //
        // ok, create the ShadowInstanceEvolutionMatrix
        //
        InstanceShadowEvolutionMatrix isem =
                new InstanceShadowEvolutionMatrix(r.agent);
        String shadowSceneLabel =
                rtu.getRsShadow().getRsScene(RsTemplates.DIRECT)
                        .getLabelScene();
        String focusSceneLabel =
                rtu.getRsFocus().getRsScene(RsTemplates.DIRECT)
                        .getLabelScene();
        // filters for instances
        InstanceMatchFilter imHectorOne =
                new InstanceMatchFilter("imHectorOne", false, "\"Hector\"",
                        shadowSceneLabel);
        InstanceMatchFilter imHectorTwo =
                new InstanceMatchFilter("imHectorTwo", false, "\"Hector\"",
                        focusSceneLabel);
        InstanceMatchFilter imAchillesOne =
                new InstanceMatchFilter("imAchillesOne", false, "\"Achilles\"",
                        shadowSceneLabel);
        InstanceMatchFilter imAchillesTwo =
                new InstanceMatchFilter("imAchillesTwo", false, "\"Achilles\"",
                        focusSceneLabel);
        InstanceMatchFilter imAny =
                new InstanceMatchFilter("any", false, "*", "*");

        // Hector with Hector
        InstanceShadowAccessFilter isaf =
                new InstanceShadowAccessFilter("Hector-Hector", imHectorTwo,
                        imHectorOne);
        isem.addColumn(isaf);
        // Achilles with Achilles
        isaf =
                new InstanceShadowAccessFilter("Achilles-Achilles",
                        imAchillesTwo, imAchillesOne);
        isem.addColumn(isaf);
        // Achilles with Hector
        isaf =
                new InstanceShadowAccessFilter("Achilles-Hector",
                        imAchillesTwo, imHectorOne);
        isem.addColumn(isaf);

        // Strongest shadow for Achilles
        isaf =
                new InstanceShadowAccessFilter("Achilles - strongest",
                        EnergyColors.SHI_GENERIC, imAchillesTwo, imAny);
        isem.addColumn(isaf);
        //
        // End of creating the shadow access filter
        //

        r.setInstanceShadowEvolutionMatrix(isem);
        // set the parameter of the RTU
        rtu.setParamsFocus(new ABStory("$Include 'P-All-NoInternal'"));
        rtu.runFocusStory(r);
        TextUi.println(isem.printMatrix("shadow_generic"));
        TestHelper.testDone();
    }

}
