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
package org.xapagy.debug.storygenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.instances.Instance;

/**
 * Contains the parts of the description which happens in one particular scene
 * of the RecordedStoryDescriptor
 * 
 * For the time being, this is used to refactor the RecordedStory ...
 * 
 * @author Ladislau Boloni
 * Created on: Feb 13, 2013
 */
public class RsScene {

    /**
     * The list of the instances created in the quoted scene. Enter here all the
     * properties and the labels we want them to be referenced with: eg.
     * "man 'Hector' #warrior1"
     * 
     */
    private List<String> instanceLabels = new ArrayList<>();
    /**
     * The instances in this scene, filled in when the initialization code is
     * running
     */
    private List<Instance> instances = new ArrayList<>();

    /**
     * The label of the scene
     */
    private String labelScene;

    /**
     * The name of the scene - describes it in the scenario
     */
    private String name;

    private Instance sceneInstance;

    /**
     * Constructor
     * 
     * @param name
     * @param rsd
     */
    public RsScene(String name, String labelScene) {
        this.name = name;
        this.labelScene = labelScene;
    }

    /**
     * Adds proper names to all the instances
     * 
     * @param properNameGenerator
     */
    public void addRandomPropernames(ProperNameGenerator properNameGenerator) {
        List<String> newList = new ArrayList<>();
        // in the main scene
        for (String concepts : instanceLabels) {
            String withPropername =
                    concepts + " " + properNameGenerator.generateProperName();
            newList.add(withPropername);
        }
        instanceLabels = newList;
    }

    /**
     * @return the instanceLabels
     */
    public List<String> getInstanceLabels() {
        return Collections.unmodifiableList(instanceLabels);
    }

    /**
     * @return the instances
     */
    public List<Instance> getInstances() {
        return instances;
    }

    /**
     * @return the labelScene
     */
    public String getLabelScene() {
        return labelScene;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the sceneInstance
     */
    public Instance getSceneInstance() {
        return sceneInstance;
    }

    public void setInstanceLabels(List<String> instanceWords2) {
        instanceLabels = new ArrayList<>(instanceWords2);
    }

    /**
     * Sets the words and labels defining the instances
     */
    public void setInstanceLabels(String... instanceWords) {
        instanceLabels = new ArrayList<>();
        for (String instanceWord : instanceWords) {
            instanceLabels.add(instanceWord);
        }
    }

    /**
     * @param sceneInstance
     *            the sceneInstance to set
     */
    public void setSceneInstance(Instance sceneInstance) {
        this.sceneInstance = sceneInstance;
    }

}
