/*
   This file is part of the Xapagy project
   Created on: Jun 11, 2011
 
   org.xapagy.ui.prettyprint.PpFslInterpration
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FslInterpretation;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpFslInterpretation {

    /**
     * Fall back on detailed
     * 
     * @param sf
     * @param topLevel
     * @return
     */
    public static String ppConcise(FslInterpretation fsli, Agent agent) {
        StringBuffer buf = new StringBuffer();
        buf.append("FSLI:");
        buf.append(fsli.getFsl().getFslType());
        buf.append(" -- "
                + PpVerbInstanceTemplate.ppConciseViTemplate(
                        fsli.getViInterpretation(), agent));
        buf.append(" -- " + Formatter.fmt(fsli.getTotalSupport(agent)));
        buf.append(" -- " + Formatter.fmt(fsli.getSupportFraction() * 100.0)
                + "%");
        return buf.toString();
    }

    /**
     * Detailed description
     * 
     * @param fsli
     * @param agent
     * @return
     */
    public static String ppDetailed(FslInterpretation fsli, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("FslInterpetation:" + fsli.getFsl().getFslType()
                + " totalSupport ="
                + Formatter.fmt(fsli.getTotalSupport(agent)));
        fmt.indent();
        fmt.add("verb instance template:");
        fmt.addIndented(PrettyPrint.ppDetailed(fsli.getViInterpretation(),
                agent));
        fmt.is("supportPercent", fsli.getSupportFraction());
        fmt.add("fsl:");
        fmt.addIndented(PrettyPrint.ppDetailed(fsli.getFsl(), agent));
        return fmt.toString();
    }

}
