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
package org.xapagy.activity.hlsmaintenance;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwInstance;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * The calculation of the score of a choice. This depends on the type of the
 * choice.
 * 
 * We have a number of score components, which are composed to create a total
 * score (returned by getScore)
 * 
 * We also have a "virtual shadow", which tries to estimate how the shadow of
 * the instantiated choice will look like.
 * 
 * @author Ladislau Boloni
 * Created on: Aug 1, 2012
 * 
 */
public class ChoiceScore implements Serializable {

    private static final long serialVersionUID = 7502065105374928123L;

    /**
     * Returns the energy for a specific choice type (differentiates between the
     * continuation and all the rest)
     * 
     * @return the energy
     */
    private static double getEnergy(ChoiceType choiceType, Instance scene) {
        if (choiceType.equals(ChoiceType.CONTINUATION)) {
            return scene.getSceneParameters().getEnergy(
                    EnergyColors.SCENE_CONTINUATION);
        } else {
            return scene.getSceneParameters().getEnergy(
                    EnergyColors.SCENE_INTERSTITIAL);
        }
    }

    /**
     * Returns the link between two Choices for a specific link name. This is
     * calculated as the link between the specific virtual shadows.
     * 
     * @param agent
     *            - the agent
     * @param choice
     *            - the current choice
     * @param choiceOther
     *            - the other choice
     * @param relationName
     * @return
     */
    public static double getLinkBetweenChoices(Agent agent, Choice choice,
            Choice choiceOther, String relationName) {
        double retval = 0;
        ViSet supportVis = choice.getChoiceScore().getVirtualShadow();
        ViSet otherSupportVis = choiceOther.getChoiceScore().getVirtualShadow();
        for (VerbInstance vi : supportVis.getParticipants()) {
            ViSet relation = agent.getLinks().getLinksByLinkName(vi, relationName);
            for (VerbInstance viOther : otherSupportVis.getParticipants()) {
                double relationValue = relation.value(viOther);
                double supportLocal = supportVis.value(vi);
                double supportOther = otherSupportVis.value(viOther);
                double support = Math.min(supportLocal, supportOther);
                if (support < 0) {
                    continue;
                }
                // for strictly positive
                if (relationValue > 0.0) {
                    double rel = support * relationValue;
                    // rel = Math.pow(rel, 0.5);
                    rel = Math.min(support, relationValue);
                    retval += rel;
                }
            }
        }
        // TextUi.println("getLink: " + Formatter.fmt(retval));
        return retval;
    }

    /**
     * Return the scenes referenced by a particular HLS. If the VI template is
     * not completely resolved, it will also go to the HlsNewInstance
     * 
     * @param hls
     * @return
     */
    public static Set<Instance> getScenesReferencedByTemplates(Hls hls) {
        VerbInstance vit = hls.getViTemplate();
        Set<Instance> scenes = new HashSet<>();
        for (ViPart part : ViStructureHelper.getAllowedInstanceParts(vit
                .getViType())) {
            if (vit.getResolvedParts().containsKey(part)) {
                Instance scene = ((Instance) vit.getPart(part)).getScene();
                scenes.add(scene);
            } else {
                HlsNewInstance hlsni = hls.getDependencies().get(part);
                if (hlsni == null) {
                    // TextUi.println("scoring a Choice with missing stuff but no dependency?");
                    continue;
                }
                scenes.add(hlsni.getScene());
            }
        }
        return scenes;
    }

    /**
     * The choice to which this choice score object is attached
     */
    private Choice choice;
    private Agent agent;
    /**
     * A html string which describes the way the independent score was
     * calculated. Created automatically by mergeSupport mostly
     */
    private String explainIndependentScore = "";
    private String explainDependentScore = "";
    private String explainMoodScore = "";
    private double scoreCharacterization = 0;
    private double scoreCrossInhibition = 0;
    private double dependencyPenaltyMultiplier = 1.0;
    private double scoreFiredShadowInhibition = 0;
    /**
     * The independent score
     */
    private double scoreIndependent = 0;
    /**
     * The virtual shadow is a ViSet which approximates the future shadow of the
     * choice if it would be implemented. It is created by adding together the
     * various Linked components of the FSL-s from various HLS-s in such a way
     * in which the supports are actually added together - i.e. the positive and
     * negative values.
     * 
     * FIXME: this probably will need to be represented as some kind of energy
     * model.
     */
    private ViSet virtualShadow = null;

