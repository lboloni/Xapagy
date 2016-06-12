/*
   This file is part of the Xapagy project
   Created on: Nov 30, 2011
 
   org.xapagy.xapi.XapiWait
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

/**
 * A reference to a wait in Xapi.
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefWait extends AbstractXapiReference {

    private static final long serialVersionUID = 6035015998279213667L;
    private double timeWait = 1.0; // default wait

    public XrefWait(double timeWait, XapiReference parent) {
        super(XapiReferenceType.WAIT, parent, XapiPositionInParent.WAIT, "");
        // assert parent.getType().equals(XapiReferenceType.STATEMENT);
        this.timeWait = timeWait;
    }

    /**
     * @return the timeWait
     */
    public double getTimeWait() {
        return timeWait;
    }

}
