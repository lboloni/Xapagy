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
package org.xapagy.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.xapi.XapiParserException;

/**
 * To be used during debugging choices. It allows us to access the choices using
 * text mode parameters.
 * 
 * @author Ladislau Boloni
 * Created on: Jun 24, 2014
 */
public class ChoiceAccess implements Serializable {

    private static final long serialVersionUID = 8755426681574300827L;
    private Agent agent;
    private ViMatch vimatch;
    private Comparator<Choice> choiceComparator;

    /**
     * @param agent
     * @param choiceComparator
     */
    public ChoiceAccess(Agent agent, Comparator<Choice> choiceComparator) {
        super();
        this.agent = agent;
        this.choiceComparator = choiceComparator;
        this.vimatch = new ViMatch(agent);
    }

    /**
     * Returns the choices of a specified type (or all choices if it is null)
     * whose ViTemplate matches the specific parameters. The parameters are the
     * ones specified for ViMatch. The choices are sorted in the order of their
     * independent score
     * 
     * @param choiceType
     * @param viType
     * @param params
     * @return
     * @throws XapiParserException
     */
    public List<Choice> getChoices(ChoiceType choiceType, ViType viType,
            String... params) {
        List<Choice> retval = new ArrayList<>();
        for (Choice choice : agent.getHeadlessComponents()
                .getChoices(choiceComparator)) {
            if ((choiceType != null)
                    && (choice.getChoiceType() != choiceType)) {
                continue;
            }
            if (!vimatch.match(choice.getHls().getViTemplate(), viType,
                    params)) {
                continue;
            }
            retval.add(choice);
        }
        return retval;
    }

    /**
     * Returns the filtered choices based on the ChoiceAccessFilter object.
     * 
     * @param caf
     * @return
     * @throws XapiParserException
     */
    public List<Choice> getChoices(ChoiceAccessFilter caf) {
        return getChoices(caf.getChoiceType(), caf.getViType(),
                caf.getParams());
    }

    /**
     * Returns the choice with the largest independent score which matches the
     * parameters, or null if none available.
     * 
     * @param choiceType
     * @param viType
     * @param params
     * @return
     * @throws XapiParserException
     */
    public Choice getOneChoice(ChoiceType choiceType, ViType viType,
            String... params) {
        List<Choice> results = getChoices(choiceType, viType, params);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    /**
     * Returns the choice with the largest independent score based on the
     * ChoiceAccessFilter object.
     * 
     * @param caf
     * @return
     * @throws XapiParserException
     */
    public Choice getOneChoice(ChoiceAccessFilter caf) {
        return getOneChoice(caf.getChoiceType(), caf.getViType(),
                caf.getParams());
    }

}
