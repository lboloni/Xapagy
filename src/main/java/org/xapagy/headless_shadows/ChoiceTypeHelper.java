/*
   This file is part of the Xapagy project
   Created on: Sep 25, 2014
 
   org.xapagy.headless_shadows.ChoiceTypeHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import org.xapagy.headless_shadows.Choice.ChoiceType;

/**
 * Different types of choices need to be handled differently
 * 
 * @author Ladislau Boloni
 *
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
