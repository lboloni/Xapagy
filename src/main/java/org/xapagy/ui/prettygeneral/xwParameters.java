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

import org.xapagy.parameters.Parameters;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Apr 18, 2014
 */
public class xwParameters {
    /**
     * Print the parameters, organized by area and group
     * 
     * @param fmt
     * @param p
     * @return
     */
    public static String xwDetailed(IXwFormatter fmt, Parameters p) {
        for (String area : p.listAreas()) {
            fmt.addH2(area);
            fmt.indent();
            xwParameters.xwDetailedArea(fmt, p, area);
            fmt.deindent();
        }
        return fmt.toString();

    }

    /**
     * @param debug
     */
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