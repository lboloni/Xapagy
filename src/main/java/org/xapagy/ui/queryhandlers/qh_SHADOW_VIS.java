package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.agents.FocusSorter;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.ColorCodeRepository;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

public class qh_SHADOW_VIS implements IQueryHandler {

    public static final int maxShadows = 5;

    /**
     * Adds a link to the VI and the strongest shadows
     * 
     * @param fmt
     * @param agent
     * @param gq
     * @param vi
     */
    public static void addViAndStrongShadows(IXwFormatter fmt, Agent agent,
            RESTQuery query, VerbInstance vi, ColorCodeRepository ccr) {
        PwQueryLinks.linkToStoryLineColor(fmt, agent, vi, ccr);
        PwQueryLinks.linkToVi(fmt, agent, query, vi);
        fmt.startEmbed();
        fmt.add(qh_VERB_INSTANCE.pwViShadow(vi, agent, query,
                qh_SHADOW_VIS.maxShadows, ccr, false));
        fmt.endEmbed();
    }

    /**
     * Generates the list of the shadows of the instances
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param session
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        Focus fc = agent.getFocus();
        String redheader = "VIs in the focus with their " +  qh_SHADOW_VIS.maxShadows  + " strongest shadows";
        fmt.addH2(redheader, "class=identifier");
        // explain the order in which the energies will be listed
        StringBuffer exp = new StringBuffer();
        exp.append("Focus energies listed: (");
        for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
            exp.append(ec.toString() + " ");
        }        
        exp.append("). Shadow energies listed: (");
        for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            exp.append(ec.toString() + " ");
        }        
        exp.append(")");
        fmt.explanatoryNote(exp.toString());
        // this set allows us to track if something did not fit in any of the categories
        // and print it separately at the end
        Set<VerbInstance> alreadyPrinted = new HashSet<>();
        Map<ViClass, List<VerbInstance>> visSplitByClass =
                ViClassifier.splitByClass(fc.getViListAllEnergies(), agent);
        //
        // Action VIs
        //
        fmt.addH2("Action VIs");
        List<VerbInstance> actionList =
                new ArrayList<>(visSplitByClass.get(ViClass.ACTION));
        FocusSorter.sortVisDecreasingFocusSalience(actionList, agent);
        for (VerbInstance vi : actionList) {
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            qh_SHADOW_VIS.addViAndStrongShadows(fmt, agent, query, vi,
                    session.colorCodeRepository);
            fmt.closeP();
            alreadyPrinted.add(vi);
        }
        //
        // Relation
        //
        fmt.addH2("Relations");
        List<VerbInstance> relationList =
                new ArrayList<>(visSplitByClass.get(ViClass.RELATION));
        FocusSorter.sortVisDecreasingFocusSalience(relationList, agent);
        for (VerbInstance vi : relationList) {
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            qh_SHADOW_VIS.addViAndStrongShadows(fmt, agent, query, vi,
                    session.colorCodeRepository);
            fmt.closeP();
            alreadyPrinted.add(vi);
        }
        //
        // Relation creation
        //
        fmt.addH2("Relation manipulation");
        List<VerbInstance> relationManipulationList =
                new ArrayList<>(visSplitByClass.get(ViClass.RELATION_MANIPULATION));
        FocusSorter.sortVisDecreasingFocusSalience(relationManipulationList,
                agent);
        for (VerbInstance vi : relationManipulationList) {
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            qh_SHADOW_VIS.addViAndStrongShadows(fmt, agent, query, vi,
                    session.colorCodeRepository);
            fmt.closeP();
            alreadyPrinted.add(vi);
        }
        //
        // Everything else in the focus
        //
        boolean missedSomething = false;
        List<VerbInstance> missedList = new ArrayList<>(fc.getViList(EnergyColors.FOCUS_VI));
        FocusSorter.sortVisDecreasingFocusSalience(missedList, agent);
        for (VerbInstance vi : missedList) {
            if (alreadyPrinted.contains(vi)) {
                continue;
            }
            if (!missedSomething) {
                missedSomething = true;
                fmt.addH2("Everything else which did not appear above");
            }
            fmt.openP();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.progressBarSlash(fc.getSalience(vi, ec),
                     fc.getEnergy(vi, ec));
             }
            qh_SHADOW_VIS.addViAndStrongShadows(fmt, agent, query, vi,
                    session.colorCodeRepository);
            fmt.closeP();
        }
    }

}
