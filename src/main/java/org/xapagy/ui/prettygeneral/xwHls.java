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
package org.xapagy.ui.prettygeneral;

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
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.prettyprint.PrintDetail;

/**
 * @author Ladislau Boloni
 * Created on: Jun 17, 2017
 */
public class xwHls {

	/**
	 * Print in a concise way: fall back onto the detailed 
	 * @param xw
	 * @param hls
	 * @param agent
	 * @return
	 */
    public static String xwConcise(IXwFormatter xw, Hls hls, Agent agent) {
    	return xwDetailed(xw, hls, agent);
    }

	
	/**
	 * Prints an HLS in a detailed way
	 * @param xw
	 * @param hls
	 * @param agent
	 * @return
	 */
    public static String xwDetailed(IXwFormatter xw, Hls hls, Agent agent) {
        xw.addLabelParagraph("HlsSupported id=", "" + hls.getIdentifier());
        xw.indent();
        xw.add("ViTemplate: (current)");
        xwVerbInstance.xwDetailed(xw, hls.getViTemplate(), agent);
        xw.add("ViTemplate: (original)");
        xwVerbInstance.xwDetailed(xw, hls.getViTemplateOriginal(), agent);
        // print the dependencies
        if (!hls.getDependencies().isEmpty()
                || !hls.getResolvedDependencies().isEmpty()) {
            xw.addLabelParagraph("Unresolved dependencies:");
            xw.indent();
            for (ViPart part : hls.getDependencies().keySet()) {
                HlsNewInstance hlsni = hls.getDependencies().get(part);
                xw.addP(PrettyPrint.ppDetailed(hlsni, agent));
            }
            xw.deindent();
            xw.addLabelParagraph("Resolved dependencies:");
            xw.indent();
            for (ViPart part : hls.getResolvedDependencies().keySet()) {
                HlsNewInstance hlsni = hls.getResolvedDependencies().get(part);
                xw.addP(PrettyPrint.ppDetailed(hlsni, agent));
            }
            xw.deindent();
        }
        xwHls.xwSupports(xw, hls, agent, PrintDetail.DTL_DETAIL);
        return xw.toString();
    }

    /**
     * Prints the supports of the HLS in a generic way
     *  
     * @param xw
     * @param hls
     * @param agent
     */
    public static void xwSupports(IXwFormatter xw, Hls hls, Agent agent, PrintDetail detailLevel) {
        xw.addLabelParagraph("Supports " + hls.getSupports().size());
        xw.indent();
        // print the supports in order
        List<FslInterpretation> listSupport = new ArrayList<>();
        listSupport.addAll(hls.getSupports());
        Collections.sort(listSupport, new FsliComparator(agent));
        Collections.reverse(listSupport);
        // the supports calculated according to different algorithms
        for (FslType alg : FslType.values()) {
            double value = hls.summativeSupport(alg, agent).getValue();
            if (value > 0.0) {
                xw.is(alg.toString().toLowerCase(), value);
            }
        }
        // if the detail level is there, add all the details
        if (detailLevel == PrintDetail.DTL_DETAIL) {
            for (FslInterpretation fsli : listSupport) {
                double value = fsli.getTotalSupport(agent);
                xw.addP(Formatter.fmt(value));
                xw.indent();
                xwFslInterpretation.xwConcise(xw, fsli, agent);
                xw.deindent();
            }
        }
    }
}
