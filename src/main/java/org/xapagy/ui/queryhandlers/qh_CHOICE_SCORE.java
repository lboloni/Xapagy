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
package org.xapagy.ui.queryhandlers;

import org.xapagy.activity.hlsmaintenance.ChoiceScore;
import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * @author Ladislau Boloni
 * Created on: Jul 25, 2013
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
