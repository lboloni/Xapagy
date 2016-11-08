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
package org.xapagy.ui.smartprint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwConceptOverlay;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.verbalize.VerbalMemoryHelper;

/**
 * Smart printing of an instance
 * 
 * @author Ladislau Boloni
 * Created on: Nov 23, 2011
 */
public class SpInstance {

    /**
     * Smart print concise - it supposed to be the most informative one line
     * printing.
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static String spc(Instance instance, Agent agent) {
        Instance viewedFromScene = agent.getFocus().getCurrentScene();
        String tmp = VerbalMemoryHelper.getFrequentReference(agent, instance,
                viewedFromScene, new HashSet<String>());
        if (tmp == null) {
            tmp = SpInstance.spInstance(instance, viewedFromScene, agent,
                    false);
        }
        return tmp + "(" + instance.getIdentifier() + ")";
    }

    /**
     * Smart printing of an instance, viewed from the point of view of a given
     * scene.
     * 
     * @param instance
     *            to be printed from
     * @param viewedFromScene
     *            the scene in the context of which we are printing
     * @param agent
     *            - the agent
     * @param printRelations
     *            - print the relations in which the instance participates
     * @return
     */
    public static String spInstance(Instance instance, Instance viewedFromScene,
            Agent agent, boolean printRelations) {
        // handle special cases
        // if it is a group
        if (GroupHelper.decideGroup(instance, agent)) {
            return SpInstance.spInstanceAsGroup(instance, viewedFromScene,
                    agent);
        }
        String retval = null;
        // labels with the most frequent reference - exclude I
        HashSet<String> exclusionList = new HashSet<>();
        exclusionList.add("I");
        retval = VerbalMemoryHelper.getFrequentReference(agent, instance,
                viewedFromScene, exclusionList);
        // if the label is still null, then refer by parameters
        if (retval == null) {
            retval = xwConceptOverlay.xwConcise(new TwFormatter(), instance.getConcepts(), agent);
        } else {
            // add the labels, if we found it by verbal reference, otherwise, it
            // is there
            for (String label : instance.getConcepts().getLabels()) {
                if (!retval.contains(label)) {
                    retval = retval + " " + label;
                }
            }
        }
        // print relations
        if (printRelations) {
            String identities = SpInstance.spInstanceIdentities(instance,
                    viewedFromScene, agent);
            if (!identities.equals("")) {
                retval = retval + "\n"
                        + identities.substring(0, identities.length() - 1);
            }
            String relations =
                    SpFocus.ppsRelations(instance, viewedFromScene, agent);
            if (!relations.equals("")) {
                retval = retval + "\n" + relations;
            }
        }
        return retval;
    }

    /**
     * Smart print an instance representing a group
     * 
     * @param instance
     * @param scene
     * @param agent
     * @return
     */
    private static String spInstanceAsGroup(Instance instance, Instance scene,
            Agent agent) {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        Set<Instance> members = GroupHelper.getMembersOfGroup(agent, instance);
        if (!members.isEmpty()) {
            for (Instance inst : members) {
                buf.append(SpInstance.spInstance(inst, scene, agent, false));
                buf.append(" + ");
            }
            buf.delete(buf.length() - 3, buf.length());
        } else {
            buf.append("Empty group!");
        }
        buf.append("}");
        return buf.toString();
    }

