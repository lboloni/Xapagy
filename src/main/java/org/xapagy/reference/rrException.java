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
package org.xapagy.reference;

import org.xapagy.ui.formatters.Formatter;

/**
 * An exception object which describes the fact that the resolution of a certain
 * reference was not successful.
 * 
 * @author Ladislau Boloni
 * Created on: Jul 29, 2012
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
