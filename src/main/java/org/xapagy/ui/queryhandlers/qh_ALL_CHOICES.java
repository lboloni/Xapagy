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

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.QueryHelper;

/**
 * Generates a webpage listing all the choices
 * 
 * @author Lotzi Boloni
 * 
 */
public class qh_ALL_CHOICES implements IQueryHandler, IQueryAttributes {

    /**
     * Applies the query for the collection of concept words
     * 
     * FIXME: implement it with sorting, filtering, cursor
     * 
     * @param query
     * @return
     */
    private static List<Choice> applyQuery(Agent agent, RESTQuery query) {
        HeadlessComponents hlc = agent.getHeadlessComponents();
        String sortedBy = query.getAttribute(Q_SORTED_BY);
        if (sortedBy.equals("SORTED_BY_DEPENDENT_SCORE")) {
            return hlc.getChoices(HeadlessComponents.comparatorDependentScore);
        }
        if (sortedBy.equals("SORTED_BY_MOOD_SCORE")) {
            return hlc.getChoices(HeadlessComponents.comparatorMoodScore);
        }
        return null;
    }

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        qh_ALL_CHOICES.pwAlternativeQueries(fmt, agent, query);
        String sortedBy = query.getAttribute(Q_SORTED_BY);
        String redheader = "";
        if (sortedBy.equals("SORTED_BY_DEPENDENT_SCORE")) {
            redheader = "Choices (sorted by dependent score)";
        }
        if (sortedBy.equals("SORTED_BY_MOOD_SCORE")) {
            redheader = "Choices (sorted by mood score)";
        }
        fmt.addH2(redheader, "class=identifier");
        fmt.explanatoryNote("Format: dependent score / mood score / choice description");
        fmt.addP("");
        fmt.addP("");
        List<Choice> choices = qh_ALL_CHOICES.applyQuery(agent, query);
        for (Choice choice : choices) {
            fmt.openP();
            fmt.progressBar(choice.getChoiceScore().getScoreDependent(), 1.0);
            fmt.add(" / ");
            fmt.progressBar(choice.getChoiceScore().getScoreMood(), 1.0);
            PwQueryLinks.linkToChoice(fmt, agent, query, choice);
            fmt.closeP();
        }
    }

    /**
     * A panel for alternative queries from the current one
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    public static void pwAlternativeQueries(IXwFormatter fmt, Agent agent,
            RESTQuery query) {
        String linkNativeOrder = "Sort by dependent score";
        String linkDynamicScoreOrder = "Sort by mood score";
        String sortedBy = query.getAttribute(Q_SORTED_BY);
        // mark it which one we are now
        if (sortedBy.equals("SORTED_BY_DEPENDENT_SCORE")) {
            linkNativeOrder = "[" + linkNativeOrder + "]";
        }
        if (sortedBy.equals("SORTED_BY_MOOD_SCORE")) {
            linkDynamicScoreOrder = "[" + linkDynamicScoreOrder + "]";
        }

        fmt.openDiv("class=alternativelinks");
        RESTQuery newq = QueryHelper.copyWithEmptyCommand(query);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_CHOICES");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_DEPENDENT_SCORE");        
        PwQueryLinks.addLinkToQuery(fmt, newq, linkNativeOrder,
                PwFormatter.CLASS_BODYLINK);
        fmt.add("&bull;");
        newq = QueryHelper.copyWithEmptyCommand(query);
        newq.setAttribute(Q_QUERY_TYPE, "ALL_CHOICES");
        newq.setAttribute(Q_SORTED_BY, "SORTED_BY_MOOD_SCORE");        
        PwQueryLinks.addLinkToQuery(fmt, newq, linkDynamicScoreOrder,
                PwFormatter.CLASS_BODYLINK);
        fmt.closeDiv();
    }

}
