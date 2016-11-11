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
 * Created on: Feb 17, 2011
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
