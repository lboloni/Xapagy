/*
   This file is part of the Xapagy project
   Created on: Nov 23, 2011
 
   org.xapagy.ui.smartprint.testSpInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.smartprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
 */
public class testSpInstance {

    @Test
    public void testSimple() {
        String description = "SpInstance ";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.createAabConcepts();
        r.printOn = true;
        r.exec("A scene #Scene1 / is-only-scene.");
        Instance instMan1 = r.exac("A w_c_bai20 / exists.").getSubject();
        String spInstMan1 = SpInstance.spc(instMan1, r.agent);
        r.println(spInstMan1);
        r.exac("The w_c_bai20 / is-a / large.");
        String spInstMan1b = SpInstance.spc(instMan1, r.agent);
        r.println(spInstMan1b);
        Instance instMan2 = r.exac("A small w_c_bai20 / exists.").getSubject();
        String spInstMan2 = SpInstance.spc(instMan2, r.agent);
        TextUi.println(spInstMan2);
        TestHelper.testIncomplete();
    }

}
