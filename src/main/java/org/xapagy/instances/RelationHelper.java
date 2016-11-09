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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Calibration;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;

/**
 * 
 * 
 * @author Ladislau Boloni
 * Created on: Jul 20, 2011
 */
public class RelationHelper {

    /**
     * Creates a relation and adds it to the focus
     * 
     * @param vo
     */
    public static void createAndAddRelation(Agent agent, VerbOverlay vo,
            Instance from, Instance to) {
        Focus fc = agent.getFocus();
        VerbInstance vi = agent.createVerbInstance(ViType.S_V_O, vo);
        vi.setSubject(from);
        vi.setObject(to);
        EnergyQuantum<VerbInstance> eq =
                EnergyQuantum.createAdd(vi, Focus.INITIAL_ENERGY_VI,
                        EnergyColors.FOCUS_VI, "RelationHelper");
        fc.applyViEnergyQuantum(eq);
    }

    /**
     * Decision on the existence of a relation - assume a low threshold
     * 
     * It can handle both the case when we are looking for a relation in the
     * focus, and when we are looking for relations in general (the latter is
     * much slower!!!)
     * 
     * @param inFocus
     * @param agent
     * @param relationName
     * @param inst
     * @return
     */
    public static boolean decideRelation(boolean inFocus, Agent agent,
            String relationName, Instance... inst) {
        double value =
                RelationHelper.getRelation(inFocus, agent, relationName, inst);
        if (value > 0.0) {
            return true;
        }
        return false;
    }

    /**
     * Returns a recursive relation
     * 
     * @param agent
     * @param relationName
     * @param instance
     * @param myPart
     * @param theirPart
     * @param recursionLimit
     *            - how deep will we go in
     * @return
     */
    public static Set<Instance> getRecursiveRelationSpecificPart(Agent agent,
            String relationName, Instance instance, ViPart myPart,
            ViPart theirPart, int recursionLimit) {
        Set<Instance> retval = new HashSet<>();
        Set<Instance> toExpand = new HashSet<>();
        toExpand.add(instance);
        for (int i = 0; i != recursionLimit; i++) {
            Set<Instance> frontier = new HashSet<>();
            for (Instance scene : toExpand) {
                Set<Instance> tmp =
                        RelationHelper.getRelationSpecificPart(agent,
                                Hardwired.VR_SCENE_SUCCESSION, scene, myPart,
                                theirPart);
                frontier.addAll(tmp);
            }
            // early termination
            if (frontier.isEmpty()) {
                break;
            }
            toExpand = frontier;
            retval.addAll(frontier);
        }
        return retval;
    }

