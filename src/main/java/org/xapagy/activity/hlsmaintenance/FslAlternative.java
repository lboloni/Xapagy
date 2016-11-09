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
package org.xapagy.activity.hlsmaintenance;

import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.set.InstanceSet;

/**
 * This class represents the possible alternatives in the interpretation of an
 * instance in the FSL.
 * 
 * Created Jun 3, 2014, to replace the SimpleEntry<InstanceSet, ConceptOverlay>
 * which had been used before.
 * 
 * @author Ladislau Boloni
 * Created on: Jun 3, 2014
 * 
 */
public class FslAlternative {
    /**
     * @param instanceSet
     * @param co
     */
    public FslAlternative(InstanceSet instanceSet, ConceptOverlay co) {
        super();
        this.instanceSet = instanceSet;
        this.co = co;
    }
    
    private InstanceSet instanceSet;



    /**
     * @return the instanceSet
     */
    public InstanceSet getInstanceSet() {
        return instanceSet;
    }



    /**
     * @return the co
     */
    public ConceptOverlay getCo() {
        return co;
    }

    private ConceptOverlay co;
}
