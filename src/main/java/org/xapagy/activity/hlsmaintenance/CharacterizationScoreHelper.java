/*
   This file is part of the Xapagy project
   Created on: Jul 9, 2011
 
   org.xapagy.recall.CharacterizationScore
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Overlay;
import org.xapagy.instances.Instance;

/**
 * The functions which characterize the value of the characterizations
 * 
 * @author Ladislau Boloni
 * 
 */
public class CharacterizationScoreHelper {

    /**
     * 
     * A metric which tries to avoid adding the same concepts to the concept
     * overlay. It performs a simulated adding. It is unclear if there is
     * anything to gain by making this gradual.
     * 
     * <ul>
     * <li>If all the components of co are already present in co, return 1.0
     * <li>If not, return 0
     * </ul>
     * 
     * 
     * @param agent
     * @param coCurrent
     * @param coAdded
     * @return
     */
    public static double getAlreadyPresentScore(Agent agent,
            ConceptOverlay coCurrent, ConceptOverlay coAdded) {
        ConceptOverlay coSimulated = new ConceptOverlay(agent);
        coSimulated.addOverlay(coCurrent);
        coSimulated.addOverlay(coAdded);
        if (coSimulated.getTotalEnergy() / coCurrent.getTotalEnergy() == 1.0) {
            return 1.0;
        }
        return 0.0;
    }

    /**
     * Calculates whether the concepts add a new "essence"
     * 
     * <ul>
     * <li>If concepts adds some kind of new essence, return 1.0
     * <li>If no new essence was present, return 2.0
     * <li>Otherwise return 0.0
     * </ul>
     * 
     * @param agent
     * @param concepts
     * @param co
     * @return
     */
    public static double getEssenceScore(Agent agent, ConceptOverlay coCurrent,
            ConceptOverlay coAdded) {
        boolean existingEssence = false;
        boolean newEssence = false;
        // check the existing essence
        for (SimpleEntry<Concept, Double> entry : coCurrent.getList()) {
            if (entry.getValue() < 0.8) {
                continue;
            }
            double area = agent.getConceptDB().getArea(entry.getKey());
            if (area >= 0.8 && area < 1.2) {
                existingEssence = true;
                break;
            }
        }
        // check the new essence
        for (SimpleEntry<Concept, Double> entry : coAdded.getList()) {
            if (entry.getValue() < 0.8) {
                continue;
            }
            double area = agent.getConceptDB().getArea(entry.getKey());
            if (area >= 0.8 && area < 1.2) {
                newEssence = true;
                break;
            }
        }
        // judget based on these
        if (!newEssence) {
            return 0;
        }
        if (existingEssence) {
            return 1;
        }
        return 2;
    }

    /**
     * @param agent
     * @param coCurrent
     * @param coAdded
     * @param coTarget
     * @return
     */
    public static double getTargetDifference(Agent agent,
            ConceptOverlay coCurrent, ConceptOverlay coAdded,
            ConceptOverlay coTarget) {
        ConceptOverlay coSimulated = new ConceptOverlay(agent);
        coSimulated.addOverlay(coCurrent);
        coSimulated.addOverlayImpacted(coAdded);
        double diffOld = Overlay.scrape(coTarget, coCurrent).getTotalEnergy();
        double diffNew = Overlay.scrape(coTarget, coSimulated).getTotalEnergy();
        return diffOld - diffNew;
    }

    /**
     * Returns 1 is the added concept overlay is unique in the scene, otherwise
     * 0
     * 
     * @param agent
     * @param instance
     * @param co
     * @return
     */
    public static double getUniquenessScore(Agent agent, Instance instance,
            ConceptOverlay coAdded) {
        Instance scene = agent.getFocus().getCurrentScene();
        for (Instance inst : scene.getSceneMembers()) {
            if (inst == instance) {
                continue;
            }
            if (CharacterizationScoreHelper.getAlreadyPresentScore(agent,
                    inst.getConcepts(), coAdded) < -50) {
                return 0;
            }
        }
        return 1;
    }
}
