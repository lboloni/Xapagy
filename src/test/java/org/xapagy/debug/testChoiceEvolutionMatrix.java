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

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.storygenerator.RsFrequentNarratives;
import org.xapagy.debug.storygenerator.RsTestingUnit;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;

/**
 * Tests whether the
 * 
 * @author Ladislau Boloni
 * Created on: Jul 7, 2014
 */
public class testChoiceEvolutionMatrix {

    /**
     * Illustrates the creation of a choice evolution matrix
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
        r.exec("$CreateScene #one CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
        r.exec(story);
        r.exec("----");
        // ok, create the ChoiceEvolutionMatrix
        ChoiceEvolutionMatrix cem = new ChoiceEvolutionMatrix(r.agent);
        cem.addColumn("wa_v_av40", ChoiceType.CONTINUATION, ViType.S_V_O,                
                "\"Achilles\"", "wa_v_av40", "\"Hector\"");
        cem.addColumn("wa_v_av41", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Achilles\"", "wa_v_av41", "\"Hector\"");
        cem.addColumn("wa_v_av42", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Achilles\"", "wa_v_av42", "\"Hector\"");
        cem.addColumn("wa_v_av43", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Achilles\"", "wa_v_av43", "\"Hector\"");
        cem.addColumn("wa_v_av44", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Achilles\"", "wa_v_av44", "\"Hector\"");

        // run the story the second time
        r.exec("$Include 'P-All-NoInternal'");
        r.exec("$CreateScene #two CloseOthers With Instances w_c_bai20 'Hector', w_c_bai21 'Achilles'");
        for (int i = 0; i != story.length(); i++) {
            r.exec(story.getLine(i));
            cem.record();
        }
        TextUi.println(cem.printMatrix("idm"));
    }

    /**
     * Illustrates the use of the choice evolution matrix with an RTU
     */
    @Test
    public void testRTU() {
        RsTestingUnit rtu = RsFrequentNarratives.createForkSimple(5, 4);
        TextUi.println(rtu.getFullStory());
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        rtu.runHistory(r);
        rtu.runShadowStory(r);
        // ok, create the ChoiceEvolutionMatrix
        ChoiceEvolutionMatrix cem = new ChoiceEvolutionMatrix(r.agent);
        cem.addColumn("H wa_v_av12 A", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Hector\"","wa_v_av12", "\"Achilles\"");
        cem.addColumn("A wa_v_av13 H", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Achilles\"", "wa_v_av13", "\"Hector\"");
        cem.addColumn("H wa_v_av14 A", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Hector\"", "wa_v_av14", "\"Achilles\"");
        cem.addColumn("A wa_v_av23 H", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Achilles\"", "wa_v_av23", "\"Hector\"");
        cem.addColumn("H wa_v_av24 A", ChoiceType.CONTINUATION, ViType.S_V_O,
                "\"Hector\"", "wa_v_av24", "\"Achilles\"");
        r.setChoiceEvolutionMatrix(cem);
        // set the parameter of the RTU
        rtu.setParamsFocus(new ABStory("$Include 'P-All-NoInternal'"));
        rtu.runFocusStory(r);
        TextUi.println(cem.printMatrix("idmIDM"));
    }

}
