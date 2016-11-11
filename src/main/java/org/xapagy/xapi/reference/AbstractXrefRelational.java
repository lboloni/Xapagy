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
public abstract class AbstractXrefRelational extends AbstractXapiReference {

    private static final long serialVersionUID = -1643251702921080940L;
    private List<XrefToInstance> instances;
    private List<XrefToVo> relations;

    public AbstractXrefRelational(XapiReferenceType referenceType,
            XrefVi parent, XapiPositionInParent positionInParent,
            List<XrefToInstance> instances, List<XrefToVo> relations,
            String text) {
        super(referenceType, parent, positionInParent, text);
        if (instances.size() != relations.size() + 1) {
            throw new Error(
                    "createXrRelational: the number of concepts must be one more than the relations!");
        }
        this.instances = new ArrayList<>();
        this.instances.addAll(instances);
        this.relations = new ArrayList<>();
        this.relations.addAll(relations);
    }

    /**
     * @return the cos
     */
    public List<XrefToInstance> getInstances() {
        return instances;
    }

    /**
     * @return the vos
     */
    public List<XrefToVo> getRelations() {
        return relations;
    }

}
