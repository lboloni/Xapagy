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
package org.xapagy.agents;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.debug.Runner;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * Various tests for the scenes components
 * 
 * @author Ladislau Boloni
 * Created on: Jun 6, 2011
 */
@SuppressWarnings("unused")
public class testScenes {

    /**
     * Creating a scene, it should not make it a current scene
     * 
     */
    @Test
    public void testCreateScene() {
        String description =
                "Creating scenes does not change the current scene";
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Instance initialScene = r.agent.getFocus().getCurrentScene();
        r.exec("A scene #Troy / exists.");
        r.exec("A w_c_bai20 / exists.");
        VerbInstance vi = r.exac("The w_c_bai20 / is-a / w_c_bai21.");
        Instance instWoman = vi.getSubject();
        Assert.assertTrue(r.agent.getFocus().getCurrentScene()
                .equals(initialScene));
        r.ah.isInScene(initialScene, instWoman);
        TestHelper.testStart(description);
        TestHelper.testDone();
    }

    /**
     * Tests that the only scene function leaves only one scene
     * 
     */
    @Test
    public void testOnlyScene() {
        String description = "Only scene discards other scenes";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        Instance initialScene = r.agent.getFocus().getCurrentScene();
        Instance troyScene =
                r.exec("A scene #Troy / exists.").get(1).getSubject();
        r.exec("$ChangeScene #Troy");
        Instance inst = r.exac("A w_c_bai20 / exists.").getSubject();
        String temp = PrettyPrint.ppDetailed(r.agent.getFocus(), r.agent);
        // TextUi.println(temp);
        // now do an only scene component
        Instance athensScene =
                r.exec("A scene #Athens / is-only-scene.").get(1).getSubject();
        Instance inst2 = r.exec("A w_c_bai21 / exists.").get(1).getSubject();
        temp = PrettyPrint.ppDetailed(r.agent.getFocus(), r.agent);
        // TextUi.println(temp);
        temp = PrettyPrint.ppDetailed(r.agent.getFocus(), r.agent);
        r.ah.isStrongInFocus(athensScene);
        r.ah.isNotInFocus(initialScene);
        r.ah.isNotInFocus(troyScene);
        r.ah.isNotInFocus(inst);
        TestHelper.testDone();
    }

    /**
     * Testing the pretty print for the scenes: create some scenes, then verify
     * visually if it is ok.
     * 
     * Testing based on string parse
     * 
     */
    @Test
    public void testPrettyPrint() {
        String description = "Test the pretty print for the scenes";
        TestHelper.testStart(description);
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        String temp = "";
        r.exec("A scene #Troy / exists.");
        r.exec("A w_c_bai20 / exists.");
        r.exec("$ChangeScene #Troy");
        r.exec("A 'Hector' / exists.");
        r.exec("An 'Achilles' / exists.");
        // print the situation
        temp = PrettyPrint.ppConcise(r.agent.getFocus(), r.agent);
        // TextUi.println(temp);
        // print the situation
        temp = PrettyPrint.ppDetailed(r.agent.getFocus(), r.agent);
        // TextUi.println(temp);
        TestHelper.testDone();
    }

}
