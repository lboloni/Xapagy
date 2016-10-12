/*
   This file is part of the Xapagy project
   Created on: Apr 9, 2013
 
   org.xapagy.debug.storyline.StoryLine
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.storyline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	 * Creates a story line from a name and from a set of scenes
	 * 
	 * @param name
	 *            - the name of the story line
	 * @param scenes
	 *            - the scenes in the story line. Normally, this should be a set
	 *            of scenes connected through succession / etc. relations
	 */
	public StoryLine(String name, List<Instance> scenes) {
		super();
		this.name = name;
		this.scenes = new ArrayList<Instance>(scenes);
	}

	/**
	 * Returns true if the story line contains the given instance
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
		//for (Instance scene : scenes) {
		//	if (scene.getReferringVis().contains(vi)) {
		//		return true;
		//	}
		//}
		//return false;
		//
		//  FIXME: cache me!!!
		//
		return getVis().contains(vi);
	}

	/**
	 * Returns the name of the story line
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the scenes of the story line in the order in which they had been
	 * passed
	 * 
	 * @return
	 */
	public List<Instance> getScenes() {
		return Collections.unmodifiableList(scenes);
	}

	/**
	 * Returns the scenes of the story line in the order in which they had been
	 * executed
	 * 
	 * @return
	 */
	public List<VerbInstance> getVis() {
		Set<VerbInstance> viSet = new HashSet<VerbInstance>();
		for (Instance scene : scenes) {
			for (Instance instance : scene.getSceneMembers()) {
				viSet.addAll(instance.getReferringVis());
			}
		}
		List<VerbInstance> retval = new ArrayList<>(viSet);
		Collections.sort(retval, new Comparator<VerbInstance>() {

			@Override
			public int compare(VerbInstance o1, VerbInstance o2) {
				return Double.compare(o1.getCreationTime(), o2.getCreationTime());
			}
		});
		return Collections.unmodifiableList(retval);
	}

	/**
	 * Returns the last verbinstance in the story line
	 * @return
	 */
	public VerbInstance lastVi() {
		List<VerbInstance> vis = getVis();
		return vis.get(vis.size()-1);
	}
	
	
	/**
	 * Two storylines overlap if they have a common scene.
	 * 
	 * In most cases, if storylines overlap, they will also be identical, but
	 * there may be cases when this is not the case (eg. evolving storylines)
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
	}

}
