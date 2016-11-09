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
package org.xapagy.links;

import java.io.Serializable;

import org.xapagy.instances.VerbInstance;

/**
 * This class records a change in the link
 *
 * @author Ladislau Boloni
 * Created on: Mar 3, 2015
 */
public class LinkQuantum implements Serializable {

    private static final long serialVersionUID = 7666586872449275796L;

    /**
     * Factory method
     *
     * @param linkName
     * @param from
     * @param to
     * @param delta
     * @param source
     * @return
     */
    public static LinkQuantum createQuantum(String linkName, VerbInstance from,
            VerbInstance to, double delta, String source) {
        LinkQuantum lq = new LinkQuantum();
        lq.linkName = linkName;
        lq.from = from;
        lq.to = to;
        lq.delta = delta;
        lq.source = source;
        return lq;
    }

    /**
     * The agent time when the LinkQuantum was applied
     */
    private double agentTimeWhenApplied;
    /**
     * The linear change in value of the link 
     */
    private double delta;
    /**
     * The VI from which the link originates
     */
    private VerbInstance from;
    /**
     * The name of the link that needs to be changed
     */
    private String linkName;
    /**
     * Description of the entity that created the change and its reason to do so.
     */
    private String source;
    /**
     * The destination VI of the change
     */
    private VerbInstance to;
    /**
     * The value of the link before the change was applied
     */
    private double valueAfter;
    /**
     * The value of the link after the change had been applied
     */
    private double valueBefore;

    public double getAgentTimeWhenApplied() {
        return agentTimeWhenApplied;
    }

    public double getDelta() {
        return delta;
    }

    public VerbInstance getFrom() {
        return from;
    }

    public String getLinkName() {
        return linkName;
    }

    public String getSource() {
        return source;
    }

    public VerbInstance getTo() {
        return to;
    }

    public double getValueAfter() {
        return valueAfter;
    }

    public double getValueBefore() {
        return valueBefore;
    }

    /**
     * Convenience function for filtering from lists of LinkQuantums the ones
     * relevant to a specific link - it only for the direct link (positive
     * changes, normally)
     *
     * @param linkName
     * @param from
     * @param to
     * @return
     */
    public boolean matches(String name, VerbInstance viFrom,
            VerbInstance viTo) {
        return name.equals(this.linkName) && viFrom.equals(this.from)
                && viTo.equals(this.to);
    }

    /**
     * Convenience function for filtering from lists of LinkQuantums the ones
     * relevant to a specific link - for the reverse direction - these are
     * negative changes for that link.
     *
     * @param linkName
     * @param from
     * @param to
     * @return
     */
    public boolean matchesReverse(String name, VerbInstance viFrom,
            VerbInstance viTo, Links vldb) {
        LinkValues lv = vldb.getLinkValues(name);
        if (!lv.isDirectionPaired()) {
            return false;
        }
        if (lv.getNameReverse().equals(name)) {
            return viFrom.equals(this.from)
                    && viTo.equals(this.to);
        }
        return false;
    }

    public void setAgentTimeWhenApplied(double agentTimeWhenApplied) {
        this.agentTimeWhenApplied = agentTimeWhenApplied;
    }

    public void setValueAfter(double valueAfter) {
        this.valueAfter = valueAfter;
    }

    public void setValueBefore(double valueBefore) {
        this.valueBefore = valueBefore;
    }

}
