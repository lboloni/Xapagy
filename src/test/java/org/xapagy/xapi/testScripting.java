/*
   This file is part of the Xapagy project
   Created on: Feb 16, 2016
 
   org.xapagy.xapi.testScripting
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.xapi;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 *
 */
public class testScripting {

    /**
     * This test simply checks whether the Javascript had been passed to the
     * system
     */
    @Test
    public void testExecution() {
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("!!print('Hello world! ' + agent);");
        r.exec("$BeginScript");
        r.exec("print('One ' + agent);");
        r.exec("print('Two ' + agent);");
        r.exec("$EndScript");
    }

    /**
     * This test simply checks whether the Javascript had been passed to the
     * system
     */
    @Test
    public void testRef() {
        Runner r = ArtificialDomain.createAabConcepts();
        r.exec("$CreateScene #first CloseOthers With Instances w_c_bai20 'Hector' #H, w_c_bai20 'Achilles' #A");
        @SuppressWarnings("unused")
        VerbInstance vi1 =
                r.exac("'Achilles' / #labeled wa_v_av40 / 'Hector'.");
        r.exec("!!print(ref.ViByLabelFromFocus('#labeled'));");
    }

}
