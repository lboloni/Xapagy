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
package org.xapagy.ui.prettygraphviz;

import org.junit.Test;

import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Apr 3, 2012
 */
public class testGraphVizHelper {

    @Test
    public void testFormatIdentifier() {
        String t = "i10-5.0";
        String t2 = GraphVizHelper.formatIdentifier(t);
        TextUi.println(t2);
    }

    @Test
    public void testFormatLabel() {
        String t = "[\"Hector\"]\n";
        String t2 = GraphVizHelper.formatLabel(t);
        TextUi.println("|" + t2 + "|");
    }

}
