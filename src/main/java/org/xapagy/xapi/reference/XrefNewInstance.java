/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefNewInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import org.xapagy.concepts.ConceptOverlay;

/**
 * A new reference for a direct instance
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefNewInstance extends AbstractXrefCo implements XrefToInstance {

    private static final long serialVersionUID = 7936727998017489610L;

    public XrefNewInstance(XrefVi parent,
            XapiPositionInParent positionInParent, ConceptOverlay co,
            String text) {
        super(XapiReferenceType.TEMPL_INSTANCE, parent, positionInParent, text,
                co);
    }

    /**
     * Creates a clone to be used in the vi this is converted to a direct
     * reference!!!
     * 
     * @param xrefvi
     * @return
     */
    @Override
    public XrefDirect cloneL0Inner(XrefVi xrefvi) {
        XrefDirect xrefNew =
                new XrefDirect(xrefvi, getPositionInParent(), getCo(), text);
        return xrefNew;
    }
}
