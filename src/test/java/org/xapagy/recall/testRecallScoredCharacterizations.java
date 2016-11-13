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
package org.xapagy.recall;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.observers.BreakObserver;

/**
 * These tests are testing the scored characterizations
 * 
 * @author Ladislau Boloni
 * Created on: Jul 14, 2011
 */
public class testRecallScoredCharacterizations {

    // @Test
    @SuppressWarnings("unused")
    public void debugCharacterizationChoices() {
        String description = "debugCharacterizationChoices";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();

        // create the history
        ABStory abs = new ABStory();
        abs.add("$CreateScene #Scene1 Current With Instances w_c_bai20 w_c_bai22 'Hector', w_c_bai20 w_c_bai22 'Achilles' w_c_bai21");
        abs.add("'Achilles'/ wa_v_av40 / 'Hector'.");
        abs.add("'Hector'/ wa_v_av41 / 'Achilles'.");
        abs.add("'Achilles'/ wa_v_av42 / 'Hector'.");
        abs.add("'Hector'/ wa_v_av43 / 'Achilles'.");
        abs.add("----");
        r.exec(abs);
        r.exec("$CreateScene #Scene2 CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        VerbInstance vi = r.exac("'Hector'/ wa_v_av41 / 'Achilles'.");
        Instance instHector = vi.getSubject();
        Instance instAchilles = vi.getObject();
        /*
         * List<Choice> choices = r.agent.getDaHlsMaintenance().daHlsmChoices
         * .getScoredCharacterizationsForInstance(instAchilles, 1.0);
         * Collections.sort(choices, new Comparator<Choice>() {
         * 
         * @Override public int compare(Choice arg0, Choice arg1) { return
         * Double.compare(arg0.getScoreNative(), arg1.getScoreNative()); }
         * 
         * }); Collections.reverse(choices); for (Choice choice : choices) {
         * TextUi.println(PrettyPrint.ppDetailed(choice, r.agent)); }
         */
    }

    @Test
    public void testCharacterizations() {
        String description = "Recall Case 1: scored characterizations";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // create the history
        ABStory abs = new ABStory();
        abs.add("$CreateScene #Scene1 Current With Instances w_c_bai20 w_c_bai22 'Hector', w_c_bai20 w_c_bai22 'Achilles' w_c_bai21");
        abs.add("'Achilles'/ wa_v_av40 / 'Hector'.");
        abs.add("'Hector'/ wa_v_av41 / 'Achilles'.");
        abs.add("'Achilles'/ wa_v_av42 / 'Hector'.");
        abs.add("'Hector'/ wa_v_av43 / 'Achilles'.");
        abs.add("----");
        // AutoBioHelper.saveStory("characterizations", abs);
        // Agent agent =
        // AutoBioHelper.getNewAgent(AutoBioPolicy.CACHE, "Iliad",
        // "characterizations", ParameterPrepack.PREPACK_DEFAULT,
        // "characterizations");
        // Runner r = new Runner(agent);
        r.exec(abs);
        // WebGui.startWebGui(r.agent);
        r.exec("$Include 'P-All'");
        r.agent.addObserver("BreakObserver", new BreakObserver(false));
        r.exec("$CreateScene #Scene2 CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        r.exec("'Hector'/ wa_v_av41 / 'Achilles'.");
        // TextUi.enterToTerminate();

        // FIXME: this fails due to the stupid verbalization which creates
        // "not-is-a" statements...

        // r.ah.sequenceInRecentHistory(ChoiceType.CHARACTERIZATION,
        // ViMatcher.VERBALIZED, "\"Hector\" / is-a / w_c_bai22.",
        // "\"Hector\" / is-a / w_c_bai23.", "\"Achilles\" / is-a / w_c_bai21.",
        // "\"Achilles\" / is-a / w_c_bai22.");
        // r.exec("$DebugHere");

        // r.exec("'Achilles'/ wa_v_av42 / 'Hector'.");
        // r.exec("'Hector'/ wa_v_av43 / 'Achilles'.");
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }

}
