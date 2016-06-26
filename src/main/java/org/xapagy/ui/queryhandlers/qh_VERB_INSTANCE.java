package org.xapagy.ui.queryhandlers;

import java.util.HashSet;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwQuantumEnergyList;
import org.xapagy.ui.prettyhtml.ColorCodeRepository;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.smartprint.XapiPrint;

public class qh_VERB_INSTANCE implements IQueryHandler, IQueryAttributes {

    /**
     * Adds an instance type component into the printout
     * 
     * @param fmt
     * @param vi
     * @param agent
     * @param query
     */
    private static void addInstanceComponent(String label, IXwFormatter fmt,
            Instance instance, Agent agent, RESTQuery query) {
        fmt.openP();
        fmt.addBold(label);
        PwQueryLinks.linkToInstance(fmt, agent, query, instance);
        fmt.closeP();
    }

    /**
     * Prints the structure - assuming the this is a fully resolved VI
     * 
     * @param vi
     * @param detailStructure
     * @param agent
     */
    private static void addStructure(IXwFormatter fmt, VerbInstance vi,
            Agent agent, RESTQuery query) {
        fmt.addH3("Parts");
        if (vi.notCompletelyResolved()) {
            throw new Error(
                    "getStructure can only be called on fully resolved VIs, this is an incomplete template");
        }
        switch (vi.getViType()) {
        case S_V_O: {
            qh_VERB_INSTANCE.addInstanceComponent("Subject: ", fmt,
                    vi.getSubject(), agent, query);
            qh_VERB_INSTANCE.addVerb(fmt, vi.getVerbs(), agent, query);
            qh_VERB_INSTANCE.addInstanceComponent("Object: ", fmt,
                    vi.getObject(), agent, query);
            break;
        }
        case S_V: {
            qh_VERB_INSTANCE.addInstanceComponent("Subject: ", fmt,
                    vi.getSubject(), agent, query);
            qh_VERB_INSTANCE.addVerb(fmt, vi.getVerbs(), agent, query);
            break;
        }
        case S_ADJ: {
            qh_VERB_INSTANCE.addInstanceComponent("Subject: ", fmt,
                    vi.getSubject(), agent, query);
            qh_VERB_INSTANCE.addVerb(fmt, vi.getVerbs(), agent, query);
            fmt.openP();
            fmt.addBold("Adjective: ");
            qh_CONCEPT_OVERLAY.generate(fmt, agent, vi.getAdjective(), query,
                    false);
            // fmt.addPre(PrettyPrint.ppDetailed(vi.getAdjective(), agent));
            fmt.closeP();
            break;
        }
        case QUOTE: {
            qh_VERB_INSTANCE.addInstanceComponent("Subject: ", fmt,
                    vi.getSubject(), agent, query);
            qh_VERB_INSTANCE.addVerb(fmt, vi.getVerbs(), agent, query);
            qh_VERB_INSTANCE.addInstanceComponent("Quote scene: ", fmt,
                    vi.getQuoteScene(), agent, query);
            fmt.openP();
            fmt.addBold("Quote: ");
            PwQueryLinks.linkToVi(fmt, agent, query, vi.getQuote());
            fmt.closeP();
            break;
        }
        }
    }

    /**
     * Adds the VERB component of the VI into the printout
     * 
     * @param fmt
     * @param vi
     * @param agent
     * @param query
     */
    private static void addVerb(IXwFormatter fmt, VerbOverlay vo, Agent agent,
            RESTQuery query) {
        fmt.openP();
        fmt.addBold("Verb: ");
        qh_VERB_OVERLAY.xwCompact(fmt, agent, vo, query);
        fmt.closeP();
    }

