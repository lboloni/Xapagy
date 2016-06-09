/*
   This file is part of the Xapagy project
   Created on: Apr 3, 2012
 
   org.xapagy.ui.prettygraphviz.pgvFocusInstances
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettygraphviz;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors;

/**
 * @author Ladislau Boloni
 * 
 */
public class pgvFocusInstances {

    /**
     * Generate the focus instances, organized by scenes
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
        pgvFocusInstances.pgvNode(fc, agent, fmt, param);
        pgvFocusInstances.pgvLinks(fc, agent, fmt, param);
        fmt.close(); // the digraph
        return fmt.toString();
    }

    /**
     * Call for all the scenes (which will call them for the instances)
     * 
     * @param fc
     * @param agent
     * @param fmt
     * @param param
     */
    public static void pgvLinks(Focus fc, Agent agent, GvFormatter fmt,
            GvParameters param) {
        for (Instance scene : fc.getSceneList(EnergyColors.FOCUS_INSTANCE)) {
            pgvScene.pgvLinks(scene, agent, fmt, param);
        }
    }

    /**
     * Adds all the scenes, which then add the instances
     * 
     * @param fc
     * @param agent
     * @param fmt
     * @param param
     */
    public static void pgvNode(Focus fc, Agent agent, GvFormatter fmt,
            GvParameters param) {
        for (Instance scene : fc.getSceneList(EnergyColors.FOCUS_INSTANCE)) {
            pgvScene.pgvNode(scene, agent, fmt, param);
        }
    }

}
