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
package org.xapagy.links;

import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.SceneHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.ViSet;

/**
 * A class that contains static functions that help us to
 * 
 * @author Ladislau Boloni Created on Jan 16, 2017
 */
public class CoincidenceHelper {

	/**
	 * Connects a new VI to a coincidence group
	 * 
	 * @param agent
	 * @param viCoincidence - a representative of the links
	 * @param viNew - the new VI
	 * @param explanation - the explanation text that should be added into the link quantums
	 */
	public static void connectToCoincidenceGroup(Agent agent, VerbInstance viCoincidence, VerbInstance viNew,
			String explanation) {
		Links la = agent.getLinks();
		la.copyAllLinks(viCoincidence, viNew, explanation);
		la.changeLinkByName(Hardwired.LINK_COINCIDENCE, viCoincidence, viNew, 1.0, explanation);
		la.changeLinkByName(Hardwired.LINK_COINCIDENCE, viNew, viCoincidence, 1.0, explanation);
	}

	/**
	 * Returns all the vis in the coincidence group as a set
	 * @param agent
	 * @param viCoincidence
	 * @return
	 */
	public static Set<VerbInstance> getVis(Agent agent, VerbInstance viCoincidence) {
		Set<VerbInstance> retval = new HashSet<>();
		retval.add(viCoincidence);
		Links la = agent.getLinks();
		ViSet others = la.getLinksByLinkName(viCoincidence, Hardwired.LINK_COINCIDENCE);
		retval.addAll(others.getParticipants());
		return retval;
	}
	
	/**
	 * Returns the dominant scenes in the coincidence set
	 * @param agent
	 * @param viCoincidence -  one arbitrary VI that identifies the coincidence set
	 * @return
	 */
	public static Set<Instance> getScenes(Agent agent, VerbInstance viCoincidence) {
		Set<Instance> retval = new HashSet<>();
		for(VerbInstance vi: getVis(agent,viCoincidence)) {
			retval.addAll(SceneHelper.extractScenes(vi, false));
		}
		return retval;
	}

	/**
	 * Returns all the instances referred to in a coincidence set
	 * @param agent
	 * @param viCoincidence - one arbitrary VI that identifies the coincidence set
	 * @param unrollGroups
	 * @return
	 */
	public static Set<Instance> getInstances(Agent agent, VerbInstance viCoincidence, boolean unrollGroups) {
		Set<Instance> retval = new HashSet<>();
		for(VerbInstance vi: getVis(agent,viCoincidence)) {
			Set<Instance> insts = SceneHelper.extractInstances(vi, false);
			for(Instance instance: insts) {
				retval.add(instance);
				if (!unrollGroups) {
					retval.addAll(GroupHelper.getMembersOfGroup(agent, instance));
				}
			}
		}
		return retval;
	}

	
}
