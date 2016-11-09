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
package org.xapagy.exceptions;

/**
 * An exception thrown when a malformed concept or verb name is passed on
 * 
 * @author Ladislau Boloni
 * Created on: May 24, 2015
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
