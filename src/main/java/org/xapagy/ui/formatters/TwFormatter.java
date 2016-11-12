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

import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.TextUiHelper;
import org.xapagy.ui.prettyhtml.ColorCode;

/**
 * A text based implementation of the generic
 * 
 * @author Ladislau Boloni
 * Created on: Apr 11, 2014
 */
public class TwFormatter extends Formatter implements IXwFormatter {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.prettyhtml.formatters.IXwFormatter#addErrorMessage(java
     * .lang.String)
     */
    @Override
    public void addErrorMessage(String notificationString) {
        add(notificationString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.prettyhtml.formatters.IXwFormatter#addH2(java.lang.String,
     * java.lang.String[])
     */
    @Override
    public void addH2(String text, String... options) {
        add(TextUiHelper.createHeader(text));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xapagy.ui.prettyhtml.formatters.IXwFormatter#addLabelParagraph(java
     * .lang.String, java.lang.String[])
     */
    @Override
    public String addLabelParagraph(String label, String... values) {
        StringBuffer tmp = new StringBuffer();
        tmp.append(label + " : ");
        for (String value : values) {
            tmp.append(value + " ");
        }
        add(tmp);
        return toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.prettyhtml.formatters.IXwFormatter#getEmpty()
     */
    @Override
    public IXwFormatter getEmpty() {
        return new TwFormatter();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.prettyhtml.formatters.IXwFormatter#grayoutEnd()
     */
    @Override
    public String grayoutEnd() {
        return toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.ui.prettyhtml.formatters.IXwFormatter#grayoutStart()
     */
    @Override
    public String grayoutStart() {
        return toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#explanatoryNote(java.lang.String)
     */
    @Override
    public String explanatoryNote(String explanation) {
        String temp = TextUiHelper.shiftText(4, "-", explanation);
        add(temp);
        return toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#addPre(java.lang.String, java.lang.String[])
     */
    @Override
    public void addPre(String text, String... options) {
        add(text);
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#openA(java.lang.String[])
     */
    @Override
    public void openA(String... options) {
        // nothing here
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#closeA()
     */
    @Override
    public void closeA() {
        // nothing here
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#openDiv(java.lang.String[])
     */
    @Override
    public void openDiv(String... options) {
        indent();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#closeDiv()
     */
    @Override
    public void closeDiv() {
        deindent();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#openP(java.lang.String[])
     */
    @Override
    public void openP(String... options) {
        // nothing here
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#closeP()
     */
    @Override
    public void closeP() {
        //add("\n");
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#addH3(java.lang.String, java.lang.String[])
     */
    @Override
    public void addH3(String text, String... options) {
        add("_" + text + "_");
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#addBold(java.lang.String)
     */
    @Override
    public String addBold(String label) {
        add(label);
        return toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#addIdentifier(org.xapagy.instances.XapagyComponent)
     */
    @Override
    public String addIdentifier(XapagyComponent xc) {
        add(xc.getIdentifier());
        return toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#startEmbedX(java.lang.String)
     */
    @Override
    public String startEmbedX(String text) {
        add(text);
        indent();
        return toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#endEmbedX()
     */
    @Override
    public String endEmbedX() {
        deindent();
        return toString();
    }

    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#startEmbedX(java.lang.String)
     */
    @Override
    public String startEmbed() {
        indent();
        return toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#endEmbedX()
     */
    @Override
    public String endEmbed() {
        deindent();
        return toString();
    }

    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#progressBar(double, double)
     */
    @Override
    public String progressBar(double value, double maxValue) {
        add("[" + Formatter.fmt(value) + " / " + Formatter.fmt(maxValue) + "]");
        return toString();
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#addP(java.lang.String, java.lang.String[])
     */
    @Override
    public void addP(String text, String... options) {
        add(text);
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#addExtensibleH2(java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    @Override
    public String addExtensibleH2(String id, String label, String content,
            boolean initiallyVisible) {
        addH2(label);
        addIndented(content);
        return null;
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#addExtensibleH3(java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    @Override
    public String addExtensibleH3(String id, String label, String content,
            boolean initiallyVisible) {
        addH3(label);
        addIndented(content);
        return null;
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IXwFormatter#progressBarSlash(double, double)
     */
    @Override
    public String progressBarSlash(double salience, double energy) {
        add("[" + Formatter.fmt(salience) + " / " + Formatter.fmt(energy) + "]");
        return toString();
    }

    /**
     * Currently not implemented here... should be implemented as accumulate is implemented. 
     */
    @Override
    public String addColorCode(ColorCode cc) {
        return toString();
    }

}
