/*
   This file is part of the Golyoscsapagy project
   Created on: June 20, 2009
 
   gcs2.ui.formatting.TimeCounter
 
   Copyright (c) 2008,2009 Ladislau Boloni
 */
package org.xapagy.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <code>golyoscsapagy.ui.text.TimeCounter</code>
 * 
 * @todo describe
 * 
 * @author Ladislau Boloni (lotzi.boloni@gmail.com)
 */

public class TimeCounter {

    private final Map<String, Long> ends = new HashMap<>();
    private final List<String> labels = new ArrayList<>();
    private final Map<String, Long> starts = new HashMap<>();

    /**
     * Ends counting on a label
     * 
     * @param label
     */
    public void end(final String label) {
        if (starts.get(label) == null) {
            throw new Error("The label " + label + " was not started ");
        }
        ends.put(label, System.currentTimeMillis());
    }

    /**
     * Formats for a label
     * 
     * @param label
     * @return
     */
    public String getLabelString(final String label) {
        boolean finished = true;
        final Long start = starts.get(label);
        if (start == null) {
            throw new Error("The label " + label + " was not started ");
        }
        Long end = ends.get(label);
        if (end == null) {
            end = System.currentTimeMillis();
            finished = false;
        }
        if (finished) {
            return label + " elapsed "
                    + TextUiHelper.formatTimeInterval(end - start)
                    + ", finished.";
        } else {
            return label + " elapsed "
                    + TextUiHelper.formatTimeInterval(end - start)
                    + ", still running.";
        }
    }

    /**
     * Starts the counting on a label
     * 
     * @param label
     */
    public void start(final String label) {
        if (labels.contains(label)) {
            throw new Error("Can not start the same label twice!");
        }
        labels.add(label);
        starts.put(label, System.currentTimeMillis());
    }

    /**
     * Formats all the current labels
     * 
     * @return
     */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("TimeCounter: " + labels.size() + " labels\n");
        for (final String label : labels) {
            buffer.append("\t" + getLabelString(label) + "\n");
        }
        return buffer.toString();
    }

}
