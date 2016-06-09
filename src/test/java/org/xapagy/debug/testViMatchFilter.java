/*
   This file is part of the Xapagy project
   Created on: Dec 23, 2014
 
   org.xapagy.debug.testViMatchFilter
 
   Copyright (c) 2008-2013 Ladislau Boloni
 */

package org.xapagy.debug;

import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 *
 */
public class testViMatchFilter {

    @Test
    public void testParsing() {
        String description = "Test the parsing of a ViMatchFilter";
        TestHelper.testStart(description);
        Formatter fmt = new Formatter();
        ViMatchFilter vmf = null;
        vmf = ViMatchFilter.createViMatchFilter(null, "wa_v_av40");
        fmt.add(vmf.toString());
        vmf = ViMatchFilter.createViMatchFilter(null, "S_ADJ, wa_v_av40");
        fmt.add(vmf.toString());
        vmf = ViMatchFilter.createViMatchFilter(null, "S_V_O,*,wa_v_av40");
        fmt.add(vmf.toString());
        vmf = ViMatchFilter.createViMatchFilter(null, "S_V_O, \"Achilles\", wa_v_av40, \"Hector\"");
        fmt.add(vmf.toString());
        vmf = ViMatchFilter.createViMatchFilter(null, "scene:#MyScene, S_V_O, \"Achilles\", wa_v_av40, \"Hector\"");
        fmt.add(vmf.toString());
        TextUi.println(fmt.toString());
        TestHelper.testDone();
    }
    
}