    /**
     * Constructor for choices other than characterizations. A large part of
     * this is the creation of the virtual shadows
     * 
     * @param hls
     * @param agent
     * @param choiceType
     */
    public ChoiceScore(Choice choice, Agent agent) {
        this.choice = choice;
        this.agent = agent;
        if (!choice.getChoiceType().equals(ChoiceType.STATIC)) {
            dependencyPenaltyMultiplier =
                    calculateDependencyPenaltyMultiplier();
        }
        // create a formatter to collect the explanations
        calculateIndependentScore();
    }

    /**
     * Calculates the independent score, sets the virtual shadow and updates the
     * explanation
     */
    public void calculateIndependentScore() {
        virtualShadow = new ViSet();
        PwFormatter fmt = new PwFormatter();
        //
        // Calculate the independent score
        //
        switch (choice.getChoiceType()) {
        case CONTINUATION: {
            mergeSupport(FslType.SUCCESSOR, 1.0, fmt);
            mergeSupport(FslType.CONTEXT, 0.1, fmt);
            mergeSupport(FslType.COINCIDENCE, 1.0, fmt);
            mergeSupport(FslType.IN_SHADOW, -1.0, fmt);
            mergeSupport(FslType.PREDECESSOR, -1.0, fmt);
            break;
        }
        case MISSING_ACTION: {
            mergeSupport(FslType.COINCIDENCE, 1.0, fmt);
            mergeSupport(FslType.IN_SHADOW, -3.0, fmt);
            double valSucc =
                    mergeSupport(FslType.SUCCESSOR, 1.0, null).getValue();
            fmt.is("valSucc", valSucc);
            double valPred =
                    mergeSupport(FslType.PREDECESSOR, 1.0, null).getValue();
            fmt.is("valPred", valPred);
            // a little trick to get the minimum
            double maxSuccPred = Math.max(valSucc, valPred);
            scoreIndependent = scoreIndependent - maxSuccPred;
            fmt.is("scoreIndependent",
                    "scoreIndependent - Math.max(valSucc, valPred)");
            break;
        }
        case MISSING_RELATION: {
            mergeSupport(FslType.CONTEXT_IMPLICATION, 1.0, fmt);
            mergeSupport(FslType.IN_SHADOW, -10.0, fmt);
            break;
        }
        case CHARACTERIZATION: {
            TextUi.println("Cannot create a characterization ChoiceScore with this constructor!!!");
            System.exit(1);
            break;
        }
        case STATIC: {
            scoreIndependent = choice.getStaticHls().getSupportSalience();
        }
        }
        explainIndependentScore = fmt.toString();
    }

    /**
     * Constructor for the Characterization
     * 
     * @param characterization
     * @param score
     */
    public ChoiceScore(Choice choice, Agent agent, double scoreCharacterization) {
        if (choice.getChoiceType() != ChoiceType.CHARACTERIZATION) {
            TextUi.println("You can only create CHARACTERIZATION ChoiceScore with this constructor");
            System.exit(1);
        }
        this.choice = choice;
        this.agent = agent;
        this.scoreCharacterization = scoreCharacterization;
        this.scoreIndependent = scoreCharacterization;
    }

    /**
     * Returns the inhibition of a specific Choice by succession links pointing
     * forward from a list of Choices
     * 
     * @param choices
     * 
     */
    public void calculateContinuationCrossInhibition(List<Choice> choices) {
        double retval = 0;
        for (Choice otherChoice : choices) {
            if (otherChoice == choice) {
                continue;
            }
            double strengthLink =
                    otherChoice.getChoiceScore().getScoreIndependent();
            double relValue =
                    ChoiceScore.getLinkBetweenChoices(agent, otherChoice,
                            choice, Hardwired.LINK_SUCCESSOR);
            double predValue = relValue * strengthLink;
            // only consider if it is larger than zero
            if (predValue > 0.0) {
                retval = retval + predValue;
            }
        }
        if (retval < 0) {
            throw new Error("Inhibition smaller than zero!!!");
        }
        scoreCrossInhibition = retval;
    }

