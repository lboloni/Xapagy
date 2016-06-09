/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefAdjective
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import org.xapagy.concepts.ConceptOverlay;

/**
 * 
 * A reference which represents an adjective inside an S-ISA-ADJ VI
 * 
 * @author Ladislau Boloni
 * 
 */
public class XrefAdjective extends AbstractXrefCo implements XrefToCo {

    private static final long serialVersionUID = -1236654760161432443L;

    /**
     * @param referenceType
     * @param parent
     * @param positionInParent
     * @param text
     */
    public XrefAdjective(AbstractXrefVi parent, ConceptOverlay co, String text,
            XapiPositionInParent positionInParent) {
        super(XapiReferenceType.ADJECTIVE, parent, positionInParent, text, co);
        this.co = co;
    }

    /**
     * Creates a clone at the previous level
     * 
     * @param xapiVi
     * @return
     */
    @Override
    public XrefAdjective cloneL0Inner(XrefVi xapiVi) {
        XrefAdjective adjNew =
                new XrefAdjective(xapiVi, getCo(), getText(),
                        getPositionInParent());
        return adjNew;
    }

}
