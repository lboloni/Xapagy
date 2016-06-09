package org.xapagy.ui.queryhandlers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.agents.Focus;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.InstanceSet;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwQuantumEnergyList;
import org.xapagy.ui.prettyhtml.ColorCodeRepository;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.PwScenePreferences;
import org.xapagy.ui.prettyprint.PpVerbOverlay;
import org.xapagy.ui.smartprint.SpInstance;

public class qh_INSTANCE implements IQueryHandler, IQueryAttributes {

    /**
     * 
     * Generates the webpage for the detailed description of an instance
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param wgpg
     *            - the generator (contains colors for the story lines)
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        String identifier = query.getAttribute(Q_ID);
        Instance instance =
                agent.getAutobiographicalMemory().getInstance(identifier);
        int countHideable = 1;
        Focus fc = agent.getFocus();
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        //
        // Header
        //
        String redheader = "Instance " + SpInstance.spc(instance, agent);
        fmt.addH2(redheader, "class=identifier");
        //
        // Attributes
        //
        fmt.addLabelParagraph("Attributes:");
        fmt.indent();
        qh_CONCEPT_OVERLAY.generate(fmt, agent, instance.getConcepts(), query,
                true);
        fmt.deindent();
        //
        // Focus energies and saliences. Also determines if it is in the focus.
        //
        boolean isInFocus = false;
        fmt.addLabelParagraph("Focus:");
        fmt.indent();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_INSTANCE)) {
            double salience = fc.getSalience(instance, ec);
            double energy = fc.getEnergy(instance, ec);
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
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.AM_INSTANCE)) {
            double salience = am.getSalience(instance, ec);
            double energy = am.getEnergy(instance, ec);
            fmt.openP();
            fmt.progressBarSlash(salience, energy);
            fmt.add(ec.toString());
            fmt.closeP();
        }
        fmt.deindent();
        //
        // If it is a scene, list the members, if not show the scene
        //
        if (instance.isScene()) {
            fmt.addLabelParagraph("The instance is a scene with the following members");
            List<Instance> members = instance.getSceneMembers();
            fmt.indent();
            PwQueryLinks.linkToInstanceList(fmt, agent, query, members);
            fmt.deindent();
            // fmt.addLabelParagraph("Scene preferences");
            // fmt.indent();
            fmt.startEmbedX("Scene preferences:");
            PwScenePreferences.pwScenePreferences(fmt,
                    instance.getSceneParameters(), agent, query);
            fmt.endEmbedX();
            // fmt.deindent();
        } else {
            fmt.addLabelParagraph("It is member of scene: ", PwQueryLinks
                    .linkToInstance(fmt.getEmpty(), agent, query,
                            instance.getScene()));
        }
        //
        // Relations
        //
        PwFormatter fmt2 = fmt.getEmpty();
        qh_INSTANCE.pwRelations(fmt2, instance, agent, query);
        fmt.addExtensibleH2("id" + countHideable++, "Relations",
                fmt2.toString(), true);
        //
        // shadows: only when in the focus
        //
        if (isInFocus) {
            String shadow =
                    qh_INSTANCE.pwInstanceShadow(instance, agent, query, 15,
                            session.colorCodeRepository, false);
            fmt.addExtensibleH2("id" + countHideable++, "Shadows", shadow, true);
        }
        //
        // Reverse shadow
        //
        fmt2 = fmt.getEmpty();
        qh_INSTANCE.pwInstanceReverseShadow(fmt2, instance, agent, query);
        fmt.addExtensibleH2("id" + countHideable++, "Reverse shadows",
                fmt2.toString(), true);
        //
        // Referring VIs
        //
        fmt2 = fmt.getEmpty();
        qh_INSTANCE.pwReferringVis(fmt2, instance, agent, query);
        fmt.addExtensibleH2("id" + countHideable++, "Referring VIs",
                fmt2.toString(), true);
        //
        // Energy quantums
        //
        boolean debugRecordQuantums =
                agent.getParameters().getBoolean("A_DEBUG",
                        "G_GENERAL",
                        "N_RECORD_FOCUS_MEMORY_QUANTUMS");
        if (debugRecordQuantums) {
            fmt2 = fmt.getEmpty();
            xwQuantumEnergyList.pwAllEnergyQuantums(fmt2, instance, agent,
                    query);
            fmt.addExtensibleH2("id" + countHideable++, "Energy quantums",
                    fmt2.toString(), true);
        } else {
            fmt.addH2("Energy quantums not available as recording is not enabled.");
        }
    }

    /**
     * Prints the shadow of this instance
     * 
     * @param fi
     * @param agent
     * @param gq
     */
    public static void pwInstanceReverseShadow(PwFormatter fmt,
            Instance instance, Agent agent, RESTQuery gq) {
        Shadows sf = agent.getShadows();
        InstanceSet reverseShadow =
                sf.getReverseShadow(instance, EnergyColors.SHI_GENERIC);
        if (!reverseShadow.isEmpty()) {
            PwQueryLinks.linkToInstanceSet(fmt, agent, gq, reverseShadow);
        } else {
            fmt.addLabelParagraph("This instance does not have any reverse shadows.");
        }
    }

