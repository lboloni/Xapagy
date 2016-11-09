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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.xapagy.agents.Agent;

/**
 * @author Ladislau Boloni
 * Created on: Feb 12, 2016
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
