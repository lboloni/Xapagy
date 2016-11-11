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
package org.xapagy.xapi;

import org.xapagy.concepts.Hardwired;

/**
 * A helper class which helps with words.
 * 
 * @author Ladislau Boloni
 * Created on: Oct 10, 2012
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
