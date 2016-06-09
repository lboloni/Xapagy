/*
   This file is part of the Xapagy project
   Created on: Jan 30, 2013
 
   org.xapagy.ui.prettyprint.testFormatTable
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.junit.Test;

import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * 
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
