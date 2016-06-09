/*
   This file is part of the Xapagy project
   Created on: Nov 24, 2011
 
   org.xapagy.verbalize.AbstractVmReference
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.io.Serializable;

import org.xapagy.instances.XapagyComponent;
import org.xapagy.xapi.reference.XapiReference;

/**
 * A verbal memory reference associates an internal Xapagy object (an instance,
 * VI, CO or VO) with a XapiReference which was associated with it. It also
 * stores the time when the access had been done.
 * 
 * @author Ladislau Boloni
 * 
 */
public abstract class AbstractVmReference implements Serializable {

    private static final long serialVersionUID = -3535965639775889868L;
    private XapagyComponent referredObject;
    private double time;
    private XapiReference xapiReference;

    /**
     * @param referenceText
     * @param time
     * @param xapiVi
     */
    public AbstractVmReference(XapagyComponent referredObject, double time,
            XapiReference xapiReference) {
        super();
        this.referredObject = referredObject;
        this.time = time;
        this.xapiReference = xapiReference;
    }

    /**
     * @return the referredObject
     */
    public XapagyComponent getReferredObject() {
        return referredObject;
    }

    /**
     * @return the time
     */
    public double getTime() {
        return time;
    }

    /**
     * @return the xapiVi of which this reference is part of
     */
    public XapiReference getXapiReference() {
        return xapiReference;
    }

}
