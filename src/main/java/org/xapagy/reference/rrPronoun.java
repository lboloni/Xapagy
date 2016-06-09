/*
   This file is part of the Xapagy project
   Created on: Feb 18, 2011
 
   org.xapagy.model.ReferenceCrossScene
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.reference;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.reference.rrException.RreType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * Resolution of references by pronouns
 * 
 * @author Ladislau Boloni
 * 
 */
public class rrPronoun {

    /**
     * Resolve a pronoun type reference. While technically, pronouns are direct
     * references, they are in reality hidden relational references. For
     * instance, the "I" pronoun means
     * "the entity identity-related to the speaker in the other scene".
     * 
     * @param rrc
     * @return
     * @throws rrException
     */
    static SimpleEntry<Instance, rrState> resolvePronoun(rrContext rrc)
            throws rrException {
        // resolving I : something in the scene which is identity linked to me
        // or is myself
        Agent agent = rrc.getAgent();
        if (Hardwired.contains(agent, rrc.getCoDirect(), Hardwired.C_I)) {
            Instance iOrig = rrc.getViInquitParent().getSubject();
            for (Instance inst : rrc.getScene().getSceneMembers()) {
                if (inst == iOrig) {
                    return new SimpleEntry<>(inst,
                            rrState.createNoCompetitionResConf());
                }
                if (IdentityHelper.isIdentityRelated(iOrig, inst, agent)) {
                    return new SimpleEntry<>(inst,
                            rrState.createNoCompetitionResConf());
                }
            }
            // ok, let us debug what happened here:
            PrettyPrint.ppc(iOrig, agent);
            TextUi.printLabeledSeparator("-");
            for (Instance inst : rrc.getScene().getSceneMembers()) {
                PrettyPrint.ppc(inst, agent);
            }
            // Create the exception, which describes what we tried to resolve
            rrException rre = new rrException(RreType.REF_PRONOUN);
            rre.setExplanation("Could not resolve I in scene "
                    + PrettyPrint.ppConcise(rrc.getScene(), agent));
            throw rre;
        }
        return null;
    }

}
