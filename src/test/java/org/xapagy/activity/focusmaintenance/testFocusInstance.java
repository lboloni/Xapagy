/*
   This file is part of the Xapagy project
   Created on: Jun 3, 2011
 
   org.xapagy.model.testFocusInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.agents.Focus;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
 */
@SuppressWarnings("unused")
public class testFocusInstance {

    /**
     * Tests that an instance which has not been referred to in a scene (for >80
     * verbinstances) will expire.
     * 
     * FIXME: not sure this is something we really want
     */
    // This test does not pass because the focus change (no instance decay
    // in April 2014
    // @Test
    public void testInstanceExpiration() {
        String testDescription =
                "Instances not referred are eventually expiring. ";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Focus fc = r.agent.getFocus();
        r.exec("$CreateScene #Reality CloseOthers With Instances 'Hector' #Hector, w_c_bai20 #referred, w_c_bai21");
        Instance instanceHector =
                r.ref.InstanceByLabel("#Hector");
        Instance scene =
                r.ref.SceneByLabel("#Reality");
        Instance instanceReferred =
                r.ref.InstanceByLabel("#referred");
        int unreferredExpiredAt = -1;
        for (int i = 0; i != 100; i++) {
            r.exec("The  w_c_bai20 / wa_v_av40 / the w_c_bai21.");
            if (unreferredExpiredAt < 0 && r.aht.isNotInFocus(instanceHector)) {
                unreferredExpiredAt = i;
            }
        }
        TextUi.println(unreferredExpiredAt);
        Assert.assertTrue("Expiration count is wrong",
                unreferredExpiredAt >= 80 && unreferredExpiredAt <= 100);
        r.ah.isStrongInFocus(scene);
        r.ah.isStrongInFocus(instanceReferred);
        TestHelper.testDone();
    }

    /**
     * Instances in the quoted scene are also regenerated when referred to.
     */
    @Test
    public void testQuotedScene() throws IOException, InterruptedException,
            ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        String testDescription =
                "Instance in a quoted sentence are also regenerating. ";
        TestHelper.testStart(testDescription);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Quoted CloseOthers With Instances 'Hector' #Hector, 'Achilles' #Achilles");
        r.exec("$CreateScene #Reality Current With Instances w_c_bai20, w_c_bai21");
        Instance instHector =
                r.ref.InstanceByLabel("#Hector");
        Instance instAchilles =
                r.ref.InstanceByLabel("#Achilles");
        Instance scene =
                r.ref.SceneByLabel("#Quoted");
        double instanceTestedFocus = 0;
        double sceneFocus = 0;
        double count = 0;
        for (int i = 0; i != 10; i++) {
            r.exec("The w_c_bai20 / wa_v_av40 / the w_c_bai21.");
            r.exec("The w_c_bai20 / says in #Quoted // 'Achilles' / wa_v_av41 / 'Hector'.");
        }
        r.ah.isStrongInFocus(instHector);
        r.ah.isStrongInFocus(instAchilles);
        TestHelper.testDone();
    }
}
