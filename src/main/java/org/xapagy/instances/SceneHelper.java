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
package org.xapagy.instances;

import java.util.HashSet;
import java.util.Set;

import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * @author lboloni Created on: Feb 25, 2017
 */
public class SceneHelper {

	/**
	 * Extracts all the scenes referenced in a VI. If an instance is not resolved yet, it skips it.
	 * 
	 * @param vi
	 *            - a fully resolved verb instance
	 * @param quotesAsWell
	 *            - do we also consider the scenes in the quotes???
	 * 
	 * @return
	 */
	public static Set<Instance> extractScenes(VerbInstance vi, boolean quotesAsWell) {
		Set<Instance> retval = new HashSet<>();
		if (!vi.getViType().equals(ViType.QUOTE)) {
			for (ViPart part : ViStructureHelper.getAllowedInstanceParts(vi.getViType())) {
				// only mess with it if it is resolved
				if (vi.getResolvedParts().containsKey(part)) {
					retval.add(((Instance) vi.getPart(part)).getScene());
				}
			}
		} else {
			// if this is a quote
			retval.add(vi.getSubject().getScene());
			if (quotesAsWell) {
				retval.addAll(SceneHelper.extractScenes(vi.getQuote(), quotesAsWell));
			}
		}
		return retval;
	}

	
	/**
	 * Extracts all the instances referenced in a VI. If an instance is not resolved yet, it skips it.
	 * 
	 * @param vi
	 *            - a fully resolved verb instance
	 * @param quotesAsWell
	 *            - do we also consider the scenes in the quotes???
	 * 
	 * @return
	 */
	public static Set<Instance> extractInstances(VerbInstance vi, boolean quotesAsWell) {
		Set<Instance> retval = new HashSet<>();
		if (!vi.getViType().equals(ViType.QUOTE)) {
			for (ViPart part : ViStructureHelper.getAllowedInstanceParts(vi.getViType())) {
				// only mess with it if it is resolved
				if (vi.getResolvedParts().containsKey(part)) {
					retval.add(((Instance) vi.getPart(part)));
				}
			}
		} else {
			// if this is a quote
			retval.add(vi.getSubject().getScene());
			if (quotesAsWell) {
				retval.addAll(SceneHelper.extractInstances(vi.getQuote(), quotesAsWell));
			}
		}
		return retval;
	}

	
	
	/**
	 * Returns the dominant scene of an instance - I think that this is the
	 * scene of the subject
	 * 
	 * @param vi
	 * @return
	 */
	public static Instance getDominantScene(VerbInstance vi) {
		return vi.getSubject().getScene();
	}

	/**
	 * Returns all the scenes referenced by the instance (also adds the scene of
	 * the referenced item)
	 *
	 * @return
	 */
	/*
	 * public Set<Instance> getReferencedScenes() { Set<Instance> retval = new
	 * HashSet<>(); for (ViPart part :
	 * ViStructureHelper.getAllowedInstanceParts(getViType())) { Instance
	 * instance = (Instance) resolvedParts.get(part); if (instance != null) {
	 * retval.add(instance.getScene()); } } return retval; }
	 */

}
