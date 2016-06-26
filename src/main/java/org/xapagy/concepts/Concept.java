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
