/*
   This file is part of the Xapagy project
   Created on: Jan 19, 2011
 
   org.xapagy.domain.MetaVerbHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
