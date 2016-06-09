/*
   This file is part of the Xapagy project
   Created on: Jul 15, 2014
 
   org.xapagy.debug.ShadowRecordComparator
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.util.Comparator;

import org.xapagy.instances.XapagyComponent;

/**
 * A comparator for ShadowRecords. It performs comparison based on the energy
 * based on the specified energy color.
 * 
 * @author Ladislau Boloni
 *
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
