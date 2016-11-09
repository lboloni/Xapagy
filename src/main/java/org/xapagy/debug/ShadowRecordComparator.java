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
package org.xapagy.debug;

import java.util.Comparator;

import org.xapagy.instances.XapagyComponent;

/**
 * A comparator for ShadowRecords. It performs comparison based on the energy
 * based on the specified energy color.
 * 
 * @author Ladislau Boloni
 * Created on: Jul 15, 2014
 */
public class ShadowRecordComparator<T extends XapagyComponent> implements
        Comparator<ShadowRecord<T>> {

    private String sortBy;
    
    
    /**
     * @param sortBy
     */
    public ShadowRecordComparator(String sortBy) {
        super();
        this.sortBy = sortBy;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(ShadowRecord<T> arg0, ShadowRecord<T> arg1) {
        double val0 = arg0.getEnergy(sortBy);
        double val1 = arg1.getEnergy(sortBy);
        return Double.compare(val0, val1);
    }

}
