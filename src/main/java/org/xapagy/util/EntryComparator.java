/*
   This file is part of the Xapagy project
   Created on: Jan 27, 2011
 
   org.xapagy.util.EntryComparator
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.util;

import java.util.Comparator;
import java.util.Map.Entry;

/**
 * @author Ladislau Boloni
 * 
 */
public class EntryComparator<T> implements Comparator<Entry<T, Double>> {
    @Override
    public int compare(Entry<T, Double> o1, Entry<T, Double> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
