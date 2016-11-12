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
package org.xapagy.activity.shadowmaintenance;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.ui.formatters.FormatTable;
import org.xapagy.ui.formatters.LatexFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Apr 4, 2013
 */
public class ShadowTableHelper {

    public static SimpleEntry<FormatTable, LatexFormatter> createShadowTables() {
        LatexFormatter lf = new LatexFormatter();
        FormatTable ft = new FormatTable(15, 15, 40, 20, 20);
        ft.header("instance1", "instance2", "verbs", "instance shadow",
                "VI shadow");
        ft.internalSeparator();
        lf.beginTabular("|p{5cm}|p{5cm}|p{2cm}|p{2cm}|p{2cm}|");
        lf.add("\\hline");
        lf.addTableLine("instance1", "instance2", "verbs", "instance shadow",
                "VI shadow");
        lf.add("\\hline");
        return new SimpleEntry<>(ft, lf);
    }

}
