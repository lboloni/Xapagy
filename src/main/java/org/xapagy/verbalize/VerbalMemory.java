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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.xapi.reference.XapiReference;
import org.xapagy.xapi.reference.XrefToCo;
import org.xapagy.xapi.reference.XrefToInstance;

/**
 * 
 * It keeps the verbal memory of the agent, matches instances and verb instances
 * to verbal expressions - it might have, in the future a way to remember also
 * causal expressions
 * 
 * @author Ladislau Boloni
 * Created on: Nov 23, 2011
 */
public class VerbalMemory implements Serializable {

    private static final long serialVersionUID = -6341961689017089799L;

    private List<VmCoReference> vmCoReferences = new ArrayList<>();

    private Map<Instance, List<VmInstanceReference>> vmInstanceReferences =
            new HashMap<>();

    private Map<VerbInstance, List<VmViReference>> vmViReferences =
            new HashMap<>();

    private List<VmVoReference> vmVoReferences = new ArrayList<>();

    public VerbalMemory() {
    }

    /**
     * Adds a reference to an co (usually from an adjective)
     * 
     * @param co
     * @param vi
     * @param viPart
     * @param referenceText
     */
    void addReferredAs(Agent agent, ConceptOverlay co, VerbInstance vi,
            ViPart viPart, XrefToCo xapiReference) {
        VmCoReference vmcor =
                new VmCoReference(agent, co, vi, viPart, xapiReference);
        vmCoReferences.add(vmcor);
    }

    /**
     * Adds a reference to an instance
     * 
     * @param instance
     * @param sceneFrom
     * @param vi
     * @param viPart
     * @param referenceText
     */
    void addReferredAs(Agent agent, Instance instance, Instance sceneFrom,
            VerbInstance vi, ViPart viPart, XrefToInstance xapiReference) {
        VmInstanceReference vmir =
                new VmInstanceReference(agent, instance, sceneFrom, vi, viPart,
                        xapiReference);
        List<VmInstanceReference> list = vmInstanceReferences.get(instance);
        if (list == null) {
            list = new ArrayList<>();
            vmInstanceReferences.put(instance, list);
        }
        list.add(vmir);
    }

    /**
     * Adds a reference to the vi
     * 
     * @param vi
     * @param referenceText
     */
    void
            addReferredAs(Agent agent, VerbInstance vi,
                    XapiReference xapiReference) {
        VmViReference vmvir = new VmViReference(agent, vi, xapiReference);
        List<VmViReference> list = vmViReferences.get(vi);
        if (list == null) {
            list = new ArrayList<>();
            vmViReferences.put(vi, list);
        }
        list.add(vmvir);
    }

    /**
     * Adds a reference to a vo (usually from the verb of a sentence)
     * 
     * @param vo
     * @param vi
     * @param viPart
     * @param referenceText
     */
    void addReferredAs(Agent agent, VerbOverlay vo, VerbInstance vi,
            ViPart viPart, XapiReference xapiReference) {
        VmVoReference vmvor =
                new VmVoReference(agent, vo, vi, viPart, xapiReference);
        vmVoReferences.add(vmvor);
    }

    /**
     * @return the vmCoReferences
     */
    public List<VmCoReference> getVmCoReferences() {
        return vmCoReferences;
    }

    /**
     * @return the vmInstanceReferences
     */
    public Map<Instance, List<VmInstanceReference>> getVmInstanceReferences() {
        return vmInstanceReferences;
    }

    /**
     * @return the vmViReferences
     */
    public Map<VerbInstance, List<VmViReference>> getVmViReferences() {
        return vmViReferences;
    }

    /**
     * @return the vmVoReferences
     */
    public List<VmVoReference> getVmVoReferences() {
        return vmVoReferences;
    }

}
