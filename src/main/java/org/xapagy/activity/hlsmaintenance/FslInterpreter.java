/*
   This file is part of the Xapagy project
   Created on: Jun 27, 2013
 
   org.xapagy.recall.FslInterpreter_ViLinkedBased
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.InstanceSet;

/**
 * 
 * Fsl interpreter: creates the interpretation only based on the ViLinked
 * component
 * 
 * @author Ladislau Boloni
 * 
 */
public class FslInterpreter {

    /**
     * Explodes an FSL into a number of FSLIs and assigns their weight
     * 
     * New version, based on the explosion of the ViLinked which should be
     * cacheable
     * 
     * @param fsl
     * @param agent
     * @return
     */
    public static
            List<FslInterpretation>
            createInterpretations(
                    FocusShadowLinked fsl,
                    Agent agent,
                    Map<VerbInstance, Map<VerbInstance, Double>> cacheViInterpretations,
                    Map<Instance, FslAlternative> cacheInstanceInterpretations) {
        //
        // Because for this model the interpretation only depends on the
        // ViLinked keep a cache for it
        //
        VerbInstance viLinked = fsl.getViLinked();
        Map<VerbInstance, Double> interpretations =
                cacheViInterpretations.get(viLinked);
        if (interpretations == null) {
            interpretations =
                    FslInterpreter.createInterpretationsOfViLinked(viLinked,
                            agent, cacheInstanceInterpretations);
            cacheViInterpretations.put(viLinked, interpretations);
        }
        List<FslInterpretation> retval = new ArrayList<>();
        for (VerbInstance viInterpretation : interpretations.keySet()) {
            double value = interpretations.get(viInterpretation);
            String identifier =
                    agent.getIdentifierGenerator()
                            .getFslInterpretationIdentifier();
            FslInterpretation fsli =
                    new FslInterpretation(identifier, fsl, viInterpretation,
                            value);
            retval.add(fsli);
        }
        return retval;
    }

    /**
     * Creates a set of possible interpretations of a ViLinked object. Returns
     * them in a hashmap with their weights
     * 
     * @param viLinked
     * @param agent
     * @param cacheInstanceInterpretations
     * @return
     */
    public static Map<VerbInstance, Double> createInterpretationsOfViLinked(
            VerbInstance viLinked, Agent agent,
            Map<Instance, FslAlternative> cacheInstanceInterpretations) {
        ViType viType = viLinked.getViType();
        VerbOverlay verbs = viLinked.getVerbs();
        Map<VerbInstance, Double> retval = new HashMap<>();
        VerbInstance viFsliTemplate =
                VerbInstance.createViTemplate(viType, verbs);
        retval.put(viFsliTemplate, 1.0);
        // resolve the instance parts one by one
        for (ViPart part : ViStructureHelper.getAllowedInstanceParts(viType)) {
            Map<VerbInstance, Double> old = retval;
            retval = new HashMap<>();
            Instance instance = (Instance) viLinked.getPart(part);
            FslAlternative partInterpretations = null;
                partInterpretations =
                        FslInterpreter.getInstanceInterpretations(instance,
                                agent, cacheInstanceInterpretations);
            // add alternatives for the possible instance interpretations
            for (VerbInstance prev : old.keySet()) {
                double value = old.get(prev);
                FslInterpreter.explodeOnInstance(agent, retval, prev, value,
                        part, partInterpretations);
            }
        }
        // resolve the adjective parts by copying it
        if (viType.equals(ViType.S_ADJ)) {
            Map<VerbInstance, Double> old = retval;
            retval = new HashMap<>();
            for (VerbInstance vi : old.keySet()) {
                double value = old.get(vi);
                VerbInstance viResolved =
                        VerbInstance.createViTemplateFromModel(vi);
                viResolved.setResolvedPart(ViPart.Adjective,
                        viLinked.getPart(ViPart.Adjective));
                retval.put(viResolved, value);
            }
        }
        // Resolve the quote component
        if (viType.equals(ViType.QUOTE)) {
            Map<VerbInstance, Double> old = retval;
            retval = new HashMap<>();
            Map<VerbInstance, Double> quoteInterpretations =
                    FslInterpreter.createInterpretationsOfViLinked(
                            viLinked.getQuote(), agent,
                            cacheInstanceInterpretations);
            for (VerbInstance vi : old.keySet()) {
                for (VerbInstance viQuote : quoteInterpretations.keySet()) {
                    VerbInstance newTemplate =
                            VerbInstance.createViTemplateFromModel(vi);
                    newTemplate.setResolvedPart(ViPart.Quote, viQuote);
                    double value =
                            old.get(vi) * quoteInterpretations.get(viQuote);
                    retval.put(newTemplate, value);
                }
            }
        }
        return retval;
    }

