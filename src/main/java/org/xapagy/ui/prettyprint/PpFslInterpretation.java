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

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Jun 11, 2011
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
