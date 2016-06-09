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
