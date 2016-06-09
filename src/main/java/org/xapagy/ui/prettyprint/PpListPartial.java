/*
   This file is part of the Xapagy project
   Created on: Sep 14, 2011
 
   org.xapagy.ui.prettyprint.PpListPartial
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.List;

import org.xapagy.agents.Agent;

/**
 * Helper function for pretty printing a partial list
 * 
 * @author Ladislau Boloni
 * 
 */
public class PpListPartial {

    /**
     * Prints a partial list, if necessary truncates it (but specifies how many
     * were left out)
     * 
     * @param list
     * @param agent
     * @param detailLevel
     * @param max
     * @return
     */
    public static <T> String ppListPartial(List<T> list, Agent agent,
            PrintDetail detailLevel, int max) {
        Formatter fmt = new Formatter();
        int i = 0;
        while (true) {
            if (i >= list.size()) {
                // list was smaller than max
                break;
            }
            if (i >= max) {
                if (i < list.size()) {
                    fmt.add("... and " + (list.size() - i) + " more.");
                }
                break;
            }
            fmt.add(PrettyPrint.pp(list.get(i), agent, detailLevel));
            i++;
        }
        return fmt.toString();
    }

}
