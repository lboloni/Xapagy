/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2012
 
   org.xapagy.ui.prettygraphviz.pgvScene
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettygraphviz;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettygeneral.xwVerbOverlay;
import org.xapagy.ui.smartprint.SpFocus;

/**
 * @author Ladislau Boloni
 * 
 */
public class pgvScene {

    /**
     * Prints the relations between scenes themselves, and then it is called to
     * do the relations between the members of the scenes
     * 
     * @param scene
     * @param agent
     * @param fmt
     * @param param
     */
    public static void pgvLinks(Instance scene, Agent agent, GvFormatter fmt,
            GvParameters param) {
        pgvScene.pgvLinksBetweenScenes(scene, agent, fmt, param);
        // fall back to printing the relationships between the instances
        List<Instance> members = scene.getSceneMembers();
        if (!members.isEmpty()) {
            for (Instance member : members) {
                pgvInstance.pgvLinks(member, agent, fmt, param);
            }
        }
    }

    /**
     * Prints the links between the given scene and other scenes
     * 
     * Called from pgvScene.pgvLinks and from the pgvFocusVis
     * 
     * @param scene
     * @param agent
     * @param fmt
     * @param param
     */
    public static void pgvLinksBetweenScenes(Instance scene, Agent agent,
            GvFormatter fmt, GvParameters param) {
        // print the links between scenes
        String color = "";
        List<VerbInstance> vis =
                RelationHelper.getRelationsFrom(agent, scene, false);
        for (VerbInstance vi : vis) {
            // create the link in whatever direction
            fmt.openLink(scene.getIdentifier(), vi.getObject().getIdentifier());
            fmt.is("ltail",
                    "cluster_"
                            + GraphVizHelper.formatIdentifier(scene
                                    .getIdentifier()));
            fmt.is("lhead",
                    "cluster_"
                            + GraphVizHelper.formatIdentifier(vi.getObject()
                                    .getIdentifier()));
            fmt.is(GvParameters.FONTNAME, param.fontname);
            fmt.is(GvParameters.FONTSIZE, "" + param.fontsize);
            // the problem here, is that due to the way the
            String label = xwVerbOverlay.ppRelationLabel(vi.getVerbs(), agent);
            // fmt.label(label);
            switch (label) {
            case "scene_fictional_future":
                color = param.relations_color_sceneFictionalFuture;
                break;
            case "scene_view":
                color = param.relations_color_sceneView;
                break;
            case "scene_succession":
                color = param.relations_color_sceneSuccession;
                break;
            default:
                //TextUi.abort("Don't understand scene-to-scene label: " + label);
                TextUi.println("Scene-to-scene label weird: " + label);
                color = param.relations_color_nonIdentity;
            }
            fmt.is(GvParameters.COLOR, color);
            fmt.close();
        }
    }

    /**
     * Adding a scene (normally, a cluster)
     * 
     * @param scene
     * @param agent
     * @param fmt
     * @param param
     */
    public static void pgvNode(Instance scene, Agent agent, GvFormatter fmt,
            GvParameters param) {
        String sceneLabel = SpFocus.ppsScene(scene, agent, false);
        fmt.openSubGraph(scene.getIdentifier());
        fmt.is(GvParameters.STYLE, param.scene_style);
        fmt.is(GvParameters.COLOR, param.scene_color);
        fmt.is(GvParameters.FONTNAME, param.scene_fontname);
        fmt.is(GvParameters.FONTSIZE, "" + param.scene_fontsize);
        fmt.label("Scene: " + sceneLabel);
        List<Instance> members = scene.getSceneMembers();
        if (!members.isEmpty()) {
            for (Instance member : members) {
                pgvInstance.pgvNode(member, agent, fmt, param);
            }
        }
        // add the scene itself
        fmt.openNode(scene.getIdentifier());
        fmt.is(GvParameters.STYLE, "invis");
        fmt.close();
        // close the cluster
        fmt.close();
    }

}
