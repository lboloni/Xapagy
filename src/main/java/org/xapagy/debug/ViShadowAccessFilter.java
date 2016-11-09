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
public class ViShadowAccessFilter implements Serializable {

    private static final long serialVersionUID = 5853332255719374642L;
    private String name;
    private ViMatchFilter vmfFocus;
    private ViMatchFilter vmfShadow;
    private String sortBy;

    /**
     * @param name
     * @param sortBy
     * @param vmfFocus
     * @param vmfShadow
     */
    public ViShadowAccessFilter(String name, String sortBy,
            ViMatchFilter vmfFocus, ViMatchFilter vmfShadow) {
        super();
        this.name = name;
        this.sortBy = sortBy;
        this.vmfFocus = vmfFocus;
        this.vmfShadow = vmfShadow;
    }

    public String getSortBy() {
        return sortBy;
    }

    /**
     * @param name
     * @param vmfFocus
     * @param vmfShadow
     */
    public ViShadowAccessFilter(String name, ViMatchFilter vmfFocus,
            ViMatchFilter vmfShadow) {
        this(name, null, vmfFocus, vmfShadow);
    }

    public String getName() {
        return name;
    }

    public ViMatchFilter getVmfFocus() {
        return vmfFocus;
    }

    public ViMatchFilter getVmfShadow() {
        return vmfShadow;
    }

}
