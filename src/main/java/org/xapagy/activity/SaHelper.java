/*
   This file is part of the Xapagy project
   Created on: Feb 12, 2016
 
   org.xapagy.activity.SaHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 *
 */
public class SaHelper {
    /**
     * Clears a specified composite DA from the agent
     * 
     * @param agent
     * @param compositeName
     *            - the name under which the composite DA is registered in the
     *            agent
     */
    public static void clearComposite(Agent agent, String compositeName) {
        SaComposite sc = (SaComposite) agent.getSaComposite(compositeName);
        sc.clearSpikeActivities();
    }

    /**
     * Instantiates a new DA by reflection, sets its parameters and adds it to
     * the specified composite from the agent
     * 
     * @param agent
     * @param groupName
     * @param daClassName
     * @param parameters
     * @throws Exception
     */
    public static void addSaToComposite(Agent agent, String groupName,
            String daName, String daClassName, Map<String, String> parameters) {
        SpikeActivity da = null;
        try {
            Class<?> daClass = Class.forName(daClassName);
            Constructor<?> daClassConstructor =
                    daClass.getConstructor(Agent.class, String.class);
            da = (SpikeActivity) daClassConstructor.newInstance(agent,
                    daName);
        } catch (ClassNotFoundException | NoSuchMethodException
                | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException x) {
            // x.printStackTrace();
            String cause = x.getCause().toString();
            cause += "\n for " + daClassName;
            cause += "\n params were: " + parameters.toString();
            Error e = new Error(cause);
            e.printStackTrace();
            throw e;
        }
        // adding the documentation
        da.setAndExtractParameters(parameters);
        da.setDescription(agent.getDocumentation());
        agent.setDocumentation("");
        SaComposite cda = (SaComposite) agent.getSaComposite(groupName);
        cda.addSpikeActivity(da);
    }

    /**
     * Updates a new DA in a group
     * 
     * @param agent
     * @param groupName
     * @param daName
     * @param parameters
     * @throws Exception
     */
    public static void updateSaInComposite(Agent agent, String groupName,
            String daName, Map<String, String> parameters) {
        SpikeActivity theDA = null;
        SaComposite cda = (SaComposite) agent.getSaComposite(groupName);
        for (SpikeActivity da : cda.getSpikeActivities()) {
            if (da.getName().equals(daName)) {
                theDA = da;
                break;
            }
        }
        if (theDA == null) {
            throw new Error("Could not found DA named " + daName + " in group "
                    + groupName);
        }
        theDA.setAndExtractParameters(parameters);
    }
}
