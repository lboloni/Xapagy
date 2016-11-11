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

/**
 * An exception thrown when a regular Xapi statement was malformed
 * @author Ladislau Boloni
 * Created on: Mar 22, 2015
 */
public class XapiParserException extends Exception {

    private static final long serialVersionUID = -6351708254865469908L;
    private String diagnosis;

    /**
     * @param diagnosis
     */
    public XapiParserException(String diagnosis) {
        super();
        this.diagnosis = diagnosis;
    }

    /**
     * @return the diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

}
