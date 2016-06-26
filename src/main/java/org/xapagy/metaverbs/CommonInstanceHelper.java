/*
   This file is part of the Xapagy project
   Created on: Aug 27, 2011
 
   org.xapagy.metaverbs.CommonInstanceHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * 
 * A series of helpers for the successor code (looks for common instances, and
 * common scenes between VIs)
 * 
 * @author Ladislau Boloni
 * 
 */
public class CommonInstanceHelper {

    /**
     * Extracts all the scenes referenced in a VI
     * 
     * @param vi
     *            - a fully resolved verb instance
     * @param quotesAsWell
     *            - do we also consider the scenes in the quotes???
     * 
     * @return
     */
    public static Set<Instance> extractScenes(VerbInstance vi,
            boolean quotesAsWell) {
        Set<Instance> retval = new HashSet<>();
        if (!vi.getViType().equals(ViType.QUOTE)) {
            for (ViPart part : ViStructureHelper.getAllowedInstanceParts(vi
                    .getViType())) {
                retval.add(((Instance) vi.getPart(part)).getScene());
            }
        } else {
            // if this is a quote
            retval.add(vi.getSubject().getScene());
            if (quotesAsWell) {
                retval.addAll(CommonInstanceHelper.extractScenes(vi.getQuote(),
                        quotesAsWell));
            }
        }
        return retval;
    }

    /**
     * Returns true of vi1 and vi2 have a common instance - it is also true if
     * there is a common instance for the groups
     * 
     * @param vi1
     * @param vi2
     * @param agent
     * @return
     */
    public static boolean haveCommonInstance(VerbInstance vi1,
            VerbInstance vi2, Agent agent) {
        // if (!ViClassifier.decideViClass(ViClass.ACTION, vi2, agent)) {
        // return false;
        // }
        for (ViPart part1 : ViStructureHelper.getAllowedInstanceParts(vi1
                .getViType())) {
            Instance instance1 = (Instance) vi1.getPart(part1);
            for (ViPart part2 : ViStructureHelper.getAllowedInstanceParts(vi2
                    .getViType())) {
                Instance instance2 = (Instance) vi2.getPart(part2);
                if (CommonInstanceHelper.relatedInstance(instance1, instance2,
                        agent)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the VI's have a common scene (including in the qoutes)
     * 
     * @param vi1
     * @param vi2
     * @param a
     * @return
     */
    public static boolean haveCommonScenes(VerbInstance vi1, VerbInstance vi2,
            Agent a, boolean quotesAsWell) {
        Set<Instance> scenes1 =
                CommonInstanceHelper.extractScenes(vi1, quotesAsWell);
        Set<Instance> scenes2 =
                CommonInstanceHelper.extractScenes(vi2, quotesAsWell);
        for (Instance inst : scenes1) {
            if (scenes2.contains(inst)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Two instances are related if they are identical, one of them is a group
     * and the other is a member, or if both of them are groups and they have
     * related members
     * 
     * 
     * @param instance1
     * @param instance2
     * @param agent
     * @return
     */
    public static boolean relatedInstance(Instance instance1,
            Instance instance2, Agent agent) {
        // case 1: they are equal
        if (instance1.equals(instance2)) {
            return true;
        }
        // case 2 instance1 is a group
        if (GroupHelper.decideGroup(instance1, agent)) {
            if (GroupHelper.isMemberOf(agent, instance2, instance1)) {
                return true;
            }
        }
        // case 3: instance2 is a group
        if (GroupHelper.decideGroup(instance2, agent)) {
            if (GroupHelper.isMemberOf(agent, instance1, instance2)) {
                return true;
            }
        }
        // case 4: both instances are groups, recursive call
        if (GroupHelper.decideGroup(instance1, agent)
                && GroupHelper.decideGroup(instance2, agent)) {
            Set<Instance> instances1 =
                    GroupHelper.getMembersOfGroup(agent, instance1);
            Set<Instance> instances2 =
                    GroupHelper.getMembersOfGroup(agent, instance2);
            for (Instance in1 : instances1) {
                for (Instance in2 : instances2) {
                    if (CommonInstanceHelper.relatedInstance(in1, in2, agent)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}