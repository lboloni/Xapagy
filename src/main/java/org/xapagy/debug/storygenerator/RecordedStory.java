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
package org.xapagy.debug.storygenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.autobiography.ABStory;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Hardwired.SceneRelation;
import org.xapagy.debug.Runner;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.formatters.Formatter;

/**
 * 
 * Recorded story
 * 
 * @author Ladislau Boloni
 * Created on: Feb 9, 2012
 */
public class RecordedStory implements XapagyComponent {

    private static long counter = System.currentTimeMillis();
    private String identifier = null;
    private List<RsOneLine> onelines = new ArrayList<>();
    /**
     * The list of recordedVis acquired from the stories
     */
    private List<VerbInstance> recordedVis = new ArrayList<>();
    private List<RsScene> rsScenes = new ArrayList<>();
    private List<ABStory> stories = new ArrayList<>();

    /**
     * The part of the initializations of the scenes and the instances
     */
    private ABStory storyInitialization;

    /**
     * Added after the initialization: setting up identities, relations between
     * scenes and so on.
     */
    private ABStory storyPostInitialization = new ABStory();

    /**
     * Create a recorded story with a number of scenes
     */
    public RecordedStory(String... scenes) {
        RecordedStory.counter++;
        for (String sceneName : scenes) {
            String labelScene = "#" + sceneName + RecordedStory.counter;
            rsScenes.add(new RsScene(sceneName, labelScene));
        }
        generate();
    }

    /**
     * Adds an identity between instances in two scenes, instances being marked
     * with count numbers
     * 
     * @param from
     *            - the name of the RsScene from where the first instance goes
     * @param i
     * @param to
     *            - the name of the RsScene of the second instance
     * @param j
     */
    public void addIdentity(String from, int i, String to, int j) {
        String idStatement =
                "The " + getRsScene(from).getInstanceLabels().get(i)
                        + " -- in -- scene " + getRsScene(from).getLabelScene()
                        + "/ is-identical / the "
                        + getRsScene(to).getInstanceLabels().get(j)
                        + " -- in -- scene " + getRsScene(to).getLabelScene()
                        + ".";
        storyPostInitialization.add(idStatement);
    }

    /**
     * Adds an identity between instances in two scenes, instances being marked
     * with labels (preferably the same ones as used to be created.
     * 
     * @param from
     *            - the name of the RsScene from where the first instance goes
     * @param fromAttributes
     * @param to
     *            - the name of the RsScene of the second instance
     * @param toAttributes
     */
    public void addIdentity(String from, String fromAttributes, String to,
            String toAttributes) {
        String idStatement =
                "The " + fromAttributes + " -- in -- scene "
                        + getRsScene(from).getLabelScene()
                        + "/ is-identical / the " + toAttributes
                        + " -- in -- scene " + getRsScene(to).getLabelScene()
                        + ".";
        storyPostInitialization.add(idStatement);
    }

    /**
     * Adds proper names to all the instances
     * 
     * @param template
     */
    public void addRandomPropernames(ProperNameGenerator sg) {
        for (RsScene rsds : rsScenes) {
            rsds.addRandomPropernames(sg);
        }
    }

    /**
     * Adds a
     * 
     * @param from
     *            - name of the scene in the recorded story
     * @param to
     *            - the name of the scene in the recorded story
     * @param relation
     *            - use: successor, view, fictional-future
     */
    public void addSceneRelation(String from, String to,
            String sceneRelationLabel) {
        SceneRelation sr =
                Hardwired.parseSceneRelationLabel(sceneRelationLabel);
        if (sr == SceneRelation.NONE) {
            return;
        }
        String relationWord = Hardwired.getSceneRelationWord(sr);
        String tmp =
                "The scene " + getRsScene(from).getLabelScene() + " / "
                        + relationWord + " / the scene "
                        + getRsScene(to).getLabelScene() + ".";
        storyPostInitialization.add(tmp);
    }

    public void addStory(ABStory story) {
        stories.add(story);
    }

    /**
     * Special implementation of exac for the RecordedStory
     * 
     * @param story
     * @param line
     * @return
     */
    private List<VerbInstance> exec(Runner r, ABStory story, int line) {
        List<AbstractLoopItem> history = r.agent.getLoop().getHistory();
        int histCountOrig = history.size();
        String storyLine = story.getLine(line);
        List<VerbInstance> vis = r.exec(storyLine);
        int histCountNew = history.size();
        List<AbstractLoopItem> changes =
                new ArrayList<>(history.subList(histCountOrig, histCountNew));
        RsOneLine rsOneLine =
                new RsOneLine(r.agent, this, story, line, changes);
        onelines.add(rsOneLine);
        return vis;
    }

    /**
     * Generates the stories
     * 
     */
    private void generate() {
        storyInitialization = generateInitialization();
    }

