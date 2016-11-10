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
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.XapiParserException;

/**
 * @author Ladislau Boloni
 * Created on: Jul 28, 2011
 */
public class rrRelational {

    /**
     * 
     * Resolves the relational reference, backwards
     * 
     * FIXME: This does not return a weighted collection, it makes the decisions
     * here. This will need to be changed in such a way that it returns a
     * weighted collection...
     * 
     * FIXME: The reference for the identity based automatic creation is too
     * subtle, one should not do this stuff, it makes other things so much
     * harder.
     * 
     * @param rrc
     *            - the reference and the context in which it needs to be solved
     * @return a pair of the resolved instance and the resolution state
     * @throws XapiParserException 
     */
    public static SimpleEntry<Instance, rrState> resolveReferenceRelational(
            rrContext rrc) throws rrException {
        Instance localScene = rrc.getScene();
        Agent agent = rrc.getAgent();

        int voCount = rrc.getRelationChain().size() - 1;
        // if the last one is an "in-scene" then resolve the last one to
        // the scene and then do a recursive call in that scene with it.
        if (Hardwired.contains(agent, rrc.getRelationChain().get(voCount),
                Hardwired.VR_IN_SCENE)) {
            // Returns the list of candidates scored with their values
            List<rrCandidate> candidates = new ArrayList<>();
            rrCollectCandidates.collectCandidatesListOfScenes(candidates,
                    agent, rrc);
            // XrefDirect lastInstance =
            // (XrefDirect) reference.getInstances().get(voCount + 1);
            ConceptOverlay co = rrc.getRelationCOs().get(voCount + 1);
            rrContext rrcStep =
                    rrContext.createRelationalReferenceStep(rrc, co,
                            rrc.getScene());
            localScene =
                    ReferenceResolution.select(rrcStep, candidates, false)
                            .getKey();
            voCount--;
        }
        boolean allowNoReference = rrc.getScene() != localScene;

        // ok, setting the resolution confidence here is problematic, because we
        // need to compose the
        // resolution confidence of the individual components!!!
        ConceptOverlay co = rrc.getRelationCOs().get(voCount + 1);
        rrContext rrc2 =
                rrContext.createDirectReference(agent, co, rrc.getVerbsInVi(),
                        rrc.getPartInVi(), localScene, rrc.getViInquitParent());
        SimpleEntry<Instance, rrState> entry =
                ReferenceResolution.resolveReference(rrc2);
        Instance currentRoot = null;
        rrState rc = null;
        if (entry == null) {
            TextUi.println("Unresolved relational reference. It is also possible "
                    + "that this is an example of the old identity based reference "
                    + "creation, not supported any more.");
            return null;
        } else {
            currentRoot = entry.getKey();
            rc = entry.getValue();
        }
        for (int i = voCount; i >= 0; i--) {
            ConceptOverlay co2 = rrc.getRelationCOs().get(i);
            rrContext rrcStep =
                    rrContext.createRelationalReferenceStep(rrc, co2,
                            localScene);
            List<rrCandidate> candidates =
                    rrCollectCandidates
                            .collectCandidatesRelational(agent, currentRoot,
                                    rrc.getRelationChain().get(i), rrcStep);
            // if we are in a different scene we can allow no reference
            // and create locally
            allowNoReference = i == 0 && rrc.getScene() != localScene;
            entry =
                    ReferenceResolution.select(rrcStep, candidates,
                            allowNoReference);
            if (entry == null) {
                // currentRoot =
                // rrRelational.resolveIdentityBasedCreation(agent,
                // currentInstance.getCo(), verbInstance, part,
                // scene, localScene);
                TextUi.println("Unresolved relational reference. It is also possible "
                        + "that this is an example of the old identity based reference "
                        + "creation, not supported any more.");
                return null;
            } else {
                currentRoot = entry.getKey();
                rc = rc.composeConfidence(entry.getValue());
            }
        }
        return new SimpleEntry<>(currentRoot, rc);
    }
}