    /**
     * Detailed printing of a VerbInstance
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    public static void xwDetailed(IXwFormatter fmt, VerbInstance vi,
            Agent agent, RESTQuery query, ColorCodeRepository ccr) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        Focus fc = agent.getFocus();
        int countHideable = 1;
        fmt.is("Identifier", vi.getIdentifier());
        qh_VERB_OVERLAY.addOverlayLabels(fmt, agent, vi.getVerbs(), query);
        // classification
        String classification = "";
        for (ViClass viclass : ViClass.values()) {
            if (ViClassifier.decideViClass(viclass, vi, agent)) {
                classification += viclass.toString() + " ";
            }
        }
        fmt.addLabelParagraph("Classified as: ", classification);
        fmt.is("Expectedness", vi.getExpectedness());
        qh_VERB_INSTANCE.addStructure(fmt, vi, agent, query);
        //
        // Focus energies and saliences. Also determines if it is in the focus.
        //
        boolean isInFocus = false;
        fmt.addLabelParagraph("Focus:");
        fmt.indent();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
            double salience = fc.getSalience(vi, ec);
            double energy = fc.getEnergy(vi, ec);
            if (energy > 0.0) {
                isInFocus = true;
            }
            fmt.openP();
            fmt.progressBarSlash(salience, energy);
            fmt.add(ec.toString());
            fmt.closeP();
        }
        fmt.deindent();
        //
        // Memory energies and saliences
        //
        fmt.addLabelParagraph("Memory:");
        fmt.indent();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_VI)) {
            double salience = am.getSalience(vi, ec);
            double energy = am.getEnergy(vi, ec);
            fmt.openP();
            fmt.progressBarSlash(salience, energy);
            fmt.add(ec.toString());
            fmt.closeP();
        }
        fmt.deindent();
        //
        // Links
        //
        String links = qh_VERB_INSTANCE.pwLinks(vi, agent, query);
        fmt.addExtensibleH2("id" + countHideable++, "Links", links, true);
        //
        // Shadows
        //
        if (isInFocus) {
            String shadow =
                    qh_VERB_INSTANCE.pwViShadow(vi, agent, query, 10, ccr, true);
            fmt.addExtensibleH2("id" + countHideable++, "Shadows", shadow, true);
        }
        //
        // Energy quantums
        //
        boolean debugRecordQuantums =
                agent.getParameters().getBoolean("A_DEBUG",
                        "G_GENERAL",
                        "N_RECORD_FOCUS_MEMORY_QUANTUMS");
        if (debugRecordQuantums) {
            IXwFormatter fmt2 = fmt.getEmpty();
            xwQuantumEnergyList.pwAllEnergyQuantums(fmt2, vi, agent, query);
            fmt.addExtensibleH2("id" + countHideable++, "Energy quantums",
                    fmt2.toString(), true);
        } else {
            fmt.addH2("Energy quantums not available as recording is not enabled.");
        }
    }

    /**
     * HTML printing of a verb instance
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        String identifier = query.getAttribute(Q_ID);
        Focus fc = agent.getFocus();
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        VerbInstance vi = am.getVerbInstance(identifier);
        if (vi == null) {
            // fmt.addErrorMessage("Could not find the VI with identifier "
            // + identifier + " in the autobiographical memory");
            // try for the focus
            for (VerbInstance vitest : fc.getViListAllEnergies()) {
                if (vitest.getIdentifier().equals(identifier)) {
                    vi = vitest;
                    break;
                }
            }
            // if it is not in the focus either, we give up
            if (vi == null) {
                fmt.addErrorMessage("Could not find the VI with identifier "
                        + identifier + " in the AM or focus.");
                return;
            }
            // otherwise, go ahead with the visualization
        }
        String concise = XapiPrint.ppsViXapiForm(vi, agent);
        //
        // Red header
        //
        String redheader = "VerbInstance " + concise;
        fmt.addH2(redheader, "class=identifier");
        //
        // Identifier, verbs, creation source, structure
        //

        xwDetailed(fmt, vi, agent, query, session.colorCodeRepository);
    }

    /**
     * Add the links from the Vi
     * 
     * @param vi
     * @param agent
     * @return
     */
    private static String pwLinks(VerbInstance vi, Agent agent, RESTQuery query) {
        PwFormatter fmt = new PwFormatter();
        addLinks(fmt, agent, query, vi, Hardwired.LINK_PREDECESSOR, "Predecessors");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_SUCCESSOR, "Successors");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_COINCIDENCE, "Coincidences");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_IR_CONTEXT, "IR Context");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_IR_CONTEXT_IMPLICATION, "IR Context Implications");

        addLinks(fmt, agent, query, vi, Hardwired.LINK_SUMMARIZATION_BEGIN, "Summarization Begin");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_SUMMARIZATION_BODY, "Summarization Body");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_SUMMARIZATION_CLOSE, "Summarization Close");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_ELABORATION_BEGIN, "Elaboration Begin");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_ELABORATION_BODY, "Elaboration Body");
        addLinks(fmt, agent, query, vi, Hardwired.LINK_ELABORATION_CLOSE, "Elaboration Close");
        return fmt.toString();
    }

    
    /**
     * Adds a section of a certain type of links
     * @param fmt
     * @param agent
     * @param vi
     * @param linkType
     * @param headerlabel
     */
    private static void addLinks(IXwFormatter fmt, Agent agent, RESTQuery query,
            VerbInstance vi, String linkType, String headerlabel) {
        ViSet vis = agent.getLinks().getLinksByLinkName(vi, linkType);
        if (!vis.isEmpty()) {
            fmt.addH2(headerlabel);
            for (SimpleEntry<VerbInstance, Double> entry : vis
                    .getDecreasingStrengthList()) {
                VerbInstance viTo = entry.getKey();
                fmt.openP();
                fmt.progressBar(entry.getValue(), 1.0);
                PwQueryLinks.linkToVi(fmt, agent, query, viTo);
                PwQueryLinks.linkToLink(fmt, "[details]", agent, vi, viTo, linkType);
                fmt.closeP();
            }
        }

    }

    /**
     * Creates a list of links to the shadows of a verb instance
     * 
     * @param fvi
     * @param agent
     * @param query
     * @param maxPrint
     * @param wgpg
     *            - contains a link to the story line colors
     * @param documentEC
     *            if true, document the list of shadow energy colors - this is
     *            not necessary when we call this from the main shadow page,
     *            where it is documented at the top of the page
     * 
     * @return
     */
    public static String pwViShadow(VerbInstance fvi, Agent agent,
            RESTQuery query, int maxPrint, ColorCodeRepository ccr,
            boolean documentEC) {
        PwFormatter fmt = new PwFormatter();
        Shadows sf = agent.getShadows();
        // document the order of the shadow energies
        if (documentEC) {
            String shadowEnergies =
                    "The shadow energies are listed in the order: ";
            for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
                shadowEnergies += " " + ec;
            }
            fmt.explanatoryNote(shadowEnergies);
        }
        //
        // now list the shadows. Don't list them again if they had already
        // been listed, but keep the in the order of the first energy etc.
        //
        Set<VerbInstance> alreadyPrinted = new HashSet<>();
        for (String ecSort : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            int count = 0;
            for (VerbInstance svi : sf.getMembers(fvi, ecSort)) {
                if (alreadyPrinted.contains(svi)) {
                    continue;
                }
                alreadyPrinted.add(svi);
                count++;
                fmt.openP();
                for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
                    double valueEnergy = sf.getEnergy(fvi, svi, ec);
                    double valueSalience = sf.getSalience(fvi, svi, ec);
                    fmt.progressBarSlash(valueSalience, valueEnergy);
                }
                // fmt.add(tmp);
                PwQueryLinks.linkToStoryLineColor(fmt, agent, svi, ccr);
                PwQueryLinks.linkToVi(fmt, agent, query, svi);
                PwQueryLinks.linkToViShadowExplanation(fmt, agent, query, fvi, svi);
                fmt.closeP();
                if (count > maxPrint) {
                    fmt.addLabelParagraph("...and "
                            + (sf.getMembersAnyEnergy(fvi).size() - maxPrint)
                            + " more");
                    break;
                }
            }
        }
        return fmt.toString();
    }

}
