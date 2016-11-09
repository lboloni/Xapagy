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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.parameters.Parameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;

/**
 * Different scenes can be parameterized differently, for instance, one is
 * fantasizing in one scene, but not in the other. The parameters (energies and
 * weights of these behaviors are collected in this class). Every instance of
 * type Scene will have a component of this class.
 * 
 * @author Ladislau Boloni
 * Created on: May 27, 2013
 */
public class SceneParameters implements Serializable {

    private static final long serialVersionUID = -2482805390697193202L;

    private Agent agent;
    /**
     * Characterization weight
     */
    private double characterizationWeightEssence = 1.0;

    private double characterizationWeightTargetDifference = 1.0;

    private double characterizationWeightUniqueness = 1.0;
    /**
     * Choice type preferences
     */
    private Map<ChoiceType, Double> choiceTypePreferences = null;
    /**
     * Continuation energy - only relevant for scenes
     */
    private double energyContinuation = 0;
    /**
     * Interstitial energy - only relevant for scenes
     */
    private double energyInterstitial = 0;
    /**
     * The collection of VIs from the shadow which had been used to instantiate
     * a new Continuation type choices. These will be inhibited from being used
     * again in this scene.
     */
    private ViSet firedShadowVis;

    /**
     * Link structure weights - to be used by DaShmLinkStructure
     */
    private Map<String, Double> linkStructureWeight = new HashMap<>();

    public SceneParameters(Agent agent) {
        this.agent = agent;
        Parameters p = agent.getParameters();
        firedShadowVis = new ViSet();
        //
        // Initialize the choice type preferences from the parameter
        //
        choiceTypePreferences = new HashMap<>();
        for (ChoiceType ct : ChoiceType.values()) {
            choiceTypePreferences.put(ct, p.get("A_HLSM",
                    "G_CHOICES",
                    "N_CHOICE_TYPE_PREFERENCES" + ct));
        }
        //
        // Initialize the story consistency weight from the parameters
        //
        linkStructureWeight = new HashMap<>();
        for(String linkType: agent.getLinks().getLinkTypeNames()) {
            linkStructureWeight.put(linkType, p.get("A_SHM",
                    "G_LINK_STRUCTURE", "N_WEIGHT-"
                            + linkType));            
        }
    }

    /**
     * Adds a set of VIs to the firedShadow ones
     * 
     */
    public void addFiredShadowVis(ViSet addon) {
        firedShadowVis.mergeIn(addon, 1.0);
    }

    /**
     * @return the characterizationWeightEssence
     */
    public double getCharacterizationWeightEssence() {
        return characterizationWeightEssence;
    }

    /**
     * @return the characterizationWeightTargetDifference
     */
    public double getCharacterizationWeightTargetDifference() {
        return characterizationWeightTargetDifference;
    }

    /**
     * @return the characterizationWeightUniqueness
     */
    public double getCharacterizationWeightUniqueness() {
        return characterizationWeightUniqueness;
    }

    /**
     * @return the choiceTypePreferences
     */
    public Map<ChoiceType, Double> getChoiceTypePreferences() {
        return choiceTypePreferences;
    }

    /**
     * @return the energyContinuation
     */
    public double getEnergy(String ec) {
        switch (ec) {
        case EnergyColors.SCENE_CONTINUATION:
            return energyContinuation;
        case EnergyColors.SCENE_INTERSTITIAL:
            return energyInterstitial;
        default:
            TextUi.errorPrint("SceneParameters: setting an invalid energy color "
                    + ec);
            System.exit(1);
        }
        return 0;
    }

    /**
     * Returns the fired shadow VIs associated with the scene.
     * 
     * 
     * @return the sceneMembers
     */
    public ViSet getFiredShadowVis() {
        return firedShadowVis;
    }

    /**
     * @return the storyConsistencyWeight
     */
    public Map<String, Double> getLinkStructureWeight() {
        return linkStructureWeight;
    }

    /**
     * @param energy
     *            the energy to set
     * @param ec
     *            - the energy color
     * 
     */
    public void setEnergy(double energy, String ec) {
        switch (ec) {
        case EnergyColors.SCENE_INTERSTITIAL:
            this.energyInterstitial = energy;
            break;
        case EnergyColors.SCENE_CONTINUATION:
            this.energyContinuation = energy;
            break;
        default:
            TextUi.errorPrint("SceneParameters: setting an invalid energy color "
                    + ec);
        }
    }

    /**
     * Resets the interstitial energy to its default value. Called after every
     * continuation, reading or internal creation.
     */
    public void resetInterstitialEnergy() {
        Parameters p = agent.getParameters();
        setEnergy(p.get("A_HLSM", "G_GENERAL",
                "N_DEFAULT_INTERSTITIAL_ENERGY"),
                EnergyColors.SCENE_INTERSTITIAL);
    }

    /**
     * Sets the dynamic score to favor recall in a specific scene by increasing
     * the continuation energy in that scene
     */
    // public static void setMoodRecall(Instance scene) {
    // scene.getSceneParameters().setEnergyContinuation(1000.0);
    // }

    /**
     * Use one unit of continuation energy
     */
    //public void useContinuationEnergy() {
    //    double energyContinuation =
    //            Math.max(0.0, getEnergyContinuation() - 1.0);
    //    setEnergy(energyContinuation, EnergyColor.SCENE_CONTINUATION);
    //}

    /**
     * Uses one unit of interstitial energy
     */
    public void useEnergy(String ec) {
        double energyInterstitials = Math.max(0.0, getEnergy(ec) - 1.0);
        setEnergy(energyInterstitials, ec);
    }

}
