/*
   This file is part of the Xapagy project
   Created on: Jun 12, 2011
 
   org.xapagy.ui.prettyprint.PrHlsElaboration
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.HlsCharacterization;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpHlsCharacterization {

    public static String pp(HlsCharacterization hls, Agent agent,
            PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        fmt.add("HlsCharacterization");
        fmt.indent();
        fmt.add("instance =" + PrettyPrint.ppConcise(hls.getInstance(), agent));
        fmt.add("attributes ="
                + PrettyPrint.ppConcise(hls.getAttributes(), agent));
        return fmt.toString();
    }

    /**
     * Pretty print in a concise way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppConcise(HlsCharacterization hls, Agent agent) {
        return PpHlsCharacterization.pp(hls, agent, PrintDetail.DTL_CONCISE);
    }

    /**
     * Pretty print in a detailed way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppDetailed(HlsCharacterization hls, Agent agent) {
        return PpHlsCharacterization.pp(hls, agent, PrintDetail.DTL_DETAIL);
    }
}
