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

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;

/**
 * 
 * A test for walking through the UI
 * 
 * @author Ladislau Boloni
 * Created on: Mar 5, 2016
 */
public class testUI {

    // @Test
    public void test() {
        String description = "Debug the UI";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$Include 'P-FocusOnly'");
        r.exec("$CreateScene #first CloseOthers With Instances w_c_bai20 'Hector' #H, w_c_bai20 'Achilles' #A");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("$DebugHere");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
        r.exec("'Achilles' / wa_v_av40 / 'Hector'.");
    }
}
