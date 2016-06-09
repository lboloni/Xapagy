/*
   This file is part of the Xapagy project
   Created on: Feb 13, 2013
 
   org.xapagy.debug.storygenerator.RsdScene
 
   Copyright (c) 2008-2014 Ladislau Boloni
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
 * 
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
