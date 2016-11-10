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