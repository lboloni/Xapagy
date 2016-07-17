/*
   This file is part of the Xapagy project
   Created on: Nov 4, 2012
 
   org.xapagy.instances.testSceneRelationHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.instances;

import java.util.AbstractMap.SimpleEntry;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import org.xapagy.ArtificialDomain;
import org.xapagy.TestHelper;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.Runner;
import org.xapagy.ui.TextUi;

/**
 * Tests for the functions in SceneRelationHelper and in general for the
 * creation of scene relations
 * 
 * @author Ladislau Boloni
 * 
 */
public class testSceneRelationHelper {

    /**
     * Creates a relation between scenes using the L2 macro
     * 
     * @param r
     * @return
     */
    private static SimpleEntry<Instance, Instance>
            createL2SceneRelation(Runner r, String relation) {
        r.exec("$CreateScene #Troy1 CloseOthers With Instances 'Hector', 'Achilles'");
        VerbInstance vi = r.exac("'Hector' / wa_v_av40 / 'Achilles'.");
        Instance scene1 = vi.getSubject().getScene();
        r.exec("$CreateScene #Camelot Current RelatedAs " + relation
                + " With Instances 'Arthur', 'Lancelot'");
        vi = r.exac("'Arthur' / wa_v_av41 / 'Lancelot'.");
        Instance scene2 = vi.getSubject().getScene();
        return new SimpleEntry<>(scene1, scene2);
    }

    /**
     * Test for the explicit creation of scene succession
     */
    @Test
    public void testFictional() {
        TestHelper.testStart(
                "scene fictional created by L2 and verified by SceneRelationHelper");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        SimpleEntry<Instance, Instance> tmp = createL2SceneRelation(r,
                Hardwired.LABEL_SCENEREL_FICTIONAL_FUTURE);
        Instance sceneReality = tmp.getKey();
        Instance sceneFictional = tmp.getValue();
        boolean isView = SceneRelationHelper.isFictionalScene(r.agent,
                sceneFictional, sceneReality);
        Assert.assertTrue(isView);
    }

    /**
     * Test for the explicit creation of scene succession
     */
    @Test
    public void testSuccession() {
        TestHelper.testStart(
                "scene succession created by L2 and verified by SceneRelationHelper");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        SimpleEntry<Instance, Instance> tmp =
                createL2SceneRelation(r, Hardwired.LABEL_SCENEREL_SUCCESSOR);
        Instance scene1 = tmp.getKey();
        Instance scene2 = tmp.getValue();
        boolean isSuccessor =
                SceneRelationHelper.isSuccessorScene(r.agent, scene2, scene1);
        boolean isSuccessor2 =
                SceneRelationHelper.isSuccessorScene(r.agent, scene1, scene2);
        Assert.assertTrue(isSuccessor);
        Assert.assertTrue(!isSuccessor2);
        Set<Instance> previous =
                SceneRelationHelper.previousChainOfScenes(r.agent, scene2);
        TextUi.println(previous);
    }

    /**
     * Test for the explicit creation of scene succession
     */
    @Test
    public void testView() {
        TestHelper.testStart(
                "scene view created by L2 and verified by SceneRelationHelper");
        Runner r = ArtificialDomain.runnerArtificialAutobiography();
        SimpleEntry<Instance, Instance> tmp =
                createL2SceneRelation(r, Hardwired.LABEL_SCENEREL_VIEW);
        Instance sceneReality = tmp.getKey();
        Instance sceneView = tmp.getValue();
        boolean isView = SceneRelationHelper.isViewScene(r.agent, sceneView,
                sceneReality);
        Assert.assertTrue(isView);
    }
}
