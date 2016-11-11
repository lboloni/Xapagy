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

import org.xapagy.concepts.VerbOverlay;

/**
 * @author Ladislau Boloni
 * Created on: Dec 31, 2011
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
