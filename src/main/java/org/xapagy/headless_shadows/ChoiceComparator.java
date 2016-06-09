/*
   This file is part of the Xapagy project
   Created on: Nov 8, 2014
 
   org.xapagy.headless_shadows.ChoiceComparator
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Ladislau Boloni
 *
 */
public class ChoiceComparator implements Comparator<Choice>, Serializable {

    private static final long serialVersionUID = 5506449668269681042L;
    enum ChoiceComparatorType {INDEPENDENT, DEPENDENT, MOOD};
    
    private ChoiceComparatorType type;

    public ChoiceComparator(ChoiceComparatorType type) {
        this.type = type;
    }
    
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Choice o1, Choice o2) {
        switch(type) {
        case DEPENDENT:
            return Double.compare(o1.getChoiceScore()
                    .getScoreDependent(), o2.getChoiceScore()
                    .getScoreDependent());
        case INDEPENDENT:
            return Double.compare(o1.getChoiceScore()
                    .getScoreIndependent(), o2.getChoiceScore()
                    .getScoreIndependent());
        case MOOD: {
            int compValue =
                    Double.compare(
                            o1.getChoiceScore().getScoreMood(), o2
                                    .getChoiceScore().getScoreMood());
            if (compValue == 0) {
                return Double.compare(o1.getChoiceScore()
                        .getScoreDependent(), o2.getChoiceScore()
                        .getScoreDependent());
            }
            return compValue;
        }          
        }
        // we should never get here
        return 0;
    }

}
