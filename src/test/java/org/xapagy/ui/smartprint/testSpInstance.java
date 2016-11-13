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
package org.xapagy.ui.smartprint;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Nov 23, 2011
 */
public class testSpInstance {

    @Test
    public void testSimple() {
        String description = "SpInstance ";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
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
