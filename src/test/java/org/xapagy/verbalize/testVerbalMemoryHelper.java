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
package org.xapagy.verbalize;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;

/**
 * @author Ladislau Boloni
 * Created on: Dec 30, 2011
 */
public class testVerbalMemoryHelper {

    @Test
    public void testGetFrequentReference() {
        String description = "basic";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        // r.tso.setTrace();
        r.exec("A scene #Athens / exists.");
        r.exec("$ChangeScene #Athens");
        Instance sceneAthens = r.agent.getFocus().getCurrentScene();
        r.exec("A w_c_bai20 'Homer' / exists.");
        r.exec("A scene #Troy / exists.");
        r.exec("$ChangeScene #Troy");
        r.exec("A 'Hector' / exists.");
        r.exec("An 'Achilles' / exists.");
        Instance instHector =
                r.exac("'Hector' -- in -- #Troy / is-a / w_c_bai20.")
                        .getSubject();
        @SuppressWarnings("unused")
        Instance instAchilles =
                r.exac("'Achilles' -- in -- #Troy / is-a / w_c_bai20.")
                        .getSubject();
        r.exec("$ChangeScene #Athens");
        r.exec("'Homer' / says in #Troy // 'Hector' / wa_v_av40 / 'Achilles'.");

        String referenceFromSelfScene =
                VerbalMemoryHelper.getFrequentReference(r.agent, instHector,
                        instHector.getScene(), new HashSet<String>());
        // TextUi.println(referenceFromSelfScene);
        Assert.assertEquals("\"Hector\" -- in -- #Troy", referenceFromSelfScene);
        String referenceFromOtherScene =
                VerbalMemoryHelper.getFrequentReference(r.agent, instHector,
                        sceneAthens, new HashSet<String>());
        Assert.assertEquals("\"Hector\" -- in -- #Troy", referenceFromOtherScene);
        //TextUi.println(referenceFromOtherScene);

        TestHelper.testDone();
    }

}
