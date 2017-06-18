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

import org.xapagy.agents.Agent;
import org.xapagy.autobiography.ABStory;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Sep 24, 2012
 */
public class xwABStory {

    /**
     * Adding the concise version of the story: it is just a pre-formatted text
     * @param xw
     * @param abs
     * @param agent
     * @return
     */
    public static String xwConcise(IXwFormatter xw, ABStory abs, Agent agent) {
		xw.addPre(abs.toString());
		return xw.toString();
    }

    /**
     * Adding the detailed version of the story - basically, we are adding line numbers 
     * @param xw
     * @param abs
     * @param agent
     * @return
     */
    public static String xwDetailed(IXwFormatter xw, ABStory abs, Agent agent) {
        Formatter fmt = new Formatter();
        for (int i = 0; i != abs.length(); i++) {
            String temp = Formatter.padTo(i, 5) + " " + abs.getLine(i);
            fmt.add(temp);
        }
		xw.addPre(abs.toString());
		return xw.toString();
    }

}
