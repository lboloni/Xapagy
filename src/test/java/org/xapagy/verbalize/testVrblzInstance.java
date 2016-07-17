/*
   This file is part of the Xapagy project
   Created on: Oct 30, 2013
 
   org.xapagy.verbalize.testVrblzInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class testVrblzInstance {

    @Test
    public void test() {
        Formatter fmt = new Formatter();
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Scene1 CloseOthers With Instances w_c_bai20, w_c_bai21");
        VerbInstance vi = r.exac("The w_c_bai20 / wa_v_av20 / the w_c_bai21.");
        // Instance inst = vi.getSubject();
        List<VrblzCandidateInstance> candidates =
                VrblzInstance.generateCandidates(r.agent, vi, ViPart.Subject);
        for (VrblzCandidateInstance cnd : candidates) {
            fmt.add(cnd.toString());
        }
        TextUi.println(fmt.toString());
    }

}
