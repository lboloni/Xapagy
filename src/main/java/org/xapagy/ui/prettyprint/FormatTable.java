/*
   This file is part of the Xapagy project
   Created on: Jan 30, 2013
 
   org.xapagy.ui.prettyprint.FormatTable
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.TextUiHelper;

/**
 * Formatting a table on the regular text
 * 
 * @author Ladislau Boloni
 * 
 */
public class FormatTable {

    private int columnWidths[];
    private Formatter fmt;
    private int width = 0;

    public FormatTable(int... columnWidths) {
        this.fmt = new Formatter();
        this.columnWidths = columnWidths;
        width = 1;
        for (double w : columnWidths) {
            width += w + 1;
        }
    }

    /**
     * Syntactic sugar
     */
    public void endTable() {
        externalSeparator();
    }

    /**
     * An external separator: just a line of "-"
     */
    public void externalSeparator() {
        String tmp = "";
        for (int i = 0; i != width; i++) {
            tmp += "-";
        }
        fmt.add(tmp + "\n");
    }

    /**
     * Header
     */
    public void header(String... headers) {
        externalSeparator();
        if (headers.length != columnWidths.length) {
            TextUi.abort("FormatTable: header size should be column size");
        }
        String tmp = "|";
        for (int i = 0; i != headers.length; i++) {
            tmp += TextUiHelper.padTo(headers[i], columnWidths[i]) + "|";
        }
        fmt.add(tmp + "\n");
    }

    /**
     * An internal separator: --+--
     */
    public void internalSeparator() {
        String tmp = "|";
        for (int i = 0; i != columnWidths.length; i++) {
            for (int j = 0; j != columnWidths[i]; j++) {
                tmp += "-";
            }
            if (i == columnWidths.length - 1) {
                tmp += "|";
            } else {
                tmp += "+";
            }
        }
        fmt.add(tmp + "\n");
    }

    /**
     * Creates a row in the table
     */
    public String row(Object... columns) {
        if (columns.length != columnWidths.length) {
            TextUi.abort("FormatTable: header size should be column size");
        }
        // internalSeparator();
        String tmp = "|";
        for (int i = 0; i != columns.length; i++) {
            String text = "";
            Object column = columns[i];
            if (column instanceof String) {
                text = (String) column;
            } else if (column instanceof Double) {
                text = Formatter.fmt((double) column);
            } else {
                text = column.toString();
            }
            tmp += TextUiHelper.padTo(text, columnWidths[i]) + "|";
        }
        fmt.add(tmp + "\n");
        return tmp;
    }

    @Override
    public String toString() {
        return fmt.toString();
    }

    /**
     * Creates rows in the table. Wraps the cell contents to fit in the column
     * width
     */
    public void wrappedRow(Object... columns) {
        if (columns.length != columnWidths.length) {
            TextUi.abort("FormatTable: header size should be column size");
        }
        // internalSeparator();
        int rows = 1;
        List<String[]> wrapped = new ArrayList<>();
        for (int i = 0; i != columns.length; i++) {
            String text = "";
            Object column = columns[i];
            if (column instanceof String) {
                text = (String) column;
            } else if (column instanceof Double) {
                text = Formatter.fmt((double) column);
            } else {
                text = column.toString();
            }
            text = TextUiHelper.wrap(text, columnWidths[i]);
            String t[] = text.split("\n");
            if (t.length > rows) {
                rows = t.length;
            }
            wrapped.add(t);
        }

        for (int j = 0; j != rows; j++) {
            String tmp = "|";
            for (int i = 0; i != columns.length; i++) {
                String t[] = wrapped.get(i);
                String text = "";
                if (j < t.length) {
                    text = t[j];
                }
                tmp += TextUiHelper.padTo(text, columnWidths[i]) + "|";
            }
            fmt.add(tmp + "\n");
        }
    }
}
