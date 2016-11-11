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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ladislau Boloni
 * Created on: Dec 31, 2011
 */
public abstract class AbstractXrefGroup extends AbstractXapiReference {

    private static final long serialVersionUID = -5117912431192737944L;

    private List<XrefToInstance> members;

    /**
     * @param referenceType
     * @param parent
     * @param positionInParent
     * @param text
     */
    protected AbstractXrefGroup(XapiReferenceType referenceType,
            XapiReference parent, XapiPositionInParent positionInParent,
            String text, List<XrefToInstance> cos) {
        super(referenceType, parent, positionInParent, text);
        this.members = new ArrayList<>();
        this.members.addAll(cos);
    }

    /**
     * @return the cos
     */
    public List<XrefToInstance> getMembers() {
        return members;
    }
}
