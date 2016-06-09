/*
   This file is part of the Xapagy project
   Created on: Jul 12, 2014

   org.xapagy.debug.InstanceShadowAccessFilter

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;

/**
 * @author Ladislau Boloni
 *
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
