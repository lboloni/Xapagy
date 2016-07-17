/*
   This file is part of the Xapagy project
   Created on: Jul 16, 2014
 
   org.xapagy.activity.shadowmaintenance.full.testShadows_Pure
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance.full;

import org.junit.Test;

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
 *
 */
public class testShadows_Pure {
    /**
     *   The simplest test. This test considers a simple repetition of two 
     *   identical stories. 
     *   
     *   It investigates the shadows of the instances ONLY.
     *   
     *   TODO: look into the VIs-s as well.
     */
    @Test
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
