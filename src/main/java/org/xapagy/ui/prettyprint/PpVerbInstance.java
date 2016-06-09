/*
   This file is part of the Xapagy project
   Created on: Aug 30, 2010
 
   org.xapagy.ui.format.tostring.tostringXViTemplate
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.Arrays;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;
import org.xapagy.verbalize.VerbalMemoryHelper;

/**
 * 
 * To string for verb instance
 * 
 * @author Ladislau Boloni
 * 
 */
public class PpVerbInstance {

    /**
     * Prints the structure
     * 
     * @param vi
     * @param detailStructure
     * @param agent
     * @return
     */
    private static String getStructure(VerbInstance vi,
            PrintDetail detailStructure, Agent agent) {
        if (vi.notCompletelyResolved()) {
            throw new Error(
                    "getStructure can only be called on fully resolved VIs, this is an incomplete template");
        }
        Formatter fmt = new Formatter();
        switch (vi.getViType()) {
        case S_V_O: {
            fmt.add("Subject: "
                    + PrettyPrint.pp(vi.getSubject(), agent, detailStructure));
            fmt.add("Object: "
                    + PrettyPrint.pp(vi.getObject(), agent, detailStructure));
            break;
        }
        case S_V: {
            fmt.add("Subject: "
                    + PrettyPrint.pp(vi.getSubject(), agent, detailStructure));
            break;
        }
        case S_ADJ: {
            fmt.add("Subject: "
                    + PrettyPrint.pp(vi.getSubject(), agent, detailStructure));
            fmt.add("Concept: "
                    + PrettyPrint.pp(vi.getAdjective(), agent, detailStructure));
            break;
        }
        case QUOTE: {
            fmt.add("Subject: "
                    + PrettyPrint.pp(vi.getSubject(), agent, detailStructure));
            fmt.add("Quote scene: "
                    + PrettyPrint.pp(vi.getQuoteScene(), agent, detailStructure));
            fmt.add("Quote: "
                    + PrettyPrint.pp(vi.getQuote(), agent, detailStructure));
            break;
        }
        }
        return fmt.toString();
    }