    /**
     * Prints the shadow of this instance (and adds links to explanation)
     * 
     * @param fi
     * @param agent
     * @param query
     * @param maxPrint
     *            - the number of shadows which will be printed
     * @param wgpg
     *            - has a link to the story line colors
     * @param documentEC
     *            TODO
     * @return
     */
    public static String pwInstanceShadow(Instance fi, Agent agent,
            RESTQuery query, int maxPrint, ColorCodeRepository ccr,
            boolean documentEC) {
        PwFormatter fmt = new PwFormatter();
        Shadows sf = agent.getShadows();
        // document the order of the shadow energies
        if (documentEC) {
            String shadowEnergies =
                    "The shadow energies are listed in the order: ";
            for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
                shadowEnergies += " " + ec;
            }
            fmt.explanatoryNote(shadowEnergies);
        }
        //
        // now, list the shadows. Don't reprint them if they had been already printed.
        //
        Set<Instance> alreadyPrinted = new HashSet<>();
        for (String ecSort : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            int count = 0;
            boolean noShadows = true;
            fmt.addH3("Shadow energy: " + ecSort);
            for (Instance si : sf.getMembers(fi, ecSort)) {
                if (alreadyPrinted.contains(si)) {
                    continue;
                }
                alreadyPrinted.add(si);
                noShadows = false;
                count++;
                fmt.openP();
                for (String ec : agent.getEnergyColors()
                        .getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
                    double valueSalience = sf.getSalience(fi, si, ec);
                    double valueEnergy = sf.getEnergy(fi, si, ec);
                    // fmt.add(sc);
                    fmt.progressBarSlash(valueSalience, valueEnergy);
                }
                PwQueryLinks.linkToStoryLineColor(fmt, agent, si, ccr);
                PwQueryLinks.linkToInstance(fmt, agent, query, si);
                PwQueryLinks.linkToInstanceShadowExplanation(fmt, agent, query, fi,
                        si);
                fmt.closeP();
                if (count > maxPrint) {
                    fmt.addLabelParagraph(".... and "
                            + (sf.getMembersAnyEnergy(fi).size() - maxPrint)
                            + " more.");
                    break;
                }
            }
            if (noShadows) {
                fmt.addLabelParagraph("No additional shadows of energy " + ecSort);
            }
        }
        return fmt.toString();
    }

    /**
     * Prints the list of referring VIs
     * 
     * @return
     */
    public static String pwReferringVis(PwFormatter fmt, Instance instance,
            Agent agent, RESTQuery query) {
        fmt.explanatoryNote("The VIs which are referring to this instance.");
        List<VerbInstance> vis = instance.getReferringVis();
        for (VerbInstance vi : vis) {
            fmt.openP();
            PwQueryLinks.linkToVi(fmt, agent, query, vi);
            fmt.closeP();
        }
        return fmt.toString();
    }

    /**
     * Returns a detailed version of the instance description which outlines the
     * composites
     * 
     * @param fmt
     * @param instance
     * @param agent
     * @param query
     * @return
     */
    public static String pwRelations(PwFormatter fmt, Instance instance,
            Agent agent, RESTQuery query) {
        boolean noRelations = true;
        Focus fc = agent.getFocus();
        // unary context relations - I think these do not exist any more!!!
        String prefix = "";
        List<VerbInstance> vis = null;
        // binary context relations, from
        vis = RelationHelper.getRelationsFrom(agent, instance, false);
        if (!vis.isEmpty()) {
            noRelations = false;
            fmt.addLabelParagraph("From this instance:");
            fmt.indent();
            for (VerbInstance vi : vis) {
                fmt.openP();
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                fmt.addBold(prefix + "this");
                fmt.addArrow();
                fmt.addBold(PpVerbOverlay.ppRelationLabel(vi.getVerbs(), agent));
                fmt.addArrow();
                Instance otherInstance = vi.getObject();
                PwQueryLinks.linkToInstance(fmt, agent, query, otherInstance);
                fmt.closeP();
            }
            fmt.deindent();
        }
        // binary context relations, to
        vis = RelationHelper.getRelationsTo(agent, instance, false);
        if (!vis.isEmpty()) {
            noRelations = false;
            fmt.addLabelParagraph("To this instance:");
            fmt.indent();
            for (VerbInstance vi : vis) {
                fmt.openP();
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                    fmt.addBold(prefix);
                }
                Instance otherInstance = vi.getSubject();
                PwQueryLinks.linkToInstance(fmt, agent, query, otherInstance);
                fmt.addArrow();
                fmt.addBold(PpVerbOverlay.ppRelationLabel(vi.getVerbs(), agent));
                fmt.addArrow();
                fmt.addBold("this");
                fmt.closeP();
            }
            fmt.deindent();
        }
        if (noRelations) {
            fmt.addLabelParagraph("This instance has no relations.");
        }
        return fmt.toString();
    }

}
