/*
   This file is part of the Xapagy project
   Created on: May 12, 2013
 
   org.xapagy.activity.hlsmaintenance.CharacterizationScore
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.hlsmaintenance;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Calibration;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.operations.Incompatibility;
import org.xapagy.instances.Instance;
import org.xapagy.instances.SceneParameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * An object which holds the various components of the characterization score
 * 
 * Currently it also holds a series of weights
 * 
 * FIXME: the weight components must be parameterized and handled on the
 * scene-by-scene basis!!!
 * 
 * @author Ladislau Boloni
 * 
 */
public class CharacterizationScore implements Serializable {

    private static final long serialVersionUID = 4228548791181132980L;

    private Instance instance;

    /**
     * The focus value of
     */
    private double scaleFocusWeight;
    /**
     * The final score, calculated in the constructor
     */
    private double score;
    /**
     * The already present score
     */
    private double scoreAlreadyPresent = 0;
    /**
     * The essence score
     */
    private double scoreEssence = 0;
    /**
     * The incompatibility score
     */
    private double scoreIncompatibility = 0;
    /**
     * The target score
     */
    private double scoreTargetDifference = 0;

    /**
     * The uniqueness score
     */
    private double scoreUniqueness = 0;

    /**
     * Generate the characterization score (for a HlsCharacterization)
     * 
     * @param agent
     *            - the agent
     * @param instance
     *            - the instance to which the HlsCharacterization will be
     *            applied
     * @param coAdded
     * 
     */
    public CharacterizationScore(Agent agent, Instance instance,
            ConceptOverlay coAdded, ConceptOverlay coTarget) {
        this.instance = instance;
        this.scaleFocusWeight =
                agent.getFocus().getSalience(instance, EnergyColors.FOCUS_INSTANCE);
        ConceptOverlay coCurrent = instance.getConcepts();
        scoreIncompatibility =
                Incompatibility.scoreIncompatibility(coCurrent, coAdded);
        if (scoreIncompatibility > Calibration.decideIncompatibility) {
            score = -1000;
            return;
        }
        this.scoreAlreadyPresent =
                CharacterizationScoreHelper.getAlreadyPresentScore(agent,
                        coCurrent, coAdded);
        if (scoreAlreadyPresent > 0.0) {
            score = -1000;
            return;
        }
        // now calculate the other components. These are more expensive to
        // calculate
        scoreUniqueness =
                CharacterizationScoreHelper.getUniquenessScore(agent, instance,
                        coAdded);
        scoreEssence =
                CharacterizationScoreHelper.getEssenceScore(agent, coCurrent,
                        coAdded);
        scoreTargetDifference =
                CharacterizationScoreHelper.getTargetDifference(agent,
                        coCurrent, coAdded, coTarget);
        // calculate the final score
        SceneParameters sp = instance.getScene().getSceneParameters();
        score =
                scaleFocusWeight
                        * (sp.getCharacterizationWeightUniqueness()
                                * scoreUniqueness
                                + sp.getCharacterizationWeightEssence()
                                * scoreEssence + sp
                                .getCharacterizationWeightTargetDifference()
                                * scoreTargetDifference);
    }

    public double getScore() {
        return score;
    }

    /**
     * Nicely formatted toString (only the values)
     */
    @Override
    public String toString() {
        Formatter fmt = new Formatter();
        SceneParameters sp = instance.getScene().getSceneParameters();
        fmt.add("CharacterizationScore: " + Formatter.fmt(score));
        // fmt.is("instance:" + PrettyPrint.ppConcise(instance, null));
        fmt.indent();
        fmt.is("scoreIncompatibility", scoreIncompatibility);
        fmt.is("scoreAlreadyPresent", scoreAlreadyPresent);
        fmt.is("scoreUniqueness", scoreUniqueness);
        fmt.is("scoreEssence", scoreEssence);
        fmt.is("scoreTargetDifference", scoreTargetDifference);
        fmt.is("scaleFocusWeight", scaleFocusWeight);
        fmt.add("weights (from the scene)");
        fmt.indent();
        fmt.is("weightUniqueness", sp.getCharacterizationWeightUniqueness());
        fmt.is("weightEssence", sp.getCharacterizationWeightEssence());
        fmt.is("weightTargetDifference",
                sp.getCharacterizationWeightTargetDifference());
        fmt.deindent();
        fmt.deindent();
        return fmt.toString();
    }
}
