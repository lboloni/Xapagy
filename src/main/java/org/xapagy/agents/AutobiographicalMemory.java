/*
   This file is part of the Xapagy project
   Created on: Feb 21, 2011
 
   org.xapagy.model.CrossLinks
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.agents;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.EnergySet;

/**
 * 
 * This class contains the memory of the agent: instances, verb instances as
 * well as data structures between them
 * 
 * @author Ladislau Boloni
 * 
 */
public class AutobiographicalMemory implements Serializable {

    private static final long serialVersionUID = -5419683121441452234L;
    private Agent agent;
    private Map<Concept, Set<Instance>> conceptsToInstances = new HashMap<>();
    private Map<String, Instance> instances = new HashMap<>();
    private Map<Instance, Set<Concept>> instancesToConcepts = new HashMap<>();
    /**
     * The energies associated with the instances
     */
    private EnergySet<Instance> ise;
    private Map<String, VerbInstance> verbInstances = new HashMap<>();
    private Map<VerbInstance, Set<Verb>> visToVerbs = new HashMap<>();
    private Map<Verb, Set<VerbInstance>> verbsToVis = new HashMap<>();
    /**
     * The energies associated with the VIs
     */
    private EnergySet<VerbInstance> vise;

    /**
     * The n
     * 
     * @param agent
     */
    public AutobiographicalMemory(Agent agent) {
        this.agent = agent;
        ise = new EnergySet<>(agent);
        vise = new EnergySet<>(agent);
    }

    /**
     * Adds energy (normally EnergyColor.AM) to an instance in the
     * autobiographical memory. If the instance was not previously in the
     * instances map, it adds it.
     * 
     * At every update, it also updates the concepts conceptsToInstance and
     * instancesToConcepts maps.
     * 
     * 
     * @param eq
     */
    public void applyInstanceEnergyQuantum(EnergyQuantum<Instance> eq) {
        //EnergyQuantum<Instance> eq =
        //        new EnergyQuantum<>(instance, null, value,
        //                EnergyQuantum.MULTIPLIER_NEUTRAL,
        //                EnergyQuantum.TIMESLICE_ONE,
        //                EnergyQuantum.STRENGTH_FULL, "General", EnergyColor.AM);
        ise.applyEnergyQuantum(eq);
        // FIXME: do this only for the addition, not for forgetting
        
        Instance instance = eq.getFocusComponent();
        instances.put(instance.getIdentifier(), instance);
        // update the instanceToConcept and conceptsToInstances maps
        // the instancesToConcepts is there only to make sure that we don't
        // need to update the other one unnecessarily
        Set<Concept> oldconcepts = instancesToConcepts.get(instance);
        Set<Concept> newconcepts = instance.getConcepts().getAsSet();
        instancesToConcepts.put(instance, newconcepts);
        for (Concept concept : newconcepts) {
            if (oldconcepts == null || !oldconcepts.contains(concept)) {
                Set<Instance> instanceSet = conceptsToInstances.get(concept);
                if (instanceSet == null) {
                    instanceSet = new HashSet<>();
                    conceptsToInstances.put(concept, instanceSet);
                }
                instanceSet.add(instance);
            }
        }
    }

    /**
     * 
     * Adds energy (normally EnergyColor.AM) to a VI in the autobiographical
     * memory. If the VI was not previously in the verbInstances map, it adds
     * it.
     * 
     * At every update, it also updates the concepts conceptsToInstance and
     * instancesToConcepts maps.
     * 
     * @param vi
     *            the verb instance
     * @param value
     *            the added energy
     * @param ec
     *            TODO
     * 
     */
    public void applyViEnergyQuantum(EnergyQuantum<VerbInstance> eq) {
        //EnergyQuantum<VerbInstance> eq =
        //        new EnergyQuantum<>(vi, null, value,
        //                EnergyQuantum.MULTIPLIER_NEUTRAL,
        //                EnergyQuantum.TIMESLICE_ONE,
        //                EnergyQuantum.STRENGTH_FULL, "General", EnergyColor.AM);
        vise.applyEnergyQuantum(eq);
        // FIXME: do this only for addition of AM, not others
        VerbInstance vi = eq.getFocusComponent();
        verbInstances.put(vi.getIdentifier(), vi);
        // update the visToVerbs and verbsToVis maps
        // the visToVerbs is there only to make sure that we don't
        // need to update the other one unnecessarily
        Set<Verb> oldverbs = visToVerbs.get(vi);
        Set<Verb> newverbs = vi.getVerbs().getAsSet();
        visToVerbs.put(vi, newverbs);
        for (Verb verb : newverbs) {
            if (oldverbs == null || !oldverbs.contains(verb)) {
                Set<VerbInstance> viset = verbsToVis.get(verb);
                if (viset == null) {
                    viset = new HashSet<>();
                    verbsToVis.put(verb, viset);
                }
                viset.add(vi);
            }
        }
    }

