/*
   This file is part of the Xapagy project
   Created on: Sep 17, 2010
 
   org.xapagy.model.InstanceSet
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.set;

import org.xapagy.instances.Instance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * 
 */
public class InstanceSet extends WeightedSet<Instance> implements
        XapagyComponent {

    private static final long serialVersionUID = -2216815505040421593L;

    public InstanceSet() {
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
