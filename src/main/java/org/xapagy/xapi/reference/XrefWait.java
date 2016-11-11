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
package org.xapagy.xapi.reference;

/**
 * A reference to a wait in Xapi.
 * 
 * @author Ladislau Boloni
 * Created on: Nov 30, 2011
 */
public class XrefWait extends AbstractXapiReference {

    private static final long serialVersionUID = 6035015998279213667L;
    private double timeWait = 1.0; // default wait

    public XrefWait(double timeWait, XapiReference parent) {
        super(XapiReferenceType.WAIT, parent, XapiPositionInParent.WAIT, "");
        // assert parent.getType().equals(XapiReferenceType.STATEMENT);
        this.timeWait = timeWait;
    }

    /**
     * @return the timeWait
     */
    public double getTimeWait() {
        return timeWait;
    }

}
