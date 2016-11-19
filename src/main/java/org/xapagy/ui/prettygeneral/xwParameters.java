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
import org.xapagy.parameters.Parameters;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Apr 18, 2014
 */
public class xwParameters {
    /**
     * Detailed printing of the parameters
     * 
     * @param xw
     * @param p
     * @param agent 
     * @return
     */
    public static String xwDetailed(IXwFormatter xw, Parameters p, Agent agent) {
        for (String area : p.listAreas()) {
            xw.addH2(area);
            xw.indent();
            xwParameters.xwDetailedArea(xw, p, area);
            xw.deindent();
        }
        return xw.toString();
    }

    private static void xwDetailedArea(IXwFormatter fmt, Parameters p,
            String area) {
        for (String group : p.listGroups(area)) {
            fmt.addLabelParagraph(group);
            fmt.indent();
            xwParameters.xwDetailedGroup(fmt, p, area, group);
            fmt.deindent();
        }
    }

    /**
     * @param fmt
     * @param p
     * @param area
     * @param group
     */
    private static void xwDetailedGroup(IXwFormatter fmt, Parameters p,
            String area, String group) {
        for (String name : p.listNames(area, group)) {
            fmt.is(name, p.get(area, group, name));
            String description = p.getDescription(area, group, name);
            if (description != null) {
                fmt.explanatoryNote(description);
            }
        }
    }
}