    /**
     * Calculates inhibition of a specific HLS by the fact that its components
     * have already fired in this scene.
     * 
     * The inhibition is calculated as the sum of the minimum between the
     * virtual shadow and the fired value.
     * 
     */
    public void calculateContinuationFiredShadowInhibition() {
        double retval = 0;
        Set<Instance> scenes =
                ChoiceScore.getScenesReferencedByTemplates(choice.getHls());
        for (Instance scene : scenes) {
            ViSet fired = scene.getSceneParameters().getFiredShadowVis();
            for (VerbInstance vi : fired.getParticipants()) {
                double valueShadow = virtualShadow.value(vi);
                double valueFired = fired.value(vi);
                retval += Math.min(valueFired, valueShadow);
            }
        }
        scoreFiredShadowInhibition = retval;
    }

    /**
     * Calculates the dynamic score of the choice
     * 
     * Currently, it is a simple product of a shaped internalEnergy, the
     * specific type of weight and the native score
     * 
     * @return
     */
    public double getScoreMood() {
        // formatter for collecting the explanation
        IXwFormatter fmt = new PwFormatter();
        double dynamicScore = 0;
        ChoiceType choiceType = choice.getChoiceType();
        Set<Instance> scenes = choice.getReferredScenes();
        double minEnergy = Double.MAX_VALUE;
        double minChoiceTypePreference = Double.MAX_VALUE;
        fmt.is("MoodScore:", dynamicScore);
        fmt.indent();
        fmt.add("Calculated as follows:");
        fmt.indent();
        fmt.is("scoreMood",
                "minChoiceTypePreference * minSalience * dependentScore");
        //
        // Calculate the minimum energy and salience for the given choicetype
        // across all the scenes referenced by the agent
        //
        fmt.add("Referred scenes:");
        for (Instance scene : scenes) {
            fmt.indent();
            fmt.is("Scene:", xwInstance.xwConcise(new TwFormatter(), scene, agent));
            fmt.indent();
            double currentEnergy = ChoiceScore.getEnergy(choiceType, scene);
            minEnergy = Math.min(minEnergy, currentEnergy);
            double currentChoiceTypePreference =
                    scene.getSceneParameters().getChoiceTypePreferences()
                            .get(choiceType);
            minChoiceTypePreference =
                    Math.min(minChoiceTypePreference,
                            currentChoiceTypePreference);
            fmt.is("choiceTypePreference", currentChoiceTypePreference);
            fmt.is("energy", currentEnergy);
            fmt.deindent();
            fmt.deindent();
        }
        double minSalience = EnergyColors.convert(minEnergy, 1.0);
        double scoreDependent = getScoreDependent();
        dynamicScore = minChoiceTypePreference * minSalience * scoreDependent;
        // now fill in what whe have calculated and the explanation
        fmt.is("minChoiceTypePreference", minChoiceTypePreference);
        fmt.is("minEnergy", minEnergy);
        fmt.is("minSalience", minSalience);
        fmt.is("scoreDependent", scoreDependent);
        fmt.deindent();
        fmt.deindent();
        explainMoodScore = fmt.toString();
        return dynamicScore;
    }

    /**
     * Creates a penalty for the creation of an instance, or a scene. This
     * version: halves it for every instance and every scene.. If the dependency
     * contains a group, removes it
     * 
     * FIXME: June 2014: for the time being the creation of the instances are
     * disconnected.
     * 
     * @return
     */
    private double calculateDependencyPenaltyMultiplier() {
        int instanceCount = 0;
        @SuppressWarnings("unused")
        int sceneCount = 0;
        for (HlsNewInstance hlsni : choice.getHls().getDependencies().values()) {
            instanceCount++;
            ConceptOverlay co = hlsni.getAttributes();
            if (Hardwired.contains(agent, co, Hardwired.C_SCENE)) {
                sceneCount++;
            }
            if (Hardwired.contains(agent, hlsni.getAttributes(),
                    Hardwired.C_GROUP)) {
                // TextUi.println("dependency penalty - new group creation - score 0.0");
                return 0.0;
            }
        }
        //
        // double retval =
        // Math.pow(0.5, instanceCount) * Math.pow(0.5, sceneCount);
        // FIXME: don't allow instance creation for the time being
        if (instanceCount == 0) {
            return 1.0;
        } else {
            return 0.0;
        }
        // return retval;
    }

