/*
   This file is part of the Xapagy project
   Created on: Jul 28, 2011
 
   org.xapagy.agents.ReferenceGroup
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.reference;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.Set;

import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;

/**
 * @author Ladislau Boloni
 * 
 */
public class rrGroup {

    /**
     * Returns the reference to a group instance (which might be created
     * on-demand)
     * 
     * @param agent
     * @param reference
     * @param verbInstance
     * @param part
     * @param scene
     * @return
     */
    public static SimpleEntry<Instance, rrState> resolveReferenceGroup(
            rrContext rrc) throws rrException {
        //XrefGroup reference = (XrefGroup) rrc.getReference();
        // resolve all the individual instances
        Set<Instance> set = new HashSet<>();
        rrState rc = null;
        for (rrContext rrcMember : rrc.getGroupMembers()) {
            SimpleEntry<Instance, rrState> entry =
                    ReferenceResolution.resolveReference(rrcMember);
            Instance inst = entry.getKey();
            if (rc == null) {
                rc = entry.getValue();
            } else {
                rc = rc.composeConfidence(entry.getValue());
            }
            set.add(inst);
        }
        // retrieves an existing group or creates a new one
        Instance group = GroupHelper.getGroupOf(rrc.getAgent(), set, true);
        return new SimpleEntry<>(group, rc);
    }

}
