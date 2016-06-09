/*
   This file is part of the Xapagy project
   Created on: Jan 3, 2013
 
   org.xapagy.ui.latex.testLatexFormatter
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.latex;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * 
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
