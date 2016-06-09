/*
   This file is part of the Xapagy project
   Created on: Sep 17, 2010
 
   org.xapagy.model.InstanceSet
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.set;

import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * 
 */
public class ViSet extends WeightedSet<VerbInstance> implements XapagyComponent {

    private static final long serialVersionUID = -2216815505040421593L;

    /**
     * Default VI set, 0 lower bound, 1 upper bound, infinite number
     */
    public ViSet() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.instances.XapagyComponent#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppString(this);
    }

}