    /**
     * A human readable explanation of the score
     * 
     * @return
     */
    public String explainScore(IXwFormatter fmt) {
        switch (choice.getChoiceType()) {
        case CONTINUATION:
        case MISSING_ACTION:
        case MISSING_RELATION: {
            fmt.is("Independent score:", scoreIndependent);
            fmt.indent();
            fmt.add("Calculated as follows:");
            fmt.indent();
            fmt.add(explainIndependentScore);
            fmt.deindent();
            fmt.deindent();
            break;
        }
        case CHARACTERIZATION: {
            fmt.add("Characterization formula:");
            fmt.indent();
            fmt.add("Sub-scores:");
            fmt.indent();
            fmt.is("characterization", scoreCharacterization);
            fmt.deindent();
            fmt.add("independent = characterization");
            fmt.indent();
            fmt.is("independent", scoreIndependent);
            fmt.deindent();
            fmt.add("score = independent");
            fmt.indent();
            fmt.is("score", getScoreDependent());
            fmt.deindent();
            fmt.deindent();
            break;
        }
        case STATIC: {
            fmt.add("Static score: to be done");
            break;
        }

        }
        if (choice.getChoiceType() != ChoiceType.CHARACTERIZATION) {
            fmt.add(explainDependentScore);
        }
        fmt.add(explainMoodScore);
        return fmt.toString();
    }

    /**
     * Calculating the dependent score and updating the explainDependentScore
     * string
     * 
     * @return
     */
    public double getScoreDependent() {
        double factorFiredShadowInhibition = 10.0; // was 2.0
        double scoreDependent =
                scoreIndependent * dependencyPenaltyMultiplier
                        - scoreCrossInhibition - factorFiredShadowInhibition
                        * scoreFiredShadowInhibition;
        IXwFormatter fmt = new PwFormatter();
        fmt.is("Dependent score", Formatter.fmt(scoreDependent));
        fmt.indent();
        fmt.add("Calculated as follows:");
        fmt.indent();
        fmt.is("scoreDependent",
                "scoreIndependent * dependencyPenaltyMultiplier - scoreCrossInhibition - factorFiredShadowInhibition * scoreFiredShadowInhibition");
        fmt.is("scoreIndependent", scoreIndependent);
        fmt.is("dependencyPenaltyMultiplier", dependencyPenaltyMultiplier);
        fmt.is("scoreCrossInhibition", scoreCrossInhibition);
        fmt.is("factorFiredShadowInhibition", factorFiredShadowInhibition);
        fmt.is("scoreFiredShadowInhibition", scoreFiredShadowInhibition);
        fmt.deindent();
        fmt.deindent();
        explainDependentScore = fmt.toString();
        return scoreDependent;
    }

    /**
     * Returns the independent score (not affected by cross-choice inhibitions)
     * 
     * @return the score
     */
    public double getScoreIndependent() {
        return scoreIndependent;
    }

    /**
     * Return the virtual shadow of this choice score
     * 
     * @param agent
     * @return
     */
    public ViSet getVirtualShadow() {
        return virtualShadow;
    }

    /**
     * This function merges a certain type of support to the score. It does
     * three different things:
     * 
     * <ul>
     * <li>takes the summative support from the HLS depending on the type
     * <li>adds it to the independent score in a weighted way
     * <li>adds it to the virtual shadow in a weighted way
     * <li>adds it to the explanation strem
     * </ul>
     * 
     * @param fslType
     *            - the type of the FSL - the HLS summative support
     * @param mergeRation
     * 
     * @return the support - only to be used if more fancy stuff is needed
     */
    private SimpleEntry<ViSet, Double> mergeSupport(FslType fslType,
            double mergeRatio, IXwFormatter fmt) {
        SimpleEntry<ViSet, Double> support =
                choice.getHls().summativeSupport(fslType, agent);
        scoreIndependent += mergeRatio * support.getValue();
        virtualShadow.mergeIn(support.getKey(), mergeRatio);
        if (fmt != null) {
            String newValue = "scoreIndependent ";
            if (mergeRatio > 0) {
                newValue += " + ";
            }
            newValue +=
                    Formatter.fmt(mergeRatio) + " * "
                            + Formatter.fmt(support.getValue());
            newValue += " // " + fslType;
            fmt.is("scoreIndependent", newValue);
        }
        return support;
    }
}
