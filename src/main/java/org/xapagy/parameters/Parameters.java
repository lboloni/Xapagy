/*
   This file is part of the Xapagy project
   Created on: Apr 17, 2014
 
   org.xapagy.parameters.ParamNew
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.parameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwParameters;

/**
 * New implementation of the parameters class. Direct access is overvalued - the
 * objective is to make it easier to extend etc.
 * 
 * So, hashtables here we come.
 * 
 * Conventions: boolean: 0.0 false, 1.0 for true, -1000.0 for not initialized
 * 
 * April 17, 2014
 * 
 * @author Ladislau Boloni
 * 
 */
public class Parameters implements Serializable {

    private static final long serialVersionUID = -8530320755301343172L;
    /**
     * Stores the areas in a list in the order in which they had been defined
     */
    private List<String> areas;
    private Map<String, Map<String, Map<String, Double>>> defaults;
    /**
     * Stores the descriptions
     */
    private Map<String, String> descriptions;
    /**
     * Stores the groups in an area in the order in which they were created
     */
    private Map<String, List<String>> groupsInArea;
    /**
     * Stores the names in groups in the order in which they were created
     */
    private Map<String, Map<String, List<String>>> namesInGroup;
    private Map<String, Map<String, Map<String, Double>>> values;

    /**
     * Constructor - initialize the variables
     */
    public Parameters() {
        values = new HashMap<>();
        defaults = new HashMap<>();
        areas = new ArrayList<>();
        groupsInArea = new HashMap<>();
        namesInGroup = new HashMap<>();
        descriptions = new HashMap<>();
    }

    /**
     * Used to initialize a parameter with boolean values
     * 
     * @param area
     * @param group
     * @param name
     * @param defValue
     * @param description
     */
    public void addParam(String area, String group, String name,
            boolean defValueBoolean, String description) {
        double defValue = defValueBoolean ? 1.0 : 0.0;
        addParam(area, group, name, defValue, null);
        if (description != null) {
            setDescription(area, group, name, description);
        }
    }

    /**
     * Used to initialize a parameter, with its empty and default values
     * 
     * @param area
     * @param group
     * @param name
     * @param defValue
     * @param emptyValue
     * @param description
     *            TODO
     */
    public void addParam(String area, String group, String name,
            double defValue, String description) {
        setIn(true, values, area, group, name, defValue);
        setIn(false, defaults, area, group, name, defValue);
        if (description != null) {
            setDescription(area, group, name, description);
        }
    }

    /**
     * Copy all the values and the descriptions
     * 
     * @param model
     */
    public void copyAll(Parameters model) {
        for (String area : model.listAreas()) {
            for (String group : model.listGroups(area)) {
                for (String name : model.listNames(area, group)) {
                    addParam(area, group, name, model.get(area, group, name),
                            null);
                }
            }
        }
        this.descriptions = new HashMap<>(model.descriptions);
    }

    /**
     * Gets the value of a certain parameter
     * 
     * @param area
     * @param group
     * @param name
     * @param value
     */
    public double get(String area, String group, String name) {
        Map<String, Map<String, Double>> valueArea = values.get(area);
        if (valueArea == null) {
            TextUi.errorPrint("Could not find area for " + area + " - " + group
                    + " - " + name);
        }
        Map<String, Double> valueGroup = valueArea.get(group);
        if (valueGroup == null) {
            TextUi.errorPrint("Could not find group for " + area + " - " + group
                    + " - " + name);
        }
        Double value = valueGroup.get(name);
        if (value == null) {
            TextUi.errorPrint("Could not find name for " + area + " - " + group
                    + " - " + name);
        }
        return value.doubleValue();
    }

    /**
     * Gets the value of a certain parameter interpreted as a boolean. In this
     * particular case, a not existing parameter is interpreted as a false
     * 
     * @param area
     * @param group
     * @param name
     * @param value
     */
    public boolean getBoolean(String area, String group, String name) {
        try {
            double value = get(area, group, name);
            return value > 0.0;
        } catch (NullPointerException npe) {
            return false;
        }
    }

    /**
     * Gets the description of a certain value (or null if there is none)
     * 
     * @param area
     * @param group
     * @param name
     * @param description
     */
    public String getDescription(String area, String group, String name) {
        String key = area + "_" + group + "_" + name;
        return descriptions.get(key);
    }

    /**
     * Gets the value of a certain parameter interpreted as an integer
     * 
     * @param area
     * @param group
     * @param name
     * @param value
     */
    public int getInteger(String area, String group, String name) {
        double value = get(area, group, name);
        return (int) value;
    }

    /**
     * Lists the areas in the order in which they had been created
     * 
     * @return
     */
    public List<String> listAreas() {
        return areas;
    }

    /**
     * Lists the groups in an area in the order in which they were created
     * 
     * @return
     */
    public List<String> listGroups(String area) {
        return groupsInArea.get(area);
    }

    /**
     * Lists the names in a specific group in an area in the order in which they
     * were created
     * 
     * @return
     */
    public List<String> listNames(String area, String group) {
        return namesInGroup.get(area).get(group);
    }

    /**
     * Sets boolean a specific parameter to a certain value -
     * 
     * @param area
     * @param group
     * @param name
     * @param value
     */
    public void set(String area, String group, String name, boolean boolValue) {
        double value = boolValue ? 1.0 : 0.0;
        set(area, group, name, value);
    }

    /**
     * Sets a specific parameter to a certain value (but assumes existence of
     * the group)
     * 
     * @param area
     * @param group
     * @param name
     * @param value
     */
    public void set(String area, String group, String name, double value) {
        // this is here to trigger an error if we are setting something which
        // had
        // not been previously initialized
        @SuppressWarnings("unused")
        double oldVal = get(area, group, name);
        Map<String, Map<String, Double>> valueArea = values.get(area);
        Map<String, Double> valueGroup = valueArea.get(group);
        valueGroup.put(name, value);
    }

    /**
     * Sets the description of a certain value
     * 
     * @param area
     * @param group
     * @param name
     * @param description
     */
    private void setDescription(String area, String group, String name,
            String description) {
        String key = area + "_" + group + "_" + name;
        descriptions.put(key, description);
    }

    /**
     * Sets a certain value in a repository
     * 
     * @param def
     *            - if true, it is the definition setting, so we are setting the
     *            lists as well
     * 
     * @param repository
     * @param area
     * @param group
     * @param name
     * @param value
     */
    private void setIn(boolean def,
            Map<String, Map<String, Map<String, Double>>> repository,
            String area, String group, String name, double value) {
        Map<String, Map<String, Double>> valueArea = repository.get(area);
        if (valueArea == null) {
            valueArea = new HashMap<>();
            repository.put(area, valueArea);
            if (def) {
                areas.add(area);
                groupsInArea.put(area, new ArrayList<String>());
                namesInGroup.put(area, new HashMap<String, List<String>>());
            }
        }
        Map<String, Double> valueGroup = valueArea.get(group);
        if (valueGroup == null) {
            valueGroup = new HashMap<>();
            valueArea.put(group, valueGroup);
            if (def) {
                groupsInArea.get(area).add(group);
                namesInGroup.get(area).put(group, new ArrayList<String>());
            }
        }
        if (def) {
            if (!valueGroup.containsKey(name)) {
                namesInGroup.get(area).get(group).add(name);
            }
        }
        valueGroup.put(name, value);
    }

    /**
     * Detailed formatting
     */
    @Override
    public String toString() {
        TwFormatter twf = new TwFormatter();
        xwParameters.xwDetailed(twf, this);
        return twf.toString();
    }

}
