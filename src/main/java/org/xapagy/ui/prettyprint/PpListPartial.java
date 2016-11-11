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

import java.util.List;

import org.xapagy.agents.Agent;

/**
 * Helper function for pretty printing a partial list
 * 
 * @author Ladislau Boloni
 * Created on: Sep 14, 2011
 */
public class PpListPartial {

    /**
     * Prints a partial list, if necessary truncates it (but specifies how many
     * were left out)
     * 
     * @param list
     * @param agent
     * @param detailLevel
     * @param max
     * @return
     */
    public static <T> String ppListPartial(List<T> list, Agent agent,
            PrintDetail detailLevel, int max) {
        Formatter fmt = new Formatter();
        int i = 0;
        while (true) {
            if (i >= list.size()) {
                // list was smaller than max
                break;
            }
            if (i >= max) {
                if (i < list.size()) {
                    fmt.add("... and " + (list.size() - i) + " more.");
                }
                break;
            }
            fmt.add(PrettyPrint.pp(list.get(i), agent, detailLevel));
            i++;
        }
        return fmt.toString();
    }

}
