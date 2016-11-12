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

import org.xapagy.agents.Agent;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.ui.prettyprint.PrintDetail;

/**
 * @author Ladislau Boloni
 * Created on: Mar 8, 2012
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
