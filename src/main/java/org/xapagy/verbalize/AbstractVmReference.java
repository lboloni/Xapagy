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
 * Created on: Nov 24, 2011
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
