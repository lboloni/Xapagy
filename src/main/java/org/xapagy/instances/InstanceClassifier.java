/*
   This file is part of the Xapagy project
   Created on: Jun 14, 2012
 
   org.xapagy.instances.InstanceClassifier
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.instances;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;

/**
 * A collection of functions which classify instances and sometimes the COs
 * 
 * @author Ladislau Boloni
 * 
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
