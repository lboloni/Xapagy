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
package org.xapagy.concepts;

import java.io.Serializable;

/**
 * 
 * The common root of Concept and Verb
 * 
 * @author Ladislau Boloni
 * Created on: Aug 16, 2010
 */
public abstract class AbstractConcept implements Serializable {

    private static final long serialVersionUID = -3979669000242254548L;
    /**
     * A comment, used to document the concept
     */
    private String documentation = "No documentation";
    private String identifier;
    protected String name;

    // for deserialization
    protected AbstractConcept() {

    }

    public AbstractConcept(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractConcept other = (AbstractConcept) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    
    public String getDocumentation() {
        return documentation;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

}
