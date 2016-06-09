/*
   This file is part of the Xapagy project
   Created on: Jul 12, 2014

   org.xapagy.debug.ViShadowAccessFilter

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;

/**
 * @author Ladislau Boloni
 *
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
