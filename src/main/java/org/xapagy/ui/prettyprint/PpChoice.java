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
package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwVerbInstance;

/**
 * Prints a choice object of various types
 * 
 * @author Ladislau Boloni
 * Created on: Sep 13, 2011
 */
public class PpChoice {

    public static String
            pp(Choice choice, Agent agent, PrintDetail detailLevel) {
        if (detailLevel == PrintDetail.DTL_DETAIL) {
            return PpChoice.ppDetailed(choice, agent);
        }
        if (detailLevel == PrintDetail.DTL_CONCISE) {
            return PpChoice.ppConcise(choice, agent);
        }
        throw new Error("Unsupported detailLevel" + detailLevel);
    }

    /**
     * FIXME: this uses verbalization, make sure the instantiation does not
     * affect things!!!
     * 
     * @param choice
     * @param agent
     * @return
     */
    public static String ppConcise(Choice choice, Agent agent) {
        // int detailLevel = DTL_CONCISE;
        Formatter fmtMargin = new Formatter();
        // hopefully, this would not mess up anything, except the counter
        String viText = null;
        String statusText = null;
        switch (choice.getChoiceStatus()) {
        case NOT_CHOSEN: {
            statusText = "NotChosen";
            Hls hls = choice.getHls();
            if (hls != null) {
                viText =
                        xwVerbInstance.xwConcise(new TwFormatter(), choice.getHls()
                                .getViTemplate(), agent);
            } else { // so it is a characterization
                viText =
                        PpHlsCharacterization.ppDetailed(
                                choice.getHlsCharacterization(), agent);
            }
            break;
        }
        case PARTIALLY_INSTANTIATED: {
            statusText = "Partial";
            viText = "vi... ongoing instantiation....";
            break;
        }
        case FULLY_INSTANTIATED: {
            statusText = "Instantiated";
            viText = "";
            for (VerbInstance vi : choice.getInstantiatedVis()) {
                viText += agent.getVerbalize().verbalize(vi) + "; ";
            }
            break;
        }
        }
        String margin =
                Formatter.fmt(choice.getChoiceScore().getScoreMood())
                        + "/"
                        + Formatter.fmt(choice.getChoiceScore()
                                .getScoreDependent());
        switch (choice.getChoiceType()) {
        case CONTINUATION: {
            fmtMargin.addWithMarginNote(margin, "[Contin:" + statusText + "] "
                    + viText);
            return fmtMargin.toString();
        }
        case MISSING_ACTION: {
            fmtMargin.addWithMarginNote(margin, "[Miss-Act:" + statusText
                    + "] " + viText);
            return fmtMargin.toString();
        }
        case MISSING_RELATION: {
            fmtMargin.addWithMarginNote(margin, "[Miss-Rel: " + statusText
                    + "] " + viText);
            return fmtMargin.toString();
        }
        case CHARACTERIZATION: {
            fmtMargin.addWithMarginNote(margin, "[Charact: " + statusText
                    + "] " + viText);
            return fmtMargin.toString();
        }
        default:
            return "cannot print choice of type " + choice.getChoiceType();
        }
    }

    /**
     * Detailed printing, lists the
     * 
     * @param choice
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String ppDetailed(Choice choice, Agent agent) {
        PrintDetail detailLevel = PrintDetail.DTL_CONCISE;
        Formatter fmtMargin = new Formatter();
        Formatter fmt = new Formatter();
        String margin =
                Formatter.fmt(choice.getChoiceScore().getScoreMood())
                        + "/"
                        + Formatter.fmt(choice.getChoiceScore()
                                .getScoreDependent());
        switch (choice.getChoiceType()) {
        case CONTINUATION: {
            fmt.add("Choice: Continuation");
            fmt.addIndented(PrettyPrint.pp(choice.getHls(), agent, detailLevel));
            fmtMargin.addWithMarginNote(margin, fmt.toString());
            return fmtMargin.toString();
        }
        case MISSING_ACTION: {
            fmt.add("Choice: Missing action");
            fmt.addIndented(PrettyPrint.pp(choice.getHls(), agent, detailLevel));
            fmtMargin.addWithMarginNote(margin, fmt.toString());
            return fmtMargin.toString();
        }
        case MISSING_RELATION: {
            fmt.add("Choice: Missing relation");
            fmt.addIndented(PrettyPrint.pp(choice.getHls(), agent, detailLevel));
            fmtMargin.addWithMarginNote(margin, fmt.toString());
            return fmtMargin.toString();
        }
        case CHARACTERIZATION: {
            fmt.add("Choice: Characterization");
            fmt.addIndented(PrettyPrint.pp(choice.getHlsCharacterization(),
                    agent, detailLevel));
            fmtMargin.addWithMarginNote(margin, fmt.toString());
            return fmtMargin.toString();
        }
        default:
            return "cannot print choice of type " + choice.getChoiceType();

        }
    }

}
