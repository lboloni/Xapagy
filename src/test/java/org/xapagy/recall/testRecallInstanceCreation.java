/*
   This file is part of the Xapagy project
   Created on: Jun 22, 2011
 
   org.xapagy.recall.testRecallInstanceCreation
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.recall;

import java.util.List;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.observers.AbstractAgentObserver.TraceWhat;

/**
 * @author Ladislau Boloni
 * 
 */
public class testRecallInstanceCreation {
    public static final String outputDirName = "output";

    /**
     * 
     * Trying it out with more concrete values, as the above one with the story
     * generator has too w_c_bai20y choices.
     * 
     */
    @Test
    public void test() {
        String description = "Recall - instance creation";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$Include 'P-FocusOnly'");
        // r.tso.setTrace();
       r.exec("$CreateScene #One CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles', w_c_bai21");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Hector'/ wa_v_av41 / 'Achilles'.");
        r.exec("'Achilles'/ wa_v_av42 / 'Hector'.");
        r.exec("'Achilles'/ wa_v_av43 / the w_c_bai21.");
        r.exec("----");
        r.exec("$Include 'P-Default'");
        r.exec("$CreateScene #Two CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");
        r.exec("'Achilles'/ wa_v_av40 / 'Hector'.");
        r.tso.setTrace(TraceWhat.CHOICES_DYNAMIC);
        @SuppressWarnings("unused")
        List<VerbInstance> recalls = r.exec("Scene / recall narrate.");
    }
}
