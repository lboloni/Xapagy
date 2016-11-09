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
import java.util.HashMap;
import java.util.Map;

import org.xapagy.instances.VerbInstance;
import org.xapagy.set.ViSet;

/**
 * This class implements the link connections of a specific type.
 *
 * The strength of the individual links are kept in the ViSet mapped to the
 * specific VI.
 *
 * @author Ladislau Boloni
 * Created on: May 18, 2011
 */
public class LinkValues implements Serializable {

    private static final long serialVersionUID = 7273359854628627877L;
    private String description = "";
    /**
     * This set contains the adjacency map. For each source VI, we have a ViSet
     * object that shows the strength of the connected components.
     */
    private Map<VerbInstance, ViSet> link = new HashMap<>();
    /**
     * The name of the link on the direct path
     */
    private String nameDirect;
    /**
     * The name of the link of the reverse path
     */
    private String nameReverse;
    /**
     * Is true if it is a direction paired link
     */
    boolean directionPaired;

    /**
     * Constructor: determines the type of the link type
     */
    public LinkValues(String nameDirect, String nameReverse, boolean directionPaired,
            String description) {
        this.nameDirect = nameDirect;
        this.nameReverse = nameReverse;
        this.directionPaired = directionPaired;
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the map of the link from a particular one
     *
     * @param iwi1
     * @param iwi2
     * @return
     */
    public ViSet getLink(VerbInstance iwi1) {
        ViSet inner = link.get(iwi1);
        if (inner != null) {
            return inner;
        }
        return new ViSet();
    }

    /**
     * Gets the value of a relation
     *
     * @param iwi1
     * @param iwi2
     * @return
     */
    public double getLink(VerbInstance iwi1, VerbInstance iwi2) {
        ViSet inner = link.get(iwi1);
        if (inner == null) {
            return 0.0;
        }
        return inner.value(iwi2);
    }

    /**
     * @return the nameDirect
     */
    public String getNameDirect() {
        return nameDirect;
    }

    /**
     * @return the nameReverse
     */
    public String getNameReverse() {
        return nameReverse;
    }

    /**
     * @return the undirected
     */
    public boolean isDirectionPaired() {
        return directionPaired;
    }

    /**
     * Puts the value of the link
     *
     * @param vi1
     * @param vi2
     * @param value
     */
    public void putLink(VerbInstance vi1, VerbInstance vi2, double value) {
        ViSet inner = link.get(vi1);
        if (inner == null) {
            inner = new ViSet();
            link.put(vi1, inner);
        }
        inner.changeTo(vi2, value);
    }

}
