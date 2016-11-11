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

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors;

/**
 * @author Ladislau Boloni
 * Created on: Apr 3, 2012
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
