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

import java.util.Stack;

import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Jan 3, 2013
 */
public class LatexFormatter extends Formatter {

    /**
     * 
     */
    private static final String LATEX_TABULAR = "tabular";

    /**
     * Processes the string for latex - basically, it performs the necessary
     * escapes
     * 
     * FIXME: escape sequences itself?
     * 
     * @return
     */
    public static String processForLatex(String string) {
        String s = string;
        s = s.replaceAll("\\[", "\\$[\\$");
        s = s.replaceAll("\\]", "\\$]\\$");
        // s = s.replaceAll("\\[", "$\\[$");
        // s = s.replaceAll("\\]", "$\\]$");
        s = s.replaceAll("\\\\", "####");
        s = s.replaceAll("\\_", "\\\\_");
        s = s.replaceAll("%", "\\\\%");
        s = s.replaceAll("\\{", "\\\\{");
        s = s.replaceAll("\\}", "\\\\}");
        s = s.replaceAll("####", "\\\\textbackslash\\{\\}");
        return s;
    }

    private Stack<String> stack = new Stack<>();

    public LatexFormatter() {
    }

    /**
     * @param string
     */
    public void addCaption(String string) {
        add("\\caption{" + LatexFormatter.processForLatex(string) + "}");
    }

    public void addTableLine(Object... columns) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i != columns.length; i++) {
            String item = null;
            Object column = columns[i];
            if (column instanceof String) {
                item = (String) column;
            } else if (column instanceof Double) {
                item = Formatter.fmt((double) column);
            } else {
                item = column.toString();
            }
            String text = LatexFormatter.processForLatex(item);
            buf.append(text);
            if (i == columns.length - 1) {
                buf.append("\\\\\n");
            } else {
                buf.append(" & ");
            }
        }
        add(buf.toString());
    }

    /**
     * Begins a generic environment
     * 
     * @param label
     * @param options
     */
    public void beginGeneric(String label, String... options) {
        stack.push(label);
        add(makeBegin(label, options));
        indent();
    }

    /**
     * @param string
     */
    public void beginTableStar(String options) {
        beginGeneric("table*", options);
    }

    public void beginTabular(String specification) {
        beginGeneric(LatexFormatter.LATEX_TABULAR, specification);
    }

    public void endGeneric(String label) {
        String closed = stack.pop();
        if (!closed.equals(label)) {
            throw new Error("Trying to end environment " + label
                    + " but what I should end is " + closed);
        }
        deindent();
        add(makeEnd(label));
    }

    /**
     * @param string
     */
    public void endTableStar() {
        endGeneric("table*");
    }

    public void endTabular() {
        endGeneric(LatexFormatter.LATEX_TABULAR);
    }

    /**
     * Makes the begin part of the environment
     * 
     * @param label
     * @param options
     * @return
     */
    @SuppressWarnings("static-method")
    private String makeBegin(String label, String... options) {
        String tmp = "\\begin{" + label + "}";
        switch (options.length) {
        case 0:
            break;
        case 1: {
            if (options[0].startsWith("[") || options[0].startsWith("{")) {
                tmp = tmp + options[0];
            } else {
                tmp = tmp + "{" + options[0] + "}";
            }
            break;
        }
        default:
            TextUi.abort("LatexFormatter.makeBegin - more than 1 options not supported");
        }
        return tmp;
    }

    /**
     * Makes the end part of the environment
     * 
     * @param label
     * @param options
     * @return
     */
    @SuppressWarnings("static-method")
    private String makeEnd(String label) {
        return "\\end{" + label + "}\n";
    }
}
