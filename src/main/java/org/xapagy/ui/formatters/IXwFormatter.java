/*
   This file is part of the Xapagy project
   Created on: Apr 11, 2014
 
   org.xapagy.ui.prettyhtml.formatters.IXwFormatter
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.formatters;

import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.prettyhtml.ColorCode;


/**
 * This interface contains a collection of functions which are sufficient to
 * format Xapagy objects. Using this interface allows us to write code which can
 * perform both a Html and a Text formatting.
 * 
 * @author Ladislau Boloni
 * 
 */
public interface IXwFormatter {
    void addErrorMessage(String notificationString);
    /**
     * An important header
     * @param text
     * @param options
     */
    void addH2(String text, String... options);
    String addLabelParagraph(String label, String... values);
    void deindent();
    IXwFormatter getEmpty();
    String grayoutEnd();
    String grayoutStart();   
    /**
     * Adds a preformatted text
     * @param text
     */
    void addPre(String text, String... options);
    void indent();
    /**
     * Adds an object at the current level of indent
     * @param object
     */
    void add(Object object);
    /**
     * Describes the value of a variable in the VAR = value format
     * @param name
     * @param object
     */
    void is(String name, Object object);
    /**
     * Adds an explanatory note, normally to the previous text
     * @param explanation
     * @return
     */
    String explanatoryNote(String explanation);
    /**
     *  Implemented in HTML based, ignored in others
     */
    void openA(String... options);
    /**
     * Implemented in HTML based, ignored in others
     */
    void closeA();
    /**
     * In HTML based, what it says, in others, an indent
     */
    void openDiv(String... options);
    /**
     * In HTML based, what it says, in others, a deindent
     */    
    void closeDiv();
    /**
     * In HTML based, what it says, in others, nothing
     */
    void openP(String... options);
    /**
     * In HTML based, what it says, in others, a newline
     */
    void closeP();
    /**
     * Some way to distinguish in text
     * @param text
     * @param options
     */
    void addH3(String text, String... options);
    /**
     * Adds a bold item online for html - just a new line in Txt
     * @param label
     * @return
     */
    String addBold(String label);
    /**
     * Adds an identifier: online for html - just a new line in Txt
     * @param xc
     * @return
     */
    String addIdentifier(XapagyComponent xc);
    /**
     * Embedding in HTML, indenting in text
     * @return
     */
    String startEmbedX(String text);
    /**
     * Ending an embedding
     * @return
     */
    String endEmbedX();
    /**
     * Progress bar
     * @param value
     * @param maxValue
     * @return
     */
    /**
     * Embedding in HTML, indenting in text
     * @return
     */
    String startEmbed();
    /**
     * Ending an embedding
     * @return
     */
    String endEmbed();
    /**
     * Progress bar
     * @param value
     * @param maxValue
     * @return
     */
    String progressBar(double value, double maxValue);
    /**
     * Shows a progress bar with a slash label, normally representing
     * salience/energy. The salience is always on the 0..1 range.
     * 
     * @param fmt
     * @param value
     * @param maxValue
     */
    public String progressBarSlash(double salience, double energy) ;
    /**
     *  Add a new paragraph
     */
    void addP(String text, String... options);
    /**
     * A collapsible section of the html page with a H2 header, fully visible in text
     * 
     * @param id
     * @param label
     * @param content
     * @param initiallyVisible
     * @return
     */
    String addExtensibleH2(String id, String label, String content,
            boolean initiallyVisible);
    /**
     * A collapsible section of the html page with a H3 header, fully visible in text
     * 
     * @param id
     * @param label
     * @param content
     * @param initiallyVisible
     * @return
     */
    String addExtensibleH3(String id, String label, String content,
            boolean initiallyVisible);
    /**
     * Adds a color code to document the different story lines. The idea is that instances
     * marked with the same color code will be coming from the same story line
     * 
     */
    String addColorCode(ColorCode cc);
}