/*
   This file is part of the Xapagy project
   Created on: Oct 16, 2011
 
   org.xapagy.ui.prettyprint.PpsFocus
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.smartprint;

import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PpVerbOverlay;

/**
 * 
 * Structured printing - for a logical structure investigation
 * 
 * @author Ladislau Boloni
 * 
 */
public class SpFocus {

    /**
     * Formats the action VIs, in the reverse order of their strength in the
     * focus, margin noted with their weight in focus
     * 
     * @param focus
     * @param agent
     * @return
     */
    public static String ppsActionVis(Focus focus, Agent agent) {
        Focus fc = agent.getFocus();
        Formatter fmt = new Formatter();

        List<VerbInstance> list = focus.getViList(EnergyColors.FOCUS_VI);
        Collections.reverse(list);
        for (VerbInstance vi : list) {
            // all the relations are handled differently
            if (ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
                continue;
            }
            // if not an action, don't print it
            // if (!MetaVerbHelper.isActionVo(entry.getKey().getVerbs())) {
            // continue;
            // }
            fmt.addWithMarginNote(
                    Formatter.fmt(fc.getSalience(vi, EnergyColors.FOCUS_VI)),
                    XapiPrint.ppsViXapiForm(vi, agent) + " " + vi.getIdentifier());
        }
        return fmt.toString();
    }

    /**
     * Printing the relations
     * 
     * @param instance
     * @param scene
     * @param agent
     * @return
     */
    static String ppsRelations(Instance instance, Instance scene, Agent agent) {
        Formatter fmt = new Formatter();
        String prefix = "";
        Focus fc = agent.getFocus();
        // unary context relations
        List<VerbInstance> vis = null;
        // binary context relations, from
        vis = RelationHelper.getRelationsFrom(agent, instance, false);
        if (!vis.isEmpty()) {
            fmt.indent();
            for (VerbInstance vi : vis) {
                // don't print identities or group member relations here
                if (ViClassifier.decideViClass(ViClass.IDENTITY, vi, agent)) {
                    continue;
                }
                if (GroupHelper.decideGroupMemberRelation(vi, agent)) {
                    continue;
                }
                VerbOverlay voOriginal = vi.getVerbs();
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                String label = PpVerbOverlay.ppRelationLabel(voOriginal, agent);
                fmt.add(prefix
                        + "--"
                        + label
                        + "-->"
                        + SpInstance.spInstance(vi.getObject(), scene, agent,
                                false));
            }
            fmt.deindent();
        }
        // binary context relations, to
        vis = RelationHelper.getRelationsTo(agent, instance, false);
        if (!vis.isEmpty()) {
            fmt.indent();
            for (VerbInstance vi : vis) {
                // don't print identities or group member relations here
                if (ViClassifier.decideViClass(ViClass.IDENTITY, vi, agent)) {
                    continue;
                }
                if (GroupHelper.decideGroupMemberRelation(vi, agent)) {
                    continue;
                }
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                String label =
                        PpVerbOverlay.ppRelationLabel(vi.getVerbs(), agent);
                fmt.add(prefix
                        + SpInstance.spInstance(vi.getSubject(), scene, agent,
                                false) + "--" + label + "--> this");
            }
            fmt.deindent();
        }
        return fmt.toString();

    }

    /**
     * Printing a scene instance in an identifiable way. Currently falls back on
     * the concise printing of attributes other than scene
     * 
     * @param scene
     * @param agent
     * @return
     */
    public static String ppsScene(Instance scene, Agent agent,
            boolean printRelations) {
        // ConceptOverlay coScene =
        ConceptOverlay.createCO(agent, Hardwired.C_SCENE);
        // ConceptOverlay coRemain = Overlay.scrape(scene.getConcepts(),
        // coScene);
        // String tmp = PpOverlay.ppConcise(coRemain, agent);
        String tmp = "";
        for (String label : scene.getConcepts().getLabels()) {
            tmp = tmp + " " + label;
        }
        if (printRelations) {
            tmp = tmp + "\n" + SpFocus.ppsSceneRelations(scene, agent);
        }
        int count = 0;
        tmp += " { ";
        for (Instance inst : scene.getSceneMembers()) {
            count++;
            tmp += SpInstance.spInstance(inst, scene, agent,
                    false);
            if (count == 3) {
                tmp += "..., ";
                break;
            } else {
            tmp += ", ";
            }
        }
        if (tmp.endsWith(", ")) {
            tmp = tmp.substring(0, tmp.length()-2);
        }
        tmp += "}";
        return tmp;
    }

