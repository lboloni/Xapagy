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
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Jun 11, 2011
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
