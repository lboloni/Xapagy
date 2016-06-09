/*
   This file is part of the Xapagy project
   Created on: Jun 3, 2014
 
   org.xapagy.activity.hlsmaintenance.FslAlternative
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
