/*
   This file is part of the Xapagy project
   Created on: Dec 27, 2011
 
   org.xapagy.xapi.AbstractXapiVi
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import java.util.HashMap;
import java.util.Map;

import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;

/**
 * A Xapi reference which in some sense is based around an event.
 * 
 * The two current major implementations of this are the XapiVi, which had been
 * clearly resolved to a Vi, and the XapiStatement which is a line in Xapi which
 * might be zero, one or two VIs
 * 
 * @author Ladislau Boloni
 * 
 */
public abstract class AbstractXrefVi extends AbstractXapiReference {

    private static final long serialVersionUID = -9020132692953578778L;
    /**
     * Shows which parts are missing
     */
    protected Map<ViPart, XapiReference> parts = new HashMap<>();
    protected ViType statementType;

    /**
     * @param referenceType
     * @param text
     */
    AbstractXrefVi(XapiReferenceType referenceType, XapiReference parent,
            XapiPositionInParent positionInParent, String text) {
        super(referenceType, parent, positionInParent, text);
    }

    public XrefAdjective getAdjective() {
        return (XrefAdjective) getPart(ViPart.Adjective);
    }

    public XapiReference getObject() {
        return parts.get(ViPart.Object);
    }

    public XapiReference getPart(ViPart part) {
        if (!ViStructureHelper.allowed(part, statementType)) {
            throw new Error("Reference not allowed!!!");
        }
        XapiReference refpart = parts.get(part);
        if (refpart == null) {
            throw new Error("Accessing a null part!!!");
        }
        return refpart;

    }

    /**
     * 
     * This can be either a XapiVi or a XapiStatement, depending on whether we
     * are in a XapiVi or XapiStatement
     * 
     * @return the quote statement
     */
    public AbstractXrefVi getQuote() {
        return (AbstractXrefVi) getPart(ViPart.Quote);
    }

    public XapiReference getQuoteScene() {
        return getPart(ViPart.QuoteScene);
    }

    public XrefToInstance getSubject() {
        return (XrefToInstance) getPart(ViPart.Subject);
    }

    public XrefVerb getVerb() {
        return (XrefVerb) getPart(ViPart.Verb);
    }

    public ViType getViType() {
        return statementType;
    }

    /**
     * Perform the initialization, from a list of parsed references
     */
    protected void initFromReferences(XapiReference subject,
            XapiReference verb, XapiReference object, XrefVi quote) {
        if (verb == null) {
            throw new Error("Cannot initialize with a null verb!!!");
        }
        switch (statementType) {
        case S_V_O: {
            parts.put(ViPart.Subject, subject);
            parts.put(ViPart.Verb, verb);
            parts.put(ViPart.Object, object);
            break;
        }
        case S_V: {
            parts.put(ViPart.Subject, subject);
            parts.put(ViPart.Verb, verb);
            break;
        }
        case S_ADJ: {
            parts.put(ViPart.Subject, subject);
            parts.put(ViPart.Verb, verb);
            parts.put(ViPart.Adjective, object);
            break;
        }
        case QUOTE: {
            parts.put(ViPart.Subject, subject);
            parts.put(ViPart.Verb, verb);
            parts.put(ViPart.QuoteScene, object);
            parts.put(ViPart.Quote, quote);
            break;
        }
        }
    }

    public void setPart(ViPart part, XapiReference reference) {
        if (!ViStructureHelper.allowed(part, statementType)) {
            throw new Error("Reference not allowed!");
        }
        parts.put(part, reference);
    }

    /**
     * @param quote
     *            the indirectStatement to set
     */
    public void setQuote(XapiReference quote) {
        setPart(ViPart.Quote, quote);
    }
}
