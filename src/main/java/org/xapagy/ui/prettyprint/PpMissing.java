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

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.ui.formatters.Formatter;

/**
 * @author Ladislau Boloni
 * Created on: Jul 5, 2011
 */
public class PpMissing {

    public static int printNBest = 3;

    /**
     * Prints the missing values
     * 
     * @param missing
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String pp(List<SimpleEntry<Hls, Double>> missing,
            Agent agent, PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        fmt.add("Headless shadows pointing to missing VIs");
        int count = 0;
        for (SimpleEntry<Hls, Double> entry : missing) {
            fmt.addWithMarginNote(Formatter.fmt(entry.getValue()),
                    PrettyPrint.pp(entry.getKey(), agent, detailLevel));
            count++;
            if (count >= PpMissing.printNBest) {
                fmt.add("... and " + (missing.size() - PpMissing.printNBest)
                        + " more...");
                break;
            }
        }
        return fmt.toString();
    }

}
