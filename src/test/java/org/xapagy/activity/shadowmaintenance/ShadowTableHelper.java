/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2013
 
   org.xapagy.activity.shadowmaintenance.ShadowTableHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.ui.formatters.LatexFormatter;
import org.xapagy.ui.prettyprint.FormatTable;

/**
 * @author Ladislau Boloni
 * 
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
