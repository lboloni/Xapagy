/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefGroup
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A reference to a group
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefGroup extends AbstractXrefGroup implements XrefToInstance {

    private static final long serialVersionUID = 77243426189493267L;

    public XrefGroup(XrefVi parent, XapiPositionInParent positionInParent,
            List<XrefToInstance> members, String text) {
        super(XapiReferenceType.REF_GROUP, parent, positionInParent, text,
                members);
    }

    /**
     * Creates a deep clone for level L0
     * 
     * @param xrefvi
     * @return
     */
    @Override
    protected AbstractXapiReference cloneL0Inner(XrefVi xrefvi) {
        List<XrefToInstance> newmembers = new ArrayList<>();
        for (XrefToInstance x : getMembers()) {
            newmembers.add((XrefToInstance) x.cloneL0(xrefvi));
        }
        XrefGroup groupNew =
                new XrefGroup(xrefvi, getPositionInParent(), newmembers, text);
        return groupNew;
    }
}
