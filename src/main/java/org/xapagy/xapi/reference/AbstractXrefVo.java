/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.AbstractXrefVo
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import org.xapagy.concepts.VerbOverlay;

/**
 * @author Ladislau Boloni
 * 
 */
public abstract class AbstractXrefVo extends AbstractXapiReference {

    /**
     * 
     */
    private static final long serialVersionUID = -6847058492093622974L;

    /**
     * The VO into which the adjective had been resolved
     */
    private VerbOverlay vo;

    /**
     * @param referenceType
     * @param parent
     * @param positionInParent
     * @param text
     */
    protected AbstractXrefVo(XapiReferenceType referenceType,
            XapiReference parent, XapiPositionInParent positionInParent,
            String text, VerbOverlay vo) {
        super(referenceType, parent, positionInParent, text);
        this.vo = vo;
    }

    /**
     * @return the vo
     */
    public VerbOverlay getVo() {
        return vo;
    }

}
