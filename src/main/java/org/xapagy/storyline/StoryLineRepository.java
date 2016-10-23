/*
   This file is part of the Xapagy project
   Created on: Apr 9, 2013
 
   org.xapagy.debug.storyline.StoryLineHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.storyline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.prettyhtml.ColorCodeRepository;

/**
 * A repository for story lines: this is used for color coding in the UI
 * 
 * @author Ladislau Boloni
 * 
 */
public class StoryLineRepository implements Serializable {

	private static final long serialVersionUID = -3341081965861340838L;

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
			if (st.getVis().contains(vi)) {
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
		List<StoryLine> newones = StoryLineReasoning.createStoryLines(agent, vis);
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