    /**
     * Printing the identities of an instance (when seen from the scene)
     * 
     * @param instance
     * @param scene
     *            the
     * @param agent
     * @return
     */
    public static String spInstanceIdentities(Instance instance, Instance scene,
            Agent agent) {
        Formatter fmt = new Formatter();
        String prefix = "";
        Focus fc = agent.getFocus();
        fmt.indent();
        // binary context relations, from
        List<VerbInstance> vis =
                RelationHelper.getRelationsFrom(agent, instance, false);
        for (VerbInstance vi : vis) {
            if (!ViClassifier.decideViClass(ViClass.IDENTITY, vi, agent)) {
                continue;
            }
            if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                prefix = "";
            } else {
                prefix = "(inactive)";
            }
            if (IdentityHelper.isSomaticIdentity(instance, vi.getObject(),
                    agent)) {
                fmt.add(prefix + "--somatic identity-->" + SpInstance
                        .spInstance(vi.getObject(), scene, agent, false));
                continue;
            }
            if (IdentityHelper.isFictionalIdentity(instance, vi.getObject(),
                    agent)) {
                fmt.add(prefix + "--fictional identity-->"
                        + SpInstance.spInstance(vi.getObject(), scene, agent,
                                false)
                        + " in " + SpFocus.ppsScene(vi.getObject().getScene(),
                                agent, false));
                continue;
            }
            if (IdentityHelper.isViewIdentity(instance, vi.getObject(),
                    agent)) {
                fmt.add(prefix + "--view identity-->"
                        + SpInstance.spInstance(vi.getObject(), scene, agent,
                                false)
                        + " in " + SpFocus.ppsScene(vi.getObject().getScene(),
                                agent, false));
                continue;
            }
            if (IdentityHelper.isSuccessionIdentity(instance, vi.getObject(),
                    agent)) {
                fmt.add(prefix + "--succession identity-->"
                        + SpInstance.spInstance(vi.getObject(), scene, agent,
                                false)
                        + " in " + SpFocus.ppsScene(vi.getObject().getScene(),
                                agent, false));
                continue;
            }
            // if we are here, it means that the scenes are unrelated
            // this happens for identities created for is-the-same-as
            fmt.add("--generic identity-->"
                    + SpInstance.spInstance(vi.getObject(), scene, agent, false)
                    + " in " + SpFocus.ppsScene(vi.getObject().getScene(),
                            agent, false));

        }
        // binary context relations, to
        vis = RelationHelper.getRelationsTo(agent, instance, false);
        if (!vis.isEmpty()) {
            fmt.indent();
            for (VerbInstance vi : vis) {
                if (!ViClassifier.decideViClass(ViClass.IDENTITY, vi, agent)) {
                    continue;
                }
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                if (IdentityHelper.isSomaticIdentity(vi.getSubject(), instance,
                        agent)) {
                    fmt.add(prefix + "<--somatic identity--" + SpInstance
                            .spInstance(vi.getSubject(), scene, agent, false));
                    continue;
                }
                if (IdentityHelper.isFictionalIdentity(vi.getSubject(),
                        instance, agent)) {
                    fmt.add(prefix + "<--fictional identity--"
                            + SpInstance.spInstance(vi.getSubject(), scene,
                                    agent, false)
                            + " in " + SpFocus.ppsScene(
                                    vi.getSubject().getScene(), agent, false));
                    continue;
                }
                if (IdentityHelper.isViewIdentity(vi.getSubject(), instance,
                        agent)) {
                    fmt.add(prefix + "<--view identity--"
                            + SpInstance.spInstance(vi.getSubject(), scene,
                                    agent, false)
                            + " in " + SpFocus.ppsScene(
                                    vi.getSubject().getScene(), agent, false));
                    continue;
                }
                if (IdentityHelper.isSuccessionIdentity(vi.getSubject(),
                        instance, agent)) {
                    fmt.add(prefix + "<--succession identity--"
                            + SpInstance.spInstance(vi.getSubject(), scene,
                                    agent, false)
                            + " in " + SpFocus.ppsScene(
                                    vi.getSubject().getScene(), agent, false));
                    continue;
                }
                // if we are here, it means that the scenes are unrelated
                // this happens for identities created for is-the-same-as
                fmt.add(prefix + "<--generic identity--"
                        + SpInstance.spInstance(vi.getSubject(), scene, agent,
                                false)
                        + " in " + SpFocus.ppsScene(vi.getSubject().getScene(),
                                agent, false));
            }
            fmt.deindent();
        }
        return fmt.toString();

    }

}
