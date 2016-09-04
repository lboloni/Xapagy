/*
   This file is part of the Xapagy project
   Created on: Feb 16, 2016
 
   org.xapagy.agents.ReferenceAPI
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A common place for making references in various ways into the Xapagy agent.
 * It should also be accessible from Javascript
 * 
 * @author Ladislau Boloni
 *
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

    public String testString(String a, String b) {
        return "ReferenceAPI is working from " + a + " --> " + b;
    }

    /**
     * Returns the VI by the passed reference
     * 
     * @return
     */
    public VerbInstance ViByLastCreated() {
        throw new Error("ReferenceAPI.ViByLastCreated not implemented yet");
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
     * Returns the VI accessed by label
     * 
     * @return
     */
    public VerbInstance ViByLabel() {
        throw new Error("ReferenceAPI.ViByLabel not implemented yet");
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
     * Returns an instance as if it is accessing a value
     * 
     * @param reference
     * @return
     */
    public Instance InstanceByRef(String reference) {
        throw new Error("ReferenceAPI.InstanceByRef not implemented yet");
    }

    /**
     * Utility function, used in test / debugging to extract a specific VI
     * 
     * @param label
     * @return
     */
    public VerbInstance ViByLabelFromFocus(String label) {
        List<VerbInstance> vis = VisByLabels(Arrays.asList(label),EnergyColors.FOCUS_VI);
        if (vis.size() != 1) {
            throw new Error(
                    "Focus.getViWithLabel - matching VIs found " + vis.size());
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
    public List<Instance> InstancesByLabelInFocus(List<String> desiredLabels,
            String ec) {
        // make the labels full
        List<String> fullDesiredLabels = new ArrayList<>();
        for (String label : desiredLabels) {
            fullDesiredLabels.add(agent.getLabelSpaces().fullLabel(label));
        }
        Focus fc = agent.getFocus();
        List<Instance> retval = new ArrayList<>();
        for (Instance instance : fc.getInstanceList(ec)) {
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
     * Utility function, used in test / debugging to extract a specific instance
     * 
     * @param label
     * @return
     */
    public Instance InstanceByLabel(String label) {
        List<Instance> insts = InstancesByLabelInFocus(Arrays.asList(label),
                EnergyColors.FOCUS_INSTANCE);
        if (insts.size() != 1) {
            throw new Error(
                    "FocusAccessByLabel.getInstanceWithLabel - matching instances found "
                            + insts.size());
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
        List<Instance> list = ScenesByLabelInFocus(Arrays.asList(label),EnergyColors.FOCUS_INSTANCE);
        if (list.size() != 1) {
            throw new Error(
                    "FocusAccessByLabel.getSceneWithLabel - matching scenes found "
                            + list.size());
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
