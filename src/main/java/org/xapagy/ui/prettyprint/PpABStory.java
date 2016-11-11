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
import org.xapagy.autobiography.ABStory;

/**
 * @author Ladislau Boloni
 * Created on: Sep 24, 2012
 */
public class PpABStory {

    public static String pp(ABStory abs, Agent agent, PrintDetail detailLevel) {
        if (detailLevel == PrintDetail.DTL_DETAIL) {
            return PpABStory.ppDetailed(abs, agent);
        }
        if (detailLevel == PrintDetail.DTL_CONCISE) {
            return PpABStory.ppConcise(abs, agent);
        }
        throw new Error("Unsupported detailLevel" + detailLevel);
    }

    public static String ppConcise(ABStory abs, Agent agent) {
        return abs.toString();
    }

    /**
     * Detailed printing, lists the
     * 
     * @param choice
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String ppDetailed(ABStory abs, Agent agent) {
        Formatter fmt = new Formatter();
        for (int i = 0; i != abs.length(); i++) {
            String temp = Formatter.padTo(i, 5) + " " + abs.getLine(i);
            fmt.add(temp);
        }
        return fmt.toString();
    }

}
