/*
   This file is part of the Xapagy project
   Created on: Sep 17, 2011
 
   org.xapagy.activity.SaFoundAnswer
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.focusmaintenance;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.VerbInstance;
import org.xapagy.questions.QuestionAnswerPairing;
import org.xapagy.questions.QuestionHelper;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * SA that identifies if a question had been answered.
 * 
 * It is called on the QUESTION! (verify this) so vi is the question vi
 * 
 * FIXME: No, it is called all the time and iterates over all the pairs, this is
 * unnecessary.
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaFcmFoundAnswer extends SpikeActivity {

    private static final long serialVersionUID = 6263518248258376256L;

    /**
     * 
     * @param agent
     * @param name
     */
    public SaFcmFoundAnswer(Agent agent, String name) {
        super(agent, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.SpikeActivity#applyInner()
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Focus fc = agent.getFocus();
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (!QuestionHelper.isAQuestion(vi, agent)) {
                continue;
            }
            // it is a question, search for an answer
            for (VerbInstance vi2 : fc.getViList(EnergyColors.FOCUS_VI)) {
                if (QuestionHelper.isAQuestion(vi2, agent)) {
                    continue;
                }
                if (QuestionAnswerPairing.isSyntacticPair(vi, vi2, agent)) {
                    // check if it already has a link
                    if (QuestionHelper.decideQuestionAnswerLinkExists(agent,
                            vi, vi2)) {
                        continue;
                    }
                    // it doesn't have a link, add a link
                    QuestionHelper.markAsQuestionAnswer(agent, vi, vi2);
                }
            }
        }
    }

    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaFcmFoundAnswer");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
