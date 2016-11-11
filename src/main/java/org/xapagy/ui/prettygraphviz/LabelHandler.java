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
package org.xapagy.ui.prettygraphviz;

import java.util.HashMap;
import java.util.Map;

/**
 * This class creates labels for arbitrary classes to be used in creation of
 * graphwiz
 * 
 * @author lboloni
 * 
 */
public class LabelHandler {

    @SuppressWarnings("rawtypes")
    private Map<Class, Integer> counters = new HashMap<>();
    private Map<Object, String> labels = new HashMap<>();

    public boolean contains(Object o) {
        String label = labels.get(o);
        return label != null;
    }

    public String getLabel(Object o) {
        String label = labels.get(o);
        if (label == null) {
            Integer nextId = counters.get(o.getClass());
            if (nextId == null) {
                nextId = 1;
            }
            counters.put(o.getClass(), nextId + 1);
            label = o.getClass().getSimpleName() + "_" + nextId;
            labels.put(o, label);
        }
        return label;
    }
}
