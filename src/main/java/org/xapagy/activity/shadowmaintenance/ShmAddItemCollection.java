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
 * Created on: Sep 23, 2012
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
