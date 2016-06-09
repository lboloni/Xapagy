/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.xapi.reference.AbstractXrefRelational
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi.reference;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ladislau Boloni
 * 
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
