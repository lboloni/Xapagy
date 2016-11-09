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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.set.ViSet;

/**
 * A collection of functions which classify VI's into different classes
 * important for the semantics of Xapagy. Note, that normally, these are soft
 * classes and they are not simply orthogonal. I am moving them here to keep
 * some level of order.
 * 
 * @author Ladislau Boloni
 * Created on: May 1, 2012
 */
public class ViClassifier {

    /**
     * Vi classes: not exclusive!!!
     * 
     * @author Ladislau Boloni
     * 
     */
    public enum ViClass {
        ACTION, COINCIDENCE, IDENTITY, RELATION, RELATION_MANIPULATION, STICKY,
        UNCLASSIFIED
    }

    /**
     * A VI is an action (currently) if any of its verbs overlap with
     * VM_SUCCESSOR_COMMON_SCENE
     * 
     * @param vi
     * @param agent
     * @return
     */
    private static boolean decideAction(VerbInstance vi, Agent agent) {
        return Hardwired.contains(agent, vi.getVerbs(),
                Hardwired.VM_ACTION_MARKER);
    }

    /**
     * A VI is coincidence VI if it has the THUS verb.
     * 
     * This means that the location of the VI in the link mesh is determined by
     * the lead VI to which it is connected.
     * 
     * @param vi
     * @param agent
     * @return
     */
    private static boolean decideCoincidence(VerbInstance vi, Agent agent) {
        return Hardwired.contains(agent, vi.getVerbs(), Hardwired.VM_THUS);
    }

    /**
     * Decides whether this is an identity relation (not the creation function
     * of it!)
     * 
     * @param vi
     * @param agent
     * @return
     */
    private static boolean decideIdentityRelation(VerbInstance vi, Agent agent) {
        if (!ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
            return false;
        }
        if (!Hardwired.contains(agent, vi.getVerbs(), Hardwired.VR_IDENTITY)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if this vi is a relation manipulation action VI
     * 
     * @param vi
     * @param agent
     * @return
     */
    private static boolean decideRelationManipulation(VerbInstance vi,
            Agent agent) {
        if (Hardwired.contains(agent, vi.getVerbs(),
                Hardwired.VM_CREATE_RELATION)) {
            return true;
        }
        if (Hardwired.contains(agent, vi.getVerbs(),
                Hardwired.VM_REMOVE_RELATION)) {
            return true;
        }
        if (Hardwired.contains(agent, vi.getVerbs(),
                Hardwired.VM_CREATE_ONE_SOURCE_RELATION)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if this is a relation VI (not a creation one!)
     * 
     * @param vi
     * @param agent
     * @return
     */
    private static boolean decideRelationVi(VerbInstance vi, Agent agent) {
        // if it is a relation creation: false
        if (ViClassifier.decideRelationManipulation(vi, agent)) {
            return false;
        }
        // must contain the relation verb
        if (!Hardwired.contains(agent, vi.getVerbs(), Hardwired.VMC_RELATION)) {
            return false;
        }
        // then, it must contain the marker
        return Hardwired.contains(agent, vi.getVerbs(),
                Hardwired.VM_RELATION_MARKER);
    }

    /**
     * Return true for "sticky" verb instances, those which only decay when
     * their instances are decaying:
     * 
     * Currently: all the relations, and the VM_ACTS_LIKE vi
     * 
     * @param vi
     * @return
     */
    public static boolean decideSticky(VerbInstance vi, Agent agent) {
        if (ViClassifier.decideRelationVi(vi, agent)) {
            return true;
        }
        if (Hardwired.contains(agent, vi.getVerbs(), Hardwired.VM_ACTS_LIKE)) {
            return true;
        }
        return false;
    }


    /**
     * Decides if a given VI verifies a certain type of (non-exclusive) class
     * 
     * @param viClass
     * @param vi
     * @param agent
     * @return
     */
    public static boolean decideViClass(ViClass viClass, VerbInstance vi,
            Agent agent) {
        switch (viClass) {
        case RELATION:
            return ViClassifier.decideRelationVi(vi, agent);
        case RELATION_MANIPULATION:
            return ViClassifier.decideRelationManipulation(vi, agent);
        case IDENTITY:
            return ViClassifier.decideIdentityRelation(vi, agent);
        case UNCLASSIFIED:
            return false;
        case ACTION:
            return ViClassifier.decideAction(vi, agent);
        case COINCIDENCE:
            return ViClassifier.decideCoincidence(vi, agent);
        case STICKY:
            return ViClassifier.decideSticky(vi, agent);
        default:
            throw new Error("Don't know how to decide on the class");
        }
    }

    /**
     * Splits a list of VIs by class
     * 
     * @param vis
     * @param agent
     * @return
     */
    public static Map<ViClass, List<VerbInstance>> splitByClass(
            List<VerbInstance> vis, Agent agent) {
        Map<ViClass, List<VerbInstance>> retval = new HashMap<>();
        for (ViClass viClass : ViClass.values()) {
            retval.put(viClass, new ArrayList<VerbInstance>());
        }
        for (VerbInstance vi : vis) {
            boolean anyMatch = false;
            for (ViClass viClass : ViClass.values()) {
                if (ViClassifier.decideViClass(viClass, vi, agent)) {
                    retval.get(viClass).add(vi);
                    anyMatch = true;
                }
            }
            // if there is no match anywhere, put it into UNCLASSIFIED
            if (anyMatch) {
                retval.get(ViClass.UNCLASSIFIED).add(vi);
            }
        }
        return retval;
    }

    /**
     * Splits the ViSet by class
     * 
     * @param vis
     * @param agent
     * @return
     */
    @Deprecated
    public static Map<ViClass, ViSet> splitByClass(ViSet vis, Agent agent) {
        Map<ViClass, ViSet> retval = new HashMap<>();
        for (ViClass viClass : ViClass.values()) {
            retval.put(viClass, new ViSet());
        }
        for (SimpleEntry<VerbInstance, Double> entry : vis.getList()) {
            boolean anyMatch = false;
            VerbInstance vi = entry.getKey();
            double value = entry.getValue();
            for (ViClass viClass : ViClass.values()) {
                if (ViClassifier.decideViClass(viClass, vi, agent)) {
                    retval.get(viClass).changeTo(vi, value);
                    anyMatch = true;
                }
            }
            // if there is no match anywhere, put it into UNCLASSIFIED
            if (anyMatch) {
                retval.get(ViClass.UNCLASSIFIED).changeTo(vi, value);
            }
        }
        return retval;
    }

}
