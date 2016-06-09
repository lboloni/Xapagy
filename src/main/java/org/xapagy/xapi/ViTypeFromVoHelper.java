/*
   This file is part of the Xapagy project
   Created on: Feb 17, 2011
 
   org.xapagy.xapi.ViTemplateIdentificationHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * 
 * This code identifies the type of the verb instance based on the verb overlay.
 * 
 * It is based on the presence of some verbs
 * 
 * @author Ladislau Boloni
 * 
 */
public class ViTypeFromVoHelper {

    /**
     * Identifies the verb instance type based on the verb overlay
     * 
     * @param object
     * 
     * @return
     * 
     */
    public static ViType identifyViType(VerbOverlay verbs, Agent agent,
            String object) {
        if (Hardwired.containsAny(agent, verbs, Hardwired.adjectiveVerbs)) {
            return ViType.S_ADJ;
        }
        if (Hardwired.contains(agent, verbs, Hardwired.V_COMMUNICATION)) {
            // this is a little hackish, handles the quote questions
            if (object.contains("what")) {
                return ViType.S_V_O;
            }
            return ViType.QUOTE;
        }
        // everything else defaults to S_V_O
        return ViType.S_V_O;
    }

}
