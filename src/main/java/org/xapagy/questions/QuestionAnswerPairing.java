/*
   This file is part of the Xapagy project
   Created on: Nov 2, 2012
 
   org.xapagy.questions.SyntacticPairing
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.questions;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.reference.rrState;
import org.xapagy.reference.rrVoCompatibility;

/**
 * Code implementing the syntactic pairing between a question and an answer
 * 
 * @author Ladislau Boloni
 * 
 */
public class QuestionAnswerPairing {

    /**
     * Ground truth pair is a pairing which requires direct identity and the
     * answer must be coming from the outside.
     * 
     * @param viQuestion
     * @param viAnswer
     * @param agent
     * @return
     */
    /*
    public static boolean isGroundTruthPair(VerbInstance viQuestion,
            VerbInstance viAnswer, Agent agent) {
        LoopItem li = viAnswer.getLoopItem();
        if (li.getType() == LoopItemType.INTERNAL) {
            return false;
        }
        return QuestionAnswerPairing.isPair(viQuestion, viAnswer, agent, false);
    }
    */

    /**
     * Inferred pair is a pairing which requires direct identity and the answer
     * must be an internally generated one (but probably not a continuation)
     * 
     * @param viQuestion
     * @param viAnswer
     * @param agent
     * @return
     */
    /*
    public static boolean isInferredPair(VerbInstance viQuestion,
            VerbInstance viAnswer, Agent agent) {
        LoopItem li = viAnswer.getLoopItem();
        if (li.getType() != LoopItemType.INTERNAL) {
            return false;
        }
        return QuestionAnswerPairing.isPair(viQuestion, viAnswer, agent, false);
    }
    */

    /**
     * Question / answer pairing generic function
     * 
     * @param viQuestion
     * @param viAnswer
     * @param agent
     * @param acceptRelationalIdentity
     *            - if it is true, the pairing allows for relational identity
     * @return
     */
    public static boolean
            isPair(VerbInstance viQuestion, VerbInstance viAnswer, Agent agent,
                    boolean acceptRelationalIdentity) {
        // some checks
        if (!QuestionHelper.isAQuestion(viQuestion, agent)) {
            throw new Error("not a question!");
        }
        // handle the special case that this is a why question, anything matches
        if (QuestionHelper.isWhyQuestion(viQuestion, agent)) {
            return true;
        }
        // handle the special case where this is a what-says question
        if (QuestionHelper.isWhatCommunicatesQuestion(viQuestion, agent)) {
            // the answer must be a quote
            if (viAnswer.getViType() != ViType.QUOTE) {
                return false;
            }
            if (!viQuestion.getSubject().equals(viAnswer.getSubject())) {
                return false;
            }
            return true;
        }
        // for all the other types, the answer must match the ViType of the
        // question
        if (viQuestion.getViType() != viAnswer.getViType()) {
            return false;
        }
        // check the verb
        if (!QuestionAnswerPairing.qaMatchVerb(viQuestion, viAnswer, agent)) {
            return false;
        }
        // case by case for the parts
        switch (viQuestion.getViType()) {
        case S_V_O: {
            if (!QuestionAnswerPairing.qaMatchInstance(viQuestion.getSubject(),
                    viAnswer.getSubject(), agent, acceptRelationalIdentity)) {
                return false;
            }
            if (!QuestionAnswerPairing.qaMatchInstance(viQuestion.getObject(),
                    viAnswer.getObject(), agent, acceptRelationalIdentity)) {
                return false;
            }
            return true;
        }
        case S_V: {
            if (!QuestionAnswerPairing.qaMatchInstance(viQuestion.getSubject(),
                    viAnswer.getSubject(), agent, acceptRelationalIdentity)) {
                return false;
            }
            return true;
        }
        case S_ADJ: {
            if (!QuestionAnswerPairing.qaMatchInstance(viQuestion.getSubject(),
                    viAnswer.getSubject(), agent, acceptRelationalIdentity)) {
                return false;
            }
            if (!QuestionAnswerPairing.qaMatchAdjective(
                    viQuestion.getAdjective(), viAnswer.getAdjective(), agent)) {
                return false;
            }
            return true;
        }
        case QUOTE: {
            throw new Error("There is no quote question!");
        }
        }
        throw new Error("One should never get here.");
    }

    /**
     * Syntactic pair is a pairing which allows for relational identity among
     * instance matching
     * 
     * @param viQuestion
     * @param viAnswer
     * @param agent
     * @return
     */
    public static boolean isSyntacticPair(VerbInstance viQuestion,
            VerbInstance viAnswer, Agent agent) {
        return QuestionAnswerPairing.isPair(viQuestion, viAnswer, agent, true);
    }

    /**
     * Returns true if the adjective of a is-a statement matches with an
     * appropriate question.
     * 
     * There are positive answers - for match.
     * 
     * But the negative answers are also good - the ones with a lot of impact!!!
     * 
     * @param coQuestion
     * @param coAnswer
     * @param agent
     * @return
     */
    private static boolean qaMatchAdjective(ConceptOverlay coQuestion,
            ConceptOverlay coAnswer, Agent agent) {
        // the next acceptable one is if there is a negative impact
        // calculate the impact by simulation
        SimpleEntry<Double, Double> impact =
                coQuestion.impact(coAnswer, 1.0, true, null);
        // if there is a negative impact, add it to the mismatch
        if (impact.getValue() < 0.0) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether an instance (from a question) can be mapped to another
     * instance (from an answer)
     * 
     * It relies on the same idea as the reference resolution. It uses the
     * question Co, without the "wh"
     * 
     * @param instanceQuestion
     * @param instanceAnswer
     * @param agent
     * @return
     */
    private static boolean qaMatchInstance(Instance instanceQuestion,
            Instance instanceAnswer, Agent agent,
            boolean acceptRelationalIdentity) {
        //
        // Matching a wh instance
        //
        if (QuestionHelper.isWhInstance(instanceQuestion, agent)) {
            ConceptOverlay ovr = instanceQuestion.getConcepts();
            ConceptOverlay coRef = new ConceptOverlay(agent);
            coRef.addOverlay(ovr);
            coRef.scrapeEnergy(agent.getConceptDB().getConcept(Hardwired.C_WH));
            // this is a simple wh - matches anything!
            if (coRef.getTotalEnergy() == 0.0) {
                return true;
            }
            rrState rc =
                    rrState.createCalculated(instanceAnswer.getConcepts(),
                            coRef);
            if (rc.decide()) {
                return true;
            }
        }
        if (instanceQuestion.equals(instanceAnswer)) {
            return true;
        }
        if (acceptRelationalIdentity) {
            if (IdentityHelper.isIdentityRelatedRecursive(instanceQuestion,
                    instanceAnswer, agent)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the verbs of a question verb instance can be mapped to
     * another verb instance (the answer)
     * 
     * only checks the verbs...
     * 
     * @param subject
     * @param subject2
     * @param agent
     * @return
     */
    private static boolean qaMatchVerb(VerbInstance viQuestion,
            VerbInstance viAnswer, Agent agent) {
        VerbOverlay voQuestionReference = viQuestion.getVerbs();
        VerbOverlay voAnswer =
                Hardwired.scrapeVerbsFromVO(agent, viAnswer.getVerbs(),
                        Hardwired.VM_WHETHER, Hardwired.VM_ACTION_MARKER,
                        Hardwired.VM_SUCCESSOR);
        return rrVoCompatibility.areCompatible(voAnswer, voQuestionReference);
    }

}
