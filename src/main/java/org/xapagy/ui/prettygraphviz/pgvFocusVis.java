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
package org.xapagy.ui.prettygraphviz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.smartprint.SpFocus;

/**
 * @author Ladislau Boloni
 * Created on: Apr 5, 2012
 */
public class pgvFocusVis {
    /**
     * Generate a graphviz picture with the focus VIs
     * 
     * @return
     */
    public static String generate(Agent agent) {
        GvFormatter fmt = new GvFormatter();
        GvParameters param = new GvParameters();
        fmt.openDiGraph();
        fmt.is("rankdir", "LR");
        fmt.is(GvParameters.SPLINES, param.splines);
        fmt.is("compound", true);
        Focus fc = agent.getFocus();
        Set<VerbInstance> vis = new HashSet<>();
        vis.addAll(fc.getViList(EnergyColors.FOCUS_VI));
        GvFormatter fmtTmp = new GvFormatter();
        // first pass, generate the list of instances to do
        pgvFocusVis.pgvLinks(fc, agent, fmtTmp, param, vis);
        // now organize them by scene of the subject
        Map<Instance, Set<VerbInstance>> scenes = new HashMap<>();
        for (VerbInstance vi : vis) {
            Instance scene = vi.getSubject().getScene();
            Set<VerbInstance> vilist = scenes.get(scene);
            if (vilist == null) {
                vilist = new HashSet<>();
                scenes.put(scene, vilist);
            }
            vilist.add(vi);
            // TextUi.println("printing " + vi.getIdentifier() + " in scene " +
            // scene.getIdentifier());
        }
        //
        for (Instance scene : scenes.keySet()) {
            String sceneLabel = SpFocus.ppsScene(scene, agent, false);
            fmt.openSubGraph(scene.getIdentifier());
            fmt.is(GvParameters.STYLE, param.scene_style);
            fmt.is(GvParameters.COLOR, param.scene_color);
            fmt.is(GvParameters.FONTNAME, param.scene_fontname);
            fmt.is(GvParameters.FONTSIZE, "" + param.scene_fontsize);
            fmt.label("Scene: " + sceneLabel);
            // the scene node itself
            fmt.openNode(scene.getIdentifier());
            fmt.is(GvParameters.STYLE, "invis");
            fmt.close(); // the scene node
            // now add the VIs
            Set<VerbInstance> visInScene = scenes.get(scene);
            pgvFocusVis.pgvNode(fc, agent, fmt, param, visInScene);
            fmt.close(); // the subgraph
            pgvScene.pgvLinksBetweenScenes(scene, agent, fmt, param);
        }
        pgvFocusVis.pgvCoincidences(fc, agent, fmt, param, vis);
        pgvFocusVis.pgvLinks(fc, agent, fmt, param, vis);
        // pgvFocusVis.pgvNode(fc, agent, fmt, param, vis);
        fmt.close(); // the digraph
        return fmt.toString();
    }

    /**
     * Printing of the coincidences. Instead of creating a full mesh link
     * between the coincidence VIs (which is very heavy handed, this function
     * creates a central node (a round item), to which it will link all the
     * coincidence VIs with specific links. We will make these links very
     * strong, in order to bring them close to each other.
     * 
     * @param fc
     * @param agent
     * @param fmt
     * @param param
     * @param vis
     */
    private static void pgvCoincidences(Focus fc, Agent agent, GvFormatter fmt,
            GvParameters param, Set<VerbInstance> vis) {
        Map<String, Set<VerbInstance>> nodes = new HashMap<>();
        // coincidences only work for action VIs
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (!ViClassifier.decideViClass(ViClass.ACTION, vi, agent)) {
                continue;
            }
            boolean newNode = true;
            // check if the vi is already in one of the nodes
            for (String node : nodes.keySet()) {
                Set<VerbInstance> participants = nodes.get(node);
                if (participants.contains(vi)) {
                    newNode = false;
                    break;
                }
            }
            if (!newNode) {
                continue;
            }
            // ok, we need to create a new node
            String nodeName = "coincidence_" + vi.getIdentifier();
            Set<VerbInstance> set = new HashSet<>();
            set.add(vi);
            ViSet visCoincidence = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_COINCIDENCE);
            set.addAll(visCoincidence.getParticipants());
            nodes.put(nodeName, set);
            vis.addAll(set);
        }
        // ok, at this moment we have the nodes and their members
        for (String name : nodes.keySet()) {
            Set<VerbInstance> links = nodes.get(name);
            if (links.size() == 1) {
                continue;
            }
            fmt.openNode(name);
            fmt.add("shape=point,");
            fmt.add("height=0.1,");
            fmt.add("width=0.1,");
            fmt.close();
            for (VerbInstance vi : links) {
                fmt.openLink(name, vi.getIdentifier());
                fmt.add("weight=10000,");
                fmt.close();
                // prevent to put them before...
                fmt.openLink(vi.getIdentifier(), name);
                fmt.add("weight=10000,");
                fmt.add("style=invis,");
                fmt.close();
            }
        }
    }

    /**
     * Call for all the scenes (which will call them for the instances)
     * 
     * @param fc
     * @param agent
     * @param fmt
     * @param param
     * @param vis
     *            - collects the vi's which had been referred
     */
    private static void pgvLinks(Focus fc, Agent agent, GvFormatter fmt,
            GvParameters param, Set<VerbInstance> vis) {
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            Set<VerbInstance> referred =
                    pgvVerbInstance.pgvLinks(vi, agent, fmt, param);
            vis.addAll(referred);
        }
    }

    /**
     * Prints all the verbs which are referred. This requires all the focus VIs,
     * but also VIs which are not in the focus but are referred
     * 
     * @param fc
     * @param agent
     * @param fmt
     * @param param
     * @param vis
     */
    private static void pgvNode(Focus fc, Agent agent, GvFormatter fmt,
            GvParameters param, Set<VerbInstance> vis) {
        Set<VerbInstance> viToDraw = new HashSet<>();
        viToDraw.addAll(vis);
        for (VerbInstance vi : viToDraw) {
            pgvVerbInstance.pgvNode(vi, agent, fmt, param);
        }
    }

}
