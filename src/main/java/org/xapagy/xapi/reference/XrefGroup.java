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
 * 
 * A reference to a group
 * 
 * @author Ladislau Boloni
 * Created on: Dec 31, 2011
 */
public class XrefGroup extends AbstractXrefGroup implements XrefToInstance {

    private static final long serialVersionUID = 77243426189493267L;

    public XrefGroup(XrefVi parent, XapiPositionInParent positionInParent,
            List<XrefToInstance> members, String text) {
        super(XapiReferenceType.REF_GROUP, parent, positionInParent, text,
                members);
    }

    /**
     * Creates a deep clone for level L0
     * 
     * @param xrefvi
     * @return
     */
    @Override
    protected AbstractXapiReference cloneL0Inner(XrefVi xrefvi) {
        List<XrefToInstance> newmembers = new ArrayList<>();
        for (XrefToInstance x : getMembers()) {
            newmembers.add((XrefToInstance) x.cloneL0(xrefvi));
        }
        XrefGroup groupNew =
                new XrefGroup(xrefvi, getPositionInParent(), newmembers, text);
        return groupNew;
    }
}
