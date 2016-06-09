/*
   This file is part of the Xapagy project
   Created on: Jul 5, 2011
 
   org.xapagy.ui.prettyprint.PpMissing
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Hls;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpMissing {

    public static int printNBest = 3;

    /**
     * Prints the missing values
     * 
     * @param missing
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String pp(List<SimpleEntry<Hls, Double>> missing,
            Agent agent, PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        fmt.add("Headless shadows pointing to missing VIs");
        int count = 0;
        for (SimpleEntry<Hls, Double> entry : missing) {
            fmt.addWithMarginNote(Formatter.fmt(entry.getValue()),
                    PrettyPrint.pp(entry.getKey(), agent, detailLevel));
            count++;
            if (count >= PpMissing.printNBest) {
                fmt.add("... and " + (missing.size() - PpMissing.printNBest)
                        + " more...");
                break;
            }
        }
        return fmt.toString();
    }

}
