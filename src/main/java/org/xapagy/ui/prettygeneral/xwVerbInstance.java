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
package org.xapagy.ui.prettygeneral;

import java.util.Arrays;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PpVerbInstanceTemplate;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.verbalize.VerbalMemoryHelper;

/**
 * 
 * @author lboloni
 * Created on Nov 7, 2016
 */
public class xwVerbInstance {

	/**
	 * Returns a concise, single line version of the verb instance description
	 * 
	 * @param xw
	 *            - the formatter
	 * @param vi
	 *            - the verb instance
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xw, VerbInstance vi, Agent agent) {
		String identifier = vi.getIdentifier();
		if (identifier != null) {
			xw.accumulate(vi.getIdentifier() + " ");
		} else {
			// this is probably a template
			xw.accumulate("template ");
		}
		String viText = VerbalMemoryHelper.getXapiStatementOfVi(vi, agent);
		if (viText != null) {
			xw.accumulate("(" + viText + ")");
		}
		switch (vi.getViType()) {
		case S_V_O: {
			xw.accumulate("S-V-O: ");
			xwConcisePart(xw, vi, ViPart.Subject, agent);
			xw.accumulate(" / ");
			xwConcisePart(xw, vi, ViPart.Verb, agent);
			xw.accumulate(" / ");
			xwConcisePart(xw, vi, ViPart.Object, agent);
			break;
		}
		case S_V: {
			xw.accumulate("S-V: ");
			xwConcisePart(xw, vi, ViPart.Subject, agent);
			xw.accumulate(" / ");
			xwConcisePart(xw, vi, ViPart.Verb, agent);
			break;
		}
		case S_ADJ: {
			xw.accumulate("S-ADJ: ");
			xwConcisePart(xw, vi, ViPart.Subject, agent);
			xw.accumulate(" / ");
			xwConcisePart(xw, vi, ViPart.Verb, agent);
			xw.accumulate(" / ");
			xwConcisePart(xw, vi, ViPart.Adjective, agent);
			break;
		}
		case QUOTE: {
			xw.accumulate("QUOTE: ");
			xwConcisePart(xw, vi, ViPart.Subject, agent);
			xw.accumulate(" / ");
			xwConcisePart(xw, vi, ViPart.Verb, agent);
			xw.accumulate(" in ");
			xwConcisePart(xw, vi, ViPart.QuoteScene, agent);
			xw.accumulate(" // ");
			xwConcise(xw, vi.getQuote(), agent);
			break;
		}
		}
		return xw.toString();
	}

	/**
	 * Concise description of a verb instance part - to be called as part the
	 * 
	 * @param fmt
	 * @param vi
	 * @param part
	 * @param agent
	 * 
	 */
	private static void xwConcisePart(IXwFormatter xw, VerbInstance vi, ViPart part, Agent agent) {
		// check if the part is resolved
		Object thePart = vi.getResolvedParts().get(part);
		if (thePart != null) {
			switch (part) {
			case Subject: {
				Instance instance = (Instance) thePart;
				xwInstance.xwSuperConcise(xw, instance, agent);
				return;
			}
			case Object: {
				Instance instance = (Instance) thePart;
				xwInstance.xwSuperConcise(xw, instance, agent);
				return;
			}
			case Adjective: {
				ConceptOverlay co = (ConceptOverlay) thePart;
				xwConceptOverlay.xwConcise(xw, co, agent);
				return;
			}
			case Verb: {
				VerbOverlay vo = (VerbOverlay) thePart;
				xwVerbOverlay.xwConcise(xw, vo, agent);
				return;
			}
			case QuoteScene: {
				Instance instance = (Instance) thePart;
				xwInstance.xwSuperConcise(xw, instance, agent);
				return;
			}
			case Quote: {
				throw new Error("ppConcisePart called for Quote, it should not be.");
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
				xw.accumulate("<<new: ");
				xwConceptOverlay.xwConcise(xw, co, agent);
				xw.accumulate(">>");
				return;
			}
			default: {
				throw new Error("New part of type:" + part + " not supported!!!");
			}
			}
		}
		// if we are here, it means that it is not resolved
		xw.accumulate("<< missing part >>");
	}

	/**
	 * Returns a detailed version of the VI description which outlines the
	 * composites
	 * 
	 * @param xw
	 * @param vi
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xw, VerbInstance vi, Agent agent) {
		// if there are missing parts or new parts, it is a template, do
		// different printing
		if (!vi.getMissingParts().isEmpty() || !vi.getNewParts().isEmpty()) {
			xw.add(PpVerbInstanceTemplate.ppDetailedViTemplate(vi, agent));
			return xw.toString();
		}
		String head = vi.getIdentifier() + " [" + xwVerbInstance.toStringViTypeStructureConcise(vi, agent) + "]";
		String viText = VerbalMemoryHelper.getXapiStatementOfVi(vi, agent);
		if (viText != null) {
			head += " (\"" + viText + "\")";
		} else {
			head += " (generated internally)";
		}
		head += " mem = " + Formatter.fmt(agent.getAutobiographicalMemory().getSalience(vi, EnergyColors.AM_VI));
		head += " focus = " + Formatter.fmt(agent.getFocus().getSalience(vi, EnergyColors.FOCUS_VI));
		xw.add(head);
		xw.indent();
		xw.add("Verbs: " + xwVerbOverlay.xwConcise(xw.getEmpty(), vi.getVerbs(), agent));
		xw.add(xwVerbOverlay.getLabels(vi.getVerbs()));
		xwStructure(xw, vi, agent);
		xw.add(ppStructuralRelations(vi, agent));
		xw.add(ppNonStructuralRelations(vi, agent));
		return xw.toString();
	}

	/**
	 * Prints the structure
	 * 
	 * @param vi
	 * @param detailStructure
	 * @param agent
	 */
	private static void xwStructure(IXwFormatter xw, VerbInstance vi, Agent agent) {
		if (vi.notCompletelyResolved()) {
			throw new Error("getStructure can only be called on fully resolved VIs, this is an incomplete template");
		}
		// all of them have a subject
		xw.add("Subject: " + xwInstance.xwConcise(xw.getEmpty(), vi.getSubject(), agent));
		switch (vi.getViType()) {
		case S_V_O: {
			xw.add("Object: " + xwInstance.xwConcise(xw.getEmpty(), vi.getObject(), agent));
			break;
		}
		case S_V: {
			break;
		}
		case S_ADJ: {
			xw.add("Adjective: " + xwConceptOverlay.xwConcise(xw.getEmpty(), vi.getAdjective(), agent));
			break;
		}
		case QUOTE: {
			xw.add("Quote scene: " + xwInstance.xwConcise(xw.getEmpty(), vi.getQuoteScene(), agent));			
		    xw.add("Quote: " + xwConcise(xw.getEmpty(), vi.getQuote(), agent));
			break;
		}
		}
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
	public static String toStringViTypeStructureConcise(VerbInstance vi, Agent agent) {
	    switch (vi.getViType()) {
	    case S_V_O:
	        return "S-V-O";
	    case S_V:
	        return "S-V";
	    case S_ADJ:
	        return "S-ADJ";
	    case QUOTE:
	        return "S-V-Sc-Q("
	                + xwVerbInstance.toStringViTypeStructureConcise(vi.getQuote(),
	                        agent) + ")";
	    }
	    throw new Error("this should not happen");
	}
	
}
