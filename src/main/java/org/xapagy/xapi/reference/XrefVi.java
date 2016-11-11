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

import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * A VI's reference in Xapi
 * 
 * @author Ladislau Boloni
 * Created on: Nov 30, 2011
 */
public class XrefVi extends AbstractXrefVi {

    /**
     * 
     */
    private static final long serialVersionUID = -6651537716898611882L;

    /**
     * Creates a XapiVi from a XrefStatement
     * 
     * @param statement
     */
    public XrefVi(XrefStatement statement,
            XapiPositionInParent positionInParent, String text) {
        super(XapiReferenceType.VI, statement, positionInParent, text);
        this.statementType = statement.getViType();
        this.xapiLevel = XapiLevel.L0;
        this.resolutionConfidence = statement.getResolutionConfidence();
        for (ViPart part : ViStructureHelper.getAllowedParts(statementType)) {
            if (part != ViPart.Quote) {
                XapiReference viCopy = statement.getPart(part).cloneL0(this);
                parts.put(part, viCopy);
            } else {
                // create the new XapiVi for the quote
                XrefStatement quoteStatement =
                        (XrefStatement) statement.getQuote();
                XrefVi xapiViQuote =
                        new XrefVi(quoteStatement, XapiPositionInParent.QUOTE,
                                quoteStatement.getText());
                parts.put(part, xapiViQuote);
            }
        }
    }

    /**
     * Constructor for initializing a XapiVi from first principles...
     * 
     * FIXME: this is problematic, because it does not create the components!!!
     * 
     * only used from ImplicitCreationHelper
     * 
     * @param subject
     * @param verb
     * @param object
     */
    public XrefVi(XrefStatement statement,
            XapiPositionInParent positionInParent, String text,
            ViType statementType, XapiReference ref1, XapiReference ref2,
            XapiReference ref3) {
        super(XapiReferenceType.VI, statement, positionInParent, text);
        this.statementType = statementType;
        this.xapiLevel = XapiLevel.L0;
        this.resolutionConfidence = statement.getResolutionConfidence();
        initFromReferences(ref1, ref2, ref3, null);
    }

    @Override
    public XrefToInstance getObject() {
        return (XrefToInstance) parts.get(ViPart.Object);
    }

    @Override
    public ViType getViType() {
        return statementType;
    }

    /**
     * @param quote
     *            the indirectStatement to set
     */
    public void setQuoteStatement(XrefVi quote) {
        parts.put(ViPart.Quote, quote);
    }

    /**
     * This needs to be changed for the create!!!
     * 
     * @param statementType
     */
    public void setViType(ViType statementType) {
        this.statementType = statementType;
    }

    @Override
    public String toString() {
        return PrettyPrint.ppConcise(this, null);
    }

}
