/*
   This file is part of the Xapagy project
   Created on: Aug 16, 2010
 
   org.xapagy.storyvisualizer.model.AbstractConcept
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.concepts;

import java.io.Serializable;

/**
 * 
 * The common root of Concept and Verb
 * 
 * @author Ladislau Boloni
 * 
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
