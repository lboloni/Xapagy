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
package org.xapagy.ui.prettygeneral;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * It is used to print the current choices (debugging recall)
 * 
 * @author Ladislau Boloni
 * Created on: Jun 25, 2011
 */
public class xwChoices {

    // initialize the choice types to all
    public static Set<Choice.ChoiceType> choiceTypesConsidered = null;
    public static int countLimit = 3;
    public static double valueLimit = 0.001;
    static {
        xwChoices.choiceTypesConsidered = new HashSet<>();
        xwChoices.choiceTypesConsidered.add(ChoiceType.CHARACTERIZATION);
        xwChoices.choiceTypesConsidered.add(ChoiceType.MISSING_ACTION);
        xwChoices.choiceTypesConsidered.add(ChoiceType.MISSING_RELATION);
        xwChoices.choiceTypesConsidered.add(ChoiceType.CONTINUATION);
    }

    /**
     * Prints a list of choices, with them being detailed
     * 
     * @param aw
     * @param list
     * @param agent
     * @return
     */
    public static String xwConcise(IXwFormatter xw, List<Choice> list, Agent agent) {
        xw.addLabelParagraph("Choices: " + list.size());
        xw.indent();
        int count = 0;
        for (Choice choice : list) {
            if (!xwChoices.choiceTypesConsidered.contains(choice
                    .getChoiceType())) {
                continue;
            }
            double value = choice.getChoiceScore().getScoreDependent();
            if (value < xwChoices.valueLimit) {
                break;
            }
            if (count >= xwChoices.countLimit) {
                break;
            }
            xwChoice.xwConcise(xw, choice, agent);
            count++;

        }
        if (count < list.size()) {
            xw.addP("... and " + (list.size() - count) + " more.");
        }
        return xw.toString();
    }

    /**
     * Changes the printing such that it prints only the continuations
     */
    public static void printContinuationsOnly() {
        xwChoices.choiceTypesConsidered = new HashSet<>();
        xwChoices.choiceTypesConsidered.add(ChoiceType.CONTINUATION);
    }
}
