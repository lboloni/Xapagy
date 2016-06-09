/*
   This file is part of the Xapagy project
   Created on: Jun 10, 2011
 
   org.xapagy.ui.prettyprint.PpHlsSupported
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.FsliComparator;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpHls {

    public static String pp(Hls hls, Agent agent, PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        fmt.add("HlsSupported id=" + hls.getIdentifier());
        fmt.indent();
        fmt.add("ViTemplate: (current)"
                + PrettyPrint.pp(hls.getViTemplate(), agent, detailLevel));
        fmt.add("ViTemplate: (original)"
                + PrettyPrint.pp(hls.getViTemplateOriginal(), agent,
                        detailLevel));
        // print the dependencies
        if (!hls.getDependencies().isEmpty()
                || !hls.getResolvedDependencies().isEmpty()) {
            fmt.add("Unresolved dependencies:");
            fmt.indent();
            for (ViPart part : hls.getDependencies().keySet()) {
                HlsNewInstance hlsni = hls.getDependencies().get(part);
                fmt.add(PrettyPrint.pp(hlsni, agent, detailLevel));
            }
            fmt.deindent();
            fmt.add("Resolved dependencies:");
            fmt.indent();
            for (ViPart part : hls.getResolvedDependencies().keySet()) {
                HlsNewInstance hlsni = hls.getResolvedDependencies().get(part);
                fmt.add(PrettyPrint.pp(hlsni, agent, detailLevel));
            }
            fmt.deindent();
        }
        PpHls.ppSupports(hls, agent, detailLevel, fmt);
        return fmt.toString();
    }

    /**
     * Pretty print in a concise way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppConcise(Hls hls, Agent agent) {
        return PpHls.pp(hls, agent, PrintDetail.DTL_CONCISE);
    }

    /**
     * Pretty print in a detailed way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppDetailed(Hls hls, Agent agent) {
        return PpHls.pp(hls, agent, PrintDetail.DTL_DETAIL);
    }

    /**
     * Prints the supports
     * 
     * @param hls
     * @param agent
     * @param detailLevel
     * @param fmt
     */
    public static void ppSupports(Hls hls, Agent agent,
            PrintDetail detailLevel, Formatter fmt) {
        fmt.add("Supports " + hls.getSupports().size());
        fmt.indent();
        // print the supports in order
        List<FslInterpretation> listSupport = new ArrayList<>();
        listSupport.addAll(hls.getSupports());
        Collections.sort(listSupport, new FsliComparator(agent));
        Collections.reverse(listSupport);
        // the supports calculated according to different algorithms
        for (FslType alg : FslType.values()) {
            double value = hls.summativeSupport(alg, agent).getValue();
            if (value > 0.0) {
                fmt.is(alg.toString().toLowerCase(), value);
            }
        }
        // if the detail level is there, add all the details
        if (detailLevel == PrintDetail.DTL_DETAIL) {
            Formatter fmt2 = new Formatter();
            for (FslInterpretation fsli : listSupport) {
                double value = fsli.getTotalSupport(agent);
                fmt2.addWithMarginNote(Formatter.fmt(value),
                        PrettyPrint.ppConcise(fsli, agent));
            }
            fmt.add(fmt2.toString());
        }
    }
}
