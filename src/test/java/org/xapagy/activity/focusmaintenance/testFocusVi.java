/*
   This file is part of the Xapagy project
   Created on: Jun 2, 2011
 
   org.xapagy.model.testFocus
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
        Runner r = ArtificialDomain.createAabConcepts();
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
