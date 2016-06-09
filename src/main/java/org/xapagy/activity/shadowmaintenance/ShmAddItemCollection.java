/*
   This file is part of the Xapagy project
   Created on: Sep 23, 2012
 
   org.xapagy.activity.shadowmaintenance.ShmAddItemCollection
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.xapagy.algorithm.IProbabilityProportionalToSize;
import org.xapagy.algorithm.PpsNaive;

/**
 * 
 * A collection of an ShmAddItems.
 * 
 * It merges together ShmAddItems with the same object
 * 
 * @author Ladislau Boloni
 * 
 */
public class ShmAddItemCollection {

    private Map<String, ShmAddItem> collection = new HashMap<>();
    private IProbabilityProportionalToSize<ShmAddItem> pps = new PpsNaive<>();

    /**
     * Adds a new item to the collection
     * 
     * @param item
     */
    public void addShmAddItem(ShmAddItem item) {
        ShmAddItem prev = collection.get(item.getObject().getIdentifier());
        if (prev != null) {
            prev.addNewItem(item);
        } else {
            collection.put(item.getObject().getIdentifier(), item);
        }
    }

    /**
     * Choose n values
     * 
     * @param n
     * @param r
     * @return
     */
    public List<ShmAddItem> choose(int n, Random r) {
        List<ShmAddItem> list = new ArrayList<>();
        list.addAll(collection.values());
        List<ShmAddItem> retval =  pps.choose(list, n, r);
        if (retval == null) {
            // this can happen if the collection was empty
            return new ArrayList<>();
        }
        return retval;
    }
}
