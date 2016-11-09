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
package org.xapagy.debug;

import java.io.Serializable;

/**
 * @author Ladislau Boloni
 * Created on: Jul 12, 2014
 */
public class InstanceShadowAccessFilter implements Serializable {

    private static final long serialVersionUID = 931426141385756369L;

    private InstanceMatchFilter imfFocus;

    private InstanceMatchFilter imfShadow;
    private String name;
    private String sortBy;

    /**
     * @param name
     * @param sortBy
     * @param imfFocus
     * @param imfShadow
     */
    public InstanceShadowAccessFilter(String name, String sortBy,
            InstanceMatchFilter imfFocus, InstanceMatchFilter imfShadow) {
        super();
        this.name = name;
        this.sortBy = sortBy;
        this.imfFocus = imfFocus;
        this.imfShadow = imfShadow;
    }

    /**
     * @param name
     * @param imfFocus
     * @param imfShadow
     */
    public InstanceShadowAccessFilter(String name,
            InstanceMatchFilter imfFocus, InstanceMatchFilter imfShadow) {
        this(name, null, imfFocus, imfShadow);
    }

    public InstanceMatchFilter getImfFocus() {
        return imfFocus;
    }

    public InstanceMatchFilter getImfShadow() {
        return imfShadow;
    }

    public String getName() {
        return name;
    }

    public String getSortBy() {
        return sortBy;
    }

    @Override
    public String toString() {
        return "InstanceShadowAccessFilter [imfFocus=" + imfFocus
                + ", imfShadow=" + imfShadow + ", name=" + name + ", sortBy="
                + sortBy + "]";
    }

}
