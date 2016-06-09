/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.AbstractXrefCo
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import org.xapagy.concepts.ConceptOverlay;

/**
 * @author Ladislau Boloni
 * 
 */
public abstract class AbstractXrefCo extends AbstractXapiReference {
    /**
     * 
     */
    private static final long serialVersionUID = 5454460443429236213L;

    /**
     * The CO into which the adjective had been resolved
     */
    protected ConceptOverlay co;

    /**
     * @param referenceType
     * @param parent
     * @param positionInParent
     * @param text
     */
    protected AbstractXrefCo(XapiReferenceType referenceType,
            XapiReference parent, XapiPositionInParent positionInParent,
            String text, ConceptOverlay co) {
        super(referenceType, parent, positionInParent, text);
        this.co = co;
    }

    /**
     * @return the co
     */
    public ConceptOverlay getCo() {
        return co;
    }

}
