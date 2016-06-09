/*
   This file is part of the Xapagy project
   Created on: Jun 11, 2011
 
   org.xapagy.ui.prettyprint.PpFocusShadowLinked
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FocusShadowLinked;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpFocusShadowLinked {

    /**
     * Fall back on detailed
     * 
     * @param sf
     * @param topLevel
     * @return
     */
    public static String ppConcise(FocusShadowLinked fsl, Agent agent) {
        return PpFocusShadowLinked.ppDetailed(fsl, agent);
    }

    public static String ppDetailed(FocusShadowLinked fsl, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("FocusShadowLinked:" + fsl.getFslType() + " is indirect: "
                + fsl.isIndirect());
        fmt.indent();
        if (fsl.isIndirect()) {
            fmt.add("Choice:" + PrettyPrint.ppConcise(fsl.getChoice(), agent));
        } else {
            fmt.add("Focus:" + PrettyPrint.ppConcise(fsl.getViFocus(), agent));
        }
        fmt.add("Shadow: " + PrettyPrint.ppConcise(fsl.getViShadow(), agent));
        fmt.add("Link: " + PrettyPrint.ppConcise(fsl.getViLinked(), agent)
                + " -- " + fsl.getFslType() + " -- "
                + Formatter.fmt(fsl.getLinkStrength(agent)));
        return fmt.toString();
    }

}
