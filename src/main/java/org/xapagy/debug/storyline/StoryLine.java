/*
   This file is part of the Xapagy project
   Created on: Apr 9, 2013
 
   org.xapagy.debug.storyline.StoryLine
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug.storyline;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;

/**
 * A story line is a set of interconnected scenes, which corresponds to our
 * definition of a story of a list of events.
 * 
 * It is not internally used by Xapagy but it can be an important debugging
 * tool.
 * 
 * The scenes in the story line can be connected in the following ways:
 * 
 * <ul>
 * <li>scene relations - succession, fictional-future, view
 * <li>quotes: any vi's where the inquit is in one scene and the quote in the
 * other
 * <li>crossing VIs, where the subject is in one scene and the object in the
 * other.
 * </ul>
 * 
 * @author Ladislau Boloni
 * 
 */
public class StoryLine implements Serializable {

    private static final long serialVersionUID = 1913364857856687297L;
    private String name = "unnamed";
    /**
     * The list of scenes in the story line
     */
    private List<Instance> scenes;
    /**
     * All the VIs in the story line
     */
    private List<VerbInstance> vis;

    /**
     * @param name
     * @param scenes
     * @param vis
     */
    public StoryLine(String name, List<Instance> scenes, List<VerbInstance> vis) {
        super();
        this.name = name;
        this.scenes = scenes;
        this.vis = vis;
    }

    /**
     * Returns true if the story line contains the given VI
     * 
     * @param vi
     * @return
     */
    public boolean contains(Instance instance) {
        for (Instance scene : scenes) {
            if (scene.equals(instance)) {
                return true;
            }
            if (scene.getSceneMembers().contains(instance)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the story line contains the given VI
     * 
     * @param vi
     * @return
     */
    public boolean contains(VerbInstance vi) {
        return vis.contains(vi);
    }

    public String getName() {
        return name;
    }

    public List<Instance> getScenes() {
        return scenes;
    }

    public List<VerbInstance> getVis() {
        return vis;
    }

    /**
     * Two storylines overlap if they have a common scene.
     * 
     * In most cases, if storylines overlap, they will also be identical, but
     * there my be cases when this is not the case (eg. evolving storylines)
     * 
     * @param st
     * @return
     */
    public boolean isOverlapping(StoryLine st) {
        Set<Instance> intersection = new HashSet<>(scenes);
        intersection.retainAll(st.scenes);
        return !intersection.isEmpty();
    }

    /**
     * Merge in the content of another storyline (this is normally done only if
     * they are overlapping)
     * 
     * @param st
     */
    public void mergeIn(StoryLine st) {
        for (Instance scene : st.scenes) {
            if (!scenes.contains(st)) {
                scenes.add(scene);
            }
        }
        for (VerbInstance vi : st.vis) {
            if (!vis.contains(vi)) {
                vis.add(vi);
            }
        }
    }

}
