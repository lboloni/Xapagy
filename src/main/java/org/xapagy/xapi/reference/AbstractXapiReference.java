/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
package org.xapagy.xapi.reference;

import org.xapagy.reference.rrState;

/**
 * @author Ladislau Boloni
 * Created on: Dec 31, 2011
 */
public abstract class AbstractXapiReference implements XapiReference {

    private static final long serialVersionUID = -1661978936809207046L;
    private XapiReference parent;
    private XapiPositionInParent positionInParent;
    /**
     * The reference type. Note that not all of them can be implemented with
     * this class.
     */
    private XapiReferenceType referenceType;
    /**
     * This also works for instance resolution
     */
    protected rrState resolutionConfidence;
    protected String text;
    /**
     * The associated reference at the higher level Xapi
     */
    private XapiReference upperIdentity;
    protected XapiLevel xapiLevel;

    /**
     * 
     * 
     * @param referenceType
     * @param xapiLevel
     * @param parent
     * @param positionInParent
     * @param text
     */
    protected AbstractXapiReference(XapiReferenceType referenceType,
            XapiReference parent, XapiPositionInParent positionInParent,
            String text) {
        this.referenceType = referenceType;
        this.xapiLevel = XapiLevel.L1;
        this.text = text;
        this.parent = parent;
        this.positionInParent = positionInParent;
        this.resolutionConfidence = rrState.createUndetermined();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.xapi.reference.XapiReference#cloneForVi(org.xapagy.xapi.reference
     * .XrefVi)
     */
    @Override
    public final XapiReference cloneL0(XrefVi xrefvi) {
        if (xapiLevel != XapiLevel.L1) {
            throw new Error("Cloning for level L0 can be done only at level 1");
        }
        AbstractXapiReference ref = cloneL0Inner(xrefvi);
        ref.setUpperIdentity(this);
        ref.setResolutionConfidence(getResolutionConfidence());
        ref.xapiLevel = XapiLevel.L0;
        return ref;
    }

    protected AbstractXapiReference cloneL0Inner(XrefVi xrefvi) {
        throw new Error("cloneForVi not implemented in class"
                + getClass().getCanonicalName());
    }

    /**
     * @return the parent
     */
    @Override
    public XapiReference getParent() {
        return parent;
    }

    /**
     * @return the positionInParent
     */
    @Override
    public XapiPositionInParent getPositionInParent() {
        return positionInParent;
    }

    /**
     * @return the resolutionConfidence
     */
    @Override
    public rrState getResolutionConfidence() {
        return resolutionConfidence;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public XapiReferenceType getType() {
        return referenceType;
    }

    @Override
    public XapiReference getUpperIdentity() {
        return upperIdentity;
    }

    /**
     * @return the xapiLevel
     */
    @Override
    public XapiLevel getXapiLevel() {
        return xapiLevel;
    }

    /**
     * @param parent
     *            the parent to set
     */
    @Override
    public void setParent(XapiReference parent) {
        this.parent = parent;
    }

    /**
     * @param resolutionConfidence
     *            the resolutionConfidence to set
     */
    @Override
    public void setResolutionConfidence(rrState resolutionConfidence) {
        this.resolutionConfidence = resolutionConfidence;
    }

    @Override
    public void setUpperIdentity(XapiReference upperIdentity) {
        this.upperIdentity = upperIdentity;
    }

    @Override
    public void setXapiLevel(XapiLevel xapiLevel) {
        this.xapiLevel = xapiLevel;
    }

    @Override
    public String toString() {
        return "XapiReference: " + referenceType + ": " + text;
    }

}
