/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.AbstractXrefGroup
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ladislau Boloni
 * 
 */
public abstract class AbstractXrefGroup extends AbstractXapiReference {

    private static final long serialVersionUID = -5117912431192737944L;

    private List<XrefToInstance> members;

    /**
     * @param referenceType
     * @param parent
     * @param positionInParent
     * @param text
     */
    protected AbstractXrefGroup(XapiReferenceType referenceType,
            XapiReference parent, XapiPositionInParent positionInParent,
            String text, List<XrefToInstance> cos) {
        super(referenceType, parent, positionInParent, text);
        this.members = new ArrayList<>();
        this.members.addAll(cos);
    }

    /**
     * @return the cos
     */
    public List<XrefToInstance> getMembers() {
        return members;
    }
}
