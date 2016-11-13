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
package org.xapagy.xapi;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;

/**
 * @author Ladislau Boloni
 * Created on: Feb 28, 2012
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
