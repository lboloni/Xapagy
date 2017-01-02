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
package org.xapagy.storyline;

import java.util.HashMap;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.IxwFormattable;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.smartprint.SpInstance;

/**
 * This object collects the mapping between two storylines, normally one of them
 * in the AM / shadows, while the other one in the focus.
 *
 * We have mappings for instance to instance, scene to scene and VI to VI.
 *
 * The normal assumption is that the mappings are bidirectional and unique.
 * (TODO: this assumption might be later revisited). We do not assume that every
 * instance and VI has a match.
 * 
 * @author Ladislau Boloni Created on Jan 1, 2017
 */
public class slrMapping implements IxwFormattable {

	// focus to shadow instance pair
	private Map<Instance, Instance> f2sInstanceMap = new HashMap<>();
	// shadow to focus instance pair
	private Map<Instance, Instance> s2fInstanceMap = new HashMap<>();

	// focus to shadow scene pair
	private Map<Instance, Instance> f2sSceneMap = new HashMap<>();

	// shadow to focus scene pair
	private Map<Instance, Instance> s2fSceneMap = new HashMap<>();

	// focus to shadow scene pair
	private Map<VerbInstance, VerbInstance> f2sViMap = new HashMap<>();

	// shadow to focus scene pair
	private Map<VerbInstance, VerbInstance> s2fViMap = new HashMap<>();

	private Agent agent;
	
	public slrMapping(Agent agent) {
		this.agent = agent;
	}
	
	
	/**
	 * Records a bidirectional mapping from instance to instance
	 * 
	 * @param fi
	 * @param si
	 */
	public void putInstanceMap(Instance fi, Instance si) {
		f2sInstanceMap.put(fi, si);
		s2fInstanceMap.put(si, fi);
	}

	/**
	 * Records a bidirectional mapping from scene to scene
	 * 
	 * @param fi
	 * @param si
	 */
	public void putSceneMap(Instance fi, Instance si) {
		f2sSceneMap.put(fi, si);
		s2fSceneMap.put(si, fi);
	}

	/**
	 * Records a bidirectional mapping from VI to VI
	 * 
	 * @param fvi
	 * @param svi
	 */
	public void putSceneMap(VerbInstance fvi, VerbInstance svi) {
		f2sViMap.put(fvi, svi);
		s2fViMap.put(svi, fvi);
	}

	/**
	 * Helper for the formatting of instance to instance mapping
	 * 
	 * @param fmt
	 * @param map
	 */
	private void formatMapping(IXwFormatter fmt, Map<Instance, Instance> map, String label) {
		if (map.isEmpty()) {
			fmt.add(label + " is EMPTY");
			return;
		}
		// not empty
		fmt.add(label);
		fmt.indent();
		for (Instance fi : map.keySet()) {
			Instance si = map.get(fi);
			if (si != null) {
				fmt.is(SpInstance.spc(fi, agent), SpInstance.spc(si, agent));
			} else {
				fmt.is(SpInstance.spc(fi, agent), "<< no mapping found >>");
			}
		}
		fmt.deindent();
	}

	/**
	 * XwFormatting function, basically just lists everything here
	 */
	@Override
	public void formatTo(IXwFormatter fmt, int detailLevel) {
		fmt.add("slrMapping");
		fmt.indent();
		formatMapping(fmt, f2sSceneMap,"Scene mapping: focus to shadow");
		formatMapping(fmt, s2fSceneMap,"Scene mapping: shadow to focus");
		formatMapping(fmt, f2sInstanceMap,"Instance mapping: focus to shadow");
		formatMapping(fmt, s2fInstanceMap,"Instance mapping: shadow to focus");
		// f2s VI map
		fmt.add("VI mapping: focus to shadow");
		fmt.indent();
		fmt.add(f2sViMap);
		fmt.deindent();
		// s2f VI map
		fmt.add("VI mapping: shadow to focus");
		fmt.indent();
		fmt.add(s2fViMap);
		fmt.deindent();

	}

	/**
	 * toString function, falls back on formatTo on text
	 */
	@Override
	public String toString() {
		IXwFormatter fmt = new TwFormatter();
		formatTo(fmt, 0);
		return fmt.toString();
	}

	/**
	 * @return the f2sInstanceMap
	 */
	public Map<Instance, Instance> getF2sInstanceMap() {
		return f2sInstanceMap;
	}

	/**
	 * @return the f2sSceneMap
	 */
	public Map<Instance, Instance> getF2sSceneMap() {
		return f2sSceneMap;
	}

	/**
	 * @return the f2sViMap
	 */
	public Map<VerbInstance, VerbInstance> getF2sViMap() {
		return f2sViMap;
	}

	/**
	 * @return the s2fInstanceMap
	 */
	public Map<Instance, Instance> getS2fInstanceMap() {
		return s2fInstanceMap;
	}

	/**
	 * @return the s2fSceneMap
	 */
	public Map<Instance, Instance> getS2fSceneMap() {
		return s2fSceneMap;
	}

	/**
	 * @return the s2fViMap
	 */
	public Map<VerbInstance, VerbInstance> getS2fViMap() {
		return s2fViMap;
	}

}
