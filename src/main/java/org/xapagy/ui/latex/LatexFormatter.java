/*
   This file is part of the Xapagy project
   Created on: Jan 3, 2013
 
   org.xapagy.ui.latex.LatexFormatter
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.latex;

import java.util.Stack;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;

/**
 * @author Ladislau Boloni
 * 
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
