/*
   This file is part of the Xapagy project
   Created on: Dec 31, 2011
 
   org.xapagy.instances.XapagyComponent
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.instances;

/**
 * Xapagy components have identifiers, and thus can be looked up by the
 * identifier for instance in GUIs. The identifiers are generated from
 * IdentifierGenerator.
 * 
 * @author Ladislau Boloni
 * 
 */
public interface XapagyComponent {

    public String getIdentifier();
}
