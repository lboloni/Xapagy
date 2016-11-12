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
import org.xapagy.headless_shadows.HlsCharacterization;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Jun 12, 2011
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
