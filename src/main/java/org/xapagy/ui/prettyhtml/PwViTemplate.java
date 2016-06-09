package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PpConceptOverlay;
import org.xapagy.ui.prettyprint.PpInstance;
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
     * @param fmt
     * @param vit
     * @param agent
     * @param query
     */
    public static String xwDetailed(IXwFormatter fmt, VerbInstance vit,
            Agent agent, RESTQuery query) {
        fmt.addLabelParagraph("template:" + vit.getViType());
        fmt.indent();
        switch (vit.getViType()) {
        case S_V_O: {
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Verb, agent, query);
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Object, agent, query);
            break;
        }
        case S_V: {
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Verb, agent, query);
            break;
        }
        case S_ADJ: {
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Verb, agent, query);
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Adjective, agent,
                    query);
            break;
        }
        case QUOTE: {
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Subject, agent, query);
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.Verb, agent, query);
            PwViTemplate.pwPartDetailed(fmt, vit, ViPart.QuoteScene, agent,
                    query);
            fmt.addLabelParagraph("Quote: ");
            fmt.add(PwViTemplate.xwDetailed(fmt.getEmpty(), vit.getQuote(),
                    agent, query));
            break;
        }
        }
        fmt.deindent();
        return fmt.toString();
    }

    /**
     * Prints a component
     * 
     * -differently, depending if it is resolved or not etc.
     * 
     * @param fmt
     * @param vit
     * @param part
     * @return
     */
    private static String pwPartConcise(IXwFormatter fmt, VerbInstance vit,
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
                String retval = PpInstance.ppSuperConcise(instance, agent);
                fmt.add(retval);
                return fmt.toString();
            }
            String retval = PrettyPrint.ppConcise(resolvedPart, agent);
            fmt.add(retval);
            return fmt.toString();
        }
        // ok it is not resolved
        ConceptOverlay newPartCo = vit.getNewParts().get(part);
        if (newPartCo != null) {
            String retval =
                    "*" + PpConceptOverlay.ppConcise(newPartCo, agent) + "*";
            fmt.add(retval);
            return fmt.toString();
        }
        // ok, it is missing
        if (vit.getMissingParts().contains(part)) {
            String retval = "<? missing>";
            fmt.add(retval);
            return fmt.toString();
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
                    "*" + PpConceptOverlay.ppConcise(newPartCo, agent) + "*";
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
