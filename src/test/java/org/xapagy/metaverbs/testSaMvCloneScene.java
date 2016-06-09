/*
   This file is part of the Xapagy project
   Created on: Nov 21, 2012
 
   org.xapagy.metaverbs.testSaMvCloneScene
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * 
 */
public class testSaMvCloneScene {

    /**
     * Test for the SaMvCloneScene which creates a new scene
     */
    @Test
    public void test() {
        String description = "SaMvForget";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #Troy CloseOthers With Instances 'Hector' #H, w_c_bai20 'Achilles' #A, w_c_bai20 'Ulysses' #U");
        VerbInstance vi = r.exac("'Ulysses' / wa_v_av40 / 'Hector'.");
        Instance instUlysses = vi.getSubject();
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Achilles' / CreateRelation wv_vr_rel1 / 'Hector'.");
        r.exec("'Hector' / wa_v_av42 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av43 / 'Hector'.");
        r.exec("'Ulysses' / forget.");
        r.exec("Scene / clone-scene / scene #Troy2.");
        r.ah.isNotInFocus(instUlysses);
        // FIXME: test that a clone had been created...
        // r.agent.addObserver(new BreakObserver(true));
        // WebGui.startWebGui(r.agent);
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }

}
