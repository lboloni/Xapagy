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

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;

/**
 * A collection of functions which classify instances and sometimes the COs
 * 
 * @author Ladislau Boloni
 * Created on: Jun 14, 2012
 */
public class InstanceClassifier {

    /**
     * Decides if the passed Co is a scene
     * 
     * @param vi
     * @param agent
     * @return
     */
    public static boolean decideSceneCo(ConceptOverlay co, Agent agent) {
        return Hardwired.contains(agent, co, Hardwired.C_SCENE);
    }

}
