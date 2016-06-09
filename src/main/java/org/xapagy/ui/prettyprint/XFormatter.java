/*
   This file is part of the Xapagy project
   Created on: Mar 8, 2012
 
   org.xapagy.ui.prettyprint.XFormatter
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * 
 */
public class XFormatter extends Formatter {

    private Agent agent;

    public XFormatter(Agent agent) {
        this.agent = agent;
    }

    /**
     * @param hlsni
     * @param detailLevel
     */
    public void addPp(Object object, PrintDetail detailLevel) {
        if (detailLevel == PrintDetail.DTL_CONCISE) {
            addPpc(object);
        } else {
            addPpd(object);
        }
    }

    /**
     * Shortcut to adding a pretty print concise
     * 
     * @param object
     * @param agent
     */
    public void addPpc(Object object) {
        add(PrettyPrint.ppConcise(object, agent));
    }

    /**
     * @param string
     * @param scene
     */
    public void addPpc(String string, Object object) {
        add(string + " " + PrettyPrint.ppConcise(object, agent));
    }

    /**
     * 
     * @param object
     */
    public void addPpd(Object object) {
        add(PrettyPrint.ppDetailed(object, agent));
    }

    /**
     * 
     * @param string
     *            label
     * @param object
     */
    public void addPpd(String label, Object object) {
        add(label);
        addIndented(PrettyPrint.ppDetailed(object, agent));
    }

}
