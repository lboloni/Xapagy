/*
   This file is part of the Xapagy project
   Created on: Jul 25, 2013
 
   org.xapagy.ui.prettyhtml.PwChoiceScore
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import org.xapagy.activity.hlsmaintenance.ChoiceScore;
import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * @author Ladislau Boloni
 * 
 */
public class qh_CHOICE_SCORE {
    /**
     * Detailed printing of a ChoiceScore in a format which would allow the
     * embedding in other webpages
     * 
     * @param fmt
     * @param choiceScore
     * @param agent
     * @param query
     * @return
     */
    public static String pwDetailed(IXwFormatter fmt, ChoiceScore choiceScore,
            Agent agent, RESTQuery query) {
        //fmt.is("Independent score", choiceScore.getScoreIndependent());
        //fmt.is("Dependent score", choiceScore.getScoreDependent());
        //fmt.is("Mood score", choiceScore.getScoreMood());
        //fmt.addBold("Explanation of scores:");
        choiceScore.explainScore(fmt);
        fmt.addH3("Virtual shadow:");
        fmt.explanatoryNote("this value will become the shadow if the choice is instantiated");
        PwQueryLinks.linkToViSet(fmt, agent, query, choiceScore.getVirtualShadow());
        return fmt.toString();
    }

}
