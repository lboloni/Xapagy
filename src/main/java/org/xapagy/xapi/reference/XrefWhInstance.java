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
 * @author Ladislau Boloni
 * Created on: Dec 31, 2011
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
