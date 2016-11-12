/*
   This file is part of the Xapagy project
   Created on: Jul 13, 2014
 
   org.xapagy.debug.testInstanceShadowEvolutionMatrix
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.debug;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.autobiography.ABStory;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;

/**
 * @author Ladislau Boloni
 *
 */
public class testInstanceShadowEvolutionMatrix {

    /**
     * Illustrates the creation of an instance shadow evolution matrix
     */
    @Test
    public void test() {
        // create the story
        ABStory story = new ABStory();
        story.add("'Achilles'/ wa_v_av40 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av41 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av42 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av43 / 'Hector'.");
        story.add("'Achilles'/ wa_v_av44 / 'Hector'.");
        // prepare the agent
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.tso.setTrace(TraceWhat.COMPACT);
        r.printOn = true;
        r.exec("$Include 'P-FocusOnly'");
        // see the story for the first time
        r.exec("$CreateScene #one CloseOthers With Instances w_c_bai20 w_c_bai22 'Hector', w_c_bai21 w_c_bai22 'Achilles'");
        r.exec(story);
        r.exec("----");
        // ok, create the ChoiceEvolutionMatrix
        InstanceShadowEvolutionMatrix isem =
                new InstanceShadowEvolutionMatrix(r.agent);

        // filters for instances
        InstanceMatchFilter imHectorOne =
                new InstanceMatchFilter("imHectorOne", false, "\"Hector\"",
                        "#one");
        InstanceMatchFilter imHectorTwo =
                new InstanceMatchFilter("imHectorTwo", false, "\"Hector\"",
                        "#two");
        InstanceMatchFilter imAchillesOne =
                new InstanceMatchFilter("imAchillesOne", false, "\"Achilles\"",
                        "#one");
        InstanceMatchFilter imAchillesTwo =
                new InstanceMatchFilter("imAchillesTwo", false, "\"Achilles\"",
                        "#two");
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

        // run the story the second time
        r.exec("$Include 'P-All-NoInternal'");
        r.exec("$CreateScene #two CloseOthers With Instances w_c_bai20 w_c_bai22 'Hector', w_c_bai21 w_c_bai22 'Achilles'");

        for (int i = 0; i != story.length(); i++) {
            r.exec(story.getLine(i));
            isem.record();
        }
        TextUi.println(isem.printMatrix("shadow_generic"));
        //
        // Test for the querying of the isem
        // Query by name and row number
        //
        ShadowRecord<Instance> sr = isem.query("Achilles - strongest", 0);
        double val = sr.getEnergy(EnergyColors.SHI_GENERIC);
        //double val = sr.getEnergy(EnergyColor.SHI_ATTRIBUTE);
        TextUi.println("Achilles strongest / 0 - generic"
                + Formatter.fmt(val));
        assertEquals("Achilles strongest / 0 - generic", 1.38, val, 0.01);
        // 
        // Query by name and vi 
        //
        sr = isem.query("Achilles - strongest", null, "wa_v_av42");
        val = sr.getEnergy(EnergyColors.SHI_GENERIC);
        TextUi.println("Achilles strongest / after wa_v_av42"
                + Formatter.fmt(val));
        assertEquals("Achilles strongest / 0 - generic", 3.2, val, 0.1);
        
    }

}