    /**
     * Gets all the active context relations from a certain instance (all
     * instances if null), the instance being a certain part (subject or object)
     * of a certain expectedType
     * 
     * @param agent
     * @param instance
     * @param expectedType
     * @param part
     *            the part the instance is playing in the relation
     * @param activeOnly
     *            - only returns the relations which are in focus
     * @return
     */
    private static List<VerbInstance> getRelation(Agent agent,
            Instance instance, ViType expectedType, ViPart part,
            boolean activeOnly) {
        Focus fc = agent.getFocus();
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : instance.getReferringVis()) {
            if (activeOnly && fc.getSalience(vi, EnergyColors.FOCUS_VI) == 0) {
                continue;
            }
            if (!ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
                continue;
            }
            if (vi.getViType() != expectedType) {
                continue;
            }
            if (vi.getPart(part) != instance) {
                continue;
            }
            retval.add(vi);
        }
        return retval;
    }

    /**
     * Returns the current value of relation relationName in the focus or in the
     * episodic memory
     * 
     * It can be either a unary relation (specify only the subject) or a binary
     * relation subject / object
     * 
     * @param inFocus
     *            if true looks in the focus, otherwise it looks in the episodic
     *            memory
     * 
     * @param agent
     * @param relationName
     * @param inst1
     * @param inst2
     * @return
     */
    public static double getRelation(boolean inFocus, Agent agent,
            String relationName, Instance... inst) {
        Collection<VerbInstance> vis = inst[0].getReferringVis();
        Verb relationVerb = agent.getVerbDB().getConcept(relationName);
        for (VerbInstance vi : vis) {
            if (!ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
                continue;
            }
            if (vi.getSubject() != inst[0]) {
                continue;
            }
            if (inst.length > 1) {
                if (vi.getObject() != inst[1]) {
                    continue;
                }
            }
            if (vi.getVerbs().getEnergy(relationVerb) < Calibration.decideRelationPresent) {
                continue;
            }
            // so it is that relation
            if (inFocus) {
                return agent.getFocus().getSalience(vi, EnergyColors.FOCUS_VI);
            } else {
                return agent.getAutobiographicalMemory().getSalience(vi,
                        EnergyColors.AM_VI);
            }
        }
        return 0;
    }

    /**
     * Gets a list of all the binary context relations in the focus
     * 
     * @param agent
     * @param instance
     * @param activeOnly
     *            - if true, returns only those in focus
     * @return
     */
    public static List<VerbInstance> getRelations(Agent agent,
            boolean activeOnly) {
        return RelationHelper.getRelation(agent, null, ViType.S_V_O, null,
                activeOnly);
    }

    /**
     * Gets a list of all the binary context relations between instSubject and
     * instObject
     * 
     * @param agent
     * @param instSubject
     * @param instObject
     * @param activeOnly
     *            - if true, returns only those in focus
     * @return
     */
    public static List<VerbInstance> getRelationsBetween(Agent agent,
            Instance instSubject, Instance instObject, boolean activeOnly) {
        List<VerbInstance> subjectList =
                RelationHelper.getRelationsFrom(agent, instSubject, activeOnly);
        List<VerbInstance> objectList =
                RelationHelper.getRelationsTo(agent, instObject, activeOnly);
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : subjectList) {
            if (objectList.contains(vi)) {
                retval.add(vi);
            }
        }
        return retval;
    }

    /**
     * Gets a list of all the binary context relations starting from an instance
     * 
     * @param agent
     * @param instance
     * @param activeOnly
     *            - if true, returns only those in focus
     * @return
     */
    public static List<VerbInstance> getRelationsFrom(Agent agent,
            Instance instance, boolean activeOnly) {
        return RelationHelper.getRelation(agent, instance, ViType.S_V_O,
                ViPart.Subject, activeOnly);
    }

    /**
     * Get a set of instances which have position theirPart in relations where
     * the instance has a position myPart
     * 
     * @param agent
     * @param relationName
     * @param instance
     * @return
     */
    public static Set<Instance> getRelationSpecificPart(Agent agent,
            String relationName, Instance instance, ViPart myPart,
            ViPart theirPart) {
        Collection<VerbInstance> vis = instance.getReferringVis();
        Verb relationVerb = agent.getVerbDB().getConcept(relationName);
        Set<Instance> retval = new HashSet<>();
        for (VerbInstance vi : vis) {
            if (!ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
                continue;
            }
            if (vi.getPart(myPart) != instance) {
                continue;
            }
            if (vi.getVerbs().getEnergy(relationVerb) < Calibration.decideRelationPresent) {
                continue;
            }
            retval.add((Instance) vi.getPart(theirPart));
        }
        return retval;
    }

    /**
     * Gets a list of all the binary context relations going to an instance
     * 
     * @param agent
     * @param instance
     * @param activeOnly
     *            - if true, returns only those in focus
     * @return
     */
    public static List<VerbInstance> getRelationsTo(Agent agent,
            Instance instance, boolean activeOnly) {
        return RelationHelper.getRelation(agent, instance, ViType.S_V_O,
                ViPart.Object, activeOnly);
    }

    /**
     * Gets all the unary context relations an instance (for all instances, if
     * instance is null)
     * 
     * @param agent
     * @param instance
     * @param activeOnly
     *            - only returns the relations which are in focus
     * @return
     */
    public static List<VerbInstance> getUnaryRelations(Agent agent,
            Instance instance, boolean activeOnly) {
        return RelationHelper.getRelation(agent, instance, ViType.S_V,
                ViPart.Subject, activeOnly);
    }

}
