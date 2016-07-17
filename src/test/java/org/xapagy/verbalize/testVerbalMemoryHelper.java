/*
   This file is part of the Xapagy project
   Created on: Dec 30, 2011
 
   org.xapagy.verbalize.testVerbalMemoryHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;

/**
 * @author Ladislau Boloni
 * 
 */
public class testVerbalMemoryHelper {

    @Test
    public void testGetFrequentReference() {
        String description = "basic";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // r.tso.setTrace();
        r.exec("A scene #Athens / exists.");
        r.exec("$ChangeScene #Athens");
        Instance sceneAthens = r.agent.getFocus().getCurrentScene();
        r.exec("A w_c_bai20 'Homer' / exists.");
        r.exec("A scene #Troy / exists.");
        r.exec("$ChangeScene #Troy");
        r.exec("A 'Hector' / exists.");
        r.exec("An 'Achilles' / exists.");
        Instance instHector =
                r.exac("'Hector' -- in -- #Troy / is-a / w_c_bai20.")
                        .getSubject();
        @SuppressWarnings("unused")
        Instance instAchilles =
                r.exac("'Achilles' -- in -- #Troy / is-a / w_c_bai20.")
                        .getSubject();
        r.exec("$ChangeScene #Athens");
        r.exec("'Homer' / says in #Troy // 'Hector' / wa_v_av40 / 'Achilles'.");

        String referenceFromSelfScene =
                VerbalMemoryHelper.getFrequentReference(r.agent, instHector,
                        instHector.getScene(), new HashSet<String>());
        // TextUi.println(referenceFromSelfScene);
        Assert.assertEquals("\"Hector\" -- in -- #Troy", referenceFromSelfScene);
        String referenceFromOtherScene =
                VerbalMemoryHelper.getFrequentReference(r.agent, instHector,
                        sceneAthens, new HashSet<String>());
        Assert.assertEquals("\"Hector\" -- in -- #Troy", referenceFromOtherScene);
        //TextUi.println(referenceFromOtherScene);

        TestHelper.testDone();
    }

}
