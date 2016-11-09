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
 * An exception thrown when a non-existent concept or verb is referred
 * 
 * @author Ladislau Boloni
 * Created on: May 24, 2015
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
