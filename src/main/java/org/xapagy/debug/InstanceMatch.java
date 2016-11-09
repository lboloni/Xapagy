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
import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.ui.TextUi;
import org.xapagy.xapi.XapiParser;
import org.xapagy.xapi.XapiParserException;

/**
 * This class is used for situations in debugging when we need to look up a
 * specific instance or scene based on the matches.
 * 
 * @author Ladislau Boloni
 * Created on: Jul 12, 2014
 */
public class InstanceMatch implements Serializable {

    private static final long serialVersionUID = -4023706078929028508L;
    /**
     * Prefix denoting that we are accepting any value
     */
    public static final String MATCH_ANY = "*";
    /**
     * Most of these matches require the use of the parser, so we keep a link to
     * it
     */
    private XapiParser xp;

    /**
     * Constructor. The agent is passed to get the parser
     * 
     * @param agent
     */
    public InstanceMatch(Agent agent) {
        xp = agent.getXapiParser();
    }

    /**
     * Selects a set of instances from a given list which match the filter
     * 
     * @param list
     * @param imf
     * @return
     */
    public List<Instance> select(List<Instance> list, InstanceMatchFilter imf) {
        List<Instance> retval = new ArrayList<>();
        for (Instance instance : list) {
            if (match(instance, imf)) {
                retval.add(instance);
            }
        }
        return retval;
    }

    /**
     * Selects a set of instances from a given list which match the parameters
     * 
     * @param list
     * @param imf
     * @return
     */
    public List<Instance> select(List<Instance> list, boolean isScene,
            String attributes, String sceneAttributes) {
        List<Instance> retval = new ArrayList<>();
        for (Instance instance : list) {
            if (match(instance, isScene, attributes, sceneAttributes)) {
                retval.add(instance);
            }
        }
        return retval;
    }

    /**
     * Performs matching based on an instance match filter object. It simply
     * passed the parameters to the detailed match function.
     * 
     * @param instance
     * @param imf
     * @return
     */
    public boolean match(Instance instance, InstanceMatchFilter imf) {
        return match(instance, imf.isScene(), imf.getAttributes(),
                imf.getSceneAttributes());
    }

    /**
     * Uniform function for matching scenes or instances. It only dispatches to
     * the appropriate functions.
     * 
     * @param instance
     *            - the instance or scene we are testing
     * @param isScene
     *            - whether we are looking for a scene or an instance
     * @param attributes
     *            - the attributes of the instance or scene
     * @param sceneAttributes
     *            - the attributed of the scene if we are talking about an
     *            instance.
     * @return
     */
    public boolean match(Instance instance, boolean isScene, String attributes,
            String sceneAttributes) {
        if (isScene) {
            return matchScene(instance, attributes);
        } else {
            return matchInstance(instance, attributes, sceneAttributes);
        }
    }

    /**
     * Syntactic sugar for accepting any scene
     * 
     * @param instance
     * @param attributes
     * @return
     */
    public boolean matchInstance(Instance instance, String attributes) {
        return matchInstance(instance, attributes, null);
    }

    /**
     * Matching an instance based on its attributes and the attributes of the
     * scene
     * 
     * @param instance
     *            - the instance we want to match
     * @param attributes
     *            - the attributes of the instance, including labels we are
     *            interested in
     * @param sceneAttributes
     *            - the attributes of the scene, including labels. If it is
     *            null, take any.
     * @return
     */
    public boolean matchInstance(Instance instance, String attributes,
            String sceneAttributes) {
        // must be an instance, not a scene
        if (instance.isScene()) {
            return false;
        }
        // check for the attributes
        if (!attributes.equals(MATCH_ANY)) {
            ConceptOverlay co = null;
            try {
                co = xp.parseCo(attributes);
            } catch (XapiParserException e) {
                e.printStackTrace();
                TextUi.abort("Should not happen here " + e.toString());
            }
            if (!instance.getConcepts().coversWithLabels(co)) {
                return false;
            }
        }
        // check for the scene, if specified
        if (sceneAttributes != null) {
            if (!matchScene(instance.getScene(), sceneAttributes)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Matching a scene based on its attributes
     * 
     * @param scene
     * @param sceneAttributes
     * @return
     */
    private boolean matchScene(Instance scene, String sceneAttributes) {
        // must be a scene not an instance
        if (!scene.isScene()) {
            return false;
        }
        // check for attributes
        if (sceneAttributes.equals(MATCH_ANY)) {
            return true;
        }
        ConceptOverlay co = null;
        try {
            co = xp.parseCo(sceneAttributes);
        } catch (XapiParserException e) {
            e.printStackTrace();
            TextUi.abort("Should not happen here " + e.toString());
        }
        return scene.getConcepts().coversWithLabels(co);
    }
}
