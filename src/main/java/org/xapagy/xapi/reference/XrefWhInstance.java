/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.XrefWhInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import org.xapagy.concepts.ConceptOverlay;

/**
 * @author Ladislau Boloni
 * 
 */
public class XrefWhInstance extends AbstractXapiReference implements
        XrefToInstance {

    private static final long serialVersionUID = 7936727998017489610L;
    private ConceptOverlay co;

    public XrefWhInstance(XrefVi parent, XapiPositionInParent positionInParent,
            ConceptOverlay co, String text) {
        super(XapiReferenceType.WH_INSTANCE, parent, positionInParent, text);
        this.co = co;
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
        XrefWhInstance whnew =
                new XrefWhInstance(xrefvi, getPositionInParent(), getCo(),
                        getText());
        return whnew;
    }

    /**
     * @return the co
     */
    public ConceptOverlay getCo() {
        return co;
    }

}
