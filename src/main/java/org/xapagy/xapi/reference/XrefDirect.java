/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefDirect
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import org.xapagy.concepts.ConceptOverlay;

/**
 * A direct reference using a ConceptOverlay to an instance
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefDirect extends AbstractXrefCo implements XrefToInstance {

    private static final long serialVersionUID = 7936727998017489610L;

    public XrefDirect(XrefVi parent, XapiPositionInParent positionInParent,
            ConceptOverlay co, String text) {
        super(XapiReferenceType.REF_DIRECT, parent, positionInParent, text, co);
    }

    /**
     * Creates a clone to be used in the vi
     * 
     * @param xrefvi
     * @return
     */
    @Override
    public AbstractXapiReference cloneL0Inner(XrefVi xrefvi) {
        XrefDirect xrefNew =
                new XrefDirect(xrefvi, getPositionInParent(), getCo(), text);
        return xrefNew;
    }

}
