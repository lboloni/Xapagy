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
package org.xapagy.verbalize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XapiReference.XapiLevel;
import org.xapagy.xapi.reference.XapiReference.XapiPositionInParent;
import org.xapagy.xapi.reference.XrefToInstance;
import org.xapagy.xapi.reference.XrefVi;

/**
 * @author Ladislau Boloni
 * Created on: Nov 23, 2011
 */
public class VerbalMemoryHelper {

    /**
     * Returns the most frequent reference to a certain instance from the same
     * scene or other scenes.
     * 
     * @param agent
     * @param instance
     * @param currentScene
     *            - the scene from which we intend to make the reference
     * @param exclusion
     *            - the text we don't want to use
     * @return the most frequent reference, or null
     */
    public static String getFrequentReference(Agent agent, Instance instance,
            Instance currentScene, Set<String> exclusion) {
        List<String> accesses = new ArrayList<>();
        List<VmInstanceReference> reflist =
                agent.getVerbalMemory().getVmInstanceReferences().get(instance);
        // the reference list can be null, for instance, after a change
        // operation
        if (reflist == null) {
            return null;
        }
        for (VmInstanceReference ref : reflist) {
            // accessing from the same scene
            if (currentScene.equals(instance.getScene())) {
                if (ref.getSceneFrom().equals(currentScene)) {
                    accesses.add(ref.getXapiReference().getText());
                }
            } else {
                // if the original reference was from the same scene from which
                // the given reference
                // had been made, add it. This is good for things like printing
                // a shadow
                if (ref.getSceneFrom().equals(instance.getScene())) {
                    accesses.add(ref.getXapiReference().getText());
                }
            }
        }
        // eliminate duplicates, eliminate exclusion lists, count them
        Map<String, Integer> refcount = new HashMap<>();
        for (String access : accesses) {
            if (exclusion.contains(access)) {
                continue;
            }
            if (refcount.get(access) == null) {
                refcount.put(access, 1);
            } else {
                refcount.put(access, refcount.get(access) + 1);
            }
        }
        // return the most often used
        int maxuse = -1;
        String retval = null;
        for (Entry<String, Integer> entry : refcount.entrySet()) {
            if (entry.getValue() > maxuse) {
                retval = entry.getKey();
            }
        }
        return retval;
    }

    /**
     * Get references for instance - there will be a list
     * 
     * @param instance
     * @return the list,
     */
    public static List<VmInstanceReference> getReferencesToInstance(
            Agent agent, Instance instance) {
        List<VmInstanceReference> references =
                agent.getVerbalMemory().getVmInstanceReferences().get(instance);
        if (references == null) {
            references = new ArrayList<>();
        }
        return Collections.unmodifiableList(references);
    }

    /**
     * If the VI had been created from a Xapi statement, returns the statement.
     * If not, returns null.
     * 
     * @param vi
     * @param agent
     * @return
     */
    public static String getXapiStatementOfVi(VerbInstance vi, Agent agent) {
        if (vi == null) {
            TextUi.println("looking for empty vi in getXapiStatementOfVi? Exit the program!!!\n\n");
            System.exit(1);
        }
        List<VmViReference> reflist =
                agent.getVerbalMemory().getVmViReferences().get(vi);
        if (reflist == null) {
            return null;
        }
        VmViReference ref = reflist.get(0);
        XapiReference xref = ref.getXapiReference();
        String retval = null;
        if (xref.getText() != null) {
            retval = xref.getText().trim();
        } else {
            // if this is an implicit creation action part, it is understandable
            if (xref.getPositionInParent() != XapiPositionInParent.IMPLICIT_CREATION_ACTIONPART
                    && xref.getPositionInParent() != XapiPositionInParent.IMPLICIT_CREATION) {
                TextUi.println("VerbalMemoryHelper.getXapiStatementOfVi: VerbalMemoryHelper: reference had been found, but xref.getText=null");
                // retval =
                // "VerbalMemoryHelper: reference had been found, but xref.getText=null";
                System.exit(1);
            }
        }
        if (retval != null && vi.getViType().equals(ViType.QUOTE)) {
            String quoteString =
                    VerbalMemoryHelper.getXapiStatementOfVi(vi.getQuote(),
                            agent);
            // this happens if the creation part is in the quote, in this case,
            // return null. Well, a more clever approach
            if (quoteString == null) {
                return null;
            }
            retval = retval + " // " + quoteString;
        }
        // TextUi.println(retval);
        return retval;
    }

    /**
     * This is called from reference resolution, and records the way in which
     * the parts have been resolved
     * 
     * @param vi
     * @param xapiVi
     */
    public static void memorizeVerbalResolution(Agent agent, Instance scene,
            VerbInstance vi, XrefVi xapiVi) {
        if (!xapiVi.getXapiLevel().equals(XapiLevel.L0)) {
            throw new Error(
                    "memorizeVerbalResolution should be called on level 0!");
        }
        VerbalMemory vm = agent.getVerbalMemory();
        // record the vi itself
        vm.addReferredAs(agent, vi, xapiVi);
        // record the verb
        vm.addReferredAs(agent, vi.getVerbs(), vi, ViPart.Verb, xapiVi);
        switch (vi.getViType()) {
        case S_V_O: {
            vm.addReferredAs(agent, vi.getSubject(), scene, vi, ViPart.Subject,
                    xapiVi.getSubject());
            vm.addReferredAs(agent, vi.getObject(), scene, vi, ViPart.Object,
                    xapiVi.getObject());
            break;
        }
        case S_V: {
            XrefToInstance refSubject = xapiVi.getSubject();
            vm.addReferredAs(agent, vi.getSubject(), scene, vi, ViPart.Subject,
                    refSubject);
            break;
        }
        case S_ADJ: {
            vm.addReferredAs(agent, vi.getSubject(), scene, vi, ViPart.Subject,
                    xapiVi.getSubject());
            vm.addReferredAs(agent, vi.getAdjective(), vi, ViPart.Adjective,
                    xapiVi.getAdjective());
            break;
        }
        case QUOTE: {
            vm.addReferredAs(agent, vi.getSubject(), scene, vi, ViPart.Subject,
                    xapiVi.getSubject());
            XrefVi xapiViQuote = (XrefVi) xapiVi.getQuote();
            vm.addReferredAs(agent, vi.getQuote(), xapiViQuote);
            break;
        }
        }
    }

}
