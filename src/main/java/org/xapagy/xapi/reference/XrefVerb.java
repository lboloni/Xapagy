/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefVerb
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import org.xapagy.concepts.VerbOverlay;

/**
 * @author Ladislau Boloni
 * 
 */
public class XrefVerb extends AbstractXrefVo implements XrefToVo {

    private static final long serialVersionUID = -6273044502692072062L;

    /**
     * @param referenceType
     * @param parent
     * @param positionInParent
     * @param text
     */
    public XrefVerb(AbstractXrefVi parent, VerbOverlay vo, String text,
            XapiPositionInParent positionInParent) {
        super(XapiReferenceType.VERB, parent, positionInParent, text, vo);
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
        XrefVerb verbNew =
                new XrefVerb(xrefvi, getVo(), getText(), getPositionInParent());
        return verbNew;
    }

}
