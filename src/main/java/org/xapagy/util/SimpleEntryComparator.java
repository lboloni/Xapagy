package org.xapagy.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;

/**
 * A comparator for a simple entry which has a double value
 * 
 * @author Ladislau Boloni
 * 
 * @param <T>
 */
public class SimpleEntryComparator<T> implements
        Comparator<SimpleEntry<T, Double>> {
    @Override
    public int compare(SimpleEntry<T, Double> o1, SimpleEntry<T, Double> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}