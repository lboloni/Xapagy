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
package org.xapagy.ui.formatters;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.LatexFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Jan 3, 2013
 */
public class testLatexFormatter {

    @Test
    public void testProcessForLatex() {
        Formatter fmt = new Formatter();
        List<String> tests = new ArrayList<>();
        tests.add("aaa");
        tests.add("a_b");
        tests.add("a{b}");
        tests.add("a % b");
        tests.add("a \\ b");
        tests.add("[b]");
        for (String test : tests) {
            fmt.is(test, LatexFormatter.processForLatex(test));
        }
        TextUi.println(fmt.toString());
    }

}
