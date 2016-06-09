/*
   This file is part of the Xapagy project
   Created on: Dec 26, 2011
 
   org.xapagy.ui.prettyprint.PpConceptOverlay
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpConceptOverlay {

    /**
     * Fallback on the generic Overlay printing
     * 
     * @param co
     * @param agent
     * @return
     */
    public static String ppConcise(ConceptOverlay co, Agent agent) {
        return PpOverlay.ppConcise(co, agent);
    }

    /**
     * Fallback on the generic Overlay printing
     * 
     * @param co
     * @param agent
     * @return
     */
    public static String ppDetailed(ConceptOverlay co, Agent agent) {
        return PpOverlay.ppDetailed(co, agent);
    }

}
