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
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a concept or an instance
 * 
 * @author Lotzi Boloni
 * 
 */
public class Concept extends AbstractConcept implements Serializable {

    private static final long serialVersionUID = -1485596021344824281L;
    //private Object foreignKey = null;
    // a foreign object associated with a concept: for example, a synset
    //private transient Object foreignObject = null;
    private Set<Concept> incompatibilitySet = null;
    private boolean isFavorite;
    private Set<Concept> supersets = new HashSet<>();

    protected Concept() {
        // for deserialization
    }

    public Concept(String name, String identifier) {
        super(name, identifier);
    }

    public Set<Concept> getIncompatibilitySet() {
        return incompatibilitySet;
    }

    public Set<Concept> getSupersets() {
        return supersets;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setIncompatibilitySet(Set<Concept> incompatibilitySet) {
        this.incompatibilitySet = incompatibilitySet;
    }

    @Override
    public String toString() {
        return name;
    }

}