    /**
     * Returns the relation between the scenes
     * 
     * @param scene
     * @param agent
     * @return
     */
    private static String ppsSceneRelations(Instance instance, Agent agent) {
        Formatter fmt = new Formatter();
        String prefix = "";
        Focus fc = agent.getFocus();
        // binary context relations, from
        List<VerbInstance> vis =
                RelationHelper.getRelationsFrom(agent, instance, false);
        if (!vis.isEmpty()) {
            fmt.indent();
            for (VerbInstance vi : vis) {
                // don't print identities or group member relations here
                if (ViClassifier.decideViClass(ViClass.IDENTITY, vi, agent)) {
                    continue;
                }
                if (GroupHelper.decideGroupMemberRelation(vi, agent)) {
                    continue;
                }
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                String label =
                        PpVerbOverlay.ppRelationLabel(vi.getVerbs(), agent);
                fmt.add(prefix + "--" + label + "-->"
                        + SpFocus.ppsScene(vi.getObject(), agent, false));
            }
            fmt.deindent();
        }
        // binary context relations, to
        vis = RelationHelper.getRelationsTo(agent, instance, false);
        if (!vis.isEmpty()) {
            fmt.indent();
            for (VerbInstance vi : vis) {
                // don't print identities or group member relations here
                if (ViClassifier.decideViClass(ViClass.IDENTITY, vi, agent)) {
                    continue;
                }
                if (GroupHelper.decideGroupMemberRelation(vi, agent)) {
                    continue;
                }
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                String label =
                        PpVerbOverlay.ppRelationLabel(vi.getVerbs(), agent);
                fmt.add(prefix
                        + SpFocus.ppsScene(vi.getSubject(), agent, false)
                        + "--" + label + "--> this");
            }
            fmt.deindent();
        }
        return fmt.toString();

    }

    /**
     * Structured printing of the focus
     * 
     * @param focus
     * @param agent
     * @return
     */
    public static String spFocus(Focus focus, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("Focus instances:");
        fmt.addIndented(SpFocus.spFocusInstances(focus, agent));
        fmt.add("Action VIs");
        fmt.addIndented(SpFocus.ppsActionVis(focus, agent));
        fmt.add("Summary VIs");
        fmt.addIndented(SpFocus.spSummaryVis(focus, agent));
        return fmt.toString();
    }

    /**
     * Structured printing of the focus instances, organized by scene
     * 
     * It also prints the relations between instances, so these will not need to
     * be printed elsewhere
     * 
     * @param focus
     * @param agent
     * @return
     */
    public static String spFocusInstances(Focus focus, Agent agent) {
        Formatter fmt = new Formatter();
        // over all the scenes
        for (Instance scene : focus.getSceneList(EnergyColors.FOCUS_INSTANCE)) {
            List<Instance> members = scene.getSceneMembers();
            if (!members.isEmpty()) {
                fmt.add("Scene:" + SpFocus.ppsScene(scene, agent, true));
                // fmt.indent();
                // fmt.add("Members:");
                fmt.indent();
                for (Instance member : members) {
                    String spi =
                            SpInstance.spInstance(member, scene, agent, true);
                    if (spi == null) {
                        @SuppressWarnings("unused")
                        String spi2 =
                                SpInstance.spInstance(member, scene, agent,
                                        true);
                        TextUi.errorPrint("Null while sp-ing an instance???");
                    }
                    fmt.add(spi);
                }
                fmt.deindent();
            }

        }
        return fmt.toString();
    }

    /**
     * Structured printing of a single summary VI. It falls back to ppsActionVi,
     * then lists the summary members
     * 
     * @param key
     * @param agent
     * @return
     */
    private static String spSummaryVi(VerbInstance vi, Agent agent) {
        return "FIXME: I don't know how to print a summary VI - old one is not working";
    }

    /**
     * Prints the currently active summary VIs, together with a concise printing
     * of the VIs which they summarize
     * 
     * @param focus
     * @param agent
     * @return
     */
    private static String spSummaryVis(Focus focus, Agent agent) {
        Formatter fmt = new Formatter();
        List<VerbInstance> list = focus.getViList(EnergyColors.FOCUS_VI);
        Collections.reverse(list);
        for (VerbInstance vi : list) {
            fmt.addWithMarginNote(
                    Formatter.fmt(focus.getSalience(vi, EnergyColors.FOCUS_VI)),
                    SpFocus.spSummaryVi(vi, agent));
        }
        return fmt.toString();
    }
}
