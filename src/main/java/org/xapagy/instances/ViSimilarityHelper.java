/*
   This file is part of the Xapagy project
   Created on: Oct 2, 2012
 
   org.xapagy.instances.ViSimilarity
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.instances;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.operations.Coverage;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * @author Ladislau Boloni
 * 
 */
public class ViSimilarityHelper {

    /**
     * Returns true if a VerbInstanceTemplate is compatible with the other.
     * 
     * Used in DaHlsSupport - if it returns true, they will not create two
     * different HlsSupports
     * 
     * Used in OldSummarizationHelper.decideSummaryAlreadyExists - if it results
     * true, do not create a new summary
     * 
     * With onTemplate = false, also used in SaExpectedViShadow, to map an HLS
     * to a newly created Vi's shadow
     * 
     * Current version: checks the compatibility of the verbs, verifies if the
     * instance parts are the same (they must be either both resolved to the
     * same component, or both null)
     * 
     * 
     * 
     * @param hls1
     * @param hls2
     * @param agent
     * @return
     */
    public static boolean decideSimilarityVi(VerbInstance first,
            VerbInstance other, Agent agent, boolean onTemplate) {
        // if not the same type:
        if (first.getViType() != other.getViType()) {
            return false;
        }
        // if the verb overlay is not compatible ... not the same
        if (!Coverage.decideSimilarity(first.getVerbs(), other.getVerbs())) {
            return false;
        }
        // check all the instance parts
        for (ViPart part : ViStructureHelper.getAllowedInstanceParts(first
                .getViType())) {
            Object part1 = first.getResolvedParts().get(part);
            Object part2 = other.getResolvedParts().get(part);
            if (part1 != part2) {
                return false;
            }
        }
        // check the adjective
        if (first.getViType() == ViType.S_ADJ) {
            ConceptOverlay co1 =
                    (ConceptOverlay) first.getResolvedParts().get(
                            ViPart.Adjective);
            ConceptOverlay co2 =
                    (ConceptOverlay) other.getResolvedParts().get(
                            ViPart.Adjective);
            if (!Coverage.decideSimilarity(co1, co2)) {
                return false;
            }
        }
        // the quote of a template mode must be also a template mode?
        if (first.getViType() == ViType.QUOTE) {
            VerbInstance vi1 =
                    (VerbInstance) first.getResolvedParts().get(ViPart.Quote);
            VerbInstance vi2 =
                    (VerbInstance) other.getResolvedParts().get(ViPart.Quote);
            if (vi1 == null && vi2 == null && onTemplate) {
                // this is ok on a template???
            } else {
                if (vi1 == null || vi2 == null) {
                    return false;
                }
                if (!ViSimilarityHelper.decideSimilarityVi(vi1, vi2, agent,
                        onTemplate)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if two VIs are compatible. Normally, only compatible VIs can
     * be in each other's shadows.
     * 
     * @param agent
     * @param fvi
     * @param svi
     * @return
     */
    public static boolean isCompatible(Agent agent, VerbInstance fvi,
            VerbInstance svi) {
        // if they are the same vi
        if (fvi == svi) {
            return false;
        }
        if (!svi.getViType().equals(fvi.getViType())) {
            return false;
        }
        for (ViPart part : ViStructureHelper.getAllowedInstanceParts(fvi
                .getViType())) {
            boolean vIsScene = ((Instance) svi.getPart(part)).isScene();
            boolean fvIsScene = ((Instance) fvi.getPart(part)).isScene();
            if (vIsScene != fvIsScene) {
                return false;
            }
        }
        // if one of them is a relation and the other one is not
        if (ViClassifier.decideViClass(ViClass.RELATION, fvi, agent) != ViClassifier
                .decideViClass(ViClass.RELATION, svi, agent)) {
            return false;
        }
        // if one of them is a relation creation and the other one is not
        if (ViClassifier.decideViClass(ViClass.RELATION_MANIPULATION, fvi,
                agent) != ViClassifier.decideViClass(
                ViClass.RELATION_MANIPULATION, svi, agent)) {
            return false;
        }
        // if one of them is an action and the other one is not
        if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent) != ViClassifier
                .decideViClass(ViClass.ACTION, svi, agent)) {
            return false;
        }
        return true;
    }

}
