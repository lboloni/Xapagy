/*
   This file is part of the Xapagy project
   Created on: Dec 24, 2010
 
   org.xapagy.util.ui.formatting.Formatter
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.ui.TextUiHelper;

/**
 * Helps create an indented, headlined toString format
 * 
 * @author Ladislau Boloni
 * 
 */
public class Formatter {

    private static String fmtDouble = "%.3f";

    /**
     * Formats a double
     * 
     * @param d
     * @return
     */
    public static String fmt(double value) {
        if (Double.MAX_VALUE == value) {
            return "+infty";
        } 
        if (Double.MAX_VALUE == -value) {
            return "-infty";
        } 
        return String.format(Formatter.fmtDouble, value);
    }

    public static String line(String element, int length) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i != length; i++) {
            buffer.append(element);
        }
        return buffer.toString();
    }

    /**
     * Pad to a string of a certain length with spaces at the left
     * 
     * @param nodeType
     * @param length
     * @return
     */
    public static String padTo(Object object, int length) {
        return Formatter.padTo(object, length, ' ');
    }

    /**
     * Pad to a string of a certain length with the specified character
     * 
     * @param nodeType
     * @param length
     * @return
     */
    public static String padTo(Object object, int length, char padCharacter) {
        String text = object.toString();
        if (text.length() > length) {
            return text.substring(0, length);
        }
        int count = length - text.length();
        StringBuffer buff = new StringBuffer(text);
        // String pad = " ". * count;
        for (int i = 0; i != count; i++) {
            buff.append(padCharacter);
        }
        return buff.toString();
    }

    protected StringBuffer buffer = new StringBuffer();
    private int currentIndent = 0;
    private int indent = 4;

    /**
     * Adds an object at the current level of indent and terminates it with a new line
     * 
     * @param object
     */
    public void add(Object object) {
        String objectString = object.toString();
        String temp = TextUiHelper.indent(currentIndent, objectString);
        buffer.append(temp);
        if (!objectString.endsWith("\n")) {
            buffer.append("\n");
        }
    }

    /**
     * For simple formatter, this is the same as add (but it will be different for HtmlFormatter)
     * @param object
     */
    protected void addLine(Object object) {
    	add(object);
    }
    
    
    /**
     * Adds an object at the current level of indent, but does not terminate it with newline
     * @param object
     */
    public void accumulate(Object object) {
        String objectString = object.toString();
        if (!objectString.endsWith("\n")) {
            objectString = objectString.substring(0, objectString.length()-1);
        }
        String temp = TextUiHelper.indent(currentIndent, objectString);
        buffer.append(temp);    	
    }
    
    
    /**
     * Adds the next object in an indented form
     */
    public void addIndented(Object object) {
        currentIndent = currentIndent + indent;
        add(object);
        currentIndent = currentIndent - indent;
    }

    public void addNonIndented(Object object) {
        String objectString = object.toString();
        buffer.append(object);
        if (!objectString.endsWith("\n")) {
            buffer.append("\n");
        }
    }

    /**
     * Adds the text shifted, with a margin note inserted at the beginning
     * 
     * Fixme: the 4,
     * 
     * @param prefix
     * @param text
     */
    public void addWithMarginNote(String prefix, String text) {
        StringBuffer buf = new StringBuffer();
        int indentValue = Math.max(8, prefix.length() + 2);
        buf.append(prefix);
        String text2 = TextUiHelper.indent(indentValue, text);
        buf.append(text2.substring(prefix.length()));
        add(buf);
    }

    /**
     * De-indents the following text
     */
    public void deindent() {
        textDeindent();
    }

    /**
     * Indents the following text
     */
    public void indent() {
        textIndent();
    }

    /**
     * Formats a name / value pair
     * 
     * @param name
     * @param object
     */
    public void is(String name, Object object) {
        String value;
        if (object == null) {
            value = "<null>";
        } else {
            if (object instanceof Double) {
                value = Formatter.fmt((Double) object);
            } else {
                value = object.toString();
            }
        }
        add(name + " = " + value);
    }

    /**
     * Creates a labeled separator (indented with the current value)
     * 
     * @param text
     */
    public void separator(String text) {
        String sep = TextUiHelper.createLabeledSeparator("-" + text);
        add(sep);
    }

    /**
     * De-indents the following text - used to access this without the virtual
     * function stuff
     */
    public void textDeindent() {
        currentIndent = currentIndent - indent;
    }

    /**
     * Indents the following text - used to access this without the virtual
     * function stuff
     */
    public void textIndent() {
        currentIndent = currentIndent + indent;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

}
