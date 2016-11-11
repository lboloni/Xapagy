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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;

/**
 * 
 * It is used to print the current choices (debugging recall)
 * 
 * @author Ladislau Boloni
 * Created on: Jun 25, 2011
 */
public class PpChoices {

    // initialize the choice types to all
    public static Set<Choice.ChoiceType> choiceTypesConsidered = null;
    public static int countLimit = 3;
    public static double valueLimit = 0.001;
    static {
        PpChoices.choiceTypesConsidered = new HashSet<>();
        PpChoices.choiceTypesConsidered.add(ChoiceType.CHARACTERIZATION);
        PpChoices.choiceTypesConsidered.add(ChoiceType.MISSING_ACTION);
        PpChoices.choiceTypesConsidered.add(ChoiceType.MISSING_RELATION);
        PpChoices.choiceTypesConsidered.add(ChoiceType.CONTINUATION);
    }

    /**
     * Prints a list of choices.
     * 
     * FIXME: various filterings needed in the future
     * 
     * @param objects
     * @param agent
     * @return
     */
    public static String pp(List<Choice> list, Agent agent,
            PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        fmt.add("Choices: " + list.size());
        fmt.indent();
        int count = 0;
        for (Choice choice : list) {
            if (!PpChoices.choiceTypesConsidered.contains(choice
                    .getChoiceType())) {
                continue;
            }
            double value = choice.getChoiceScore().getScoreDependent();
            if (value < PpChoices.valueLimit) {
                break;
            }
            if (count >= PpChoices.countLimit) {
                break;
            }
            fmt.add(PpChoice.pp(choice, agent, detailLevel));
            count++;

        }
        if (count < list.size()) {
            fmt.add("... and " + (list.size() - count) + " more.");
        }
        return fmt.toString();
    }

    /**
     * Changes the printing such that it prints only the continuations
     */
    public static void printContinuationsOnly() {
        PpChoices.choiceTypesConsidered = new HashSet<>();
        PpChoices.choiceTypesConsidered.add(ChoiceType.CONTINUATION);
    }
}
