/*
   This file is part of the Xapagy project
   Created on: Sep 23, 2012
 
   org.xapagy.activity.shadowmaintenance.ShmAddItem
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import org.xapagy.agents.Agent;
import org.xapagy.algorithm.ISized;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.XapagyComponent;
import org.xapagy.ui.TextUi;

/**
 * This class represents an increment in the pair energy of color ec between focusComponent and shadowComponent.
 * The goal of this object is that put into a ShmAddItemCollection, it can then be used to provide stochastic 
 * sampling from these implements. 
 * 
 * To achieve this, this class implements the ISized interface. When the ppsSampling will be applied to this class,
 * the added energy will be the one. 
 * 
 * @author Ladislau Boloni
 * 
 */
public class ShmAddItem implements ISized {
    /**
     * The shadow energy which would be added if this choice is triggered
     */
    private double addedEnergy;
    /**
     * The instance color we want to add
     */
    private String ecInstance;
    /**
     * The VI color we want to add
     */
    private String ecVi;    
    /**
     * The focus component, either an Instance or a VerbInstance
     */
    private XapagyComponent focusComponent;
    /**
     * The shadow component, either an Instance or a VerbInstance
     */
    private XapagyComponent shadowComponent;

    /**
     * Creates an ShmAddItem from an instance
     * 
     * @param mi
     *            - the memory instance which will be added the shadow
     * @param fi
     *            - the focus instance into whose shadow we are adding stuff
     * @param score
     *            - the score with which we are adding stuff
     */
    public ShmAddItem(Agent agent, Instance mi, Instance fi, double daScore,
            String ecInstance) {
        this.shadowComponent = mi;
        this.focusComponent = fi;
        this.addedEnergy = daScore;
        this.ecInstance = ecInstance;
    }

    /**
     * Creates an ShmAddItem from an instance
     * 
     * @param mi
     * @param fi
     * @param score
     */
    public ShmAddItem(Agent agent, VerbInstance mvi, VerbInstance fvi,
            double daScore, String ecVi, String ecInstance) {
        this.shadowComponent = mvi;
        this.focusComponent = fvi;
        this.addedEnergy = daScore;
        this.ecVi = ecVi;
        this.ecInstance = ecInstance;
    }

    /**
     * Adds another item to this one: sum the
     * 
     * @param other
     */
    public void addNewItem(ShmAddItem other) {
        if (other.shadowComponent != shadowComponent) {
            TextUi.abort("ShmAddItem.addNewItem - the objects are not the same!!!");
        }
        addedEnergy += other.addedEnergy;
    }

    /**
     * @return the addedEnergy
     */
    public double getAddedEnergy() {
        return addedEnergy;
    }

    public String getEnergyColorInstance() {
        return ecInstance;
    }

    public String getEnergyColorVi() {
        return ecVi;
    }

    
    /**
     * Syntactic sugar, avoids casting outside
     * 
     * @return
     */
    public Instance getFocusInstance() {
        return (Instance) focusComponent;
    }

    /**
     * Syntactic sugar, avoids casting outside
     * 
     * @return
     */
    public VerbInstance getFocusVi() {
        return (VerbInstance) focusComponent;
    }

    /**
     * @return the shadowComponent
     */
    public XapagyComponent getObject() {
        return shadowComponent;
    }

    /**
     * Syntactic sugar, avoids casting outside
     * 
     * @return
     */
    public Instance getShadowInstance() {
        return (Instance) shadowComponent;
    }

    /**
     * Syntactic sugar, avoids casting outside
     * 
     * @return
     */
    public VerbInstance getShadowVi() {
        return (VerbInstance) shadowComponent;
    }

    /**
     * For the implementation of ISized - IProbabilityProportionalToSize
     * 
     * The size of the item for the implementation of the probability
     * proportional to size algorithm
     * 
     * In this simple version, we assume that the size is proportional to the
     * added energy
     * 
     * @return
     */
    @Override
    public double getSize() {
        return addedEnergy;
    }
}
