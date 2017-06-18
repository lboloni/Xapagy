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
package org.xapagy.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.reference.ReferenceResolution;
import org.xapagy.reference.rrContext;
import org.xapagy.reference.rrException;
import org.xapagy.reference.rrState;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;

/**
 * A common place for making references in various ways into the Xapagy agent.
 * It should also be accessible from Javascript
 * 
 * @author Ladislau Boloni Created on: Feb 16, 2016
 */
public class ReferenceAPI implements Serializable {

	private static final long serialVersionUID = -1085256278208360441L;
	private Agent agent;

	/**
	 * @param agent
	 */
	public ReferenceAPI(Agent agent) {
		super();
		this.agent = agent;
	}

	/**
	 * Returns the VI by the passed reference
	 * 
	 * @return
	 */
	public VerbInstance ViByLastCreated() {
		return agent.getLastVerbInstance();
	}

	/**
	 * Returns the VI using the ViMatchFilter mechanims
	 * 
	 * @return
	 */
	public VerbInstance ViByViMatchFilter() {
		throw new Error("ReferenceAPI.ViByViMatchFilter not implemented yet");
	}

	/**
	 * Returns a list of VIs using the ViMatchFilter mechanism
	 * 
	 * @return
	 */
	public List<VerbInstance> VisByViMatchFilter() {
		throw new Error("ReferenceAPI.VisByViMatchFilter not implemented yet");
	}

	/**
	 * Returns an instance as if it would have been done through the Xapi reference
	 * 
	 * @param ref
	 * @return
	 */
	public Instance InstanceByRef(String ref) {
        // allow ' in the text as a replacement for "
        ref = ref.replaceAll("'", "\"");
		XapiParser xp = agent.getXapiParser();
		Instance scene = agent.getFocus().getCurrentScene();
		ViPart partInVi = ViPart.Subject;
		VerbOverlay verbsInVi = VerbOverlay.createVO(agent, Hardwired.V_DOES_NOTHING, Hardwired.VM_ACTION_MARKER);
		VerbInstance viInquitParent = null;
		XapiReference reference;
		try {
			reference = xp.parseNounPhrase(ref, XapiPositionInParent.SUBJECT);
		} catch (XapiParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		rrContext rrc = rrContext.createFromXapiReference(agent, reference, verbsInVi, partInVi, scene, viInquitParent);
        SimpleEntry<Instance, rrState> result;
		try {
			result = ReferenceResolution.resolveReference(rrc);
		} catch (rrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
        // TextUi.println(result.getValue().toString());
        // TextUi.println(result.getKey().toString());
        return result.getKey();
	}

	/**
	 * Utility function, used in test / debugging to extract a specific VI
	 * 
	 * @param label
	 * @return
	 */
	public VerbInstance ViByLabelFromFocus(String label) {
		List<VerbInstance> vis = VisByLabels(Arrays.asList(label), EnergyColors.FOCUS_VI);
		if (vis.size() != 1) {
			throw new Error("Focus.getViWithLabel - matching VIs found " + vis.size());
		}
		return vis.get(0);
	}

	/**
	 * Returns a set of instances which are in the focus, have the
	 * FOCUS_INSTANCE energy and they have ANY of these labels
	 * 
	 * @param agent
	 *            the agent
	 * @param desiredLabels
	 * @return
	 */
	public List<Instance> InstancesByLabelInFocus(List<String> desiredLabels, String ec) {
		// make the labels full
		List<String> fullDesiredLabels = new ArrayList<>();
		for (String label : desiredLabels) {
			fullDesiredLabels.add(agent.getLabelSpaces().fullLabel(label));
		}
		Focus fc = agent.getFocus();
		List<Instance> retval = new ArrayList<>();
		for (Instance instance : fc.getInstanceList(ec)) {
			ConceptOverlay co = instance.getConcepts();
			// TextUi.println(PrettyPrint.ppConcise(co, agent));
			List<String> labels = co.getLabels();
			if (labels.isEmpty()) {
				continue;
			}
			for (String label : fullDesiredLabels) {
				if (labels.contains(label)) {
					retval.add(instance);
					break;
				}
			}
		}
		return retval;
	}

	/**
	 * Utility function, used in test / debugging to extract a specific instance
	 * 
	 * @param label
	 * @return
	 */
	public Instance InstanceByLabel(String label) {
		List<Instance> insts = InstancesByLabelInFocus(Arrays.asList(label), EnergyColors.FOCUS_INSTANCE);
		if (insts.size() != 1) {
			throw new Error("FocusAccessByLabel.getInstanceWithLabel - matching instances found " + insts.size());
		}
		return insts.get(0);
	}

	/**
	 * Returns a list of scenes which are in the focus and they have ANY of
	 * these labels
	 * 
	 * @param agent
	 *            the agent
	 * @param desiredLabels
	 * @return
	 */
	public List<Instance> ScenesByLabelInFocus(List<String> desiredLabels, String ec) {
		// make the labels full
		List<String> fullDesiredLabels = new ArrayList<>();
		for (String label : desiredLabels) {
			fullDesiredLabels.add(agent.getLabelSpaces().fullLabel(label));
		}
		Focus fc = agent.getFocus();
		List<Instance> retval = new ArrayList<>();
		for (Instance instance : fc.getSceneList(ec)) {
			ConceptOverlay co = instance.getConcepts();
			TextUi.println(PrettyPrint.ppConcise(co, agent));
			List<String> labels = co.getLabels();
			if (labels.isEmpty()) {
				continue;
			}
			for (String label : fullDesiredLabels) {
				if (labels.contains(label)) {
					retval.add(instance);
					break;
				}
			}
		}
		return retval;
	}

	/**
	 * Utility function, used in test / debugging to extract a specific scene
	 * 
	 * @param label
	 * @return
	 */
	public Instance SceneByLabel(String label) {
		List<Instance> list = ScenesByLabelInFocus(Arrays.asList(label), EnergyColors.FOCUS_INSTANCE);
		if (list.size() != 1) {
			throw new Error("FocusAccessByLabel.getSceneWithLabel - matching scenes found " + list.size());
		}
		return list.get(0);
	}

	/**
	 * Returns a list of VIs which are in the focus and they have ANY of these
	 * labels
	 * 
	 * @param agent
	 *            the agent
	 * @param desiredLabels
	 * @return
	 */
	public List<VerbInstance> VisByLabels(List<String> desiredLabels, String ec) {
		// make the labels full
		List<String> fullDesiredLabels = new ArrayList<>();
		for (String label : desiredLabels) {
			fullDesiredLabels.add(agent.getLabelSpaces().fullLabel(label));
		}
		Focus fc = agent.getFocus();
		List<VerbInstance> retval = new ArrayList<>();
		for (VerbInstance vi : fc.getViList(ec)) {
			VerbOverlay vo = vi.getVerbs();
			List<String> labels = vo.getLabels();
			if (labels.isEmpty()) {
				continue;
			}
			for (String label : fullDesiredLabels) {
				if (labels.contains(label)) {
					retval.add(vi);
					break;
				}
			}
		}
		return retval;
	}
}
