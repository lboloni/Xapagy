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
package org.xapagy.activity.focusmaintenance;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * 
 * Various tests about the time spent by different kinds of verb instances in
 * the focus
 * 
 * @author Ladislau Boloni
 * Created on: Jun 2, 2011
 */
public class testFocusVi {

    /**
     * Action verbs - pushed out in 4-5 Not-action verbs - decay in 16-20 if not
     * relevant
     * 
     */
    @Test
    public void testActionVerbPushout() {
        String testDescription =
                "Test the pushout of action verbs, by dependent action verbs";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Reality CloseOthers With Instances w_c_bai20, w_c_bai21, w_c_bai22");
        r.exec("$CreateScene #Reality2 Current With Instances 'Hector', 'Achilles'");
        // r.debugMode();
        VerbInstance viOtherSceneAction =
                r.exac("'Hector' / wa_v_av41 / 'Achilles'.");
        VerbInstance viOtherSceneNonAction =
                r.exac("'Hector' / is-a / w_c_bai24.");
        r.exec("$ChangeScene #Reality");
        VerbInstance viAction =
                r.exac("The  w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        VerbInstance viNotAction = r.exac("The w_c_bai22 / is-a / w_c_bai23.");
        int exitAction = -1;
        int exitNonAction = -1;
        int exitOtherSceneAction = -1;
        int exitOtherSceneNonAction = -1;
        for (int i = 0; i != 100; i++) {
            r.exec("The  w_c_bai20 / wa_v_av40 / the w_c_bai21.");
            if (exitAction < 0 && r.aht.isNotInFocus(viAction)) {
                exitAction = i;
            }
            if (exitNonAction < 0 && r.aht.isNotInFocus(viNotAction)) {
                exitNonAction = i;
            }
            if (exitOtherSceneAction < 0
                    && r.aht.isNotInFocus(viOtherSceneAction)) {
                exitOtherSceneAction = i;
            }
            if (exitOtherSceneNonAction < 0
                    && r.aht.isNotInFocus(viOtherSceneNonAction)) {
                exitOtherSceneNonAction = i;
            }

        }
        // Formatter fmt = new Formatter();
        // fmt.add("Dependent action verb survived for " + count + " seconds.");
        // TextUi.println(fmt);
        r.ah.inInterval(exitAction, 3, 4);
        r.ah.inInterval(exitNonAction, 2, 2);
        r.ah.inInterval(exitOtherSceneAction, 40, 50);
        r.ah.inInterval(exitOtherSceneNonAction, 0, 0);
        TestHelper.testDone();
    }

}
