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

import java.util.Collection;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.ui.formatters.XFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Mar 8, 2012
 */
public class PpHlsNewInstance {

    public static String pp(HlsNewInstance hlsni, Agent agent,
            PrintDetail detailLevel) {
        XFormatter fmt = new XFormatter(agent);
        fmt.add("HlsNewInstance id = " + hlsni.getIdentifier());
        fmt.indent();
        fmt.addPpc("Scene", hlsni.getScene());
        fmt.addPpc("Attributes", hlsni.getAttributes());
        fmt.is("Resolved", hlsni.isResolved());
        if (hlsni.isResolved()) {
            fmt.addPpc("ResolvedInstance", hlsni.getResolvedInstance());
        }
        return fmt.toString();
    }

    /**
     * Pretty print in a concise way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppConcise(HlsNewInstance hlsni, Agent agent) {
        return PpHlsNewInstance.pp(hlsni, agent, PrintDetail.DTL_CONCISE);
    }

    /**
     * Pretty print in a detailed way
     * 
     * @param focus
     * @param topLevel
     * @return
     */
    public static String ppDetailed(HlsNewInstance hlsni, Agent agent) {
        return PpHlsNewInstance.pp(hlsni, agent, PrintDetail.DTL_DETAIL);
    }

    /**
     * @param hlsNewInstances
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String ppList(Collection<HlsNewInstance> hlsNewInstances,
            Agent agent, PrintDetail detailLevel) {
        XFormatter fmt = new XFormatter(agent);
        for (HlsNewInstance hlsni : hlsNewInstances) {
            fmt.addPp(hlsni, detailLevel);
        }
        return fmt.toString();
    }

}
