/*
   This file is part of the Xapagy project
   Created on: Oct 10, 2012
 
   org.xapagy.xapi.WordNamingConventions
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.xapi;

import org.xapagy.concepts.Hardwired;

/**
 * A helper class which helps with words.
 * 
 * @author Ladislau Boloni
 * 
 */
public class WordNamingConventions {

    /**
     * Returns a lead letter for a concept word
     * 
     * @param cw
     * 
     */
    public static char getConceptWordLetter(ConceptWord cw) {
        String text = cw.getTextFormat();
        char c;
        if (WordNamingConventions.isANegation(cw)) {
            c = text.charAt(Hardwired.CONCEPT_PREFIX_NEGATION.length());
        } else {
            c = text.charAt(0);
        }
        return Character.toUpperCase(c);
    }

    /**
     * Returns a lead letter for a concept word
     * 
     * @param cw
     * 
     */
    public static char getVerbWordLetter(VerbWord vw) {
        String text = vw.getTextFormat();
        char c;
        if (WordNamingConventions.isANegation(vw)) {
            c = text.charAt(Hardwired.CONCEPT_PREFIX_NEGATION.length());
        } else {
            c = text.charAt(0);
        }
        return Character.toUpperCase(c);
    }

    /**
     * Checks whether a concept word is a negation
     * 
     * These words echo the negation concepts, might not have a long future in
     * Xapagy, but they are currently useful.
     * 
     * @param cw
     * @return
     */
    public static boolean isANegation(ConceptWord cw) {
        return cw.getTextFormat().startsWith(Hardwired.CONCEPT_PREFIX_NEGATION);
    }

    /**
     * Checks whether a verb word is a negation
     * 
     * These words echo the negation concepts, might not have a long future in
     * Xapagy, but they are currently useful.
     * 
     * @param vw
     * @return
     */
    public static boolean isANegation(VerbWord vw) {
        return vw.getTextFormat().startsWith(Hardwired.CONCEPT_PREFIX_NEGATION);
    }

}
