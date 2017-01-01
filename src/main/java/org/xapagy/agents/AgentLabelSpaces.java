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
package org.xapagy.agents;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains the values of $Define-s, as well as current values of
 * namespaces. As these values are used for debugging and development and they
 * should not normally impact the functioning of the system, they had been
 * extracted here.
 * 
 * @author Ladislau Boloni
 * Created on: Feb 20, 2016
 */
public class AgentLabelSpaces implements Serializable {

    private static final long serialVersionUID = 3769443179410044801L;
    
    /**
     * The prefix attached to every namespace
     */
    public static final String NAMESPACE_PREFIX = "XNS_";
    
    @SuppressWarnings("unused")
    private Agent agent;
    /**
     * The collection of $Define values. Currently the only use of these are to prevent 
     * multiple inclusions of domains
     */
    private Set<String> defines = new HashSet<>();
    /**
     * A collection of all the namespaces the agent had in the past
     */
    private Set<String> namespaces = new HashSet<>();
    /**
     * The current namespace. Labels are prefixed with the namespace at creation time. 
     */
    private String namespace;

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }
    
    /**
     * Sets the current namespace. This might or might not be a new namespace.
     * 
     * @param namespace
     * @return true if a new namespace had been created
     */
    public boolean setNamespace(String namespace) {
        if (namespaces.contains(namespace)) {
            this.namespace = namespace;
            return false;
        }
        // we are creating a new namespace - FIXME: possible validation here.
        this.namespace = namespace;
        namespaces.add(namespace);
        return true;
    }

    /**
     * Creates a new namespace - which will be PREFIX-num where num is an integer that
     * describes the namespace created
     * @return
     */
    public String createNamespace() {
        namespace = NAMESPACE_PREFIX + namespaces.size();
        namespaces.add(namespace);    
        return namespace;
    }
    
    
    /**
     * Takes a label (starting with #), checks whether it has a namespace part, and if not, 
     * it completes the
     * 
     * @param label
     * @return
     */
    public String fullLabel(String label) {
         if (!label.startsWith("#")) {
             throw new Error("Labels should start with #, it was " + label);
         }
         String label2 = label.substring(1);
         // check if it already has a namespace
         if (label2.contains(".")) {
             return label;
         }
         return "#" + namespace + "." + label2;
    }
    
    
    
    /**
     * @param agent
     */
    public AgentLabelSpaces(Agent agent) {
        super();
        this.agent = agent;
        createNamespace();
    }

    public Set<String> getDefines() {
        return defines;
    }
}
