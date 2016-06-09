/*
   This file is part of the Xapagy project
   Created on: Apr 22, 2011
 
   org.xapagy.story.Activity
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.ui.formatters.IxwFormattable;

/**
 * 
 * The ancestor of all the activity classes (Spike and Diffusion) These
 * activities must be self describing and self-monitoring for debugging
 * purposes.
 * 
 * @author Ladislau Boloni
 * 
 */
public abstract class Activity implements Serializable, IxwFormattable {

    private static final long serialVersionUID = 7200661641720685777L;
    /**
     * Link to the agent to which they are tied
     */
    protected Agent agent;
    /**
     * The description of the activity
     */
    private String description;
    /**
     * The name of the activity - it is necessary, because the same activity, differently 
     * parameterized might be serving in several places
     */
    private String name;
    protected Map<String, String> parameters = new HashMap<>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Default constructor, forces name in the rest of the activities
     * 
     * @param agent
     * @param name
     */
    public Activity(Agent agent, String name) {
        super();
        this.agent = agent;
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Overwrites the existing parameters and calls extractParameters
     * @param parameters the parameters to set
     */
    public void setAndExtractParameters(Map<String, String> param) {
        parameters = new HashMap<>();
        for(String key: param.keySet()) {
            parameters.put(key, param.get(key));
        }
        extractParameters();
    }

    /**
     * Gets a parameter, throws appropriate error if not found
     * @param name
     * @return
     */
    public String getParameterString(String name) {
        String value = parameters.get(name);
        if (value == null) {
            throw new Error("Expected parameter " + name + " not found");
        }
        return value;
    }

    /**
     * Gets a parameter, throws appropriate exception if not found
     * @param name
     * @return
     * @throws Exception
     */
    public double getParameterDouble(String name) {
        String value = null;
        double b = 0.0;
        try {
            value = parameters.get(name);
            b = Double.parseDouble(value);
        } catch(NullPointerException npe) {
            throw new Error("Expected parameter " + name + " not found");            
        } catch(NumberFormatException nfe) {
            throw new Error("The value " + value + " for parameter " + name + " cannot be parsed to double.");                        
        }
        return b;
    }

    /**
     *  Extracts the parameters from parameters into local variables. MUST be overwritten
     *  in every Activity
     */
    public abstract void extractParameters();

}
