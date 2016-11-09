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

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Ladislau Boloni
 * Created on: Nov 8, 2014
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
