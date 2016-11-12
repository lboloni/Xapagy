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
package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.formatters.Formatter;

/**
 * 
 * To string for verb instance
 * 
 * @author Ladislau Boloni
 *  Created on: Aug 30, 2010
 */
public class PpVerbInstanceTemplate {




    /**
     * Prints the VI interpreted as a template in a concise way. Current version
     * falls back to detailed
     * 
     * 
     * @param verbInstanceTemplate
     * @param agent
     * @return
     */
    public static String ppConciseViTemplate(VerbInstance verbInstanceTemplate,
            Agent agent) {
        return PpVerbInstanceTemplate.ppDetailedViTemplate(verbInstanceTemplate, agent);
    }


    

    /**
     * Prints the Vi interpreted as a template
     * 
     * @param is
     * @param agent
     * @return
     */
    public static String ppDetailedViTemplate(VerbInstance vi, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("ppDetailedViTemplate: improve this !!! ViTemplate: " + vi.getViType() + "(missing: "
                + vi.getMissingParts().size() + ", new: "
                + vi.getNewParts().keySet().size() + ")");
        fmt.indent();
        fmt.add("Verbs:" + PrettyPrint.ppConcise(vi.getVerbs(), agent));
        fmt.add("Resolved instance parts:");
        fmt.indent();
        for (ViPart part : ViStructureHelper.getAllowedInstanceParts(vi
                .getViType())) {            
            fmt.add("" + part + " = " + vi.getResolvedParts().get(part));
        }
        fmt.deindent();
        return fmt.toString();
    }

}
