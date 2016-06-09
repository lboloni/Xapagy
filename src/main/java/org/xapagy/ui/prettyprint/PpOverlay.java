/*
   This file is part of the Xapagy project
   Created on: Aug 30, 2010
 
   org.xapagy.ui.format.tostring.tostringOverlay
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConcept;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.Overlay;
import org.xapagy.concepts.Verb;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpOverlay {

    /**
     * Concise printing of an overlay: a simple, space separated list of the
     * concepts.
     * 
     * If it is fully energized, it does not write anything. If it is not, then
     * it is writing energy/area
     * 
     * It sorts the concepts from smaller area towards larger one
     * 
     * @param <T>
     * @param overlay
     * @param agent
     * @return
     */
    public static <T extends AbstractConcept> String ppConcise(
            Overlay<T> overlay, Agent agent) {
        List<SimpleEntry<T, Double>> list = overlay.getList();
        List<SimpleEntry<String, String>> listPrinted = new ArrayList<>();
        for (SimpleEntry<T, Double> entry : list) {
            AbstractConcept ac = entry.getKey();
            String temp = ac.getName();
            double value = entry.getValue();
            double area = 0;
            if (ac instanceof Concept) {
                area = agent.getConceptDB().getArea((Concept) ac);
            } else {
                area = agent.getVerbDB().getArea((Verb) ac);
            }
            if (area != value || area != 1.0) {
                temp +=
                        "=" + Formatter.fmt(entry.getValue()) + "/"
                                + Formatter.fmt(area);
            }
            listPrinted.add(new SimpleEntry<>(temp, ac.getName()));
        }
        Comparator<SimpleEntry<String, String>> comp =
                new Comparator<SimpleEntry<String, String>>() {

                    @Override
                    public int compare(SimpleEntry<String, String> o1,
                            SimpleEntry<String, String> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                };
        Collections.sort(listPrinted, comp);
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        // add the labels
        for (String label : overlay.getLabels()) {
            buffer.append(label + " ");
        }
        // add the overlay components, in decreasing weight
        for (int i = 0; i != listPrinted.size(); i++) {
            buffer.append(listPrinted.get(i).getKey());
            if (i != listPrinted.size() - 1) {
                buffer.append(" ");
            }
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * Detailed printing of an overlay concepts
     * 
     * @param <T>
     * @param overlay
     * @param agent
     * @return
     */
    public static <T extends AbstractConcept> String ppDetailed(
            Overlay<T> overlay, Agent agent) {
        Formatter fmt = new Formatter();
        List<SimpleEntry<T, Double>> list = overlay.getSortedByExplicitEnergy();
        if (list.isEmpty()) {
            fmt.add("Overlay: empty");
            return fmt.toString();
        }
        fmt.add("Overlay: energy = " + Formatter.fmt(overlay.getTotalEnergy()));
        fmt.indent();
        for (SimpleEntry<T, Double> entry : list) {
            AbstractConcept ac = entry.getKey();
            String temp = ac.getName() + " = " + entry.getValue() + " / ";
            if (ac instanceof Concept) {
                temp += agent.getConceptDB().getArea((Concept) ac);
            } else {
                temp += agent.getVerbDB().getArea((Verb) ac);
            }
            fmt.add(temp);
        }
        return fmt.toString();
    }

}
