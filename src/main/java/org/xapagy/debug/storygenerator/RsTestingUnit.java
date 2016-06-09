/*
   This file is part of the Xapagy project
   Created on: Feb 17, 2012

   test.xapagy.activity.shadowmaintenance.StoryTestingUnit

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug.storygenerator;

import java.util.List;

import org.xapagy.autobiography.ABStory;
import org.xapagy.debug.Runner;
import org.xapagy.httpserver.WebGui;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.observers.BreakObserver;

/**
 *
 * General purpose unit for testing shadowing using RecordedStory units.
 *
 * The general idea is that the test requires: a history, a specific shadow
 * story in the past (which would be tested as the shadow of the current story)
 * and the focus story. We can specify the different parametrization of the
 * shadows.
 *
 * During the execution, the values are kept in RecordedStory objects, which
 * allow us to access the various VIs and instances directly.
 *
 * The idea here is that using this setup, for different parameters, we can test
 * how the different shadow maintenance DAs are operating.
 *
 * @author Ladislau Boloni
 *
 */
public class RsTestingUnit {

    private ABStory paramsFocus;
    private ABStory paramsHistory;
    private RecordedStory rsFocus;
    private List<RecordedStory> rsHistory = null;
    private RecordedStory rsShadow;
    private Shadows sf;

    /**
     * Constructor of a testing unit
     *
     * @param r
     *            - a Runner with an active agent (this agent might already has
     *            a history)
     * @param paramsHistory
     *            - include string for the parameters used when creating the history and the
     *            shadow, this can be normally
     * @param rsHistory
     *            - a series of stories which constitute the history of the
     *            agent
     * @param rsShadow
     *            - the specific story with which we will test matching
     * @param paramsFocusString
     *            - input string for the parameters used when running the focus (ie. during
     *            testing)
     * @param rsFocus
     *            - the story in the focus
     *
     */
    public RsTestingUnit(ABStory paramsHistory, List<RecordedStory> rsHistory,
            RecordedStory rsShadow, ABStory paramsFocus, RecordedStory rsFocus) {
        this.rsHistory = rsHistory;
        this.rsShadow = rsShadow;
        this.rsFocus = rsFocus;
        this.paramsHistory = paramsHistory;
        this.paramsFocus = paramsFocus;
    }

    /**
     * Starts a debugging sequence of the RsTesting unit. It can directly
     * replace the runAll()
     *
     */
    public void debugAll(Runner r) {
        WebGui.startWebGui(r.agent);
        r.agent.getDebugInfo().setRsTestingUnit(this);
        r.agent.addObserver("BreakObserver", new BreakObserver(false));
        runAll(r);
        TextUi.enterToTerminate();
    }

    /**
     * Creates a story object which comprises the full story of this test unit
     *
     * @return
     */
    public ABStory getFullStory() {
        ABStory story = new ABStory();
        for (RecordedStory rs : rsHistory) {
            story.add(rs.getFullStory());
            story.add("----");
        }
        story.add(rsShadow.getFullStory());
        story.add("----");
        story.add(rsFocus.getFullStory());
        return story;
    }

    /**
     * Returns the shadow energy between indexed instances in a RsTestingUnit
     *
     * @param sceneIdFocus
     *            - the id of the scene
     * @param idFocus
     * @param sceneIdShadow
     * @param idShadow
     * @param ec
     *            - the specific energy
     * @return
     */
    public double getInstanceShadowEnergy(String sceneIdFocus, int idFocus,
            String sceneIdShadow, int idShadow, String ec) {
        RsScene rssFocus = rsFocus.getRsScene(sceneIdFocus);
        RsScene rssShadow = rsShadow.getRsScene(sceneIdShadow);
        Instance instanceFocus = rssFocus.getInstances().get(idFocus);
        Instance instanceShadow = rssShadow.getInstances().get(idShadow);
        double retval = sf.getEnergy(instanceFocus, instanceShadow, ec);
        return retval;
    }

    /**
     * Returns the shadow salience between indexed instances in a RsTestingUnit
     *
     * @param sceneIdFocus
     *            - the id of the scene
     * @param idFocus
     * @param sceneIdShadow
     * @param idShadow
     * @param ec
     *            - the specific energy
     * @return
     */
    public double getInstanceShadowSalience(String sceneIdFocus, int idFocus,
            String sceneIdShadow, int idShadow, String ec) {
        RsScene rssFocus = rsFocus.getRsScene(sceneIdFocus);
        RsScene rssShadow = rsShadow.getRsScene(sceneIdShadow);
        Instance instanceFocus = rssFocus.getInstances().get(idFocus);
        Instance instanceShadow = rssShadow.getInstances().get(idShadow);
        double retval = sf.getSalience(instanceFocus, instanceShadow, ec);
        return retval;
    }

