/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefRelational
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A relational reference
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefRelational extends AbstractXrefRelational implements
        XrefToInstance {

    private static final long serialVersionUID = -9044368883424353307L;

    public XrefRelational(XrefVi parent, XapiPositionInParent positionInParent,
            List<XrefToInstance> instances, List<XrefToVo> relations,
            String text) {
        super(XapiReferenceType.REF_RELATIONAL, parent, positionInParent,
                instances, relations, text);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.xapi.reference.XapiReference#cloneForVi(org.xapagy.xapi.reference
     * .XrefVi)
     */
    @Override
    protected AbstractXapiReference cloneL0Inner(XrefVi xrefvi) {
        // create a a deep copy
        List<XrefToInstance> newInstances = new ArrayList<>();
        for (XrefToInstance xref : getInstances()) {
            newInstances.add((XrefToInstance) xref.cloneL0(xrefvi));
        }
        List<XrefToVo> newRelations = new ArrayList<>();
        for (XrefToVo xref : getRelations()) {
            newRelations.add((XrefToVo) xref.cloneL0(xrefvi));
        }
        XrefRelational relrefnew =
                new XrefRelational(xrefvi, getPositionInParent(), newInstances,
                        newRelations, getText());
        return relrefnew;
    }

}
