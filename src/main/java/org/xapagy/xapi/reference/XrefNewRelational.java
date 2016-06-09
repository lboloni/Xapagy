/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefNewRelational
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * A relational reference for the template
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefNewRelational extends AbstractXrefRelational implements
        XrefToInstance {

    private static final long serialVersionUID = -9044368883424353307L;

    public XrefNewRelational(XrefVi parent,
            XapiPositionInParent positionInParent,
            List<XrefToInstance> instances, List<XrefToVo> references,
            String text) {
        super(XapiReferenceType.TEMPL_RELATIONAL, parent, positionInParent,
                instances, references, text);
    }

    /**
     * This one creates a simple relational!!!
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
