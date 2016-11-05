/*
   This file is part of the Xapagy project
   Created on: Nov 29, 2011
 
   org.xapagy.agents.ResolutionConfidence
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.reference;

import java.io.Serializable;

import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.operations.Coverage;
import org.xapagy.concepts.operations.Incompatibility;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwRrState;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * This class tracks the state of the reference resolution of a candidate
 * 
 * @author Ladislau Boloni
 * 
 */
public class rrState implements Serializable {

    /**
     * For the case when it was a winner of the resolution, it indicates the
     * grounds on which the decision had been made. This was actually a sensible
     * choice before, but probably will be refactored at some moment.
     * 
     * <ul>
     * <li>UNDETERMINED - either the decision had not been made, or lost
     * <li>ONLY_COMPATIBLE - resolution had been made and this is the only
     * compatible item
     * <li>MAX_COMPATIBLE - there were multiple compatible items, this was the
     * largest one
     * <li>CURRENT_SCENE - special case, nothing was compatible, falling back to
     * the current scene
     * </ul>
     */
    public enum rrJustification {
        CURRENT_SCENE, MAX_COMPATIBLE, NO_COMPETITION, ONLY_COMPATIBLE,
        UNDETERMINED
    }

    /**
     * Describes the phase of the resolution process. Also, if the resolution is
     * over, whether it had been the winner or the looser of the resolution
     * process.
     * 
     * <ul>
     * <li>UNDETERMINED - it was not yet participating in a resoluion process
     * <li>WINNER - had been the winner of the resolution
     * <li>NOT_WINNER - had not been the winner of the resolution
     * <li>MAYBE - after a round of resolution
     * </ul>
     */
    public enum rrPhase {
        MAYBE, NOT_WINNER, UNDETERMINED, WINNER
    };

    private static final long serialVersionUID = 1910853599626550355L;;

    /**
     * Creates state which specifies that the resolution is not yet resolved,
     * but the values are calculated
     * 
     * @param the
     *            attributes of the instance to which the reference is checked
     * @param the
     *            co with which the reference is made
     * @return
     */
    public static rrState createCalculated(ConceptOverlay coSource,
            ConceptOverlay coReference) {
        rrState retval = new rrState();
        retval.setPhase(rrPhase.UNDETERMINED);
        retval.setJustification(rrJustification.UNDETERMINED);
        retval.scoreSimilarity = Coverage.scoreCoverage(coSource, coReference);
        retval.scoreIncompatibility =
                Incompatibility.scoreIncompatibility(coSource, coReference);
        return retval;
    }

    /**
     * Creates a resolution confidence which marks the fact that there are no
     * ambiguities about this resolution... non-competitive.
     * 
     * This is the case for VO, CO, pronouns in ReferecenCrossCompetitive
     * 
     * @return
     */
    public static rrState createNoCompetitionResConf() {
        rrState retval = new rrState();
        retval.setPhase(rrPhase.WINNER);
        retval.setJustification(rrJustification.NO_COMPETITION);
        return retval;
    }

    /**
     * Creates an undetermined one - only used in AbstractXapiReference - a bit
     * unclear to me how this works.
     * 
     * @param coItem
     * @param coReference
     * @return
     */
    public static rrState createUndetermined() {
        rrState retval = new rrState();
        retval.setPhase(rrPhase.UNDETERMINED);
        retval.setJustification(rrJustification.UNDETERMINED);
        return retval;
    }

    private rrState compositionParent;
    private rrJustification justification;
    private rrPhase phase;
    private double scoreBias;
    private double scoreIncompatibility;
    private double scoreSimilarity;

    private rrState() {
        scoreBias = 0;
        scoreSimilarity = 0;
        scoreIncompatibility = 0;
        phase = rrPhase.UNDETERMINED;
        justification = rrJustification.UNDETERMINED;
    }

    /**
     * 
     * Composition of confidences.
     * 
     * Called from the relational reference and group reference.
     * 
     * For the time being, we are only keeping these around, no clear story
     * about how they are going to be used!
     * 
     * @param value
     * @return
     */
    public rrState composeConfidence(rrState value) {
        value.compositionParent = this;
        return value;
    }

    /**
     * Decide whether the reference is good enough
     * 
     * @return
     */
    public boolean decide() {
        return getOverallScore() > 0;
    }

    /**
     * @return the compositionParent
     */
    public rrState getCompositionParent() {
        return compositionParent;
    }

    /**
     * @return the resCoType
     */
    public rrJustification getJustification() {
        return justification;
    }

    /**
     * Calculates an overall score of the resolution confidence
     * 
     * FIXME: justify these weights
     * 
     * @return
     */
    public double getOverallScore() {
        double overallScore =
                scoreBias + 10 * scoreSimilarity - 100 * scoreIncompatibility;
        return overallScore;
    }

    /**
     * @return the resConfState
     */
    public rrPhase getPhase() {
        return phase;
    }

    /**
     * @return the scoreStrength
     */
    public double getScoreBias() {
        return scoreBias;
    }

    /**
     * @return the scoreIncompatibility
     */
    public double getScoreIncompatibility() {
        return scoreIncompatibility;
    }

    /**
     * @return the scoreSimilarity
     */
    public double getScoreSimilarity() {
        return scoreSimilarity;
    }

    /**
     * @param resCoType
     *            the resCoType to set
     */
    public void setJustification(rrJustification resCoType) {
        this.justification = resCoType;
    }

    /**
     * @param resConfState
     *            the resConfState to set
     */
    public void setPhase(rrPhase resConfState) {
        this.phase = resConfState;
    }

    /**
     * @param scoreBias
     *            the scoreStrength to set
     */
    public void setScoreBias(double scoreBias) {
        this.scoreBias = scoreBias;
    }

    @Override
    public String toString() {
        TwFormatter twf = new TwFormatter();
        String retval = xwRrState.xwDetailed(twf, this, PrettyPrint.lastAgent);
        return retval;
    }

}