    /**
     * Returns the energy value of an instance
     * 
     * @param instance
     * @param ec
     *            the energy color
     * @return
     */
    public double getEnergy(Instance instance, String ec) {
        return ise.valueEnergy(instance, ec);
    }

    /**
     * Returns the energy value of an instance
     * 
     * @param vi
     * @param ec
     *            the energy color
     * @return
     */
    public double getEnergy(VerbInstance vi, String ec) {
        return vise.valueEnergy(vi, ec);
    }

    /**
     * Returns an instance by its identifier
     * 
     * @param identifier
     * @return
     */
    public Instance getInstance(String identifier) {
        return instances.get(identifier);
    }

    /**
     * Returns the set of instance identifiers as an unmodifiable set
     * 
     * @return
     */
    public Set<String> getInstanceIdentifiers() {
        return Collections.unmodifiableSet(instances.keySet());
    }

    /**
     * Returns the set of all instances which have or overlap with any of the
     * concepts in the concept overlay
     * 
     * @param agent
     * @param co
     * @return
     */
    public Set<Instance> getInstancesOverlappingWithCo(ConceptOverlay co) {
        Set<Instance> retval = new HashSet<>();
        Set<Concept> overlapping =
                agent.getConceptDB().getAllOverlappingConcepts(co);
        for (Concept c : overlapping) {
            Set<Instance> set = conceptsToInstances.get(c);
            if (set != null) {
                retval.addAll(set);
            }
        }
        return retval;
    }

    /**
     * Returns the salience of an instance
     * 
     * @param instance
     * @param ec
     *            the energy color
     * @return
     */
    public double getSalience(Instance instance, String ec) {
        double param = agent.getEnergyColors().getEnergyToSalience(ec);
        return EnergyColors.convert(ise.valueEnergy(instance, ec), param);
    }

    /**
     * Returns the salience of a verb instance
     * 
     * @param vi
     * @param ec
     *            the energy color
     * @return
     */
    public double getSalience(VerbInstance vi, String ec) {
        double param = agent.getEnergyColors().getEnergyToSalience(ec);
        return EnergyColors.convert(vise.valueEnergy(vi, ec), param);
    }

    /**
     * Returns a verb instance by its identifier
     * 
     * @param identifier
     * @return
     */
    public VerbInstance getVerbInstance(String identifier) {
        return verbInstances.get(identifier);
    }

    /**
     * Returns the set of instance identifiers as an unmodifiable set
     */
    public Set<String> getViIdentifiers() {
        return Collections.unmodifiableSet(verbInstances.keySet());
    }

    /**
     * Returns the set of all verb instances which overlaps with the verbs in
     * the verb overlay
     * 
     * @param vo
     * @return
     */
    public Set<VerbInstance> getVisOverlappingWithVo(VerbOverlay vo) {
        Set<VerbInstance> retval = new HashSet<>();
        Set<Verb> overlapping = agent.getVerbDB().getAllOverlappingConcepts(vo);
        for (Verb verb : overlapping) {
            Set<VerbInstance> set = verbsToVis.get(verb);
            if (set != null) {
                retval.addAll(set);
            }
        }
        return retval;
    }

    /**
     * Returns a list of the energy quantums affecting the focus of fi for
     * energy ec
     * 
     * @param fi
     * @param si
     * @param ec
     * @return
     */
    public List<EnergyQuantum<Instance>> getEnergyQuantums(Instance fi,
            String ec) {
        return ise.getEnergyQuantums(fi, ec);
    }

    /**
     * Returns a list of the energy quantums affecting the focus of fvi in
     * energy color ec. Used for the explanation
     * 
     * @param fi
     * @param si
     * @param ec
     * @return
     */
    public List<EnergyQuantum<VerbInstance>> getEnergyQuantums(
            VerbInstance fvi, String ec) {
        return vise.getEnergyQuantums(fvi, ec);
    }

}
