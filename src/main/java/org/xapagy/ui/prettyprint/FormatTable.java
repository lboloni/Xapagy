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

import java.util.ArrayList;
import java.util.List;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.TextUiHelper;

/**
 * Formatting a table on the regular text
 * 
 * @author Ladislau Boloni
 * Created on: Jan 30, 2013
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
