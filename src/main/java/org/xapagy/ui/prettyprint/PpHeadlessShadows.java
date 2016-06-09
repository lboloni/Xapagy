/*
   This file is part of the Xapagy project
   Created on: Jun 10, 2011
 
   org.xapagy.ui.prettyprint.PpHeadlessShadows
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.util.SimpleEntryComparator;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpHeadlessShadows {

    /**
     * Pretty print in a concise way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppConcise(HeadlessComponents hlss, Agent agent) {
        return PpHeadlessShadows.ppPrint(hlss, agent, PrintDetail.DTL_CONCISE,
                FslType.SUCCESSOR, true);
    }

    /**
     * Pretty print in a detailed way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppDetailed(HeadlessComponents hlss, Agent agent) {
        return PpHeadlessShadows.ppPrint(hlss, agent, PrintDetail.DTL_DETAIL,
                FslType.SUCCESSOR, true);
    }

    public static String ppPrint(HeadlessComponents hlc, Agent agent,
            PrintDetail detailLevel, FslType algorithm, boolean printAll) {
        Formatter fmt = new Formatter();
        fmt.add("Headless shadows: total = " + hlc.getHlss().size());
        // sort them according to
        List<SimpleEntry<Hls, Double>> hlsList = new ArrayList<>();
        for (Hls hls : hlc.getHlss()) {
            double value = hls.summativeSupport(algorithm, agent).getValue();
            if (printAll || value > 0.0) {
                hlsList.add(new SimpleEntry<>(hls, value));
            }
        }
        Collections.sort(hlsList, new SimpleEntryComparator<Hls>());
        Collections.reverse(hlsList);
        for (SimpleEntry<Hls, Double> entry : hlsList) {
            // fmt.add(PrettyPrint.pp(entry, agent, detailLevel));
            String text = PrettyPrint.pp(entry.getKey(), agent, detailLevel);
            fmt.addWithMarginNote(Formatter.fmt(entry.getValue()), text);
        }
        return fmt.toString();
    }

}
