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
package org.xapagy.metaverbs;

import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;

/**
 * @author Ladislau Boloni
 * Created on: Nov 21, 2012
 */
public class testSaMvForget {

    /**
     * Test for the SaMvForget verb which removes somebody from the focus
     */
    @Test
    public void test() {
        String description = "SaMvForget";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // r.tso.setTrace();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles', w_c_bai20 'Ulysses'");
        VerbInstance vi = r.exac("'Ulysses' / wa_v_av40 / 'Hector'.");
        Instance instUlysses = vi.getSubject();
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Ulysses' / forget.");
        r.exec("'Hector' / wa_v_av40 / 'Achilles'.");
        r.ah.isNotInFocus(instUlysses);
        TestHelper.testDone();
    }

}
