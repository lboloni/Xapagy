/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
package org.xapagy.debug;

import org.junit.Test;

import org.xapagy.TestHelper;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Dec 23, 2014
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
