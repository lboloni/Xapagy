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
package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettygeneral.xwConceptOverlay;
import org.xapagy.ui.prettygeneral.xwInstance;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * Pretty HTML printing functions for the ViTemplate.
 * 
 * ViTemplates are just VIs from which certain components are missing.
 * Nevertheless they need to be pretty printed differently.
 * 
 * One of the problems we are facing here, is that in the current systems, the
 * ViTemplates do not have an identifier, so they might not be easily
 * printable...
 * 
 * @author Lotzi Boloni
 * 
 */
public class PwViTemplate {

    public static String pwConcise(IXwFormatter fmt, VerbInstance vit,
            Agent agent) {
        return PwViTemplate.pwConcise(fmt, vit, agent, false);
    }

    /**
     * Concise printing of a VI template
     * 
     * @param vit
     * @param agent
     * @return
     */
    public static String pwConcise(IXwFormatter fmt, VerbInstance vit,
            Agent agent, boolean prefixWithTemplate) {
        if (prefixWithTemplate) {
            fmt.add("template:" + vit.getViType());
        }
        switch (vit.getViType()) {
        case S_V_O: {
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Subject, agent);
            fmt.add(" / ");
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Verb, agent);
            fmt.add(" / ");
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Object, agent);
            break;
        }
        case S_V: {
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Subject, agent);
            fmt.add(" / ");
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Verb, agent);
            break;
        }
        case S_ADJ: {
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Subject, agent);
            fmt.add(" / ");
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Verb, agent);
            fmt.add(" / ");
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Adjective, agent);
            break;
        }
        case QUOTE: {
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Subject, agent);
            fmt.add(" / ");
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.Verb, agent);
            fmt.add(" in ");
            PwViTemplate.pwPartConcise(fmt, vit, ViPart.QuoteScene, agent);
            fmt.add(" // ");
            fmt.add(PwViTemplate.pwConcise(fmt.getEmpty(), vit.getQuote(),
                    agent, false));
            break;
        }
        }
        return fmt.toString();
    }

    /**
     * A detailed description of the VI template
     * 
     * @param xw
     * @param vit
     * @param agent
     * @param query
     */
    public static String xwDetailed(IXwFormatter xw, VerbInstance vit,
            Agent agent, RESTQuery query) {
        xw.addLabelParagraph("template:" + vit.getViType());
        xw.indent();
        switch (vit.getViType()) {
        case S_V_O: {
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Verb, agent, query);
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Object, agent, query);
            break;
        }
        case S_V: {
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Verb, agent, query);
            break;
        }
        case S_ADJ: {
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Verb, agent, query);
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Adjective, agent,
                    query);
            break;
        }
        case QUOTE: {
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.Verb, agent, query);
            PwViTemplate.pwPartDetailed(xw, vit, ViPart.QuoteScene, agent,
                    query);
            xw.addLabelParagraph("Quote: ");
            xw.add(PwViTemplate.xwDetailed(xw.getEmpty(), vit.getQuote(),
                    agent, query));
            break;
        }
        }
        xw.deindent();
        return xw.toString();
    }

    /**
     * Prints a component
     * 
     * -differently, depending if it is resolved or not etc.
     * 
     * @param xw
     * @param vit
     * @param part
     * @return
     */
    private static String pwPartConcise(IXwFormatter xw, VerbInstance vit,
            ViPart part, Agent agent) {
        // check if it is a resolved part
        XapagyComponent resolvedPart = null;
        if (part == ViPart.Verb) {
            resolvedPart = vit.getVerbs();
        } else {
            resolvedPart = vit.getResolvedParts().get(part);
        }
        if (resolvedPart != null) {
            // us the super concise for instances
            if (resolvedPart instanceof Instance) {
                Instance instance = (Instance) resolvedPart;
                // String retval = SpInstance.spc(instance, agent);
                String retval = xwInstance.xwSuperConcise(xw, instance, agent);
                xw.add(retval);
                return xw.toString();
            }
            String retval = PrettyPrint.ppConcise(resolvedPart, agent);
            xw.add(retval);
            return xw.toString();
        }
        // ok it is not resolved
        ConceptOverlay newPartCo = vit.getNewParts().get(part);
        if (newPartCo != null) {
            String retval =
                    "*" + xwConceptOverlay.xwConcise(xw.getEmpty(), newPartCo, agent) + "*";
            xw.add(retval);
            return xw.toString();
        }
        // ok, it is missing
        if (vit.getMissingParts().contains(part)) {
            String retval = "<? missing>";
            xw.add(retval);
            return xw.toString();
        }
        throw new Error("not resolved, new or missing");
    }

    /**
     * Prints a component for the detailed printing. Differently, depending if
     * it is resolved or not etc. Uses links whenever possible.
     * 
     * @param fmt
     * @param vit
     * @param part
     * @return
     */
    private static String pwPartDetailed(IXwFormatter fmt, VerbInstance vit,
            ViPart part, Agent agent, RESTQuery query) {
        fmt.openP();
        fmt.addBold(part.toString() + ": ");
        // check if it is a resolved part
        XapagyComponent resolvedPart = null;
        if (part == ViPart.Verb) {
            resolvedPart = vit.getVerbs();
        } else {
            resolvedPart = vit.getResolvedParts().get(part);
        }
        if (resolvedPart != null) {
            // us the super concise for instances
            if (resolvedPart instanceof Instance) {
                Instance instance = (Instance) resolvedPart;
                // String retval = SpInstance.spc(instance, agent);
                String retval =
                        PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                                instance);
                fmt.add(retval);
                fmt.closeP();
                return fmt.toString();
            }
            String retval = PrettyPrint.ppConcise(resolvedPart, agent);
            fmt.add(retval);
            fmt.closeP();
            return fmt.toString();
        }
        // ok it is not resolved
        ConceptOverlay newPartCo = vit.getNewParts().get(part);
        if (newPartCo != null) {
            String retval =
                    "*" + xwConceptOverlay.xwConcise(fmt, newPartCo, agent) + "*";
            fmt.add(retval);
            fmt.closeP();
            return fmt.toString();
        }
        // ok, it is missing
        if (vit.getMissingParts().contains(part)) {
            String retval = "<? missing>";
            fmt.add(retval);
            fmt.closeP();
            return fmt.toString();
        }
        throw new Error("not resolved, new or missing");
    }

}
