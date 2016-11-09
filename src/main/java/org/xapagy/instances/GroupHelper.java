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
package org.xapagy.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.TextUi;

/**
 * Various utility functions with regards the handling of the groups
 * 
 * @author Ladislau Boloni
 * Created on: Jul 21, 2011
 */
public class GroupHelper {

    /**
     * Creates on-demand a group of instances
     * 
     * The scene of the group links will be the scene from which this was called
     * from
     * 
     * @param agent
     * @param instances
     * @return
     */
    private static Instance createGroup(Agent agent, Set<Instance> instances,
            Instance scene) {
        Focus fc = agent.getFocus();
        Instance group = agent.createInstance(scene);
        group.getConcepts().addFullEnergy(
                agent.getConceptDB().getConcept(Hardwired.C_GROUP));
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createAdd(group, Focus.INITIAL_ENERGY_INSTANCE,
                        EnergyColors.FOCUS_INSTANCE, "GroupHelper");
        fc.applyInstanceEnergyQuantum(eq);
        for (Instance inst : instances) {
            VerbOverlay verbs =
                    VerbOverlay.createVO(agent, Hardwired.VR_MEMBER_OF_GROUP,
                            Hardwired.VM_RELATION_MARKER);
            VerbInstance vi = agent.createVerbInstance(ViType.S_V_O, verbs);
            // vi.setScene(verbInstance.getScene());
            vi.setSubject(inst);
            vi.setObject(group);
            EnergyQuantum<VerbInstance> eqv =
                    EnergyQuantum.createAdd(vi, Focus.INITIAL_ENERGY_VI,
                            EnergyColors.FOCUS_VI, "GroupHelper");
            fc.applyViEnergyQuantum(eqv);
        }
        return group;
    }

    /**
     * Returns true if the instance is a group
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static boolean decideGroup(Instance instance, Agent agent) {
        return Hardwired.contains(agent, instance.getConcepts(),
                Hardwired.C_GROUP);
    }

    /**
     * Decides if this is a group member relation
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static boolean
            decideGroupMemberRelation(VerbInstance vi, Agent agent) {
        return Hardwired.contains(agent, vi.getVerbs(),
                Hardwired.VR_MEMBER_OF_GROUP);
    }

    /**
     * Returns the group which is the group of exactly the instances listed
     * 
     * The use of this function to refer to groups makes them unique
     * 
     * @param agent
     * @param instances
     * @param verbInstance
     * @return
     */
    public static Instance getGroupOf(Agent agent, Set<Instance> instances,
            boolean createOnDemand) {
        Focus fc = agent.getFocus();
        List<Instance> retval = new ArrayList<>();
        Instance scene = null;
        // verify that all the instances are in the same scene
        for (Instance inst : instances) {
            if (scene == null) {
                scene = inst.getScene();
            } else if (scene != inst.getScene()) {
                throw new Error(
                        "GroupHelper.getGroupOf: instances from different scenes!!!");
            }
        }

        for (Instance instance : fc.getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
            if (!GroupHelper.decideGroup(instance, agent)) {
                continue;
            }
            Set<Instance> members =
                    GroupHelper.getMembersOfGroup(agent, instance);
            if (members.size() != instances.size()) {
                continue;
            }
            boolean allThere = true;
            for (Instance inst : instances) {
                if (!members.contains(inst)) {
                    allThere = false;
                    break;
                }
            }
            if (allThere) {
                retval.add(instance);
            }
        }
        if (retval.size() == 0) {
            if (!createOnDemand) {
                TextUi.println("Get group of did not find the corresponding group, return null");
                return null;
            } else {
                Instance newGroup =
                        GroupHelper.createGroup(agent, instances, scene);
                return newGroup;
            }
        }
        if (retval.size() == 1) {
            return retval.get(0);
        }
        TextUi.println("More than two groups of this kind have been found, return the first one!");
        return retval.get(0);
    }

    /**
     * Returns the members of a certain group
     * 
     * @param agent
     * @param instGroup
     * @return
     */
    public static Set<Instance> getMembersOfGroup(Agent agent,
            Instance instGroup) {
        return RelationHelper.getRelationSpecificPart(agent,
                Hardwired.VR_MEMBER_OF_GROUP, instGroup, ViPart.Object,
                ViPart.Subject);
    }

    /**
     * Returns true if instance is member of group inst
     * 
     * @param agent
     * @param instance
     * @param instGroup
     * @return
     */
    public static boolean isMemberOf(Agent agent, Instance instance,
            Instance instGroup) {
        double val =
                RelationHelper.getRelation(true, agent,
                        Hardwired.VR_MEMBER_OF_GROUP, instance, instGroup);
        double minValue = 1.0;
        minValue =
                Math.min(
                        minValue,
                        agent.getFocus().getSalience(instance,
                                EnergyColors.FOCUS_INSTANCE));
        minValue =
                Math.min(
                        minValue,
                        agent.getFocus().getSalience(instGroup,
                                EnergyColors.FOCUS_INSTANCE));
        return val > 0.8 * minValue;
    }
}
