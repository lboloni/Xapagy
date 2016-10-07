/*
   This file is part of the Xapagy project
   Created on: Apr 9, 2013
 
   org.xapagy.debug.storyline.StoryLineHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.storyline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.prettyhtml.ColorCodeRepository;

/**
 * A repository for story lines
 * 
 * @author Ladislau Boloni
 * 
 */
public class StoryLineRepository implements Serializable {

    private static final long serialVersionUID = -3341081965861340838L;

    /**
     * Extract a list of story lines from a set of VIs. In practice this is used
     * to extract all the story lines that are currently referenced by any of the
     * shadows...
     * 
     * @param agent
     * @param vis
     * @return
     */
    public static List<StoryLine> createStoryLines(Agent agent,
            List<VerbInstance> vis) {
        List<StoryLine> retval = new ArrayList<>();
        List<VerbInstance> vis2 = new ArrayList<>(vis);
        while (!vis2.isEmpty()) {
            // initialize the components of the scene
            List<VerbInstance> stVis = new ArrayList<>();
            Set<Instance> stScenes = new HashSet<>();
            VerbInstance starter = vis2.get(0);
            vis2.remove(0);
            stVis.add(starter);
            stScenes.addAll(starter.getReferencedScenes());
            stScenes =
                    StoryLineRepository.explodeRelatedScenes(agent, stScenes);
            for (Iterator<VerbInstance> vit = vis2.iterator(); vit.hasNext();) {
                VerbInstance current = vit.next();
                Set<Instance> test = new HashSet<>(stScenes);
                test.retainAll(current.getReferencedScenes());
                if (test.isEmpty()) {
                    continue;
                }
                // add the vi, and the
                stVis.add(current);
                stScenes.addAll(current.getReferencedScenes());
                stScenes =
                        StoryLineRepository.explodeRelatedScenes(agent,
                                stScenes);
                vit.remove();
            }
            // create the story line from the scenes sorted by time
            List<Instance> scenes = new ArrayList<>(stScenes);
            Collections.sort(scenes, new Comparator<Instance>() {

				@Override
				public int compare(Instance o1, Instance o2) {
					return Double.compare(o1.getCreationTime(), o2.getCreationTime());
				}
			});
            StoryLine st =
                    new StoryLine("storyline", scenes);
            retval.add(st);
        }
        return retval;
    }

    /**
     * Takes a set of scenes. Then for each of the scenes, adds to the set all
     * the scenes which are related
     * 
     * @return
     */
    private static Set<Instance> explodeRelatedScenes(Agent agent,
            Set<Instance> scenes) {
        Set<Instance> retval = new HashSet<>();
        retval.addAll(scenes);
        while (true) {
            int initSize = retval.size();
            Set<Instance> set = new HashSet<>(retval);
            for (Instance scene : retval) {
                Set<Instance> addOn = null;
                // add the succession ones
                addOn =
                        RelationHelper.getRelationSpecificPart(agent,
                                Hardwired.VR_SCENE_SUCCESSION, scene,
                                ViPart.Object, ViPart.Subject);
                set.addAll(addOn);
                addOn =
                        RelationHelper.getRelationSpecificPart(agent,
                                Hardwired.VR_SCENE_SUCCESSION, scene,
                                ViPart.Subject, ViPart.Object);
                set.addAll(addOn);
                // add the fictional-future
                addOn =
                        RelationHelper.getRelationSpecificPart(agent,
                                Hardwired.VR_SCENE_FICTIONAL_FUTURE, scene,
                                ViPart.Object, ViPart.Subject);
                set.addAll(addOn);
                addOn =
                        RelationHelper.getRelationSpecificPart(agent,
                                Hardwired.VR_SCENE_FICTIONAL_FUTURE, scene,
                                ViPart.Subject, ViPart.Object);
                set.addAll(addOn);
                // add the view
                addOn =
                        RelationHelper.getRelationSpecificPart(agent,
                                Hardwired.VR_SCENE_VIEW, scene, ViPart.Object,
                                ViPart.Subject);
                set.addAll(addOn);
                addOn =
                        RelationHelper.getRelationSpecificPart(agent,
                                Hardwired.VR_SCENE_VIEW, scene, ViPart.Subject,
                                ViPart.Object);
                set.addAll(addOn);
            }
            // iterate until we are not adding anything any more
            if (set.size() == initSize) {
                break;
            }
            retval = set;
        }
        return retval;
    }

    private Agent agent;
    // the last time it was updated, avoids repeated calls
    private double lastUpdated = -1;
    private List<StoryLine> storyLines = new ArrayList<>();

    /**
     * @param agent
     */
    public StoryLineRepository(Agent agent) {
        super();
        this.agent = agent;
    }

    /**
     * Returns the story line in which a certain instance is present, and null
     * if none of the story lines contains it
     * 
     * @param svi
     * @return
     */
    public StoryLine getStoryLineOfInstance(Instance instance) {
        for (StoryLine st : storyLines) {
            if (st.contains(instance)) {
                return st;
            }
        }
        return null;
    }

    /**
     * Returns the story line in which a certain VI is present, and null if none
     * of the story lines contains it
     * 
     * @param svi
     * @return
     */
    public StoryLine getStoryLineOfVi(VerbInstance vi) {
        for (StoryLine st : storyLines) {
            if (st.contains(vi)) {
                return st;
            }
        }
        return null;
    }

    /**
     * Get the list of stories
     * 
     * @return
     */
    public List<StoryLine> getStoryLines() {
        return storyLines;
    }

    /**
     * Maintains the story line list. If there are new story lines created, it
     * adds them. If there is overlap with one or more of the old one, merges
     * the new one in the first of them, and removes the other ones.
     * 
     * 
     */
    public void update(List<VerbInstance> vis) {
        List<StoryLine> newones =
                StoryLineRepository.createStoryLines(agent, vis);
        for (StoryLine st : newones) {
            List<StoryLine> existingOnes = new ArrayList<>();
            for (StoryLine stex : storyLines) {
                if (st.isOverlapping(stex)) {
                    existingOnes.add(stex);
                }
            }
            // if there is none, add the new ones
            if (existingOnes.isEmpty()) {
                storyLines.add(st);
            } else {
                // merge into the first and remove the other ones.
                existingOnes.get(0).mergeIn(st);
                existingOnes.remove(0);
                storyLines.removeAll(existingOnes);
            }
        }
    }

    /**
     * Generate the story lines, updates the color codes with the story lines
     * FIXME: this might be expensive, but we could cache it...
     * 
     * @param agent
     * @param colorCodeRepository
     */
    public void updateColorCodesWithFocus(ColorCodeRepository colorCodeRepository) {
        if (lastUpdated == agent.getTime()) {
            return;
        }
        lastUpdated = agent.getTime();
        Focus fc = agent.getFocus();
        Shadows sf = agent.getShadows();
        Set<VerbInstance> viset = new HashSet<>();
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            viset.add(vi);
            for (VerbInstance vishadow : sf.getMembers(vi, EnergyColors.SHV_GENERIC)) {
                viset.add(vishadow);
            }
        }
        List<VerbInstance> vilist = new ArrayList<>(viset);
        agent.getStoryLineRepository().update(vilist);
        for (StoryLine st : agent.getStoryLineRepository().getStoryLines()) {
            colorCodeRepository.getColorCode(st);
        }
    }

}
