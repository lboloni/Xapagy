/*
   This file is part of the Xapagy project
   Created on: Apr 3, 2012
 
   org.xapagy.ui.prettygraphviz.testGraphVizHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettygraphviz;

import org.junit.Test;

import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
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
