/*
   This file is part of the Xapagy project
   Created on: Feb 28, 2012
 
   org.xapagy.xapi.testXapiL2
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * 
 */
public class testMacroParser {

    @Test
    public void testInquitRepeat() {
        String description = "Macro: repeat an inquit";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // r.tso.setTrace(TraceWhat.VERBALIZATION);
        r.exec("A scene #first / exists.");
        r.exec("$ChangeScene #first");        
        r.exec("A 'Hector' / exists.");
        r.exec("A scene #second / exists.");
        r.exec("The scene #first / has-view / the scene #second.");
        r.exec("$ChangeScene #second");
        r.exec("A w_c_bai20 / exists.");
        r.exec("A w_c_bai21 / exists.");
        r.exec("$ChangeScene #first");
        r.exec("'Hector' / says in scene -- view-of -- scene #first // The w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        r.exec("$..// the w_c_bai21 / wa_v_av40 / the w_c_bai20.");
        r.exec("$..// the w_c_bai20 / wa_v_av40 / the w_c_bai21.");
        TestHelper.testDone();
    }

    
}
