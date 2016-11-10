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
package org.xapagy.reference;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.Set;

import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;

/**
 * @author Ladislau Boloni
 * Created on: Jul 28, 2011
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
