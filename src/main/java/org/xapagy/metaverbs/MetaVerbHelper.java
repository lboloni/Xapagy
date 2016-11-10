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
package org.xapagy.metaverbs;

import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Overlay;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;

/**
 * @author Ladislau Boloni
 * Created on: Jan 19, 2011
 */
public class MetaVerbHelper {

    // the set of meta verbs - verbs with side effect
    private static Set<String> metaVerbs = null;

    /**
     * Returns the list of meta verbs - verbs which have a side effect
     * 
     * @param agent
     * @return
     */
    private static Set<String> getMetaVerbs() {
        if (MetaVerbHelper.metaVerbs == null) {
            MetaVerbHelper.metaVerbs = new HashSet<>();
            // action verbs
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_SUCCESSOR);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_ACTION_MARKER);
            // other metaverbs
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_CHANGES);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_CREATE_RELATION);
            MetaVerbHelper.metaVerbs
                    .add(Hardwired.VM_CREATE_ONE_SOURCE_RELATION);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_IS_A);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_NARRATE);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_RECALL);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_REMOVE_RELATION);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_SCENE_IS_ONLY);
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_ACTS_LIKE);
            // relation
            MetaVerbHelper.metaVerbs.add(Hardwired.VM_RELATION_MARKER);
        }
        return MetaVerbHelper.metaVerbs;
    }

    /**
     * Checks whether a specific verb is a metaverb -used in create negation of
     * verb word
     * 
     * @param agent
     * @return
     */
    public static boolean isMetaVerb(Verb verb, Agent agent) {
        return MetaVerbHelper.getMetaVerbs().contains(verb.getName());
    }

    /**
     * Removes all the meta verbs from a verb overlay
     * 
     * @param vi
     * @param agent
     * @return
     */
    public static VerbOverlay
            removeMetaVerbs(VerbOverlay original, Agent agent) {
        VerbOverlay mvs =
                VerbOverlay.createVO(agent, MetaVerbHelper.getMetaVerbs());
        VerbOverlay retval = Overlay.scrape(original, mvs);
        return retval;
    }

}
