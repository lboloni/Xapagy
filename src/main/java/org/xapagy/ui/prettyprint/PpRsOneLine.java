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
import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.debug.storygenerator.RsOneLine;

/**
 * @author Ladislau Boloni
 * Created on: Apr 4, 2013
 */
public class PpRsOneLine {

    /**
     * Fall back on detailed
     * 
     * @param rsol
     * @param agent
     * @return
     */
    public static String ppConcise(RsOneLine rsol, Agent agent) {
        return PpRsOneLine.ppDetailed(rsol, agent);
    }

    public static String ppDetailed(RsOneLine rsol, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("RsOneLine");
        fmt.indent();
        fmt.add("" + rsol.getLineNo() + ":"
                + rsol.getAbStory().getLine(rsol.getLineNo()));
        fmt.add("LoopItems");
        fmt.indent();
        for (AbstractLoopItem li : rsol.getLoopItems()) {
            fmt.add(PpLoopItem.ppDetailed(li, agent));
        }
        fmt.deindent();
        fmt.deindent();
        return fmt.toString();
    }

}
