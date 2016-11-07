/*
   This file is part of the Xapagy project
   Created on: Aug 30, 2010
 
   org.xapagy.ui.format.tostring.tostringXViTemplate
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.prettygeneral.xwVerbInstance;

/**
 * 
 * To string for verb instance
 * 
 * @author Ladislau Boloni
 * 
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
