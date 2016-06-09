/*
   This file is part of the Xapagy project
   Created on: May 24, 2015
 
   org.xapagy.exceptions.NoSuchConceptOrVerb
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.exceptions;

/**
 * An exception thrown when a non-existent concept or verb is referred
 * 
 * @author Ladislau Boloni
 *
 */
public class NoSuchConceptOrVerb extends Error {
    
    private static final long serialVersionUID = 9092130928025511120L;
    private String conceptName;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NoSuchConceptOrVerb [conceptName=" + conceptName + "]";
    }

    /**
     * @param conceptName
     */
    public NoSuchConceptOrVerb(String conceptName) {
        super();
        this.conceptName = conceptName;
    }

    /**
     * @return the conceptName
     */
    public String getConceptName() {
        return conceptName;
    }
    
    

}
