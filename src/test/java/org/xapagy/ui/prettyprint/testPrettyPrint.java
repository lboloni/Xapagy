/*
   This file is part of the Xapagy project
   Created on: Dec 28, 2011
 
   org.xapagy.ui.prettyprint.testPrettyPrint
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.verbalize.VmInstanceReference;

/**
 * @author Ladislau Boloni
 * 
 */
public class testPrettyPrint {

    @Test
    public void testSimple() {
        String description = "Tests whether it prints based on the ancestor";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.printOn = true;
        // r.tso.trace();
        // r.tso.setCompactTraceIncludingVerbalization(true);
        r.exec("$CreateScene #Scene1 CloseOthers With Instances 'Jenny'");

        VerbInstance vi = r.exac("'Jenny' / is-a / w_c_bai20.");
        Instance instJenny = vi.getSubject();
        VmInstanceReference vir =
                r.agent.getVerbalMemory().getVmInstanceReferences()
                        .get(instJenny).get(1);
        String pp = PrettyPrint.ppDetailed(vir.getXapiReference(), r.agent);
        TextUi.println(pp);
        TestHelper.testDone();
    }

}
