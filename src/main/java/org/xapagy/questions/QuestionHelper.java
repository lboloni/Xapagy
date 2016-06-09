/*
   This file is part of the Xapagy project
   Created on: Feb 20, 2011
 
   org.xapagy.model.QuestionHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.questions;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * Contains static functions which help with the questions
 * 
 * @author Ladislau Boloni
 * 
 */
public class QuestionHelper {

    /**
     * Decides whether the question answer relationship between these have
     * already been marked
     * 
     * @param agent
     * @param viQuestion
     * @param viAnswer
     * @return
     */
    public static boolean decideQuestionAnswerLinkExists(Agent agent,
            VerbInstance viQuestion, VerbInstance viAnswer) {
        double value = agent.getLinks().getLinksByLinkName(viQuestion, Hardwired.LINK_ANSWER).value(viAnswer);
        // check if it already has a link
        if (value > 0.0) {
            return true;
        }
        return false;
    }

    /**
     * If is a question verb instance, mark it as such.
     * 
     * It also sets the marker as a question
     * 
     * @param ovr
     * @param agent
     * @return is it is a question or not
     */
    public static boolean isAQuestion(VerbInstance vi, Agent agent) {
        boolean retval = false;
        switch (vi.getViType()) {
        case S_V_O: {
            if (QuestionHelper.isWhInstance(vi.getSubject(), agent)) {
                retval = true;
                break;
            }
            if (QuestionHelper.isWhInstance(vi.getObject(), agent)
                    || QuestionHelper.isWhatInstance(vi.getObject(), agent)) {
                retval = true;
                break;
            }
            if (QuestionHelper.isWhVerbOverlay(vi.getVerbs(), agent)) {
                retval = true;
                break;
            }
            break;
        }
        case S_V: {
            if (QuestionHelper.isWhInstance(vi.getSubject(), agent)) {
                retval = true;
                break;
            }
            if (QuestionHelper.isWhVerbOverlay(vi.getVerbs(), agent)) {
                retval = true;
                break;
            }
            break;
        }
        case S_ADJ: {
            if (QuestionHelper.isWhInstance(vi.getSubject(), agent)) {
                retval = true;
                break;
            }
            if (QuestionHelper.isWhConceptOverlay(vi.getAdjective(), agent)) {
                retval = true;
                break;
            }
            if (QuestionHelper.isWhVerbOverlay(vi.getVerbs(), agent)) {
                retval = true;
                break;
            }
            break;
        }
        case QUOTE: {
            // assume that the quoted sentence cannot be a question.
            break;
        }
        }
        return retval;
    }

    /**
     * Returns true if this is a what type question
     * 
     * @param viQuestion
     * @param agent
     * @return
     */
    public static boolean isWhatCommunicatesQuestion(VerbInstance viQuestion,
            Agent agent) {
        if (viQuestion.getViType() != ViType.S_V_O) {
            return false;
        }
        if (!Hardwired.contains(agent, viQuestion.getObject().getConcepts(),
                Hardwired.C_WHAT)) {
            return false;
        }
        if (!Hardwired.contains(agent, viQuestion.getVerbs(),
                Hardwired.V_COMMUNICATION)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if this concept overlay is a what -marked one
     * 
     * @param co
     * @param agent
     * @return
     */
    public static boolean isWhatConceptOverlay(ConceptOverlay co, Agent agent) {
        return Hardwired.contains(agent, co, Hardwired.C_WHAT);
    }

    /**
     * Returns true if this instance is a wh marked one
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static boolean isWhatInstance(Instance instance, Agent agent) {
        return QuestionHelper.isWhatConceptOverlay(instance.getConcepts(),
                agent);
    }

    /**
     * Returns true if this concept overlay is a wh-marked one
     * 
     * @param co
     * @param agent
     * @return
     */
    public static boolean isWhConceptOverlay(ConceptOverlay co, Agent agent) {
        return Hardwired.contains(agent, co, Hardwired.C_WH);
    }

    /**
     * Returns true if this instance is a wh marked one
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static boolean isWhInstance(Instance instance, Agent agent) {
        return QuestionHelper.isWhConceptOverlay(instance.getConcepts(), agent);
    }

    /**
     * Returns true if this verb overlay is a whether
     * 
     * @param ovr
     * @param agent
     * @return
     */
    public static boolean isWhVerbOverlay(VerbOverlay ovr, Agent agent) {
        if (Hardwired.contains(agent, ovr, Hardwired.VM_WHETHER)
                || Hardwired.contains(agent, ovr, Hardwired.VM_WHY)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if viQuestion is a why type question. Basically it is enough
     * to test for the VM_WHY in the verb.
     * 
     * @param viQuestion
     * @param agent
     * @return
     */
    public static boolean isWhyQuestion(VerbInstance viQuestion, Agent agent) {
        return Hardwired.contains(agent, viQuestion.getVerbs(),
                Hardwired.VM_WHY);
    }

    /**
     * Marks the found relationship between a question and the answer
     * 
     * @param agent
     * @param viQuestion
     * @param viAnswer
     */
    public static void markAsQuestionAnswer(Agent agent,
            VerbInstance viQuestion, VerbInstance viAnswer) {
        TextUi.println("Answer found: \n"
                + PrettyPrint.ppConcise(viQuestion, agent) + " answered by "
                + PrettyPrint.ppConcise(viAnswer, agent));
        agent.getLinks().changeLinkByName(Hardwired.LINK_ANSWER, viQuestion, viAnswer, 1.0, "markAsQuestionAnswer" + "+changeAnswer");
    }
}
