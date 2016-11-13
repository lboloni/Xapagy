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

import org.junit.Test;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.FormatTable;

/**
 * @author Ladislau Boloni
 * Created on: Jan 30, 2013
 */
public class testFormatTable {

    @Test
    public void test() {
        FormatTable ft = new FormatTable(10, 20, 30);
        ft.header("One", "Two", "Three");
        ft.row("X", 45.6, 5);
        ft.endTable();
        TextUi.println(ft);
    }

}