    /**
     * Generate init part (should work for arbitrary number of scenes)
     * 
     * @return
     */
    private ABStory generateInitialization() {
        ABStory retval = new ABStory();
        for (int i = 0; i != rsScenes.size(); i++) {
            RsScene rsds = rsScenes.get(i);
            if (i == 0) {
                retval.add("A scene " + rsds.getLabelScene()
                        + " / is-only-scene.");
            } else {
                retval.add("A scene " + rsds.getLabelScene() + " / exists.");
                retval.add("$ChangeScene " + rsds.getLabelScene());
            }
            for (String concepts : rsds.getInstanceLabels()) {
                retval.add("A " + concepts + " / exists.");
            }
        }
        // move back to the first scene as the current scene
        // retval.add("The scene " + rsScenes.get(0).getLabelScene()
        // + " / is-current-scene.");
        retval.add("$ChangeScene " + rsScenes.get(0).getLabelScene());
        retval.add(storyPostInitialization);
        return retval;
    }

    /**
     * Returns all the story portions assembled together
     * 
     * @return
     */
    public ABStory getFullStory() {
        generate();
        ABStory retval = new ABStory();
        retval.add(storyInitialization);
        for (ABStory story : stories) {
            retval.add(story);
        }
        return retval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.instances.XapagyComponent#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    public List<RsOneLine> getOnelines() {
        return onelines;
    }

    /**
     * Returns a read-only copy of the VIs
     * 
     * @return the recordedVis
     */
    public List<VerbInstance> getRecordedVis() {
        return Collections.unmodifiableList(recordedVis);
    }

    /**
     * Returns the RsdScene with a given name
     * 
     * @param name
     * @return
     */
    public RsScene getRsScene(String name) {
        for (RsScene rsds : rsScenes) {
            if (rsds.getName().equals(name)) {
                return rsds;
            }
        }
        throw new Error("Could not find RsdScene with name: " + name);
    }

    /**
     * @return the rsScenes
     */
    public List<RsScene> getRsScenes() {
        return rsScenes;
    }

    /**
     * @return the stories
     */
    public List<ABStory> getStories() {
        return stories;
    }

    /**
     * @return the storyInitialization
     */
    public ABStory getStoryInitialization() {
        return storyInitialization;
    }

    /**
     * @return the storyPostInitialization
     */
    public ABStory getStoryPostInitialization() {
        return storyPostInitialization;
    }

    /**
     * Sets the identifier, then runs all the components
     * 
     * @param r
     */
    public void runAll(Runner r) {
        identifier =
                r.agent.getIdentifierGenerator().getRecordedStoryIdentifier();
        generate();
        runInitialization(r);
        for (ABStory story : stories) {
            runStory(r, story);
        }
    }

    /**
     * Runs the initialization and assigns the instances in the RsdScene
     * objects.
     * 
     * It is based on the exact match with the generateInitialization
     * 
     * @param r
     */
    private void runInitialization(Runner r) {
        int count = 0;
        // String statement = null;
        for (int i = 0; i != rsScenes.size(); i++) {
            RsScene rsds = rsScenes.get(i);
            List<VerbInstance> vis = exec(r, storyInitialization, count++);
            VerbInstance viAction = r.eh.getAction(vis);
            rsds.setSceneInstance(viAction.getSubject());

            for (int j = 0; j != rsds.getInstanceLabels().size(); j++) {
                vis = exec(r, storyInitialization, count++);
                viAction = r.eh.getAction(vis);
                if (viAction != null) {
                    rsds.getInstances().add(viAction.getSubject());
                }
            }
        }
        // select back the first scene
        exec(r, storyInitialization, count++);
        // if there is anything more to run, run it
        while (count < storyInitialization.length()) {
            exec(r, storyInitialization, count++);
        }
    }

    /**
     * Runs a certain story
     * 
     * @param r
     * @param story
     *            - the story to run
     */
    private void runStory(Runner r, ABStory story) {
        // r.agent.getLoop().getHistory();
        // for (String st : story.getLines()) {
        // recordedVis.add(r.exac(st));
        // }
        for (int i = 0; i != story.getLines().size(); i++) {
            List<VerbInstance> vis = exec(r, story, i);
            // keep the old style... actions only
            if (!vis.isEmpty()) {
                VerbInstance viAction = r.eh.getAction(vis);
                recordedVis.add(viAction);
            }
        }
    }

    /**
     * @param identifier
     *            the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Default toString function -extend it
     */
    @Override
    public String toString() {
        Formatter fmt = new Formatter();
        fmt.add("RecordedStory");
        fmt.indent();
        fmt.add("The full story:");
        fmt.add(getFullStory());
        return fmt.toString();
    }

}
