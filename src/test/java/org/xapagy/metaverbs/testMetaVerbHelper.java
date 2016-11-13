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
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;

/**
 * Tests the functions in the MetaVerbHelper
 * 
 * @author Ladislau Boloni
 * Created on: Jan 19, 2011
 */
public class testMetaVerbHelper {

    /**
     * This test simply verifies if the removeMetaVerbs removes the successor
     * common metaverb
     */
    @Test
    public void testRemoveMetaVerbs() {
        String description = "Removing meta verbs from a VO";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        r.exec("$CreateScene #Iliad CloseOthers With Instances 'Achilles', 'Hector', 'Ajax', 'Ulysses', 'Patrocles'");
        r.exec("'Hector' / is-a / w_c_bai20.");
        r.exec("'Achilles' / is-a / w_c_bai21.");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        VerbOverlay original = vi.getVerbs();
        r.ah.voContains(original, Hardwired.VM_SUCCESSOR);
        VerbOverlay removed = MetaVerbHelper.removeMetaVerbs(original, r.agent);
        r.ah.voDoesntContain(removed, Hardwired.VM_SUCCESSOR);
        TestHelper.testDone();
    }

}
