/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
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
 * Created on: Jun 10, 2011
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
