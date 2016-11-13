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
 * Created on: Jul 13, 2014
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
