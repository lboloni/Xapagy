/*
   This file is part of the Xapagy project
   Created on: Jun 6, 2011
 
   org.xapagy.agents.testScenes
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
