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
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * Tests whether the successor verb creates the links as expected
 * 
 * @author Ladislau Boloni
 * Created on: Dec 29, 2013
 */
public class testSaMvSuccessor {

    /**
     * Tests whether the SaMvSuccessor verb creates the links indeed
     */
    @Test
    public void testLinkCreation() {
        String description =
                "SaMvSuccessor - test that the link Successor verb creates a successor link";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Troy CloseOthers With Instances w_c_bai20 'Hector', w_c_bai20 'Achilles'");

        VerbInstance vi1 = r.exac("'Hector' / wa_v_av1 / 'Achilles'.");
        VerbInstance vi2 = r.exac("'Achilles' / wa_v_av2 / 'Hector'.");
        // test the link mesh
        r.ah.linkedBy(Hardwired.LINK_SUCCESSOR, vi1, vi2);
        // r.ah.linkedBy(ViLinkDB.SUCCESSOR, vi2, viskip);
        // WebGui.startWebGui(r.agent);
        // TextUi.enterToTerminate();
        TestHelper.testDone();
    }

}
