/*
   This file is part of the Xapagy project
   Created on: Jul 29, 2012
 
   org.xapagy.reference.ReferenceResolutionException
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.reference;

import org.xapagy.ui.prettyprint.Formatter;

/**
 * An exception object which describes the fact that the resolution of a certain
 * reference was not successful.
 * 
 * @author Ladislau Boloni
 * 
 */
public class rrException extends Exception {

    public enum RreType {
        REF_PRONOUN, REF_SIMPLE
    };

    private static final long serialVersionUID = -9162508457364093352L;
    private String explanation;
    private RreType type;

    public rrException(RreType type) {
        this.type = type;
    }

    /**
     * @return the explanation
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * @param explanation
     *            the explanation to set
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        Formatter fmt = new Formatter();
        fmt.add("ReferenceResolutionException");
        fmt.indent();
        fmt.is("Type", type);
        fmt.add("Explanation:");
        fmt.addIndented(explanation);
        return fmt.toString();
    }
}
