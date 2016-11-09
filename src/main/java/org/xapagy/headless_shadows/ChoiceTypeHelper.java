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
package org.xapagy.headless_shadows;

import org.xapagy.headless_shadows.Choice.ChoiceType;

/**
 * Different types of choices need to be handled differently
 * 
 * @author Ladislau Boloni
 * Created on: Sep 25, 2014
 */
public class ChoiceTypeHelper {

    public static boolean isHlsBased(Choice choice) {
        ChoiceType choiceType = choice.getChoiceType();
        return choiceType.equals(ChoiceType.CONTINUATION)
                || choiceType.equals(ChoiceType.MISSING_ACTION)
                || choiceType.equals(ChoiceType.MISSING_RELATION)
                || choiceType.equals(ChoiceType.MISSING_ACTION);
    }

    
    public static boolean isCharacterization(Choice choice) {
        ChoiceType choiceType = choice.getChoiceType();
        return choiceType.equals(ChoiceType.CHARACTERIZATION);
    }

    public static boolean isStatic(Choice choice) {
        ChoiceType choiceType = choice.getChoiceType();
        return choiceType.equals(ChoiceType.STATIC);
    }

}
