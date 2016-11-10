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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.concepts.operations.Incompatibility;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.reference.rrVoCompatibility;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;

/**
 * The common ancestor of SAs which deal with the creation and removal of
 * relations
 * 
 * @author Ladislau Boloni
 * Created on: Jan 5, 2012
 */
public abstract class AbstractSaMvRelation extends AbstractSaMetaVerb {

    private static final long serialVersionUID = -2387222347863304670L;

    /**
     * Extracts the relation from a verb overlay. Returns a pair of the vo which
     * contains the ones which are relations, and an overlay of the rest
     * 
     * @param agent
     * @param vo
     * @return
     */
    public static SimpleEntry<VerbOverlay, VerbOverlay> extractRelation(
            Agent agent, VerbOverlay vo) {
        Verb relation = agent.getVerbDB().getConcept(Hardwired.VMC_RELATION);
        VerbOverlay voRelations = new VerbOverlay(agent);
        VerbOverlay voResidue = new VerbOverlay(agent);
        for (SimpleEntry<Verb, Double> entry : vo.getList()) {
            Verb verb = entry.getKey();
            if (agent.getVerbDB().getOverlap(verb, relation) > 0.5) {
                voRelations.addSpecificEnergy(verb, entry.getValue());
            } else {
                voResidue.addSpecificEnergy(verb, entry.getValue());
            }
        }
        return new SimpleEntry<>(voRelations, voResidue);
    }

    /**
     * @param name
     * @param agent
     * @param activityResources
     * @param viType
     */
    public AbstractSaMvRelation(Agent agent, String name, ViType viType) {
        super(agent, name, viType);
    }

    /**
     * Return the relations which are between the same S and V as verbInstance
     * and compatible with voRelation
     * 
     * @param voRelation
     * @return
     */
    protected List<VerbInstance> getCompatiblesBetweenTheSameInstances(VerbInstance verbInstance,
            VerbOverlay voRelation) {
        List<VerbInstance> candidates =
                RelationHelper.getRelationsBetween(agent,
                        verbInstance.getSubject(), verbInstance.getObject(),
                        true);
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : candidates) {
            if (rrVoCompatibility.areCompatible(vi.getVerbs(), voRelation)) {
                retval.add(vi);
            }
        }
        return retval;
    }

    /**
     * Return the relations which are from the same source and compatible with
     * voRelation
     * 
     * @param voRelation
     * @return
     */
    protected List<VerbInstance> getCompatiblesFromSameSource(VerbInstance verbInstance,
            VerbOverlay voRelation) {
        List<VerbInstance> candidates =
                RelationHelper.getRelationsFrom(agent,
                        verbInstance.getSubject(), true);
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : candidates) {
            if (rrVoCompatibility.areCompatible(vi.getVerbs(), voRelation)) {
                retval.add(vi);
            }
        }
        return retval;
    }

    /**
     * Returns the relations which are between the same S and V as verbInstance
     * and incompatible with voRelation
     * 
     * @param voRelation
     * @return
     */
    protected List<VerbInstance> getIncompatiblesBetweenTheSameInstances(VerbInstance verbInstance,
            VerbOverlay voRelation) {
        List<VerbInstance> retval = new ArrayList<>();
        List<VerbInstance> candidates =
                RelationHelper.getRelationsBetween(agent,
                        verbInstance.getSubject(), verbInstance.getObject(),
                        true);
        for (VerbInstance vi : candidates) {
            VerbOverlay voCandidates =
                    AbstractSaMvRelation.extractRelation(agent, vi.getVerbs())
                            .getKey();
            if (Incompatibility.decideIncompatibility(voCandidates, voRelation)) {
                retval.add(vi);
            }
        }
        return retval;
    }

    /**
     * The relation-creation / delete SA carries the relations within itself.
     * These are the verbs which overlap with vmc_Relation. This function
     * creates the VO of the corresponding relation function: it contains those
     * verbs which overlap with the vmc_Relation plus it adds the relation
     * marker.
     * 
     * @return
     */
    protected VerbOverlay getRelationVo(VerbInstance verbInstance) {
        VerbOverlay voNew =
                AbstractSaMvRelation.extractRelation(agent,
                        verbInstance.getVerbs()).getKey();
        Verb relationMarker =
                agent.getVerbDB().getConcept(Hardwired.VM_RELATION_MARKER);
        voNew.addFullEnergy(relationMarker);
        return voNew;
    }

    /**
     * Removes from the focus the relations listed
     * 
     * @param relationsToRemove
     */
    protected void removeRelations(List<VerbInstance> relationsToRemove) {
        Focus fc = agent.getFocus();
        for (VerbInstance vi : relationsToRemove) {
            EnergyQuantum<VerbInstance> eq =
                    EnergyQuantum.createMult(vi, 0.0, EnergyColors.FOCUS_VI,
                            "AbstractSaMvRelation");
            fc.applyViEnergyQuantum(eq);
        }
    }

}