    /**
     * Concise verb representation: single line - should work for templates and
     * full vi's as well!!!
     * 
     * @param vi
     * @param agent
     * @return
     */
    public static String ppConcise(VerbInstance vi, Agent agent) {
        StringBuffer buf = new StringBuffer();
        String identifier = vi.getIdentifier();
        if (identifier != null) {
            buf.append(vi.getIdentifier() + " ");
        } else {
            // this is probably a template
            buf.append("template ");
        }
        String viText = VerbalMemoryHelper.getXapiStatementOfVi(vi, agent);
        if (viText != null) {
            buf.append("(" + viText + ")");
        }
        switch (vi.getViType()) {
        case S_V_O: {
            buf.append("S-V-O: ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Subject, agent));
            buf.append(" / ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Verb, agent));
            buf.append(" / ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Object, agent));
            break;
        }
        case S_V: {
            buf.append("S-V: ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Subject, agent));
            buf.append(" / ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Verb, agent));
            break;
        }
        case S_ADJ: {
            buf.append("S-ADJ: ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Subject, agent));
            buf.append(" / ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Verb, agent));
            buf.append(" / ");
            buf.append(PpVerbInstance
                    .ppConcisePart(vi, ViPart.Adjective, agent));
            break;
        }
        case QUOTE: {
            buf.append("QUOTE: ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Subject, agent));
            buf.append(" / ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.Verb, agent));
            buf.append(" in ");
            buf.append(PpVerbInstance.ppConcisePart(vi, ViPart.QuoteScene,
                    agent));
            buf.append(" // ");
            buf.append(PpVerbInstance.ppConcise(vi.getQuote(), agent));
            break;
        }
        }
        return buf.toString();
    }

    /**
     * Concise printing of a subject - it prints it correctly whether it is
     * resolved or not
     * 
     * @return
     */
    private static String ppConcisePart(VerbInstance vi, ViPart part,
            Agent agent) {
        // check if the part is resolved
        Object thePart = vi.getResolvedParts().get(part);
        if (thePart != null) {
            switch (part) {
            case Subject: {
                Instance instance = (Instance) thePart;
                return PpInstance.ppSuperConcise(instance, agent);
            }
            case Object: {
                Instance instance = (Instance) thePart;
                return PpInstance.ppSuperConcise(instance, agent);
            }
            case Adjective: {
                ConceptOverlay co = (ConceptOverlay) thePart;
                return PrettyPrint.ppConcise(co, agent);
            }
            case Verb: {
                VerbOverlay vo = (VerbOverlay) thePart;
                return PpVerbOverlay.ppConcise(vo, agent);
            }
            case QuoteScene: {
                Instance instance = (Instance) thePart;
                return PpInstance.ppSuperConcise(instance, agent);
            }
            case Quote: {
                throw new Error(
                        "ppConcisePart called for Quote, it should not be.");
            }
            }
        }
        // check if it a new part
        thePart = vi.getNewParts().get(part);
        if (thePart != null) {
            switch (part) {
            case Subject:
            case Object:
            case QuoteScene: {
                ConceptOverlay co = (ConceptOverlay) thePart;
                return "<<new: " + PrettyPrint.ppConcise(co, agent) + ">>";
            }
            default: {
                throw new Error("New part of type:" + part
                        + " not supported!!!");
            }
            }
        }

        // if we are here, it means that it is not resolved
        return "<<missing>>";
    }

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
        return PpVerbInstance.ppDetailedViTemplate(verbInstanceTemplate, agent);
    }

    /**
     * Detailed printing of the VI
     * 
     * @param vi
     * @param agent
     * @return
     */
    public static String ppDetailed(VerbInstance vi, Agent agent) {
        TextUi.println("PpVerbInstance called!!!");
        // if there are missing parts or new parts, it is a template, do
        // different printing
        if (!vi.getMissingParts().isEmpty() || !vi.getNewParts().isEmpty()) {
            return PpVerbInstance.ppDetailedViTemplate(vi, agent);
        }
        Formatter fmt = new Formatter();
        String head =
                vi.getIdentifier() + " ["
                        + PpVerbInstance.ppViTypeStructureConcise(vi, agent)
                        + "]";
        String viText = VerbalMemoryHelper.getXapiStatementOfVi(vi, agent);
        if (viText != null) {
            head += " (\"" + viText + "\")";
        } else {
            head += " (generated internally)";
        }
        head +=
                " mem = "
                        + Formatter.fmt(agent.getAutobiographicalMemory()
                                .getSalience(vi, EnergyColors.AM_VI));
        head +=
                " focus = "
                        + Formatter.fmt(agent.getFocus().getSalience(vi,
                                EnergyColors.FOCUS_VI));
        fmt.add(head);
        fmt.indent();
        fmt.add("Verbs: " + PpVerbOverlay.ppConcise(vi.getVerbs(), agent));
        PrintDetail detailStructure = PrintDetail.DTL_CONCISE;
        fmt.add(PpVerbOverlay.getLabels(vi.getVerbs()));
        fmt.add(PpVerbInstance.getStructure(vi, detailStructure, agent));
        fmt.add(PpVerbInstance.ppStructuralRelations(vi, agent));
        fmt.add(PpVerbInstance.ppNonStructuralRelations(vi, agent));
        return fmt.toString();
        //
        //  Fixme: doing it this way not only it is ugly, but it creates some 
        //  kind of recursive call in the webpage
        //        
        //TwFormatter fmt = new TwFormatter();
        //PwVerbInstance.xwDetailed(fmt, vi, agent, new GsQuery(), null);
        //return fmt.toString();
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

    /**
     * 
     * Prints all the relations to / from which are _not _
     * 
     * @param vi
     * @param agent
     * @return
     */
    private static String
            ppNonStructuralRelations(VerbInstance vi, Agent agent) {
        List<String> linkNames =
                Arrays.asList(Hardwired.LINK_ANSWER, Hardwired.LINK_QUESTION,
                        Hardwired.LINK_IR_CONTEXT, Hardwired.LINK_IR_CONTEXT_IMPLICATION);
        Formatter fmt = new Formatter();
        for (String linkName : linkNames) {
            ViSet vis = agent.getLinks().getLinksByLinkName(vi, linkName);
            if (!vis.isEmpty()) {
                String linkString = PrettyPrint.ppConcise(vis, agent);
                fmt.add(linkName + ": " + linkString);
            }
        }
        return fmt.toString();
    }

    /**
     * Prints the structural relations of a VI (pred / succ / summary /
     * summed-by)
     * 
     * @param vi
     * @param agent
     */
    private static String ppStructuralRelations(VerbInstance vi, Agent agent) {
        Formatter fmt = new Formatter();
        ViSet visPred = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_PREDECESSOR);
        if (!visPred.isEmpty()) {
            String pred = PrettyPrint.ppConcise(visPred, agent);
            fmt.add("Predecessors: " + pred);
        }
        ViSet visSucc = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_SUCCESSOR); 
        if (!visSucc.isEmpty()) {
            String succ = PrettyPrint.ppConcise(visSucc, agent);
            fmt.add("Successors: " + succ);
        }
        ViSet visCoincidence = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_COINCIDENCE); 
        if (!visCoincidence.isEmpty()) {
            String coincidenceGroup =
                    PrettyPrint.ppConcise(visCoincidence, agent);
            fmt.add("Coincidence with: " + coincidenceGroup);
        }
        return fmt.toString();
    }

    /**
     * Printing the VI structure
     * 
     * @param vi
     * @param agent
     * @return
     */
    public static String ppViTypeStructureConcise(VerbInstance vi, Agent agent) {
        switch (vi.getViType()) {
        case S_V_O:
            return "S-V-O";
        case S_V:
            return "S-V";
        case S_ADJ:
            return "S-ADJ";
        case QUOTE:
            return "S-V-Sc-Q("
                    + PpVerbInstance.ppViTypeStructureConcise(vi.getQuote(),
                            agent) + ")";
        }
        throw new Error("this should not happen");
    }

}
