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
 * A relational reference
 * 
 * @author Ladislau Boloni
 * Created on: Dec 31, 2011
 */
public class XrefRelational extends AbstractXrefRelational implements
        XrefToInstance {

    private static final long serialVersionUID = -9044368883424353307L;

    public XrefRelational(XrefVi parent, XapiPositionInParent positionInParent,
            List<XrefToInstance> instances, List<XrefToVo> relations,
            String text) {
        super(XapiReferenceType.REF_RELATIONAL, parent, positionInParent,
                instances, relations, text);
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
        // create a a deep copy
        List<XrefToInstance> newInstances = new ArrayList<>();
        for (XrefToInstance xref : getInstances()) {
            newInstances.add((XrefToInstance) xref.cloneL0(xrefvi));
        }
        List<XrefToVo> newRelations = new ArrayList<>();
        for (XrefToVo xref : getRelations()) {
            newRelations.add((XrefToVo) xref.cloneL0(xrefvi));
        }
        XrefRelational relrefnew =
                new XrefRelational(xrefvi, getPositionInParent(), newInstances,
                        newRelations, getText());
        return relrefnew;
    }

}
