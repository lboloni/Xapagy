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
 * Created on: Apr 22, 2011
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
