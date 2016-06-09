/*
   This file is part of the Xapagy project
   Created on: Jul 12, 2014

   org.xapagy.debug.InstanceMatchFilter

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;

/**
 * This object captures all the parameters of the filtering in the ViMatch
 * access in a single object.
 *
 * It also adds a name field, which allows for later printing etc.
 *
 * It is an immutable object.
 *
 * @author Ladislau Boloni
 *
 */
public class InstanceMatchFilter implements Serializable {

    private static final long serialVersionUID = 8700344720549279605L;

    private String attributes;

    private boolean isScene;
    private String name;
    private String sceneAttributes;

    /**
     * @param name
     * @param isScene
     * @param attributes
     * @param sceneAttributes
     */
    public InstanceMatchFilter(String name, boolean isScene, String attributes,
            String sceneAttributes) {
        super();
        this.name = name;
        this.isScene = isScene;
        this.attributes = attributes;
        this.sceneAttributes = sceneAttributes;
    }

    public String getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public String getSceneAttributes() {
        return sceneAttributes;
    }

    public boolean isScene() {
        return isScene;
    }

    @Override
    public String toString() {
        return "InstanceMatchFilter [attributes=" + attributes + ", isScene="
                + isScene + ", name=" + name + ", sceneAttributes="
                + sceneAttributes + "]";
    }

}
