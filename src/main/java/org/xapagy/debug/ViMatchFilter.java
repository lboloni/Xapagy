/*
   This file is part of the Xapagy project
   Created on: Jun 30, 2014

   org.xapagy.debug.ViMatchFilter

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xapagy.instances.ViStructureHelper.ViType;

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
public class ViMatchFilter implements Serializable {

    private static final long serialVersionUID = -2544974015871624048L;
    private String name;
    private String params[];
    private String sceneAttributes = null;
    private ViType type;

    /**
     * Constructor in which we are also specifying the scene attributes
     *
     * @param name
     * @param sceneAttributes
     * @param type
     * @param params
     */
    public ViMatchFilter(String name, String sceneAttributes, ViType type,
            String[] params) {
        super();
        this.name = name;
        this.sceneAttributes = sceneAttributes;
        this.type = type;
        this.params = params;
    }

    /**
     * Constructor in which we are also specifying the scene attributes
     *
     * @param name
     * @param sceneAttributes
     * @param type
     * @param params
     */
    public ViMatchFilter(String name, ViType type, String... params) {
        super();
        this.name = name;
        this.type = type;
        this.params = params;
    }

    /**
     * Factory function which creates a ViMatch filter from a parsed text. The
     * different parameters are supposed to be passed in a comma separated list.
     * 
     * If a ViType object is passed, it is considered as such.
     *
     * Scene:#various,
     *
     * @param name
     * @param text
     *            - the text to be parsed
     */
    public static ViMatchFilter createViMatchFilter(String name, String text) {
        ViType type = null;
        String components[] = text.split(",");
        String sceneAttributes = null;
        List<String> listparams = new ArrayList<>();
        for (int i = 0; i != components.length; i++) {
            String tmp = components[i];
            tmp = tmp.trim();
            // if the first starts with scene:, treat it as a scene label
            if (i == 0 && tmp.startsWith("scene:")) {
                sceneAttributes = tmp.substring("scene:".length());
                continue;
            }
            try {
                ViType tryType = ViType.valueOf(tmp);
                type = tryType;
                continue;
            } catch (IllegalArgumentException iaex) {
                // fine, not a type
            }
            listparams.add(tmp);
        }
        String params[] = new String[listparams.size()];
        for (int i = 0; i != params.length; i++) {
            params[i] = listparams.get(i);
        }
        ViMatchFilter retval =
                new ViMatchFilter(name, sceneAttributes, type, params);
        return retval;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the params
     */
    public String[] getParams() {
        return params;
    }

    public String getSceneAttributes() {
        return sceneAttributes;
    }

    /**
     * @return the type
     */
    public ViType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ViMatchFilter [name=" + name + ", params="
                + Arrays.toString(params) + ", type=" + type
                + ", sceneAttributes=" + sceneAttributes + "]";
    }

}
