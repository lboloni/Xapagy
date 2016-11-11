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

import org.xapagy.concepts.ConceptOverlay;

/**
 * 
 * A reference which represents an adjective inside an S-ISA-ADJ VI
 * 
 * @author Ladislau Boloni
 * Created on: Dec 31, 2011
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
