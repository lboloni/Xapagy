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
 * This object captures all the parameters of the filtering in the ViMatch
 * access in a single object.
 *
 * It also adds a name field, which allows for later printing etc.
 *
 * It is an immutable object.
 *
 * @author Ladislau Boloni
 * Created on: Jul 12, 2014
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