    public ABStory getParamsFocus() {
        return paramsFocus;
    }

    public ABStory getParamsHistory() {
        return paramsHistory;
    }

    public RecordedStory getRsFocus() {
        return rsFocus;
    }

    public List<RecordedStory> getRsHistory() {
        return rsHistory;
    }

    public RecordedStory getRsShadow() {
        return rsShadow;
    }

    /**
     * Returns the shadow energy between indexed scenes in a RsTestingUnit
     *
     * @param sceneIdFocus
     * @param sceneIdShadow
     * @param ec
     * @return
     */
    public double getSceneShadowEnergy(String sceneIdFocus,
            String sceneIdShadow, String ec) {
        RsScene rssFocus = rsFocus.getRsScene(sceneIdFocus);
        RsScene rssShadow = rsShadow.getRsScene(sceneIdShadow);
        Instance sceneFocus = rssFocus.getSceneInstance();
        Instance sceneShadow = rssShadow.getSceneInstance();
        double retval = sf.getSalience(sceneFocus, sceneShadow, ec);
        return retval;
    }

    /**
     * Returns the shadow salience between indexed scenes in a RsTestingUnit
     *
     * @param sceneIdFocus
     * @param sceneIdShadow
     * @param ec
     * @return
     */
    public double getSceneShadowSalience(String sceneIdFocus,
            String sceneIdShadow, String ec) {
        RsScene rssFocus = rsFocus.getRsScene(sceneIdFocus);
        RsScene rssShadow = rsShadow.getRsScene(sceneIdShadow);
        Instance sceneFocus = rssFocus.getSceneInstance();
        Instance sceneShadow = rssShadow.getSceneInstance();
        double retval = sf.getSalience(sceneFocus, sceneShadow, ec);
        return retval;
    }

    /**
     * Returns the energy of the shadow for a given energy color between two VIs
     *
     * @param idFocus
     *            - the count of the VI in the focus story
     * @param idShadow
     *            - the count of the VI in the shadow story
     * @param ec
     *            - the energy color
     *
     * @return
     */
    public double getViShadowEnergy(int idFocus, int idShadow, String ec) {
        VerbInstance viFocus = rsFocus.getRecordedVis().get(idFocus);
        VerbInstance viShadow = rsShadow.getRecordedVis().get(idShadow);
        double retval = sf.getEnergy(viFocus, viShadow, ec);
        return retval;
    }

    /**
     * Returns the salience of the shadow for a given energy color between two
     * VIs
     *
     * @param idFocus
     *            - the count of the VI in the focus story
     * @param idShadow
     *            - the count of the VI in the shadow story
     * @param ec
     *            - the energy color
     *
     * @return
     */
    public double
            getViShadowSalience(int idFocus, int idShadow, String ec) {
        VerbInstance viFocus = rsFocus.getRecordedVis().get(idFocus);
        VerbInstance viShadow = rsShadow.getRecordedVis().get(idShadow);
        double retval = sf.getSalience(viFocus, viShadow, ec);
        return retval;
    }

    /**
     * Run all the stories of the RsTestingUnit
     */
    public void runAll(Runner r) {
        r.agent.getDebugInfo().setRsTestingUnit(this);
        sf = r.agent.getShadows();
        runHistory(r);
        runShadowStory(r);
        runFocusStory(r);
    }

    /**
     * Run the focus story
     */
    public void runFocusStory(Runner r) {
        r.agent.getDebugInfo().setRsTestingUnit(this);
        r.exec(paramsFocus);
        rsFocus.runAll(r);
    }

    /**
     * Runs the autobiography of the agent
     */
    public void runHistory(Runner r) {
        r.agent.getDebugInfo().setRsTestingUnit(this);
        r.exec(paramsHistory);
        for (RecordedStory rs : rsHistory) {
            rs.runAll(r);
            r.exec("---");
        }
    }

    /**
     * Run the shadow story we are interested in
     *
     */
    public void runShadowStory(Runner r) {
        r.agent.getDebugInfo().setRsTestingUnit(this);
        if (rsShadow == null) {
            return;
        }
        r.exec(paramsHistory);
        rsShadow.runAll(r);
        r.exec("---");
    }

    public void setParamsFocus(ABStory paramsFocus) {
        this.paramsFocus = paramsFocus;
    }

    public void setParamsHistory(ABStory paramsHistory) {
        this.paramsHistory = paramsHistory;
    }

}