    /**
     * Takes a partially done VI interpretation, and explodes it based on a
     * certain part and its interpretations
     * 
     * @param agent
     * @param collector
     *            a collection which will hold the exploded new VI templates
     * @param viOriginal
     *            the VI template, which is currently being built - this will be
     *            exploded based on the interpretations
     * @param viOriginalWeight
     *            the current weight carried by the current template. This will
     *            be split among the exploded instances
     * @param part
     *            - the part on which we are exploding upon - should be an
     *            instance in the ViLinked
     * @param partInterpretations
     *            - the already made interpretations of the instance in the form
     *            of a pair of InstanceSet and CO for creation
     */
    private static void explodeOnInstance(Agent agent,
            Map<VerbInstance, Double> collector, VerbInstance viOriginal,
            double viOriginalWeight, ViPart part,
            FslAlternative partInterpretations) {
        double strongestExisting = -1.0;
        for (SimpleEntry<Instance, Double> entry : partInterpretations
                .getInstanceSet().getList()) {
            Instance resolvedInstance = entry.getKey();
            if (viOriginal.hasInstance(resolvedInstance)) {
                continue;
            }
            double interpretationWeight = entry.getValue();
            if (interpretationWeight <= 0) {
                continue;
            }
            VerbInstance viMoreResolved =
                    VerbInstance.createViTemplateFromModel(viOriginal);
            viMoreResolved.setResolvedPart(part, resolvedInstance);
            double weight = viOriginalWeight * interpretationWeight;
            collector.put(viMoreResolved, weight);
            strongestExisting =
                    Math.max(strongestExisting, interpretationWeight);
        }
        // do not create a new scene or speaker in the quote
        if (viOriginal.getViType() == ViType.QUOTE) {
            return;
        }
        // adds the alternative where the instance is resolved to the instance
        // which is new
        double newSupportPercent =
                1.0 - partInterpretations.getInstanceSet().getSum();
        if (newSupportPercent > 0.0) {
            ConceptOverlay coNew = partInterpretations.getCo();
            VerbInstance viWithNewPart =
                    VerbInstance.createViTemplateFromModel(viOriginal);
            viWithNewPart.setNewPart(part, coNew);
            double weight = viOriginalWeight * newSupportPercent;
            collector.put(viWithNewPart, weight);
        }
    }

    /**
     * Generate all the FslInterpretation objects for the list of FSLs specified
     * 
     * @param agent
     * @param fsls
     */
    public static List<FslInterpretation> generateInterpretations(Agent agent,
            List<FocusShadowLinked> fsls) {
        List<FslInterpretation> listFsli = new ArrayList<>();
        // the cache will keep the repetitive instances
        Map<VerbInstance, Map<VerbInstance, Double>> cacheViInterpretations =
                new HashMap<>();
        Map<Instance, FslAlternative> cacheInstanceInterpretations =
                new HashMap<>();
        for (FocusShadowLinked relative : fsls) {
            List<FslInterpretation> set =
                    FslInterpreter.createInterpretations(relative, agent,
                            cacheViInterpretations,
                            cacheInstanceInterpretations);
            listFsli.addAll(set);
        }
        return listFsli;
    }

    /**
     * Interprets an instance coming from a ViLinked into focus instances.
     * 
     * Returns a pair of an instance set and a CO.
     * 
     * The participants of the instance set represent possible, existing
     * interpretations. Their weight represent the probability that the
     * interpretation is correct. These weights might not add up to 1.0 - the
     * reminder is interpreted as the probability that the correct
     * interpretation is a new instance, which does not exist yet in the focus.
     * 
     * This instance would need to be created with the attributes defined in the
     * CO.
     * 
     * @param instance
     * @param agent
     * @param cacheInstanceInterpretations
     * @return
     */
    public static FslAlternative getInstanceInterpretations(Instance instance,
            Agent agent,
            Map<Instance, FslAlternative> cacheInstanceInterpretations) {
        FslAlternative retval = cacheInstanceInterpretations.get(instance);
        if (retval != null) {
            return retval;
        }
        InstanceSet is = new InstanceSet();
        // the reverse shadows of the instance
        InstanceSet reverseShadow =
                agent.getShadows().getReverseShadow(instance,
                        EnergyColors.SHI_GENERIC);
        // the reverse shadows of the scene -
        InstanceSet reverseShadowScene =
                agent.getShadows().getReverseShadow(instance.getScene(),
                        EnergyColors.SHI_GENERIC);

        double sum = reverseShadow.getSum();
        double sumScene = reverseShadowScene.getSum();
        //
        // Decision with regards to whether we believe the interpretations
        // or we propose the creation of a new mapping. If the reverse shadow
        // is at least the reverse shadow of the scene, we do not propose the
        // creation of a new one... this must be calibrated
        //
        if (sum > sumScene / 2.0) {
            // the returned
            double mergeRatio = 1.0 / sum;
            is.mergeIn(reverseShadow, mergeRatio);
        } else {
            // the returned is empty
        }
        ConceptOverlay co = instance.getConcepts();
        retval = new FslAlternative(is, co);
        cacheInstanceInterpretations.put(instance, retval);
        return retval;
    }


}
