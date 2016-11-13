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
public class testSaMvCloneScene {

    /**
     * Test for the SaMvCloneScene which creates a new scene
     */
    @Test
    public void test() {
        String description = "SaMvForget";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Troy CloseOthers With Instances 'Hector' #H, w_c_bai20 'Achilles' #A, w_c_bai20 'Ulysses' #U");
        VerbInstance vi = r.exac("'Ulysses' / wa_v_av40 / 'Hector'.");
        Instance instUlysses = vi.getSubject();
        r.exec("'Achilles' / wa_v_av41 / 'Hector'.");
        r.exec("'Achilles' / CreateRelation wv_vr_rel1 / 'Hector'.");
        r.exec("'Hector' / wa_v_av42 / 'Achilles'.");
        r.exec("'Achilles' / wa_v_av43 / 'Hector'.");
        r.exec("'Ulysses' / forget.");
        r.exec("Scene / clone-scene / scene #Troy2.");
        r.ah.isNotInFocus(instUlysses);
        // FIXME: test that a clone had been created...
        // r.agent.addObserver(new BreakObserver(true));
        // WebGui.startWebGui(r.agent);
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }

}
