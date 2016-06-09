/*
   This file is part of the Xapagy project
   Created on: May 24, 2015
 
   org.xapagy.exceptions.MalformedConceptOrVerbName
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.exceptions;

/**
 * An exception thrown when a malformed concept or verb name is passed on
 * 
 * @author Ladislau Boloni
 *
 */
public class MalformedConceptOrVerbName extends Error {

    private static final long serialVersionUID = -6952936073148975083L;
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MalformedConceptOrVerbName [conceptName=" + conceptName
                + ", explanation=" + explanation + "]";
    }

    /**
     * @param conceptName
     * @param explanation
     */
    public MalformedConceptOrVerbName(String conceptName, String explanation) {
        super();
        this.conceptName = conceptName;
        this.explanation = explanation;
    }

    /**
     * @return the conceptName
     */
    public String getConceptName() {
        return conceptName;
    }

    /**
     * @return the explanation
     */
    public String getExplanation() {
        return explanation;
    }

    private String conceptName;
    private String explanation;

}
