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
package org.xapagy.set;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ladislau Boloni
 * Created on: May 5, 2014
 */
public class EnergyColors implements Serializable {

    private static final long serialVersionUID = -382377352521001126L;

    public static final String SHI_ACTION = "SHI_ACTION";
    public static final String SHI_RELATION = "SHI_RELATION";
    public static final String SHI_ATTRIBUTE = "SHI_ATTRIBUTE";
    public static final String SHV_ACTION_MATCH = "SHV_ACTION_MATCH";
    public static final String SHV_TEMPORAL_ORDER = "SHV_TEMPORAL_ORDER";
    public static final String SHV_GENERIC = "SHV_GENERIC";
    public static final String SHI_GENERIC = "SHI_GENERIC";
    public static final String SCENE_INTERSTITIAL = "SCENE_INTERSTITIAL";
    public static final String SCENE_CONTINUATION = "SCENE_CONTINUATION";
    public static final String FOCUS_SUMMARIZATION_VI = "FOCUS_SUMMARIZATION_VI";
    public static final String FOCUS_SUMMARIZATION_INSTANCE = "FOCUS_SUMMARIZATION_INSTANCE";
    public static final String FOCUS_VI = "FOCUS_VI";
    public static final String FOCUS_INSTANCE = "FOCUS_INSTANCE";
    public static final String AM_VI = "AM_VI";
    public static final String AM_INSTANCE = "AM_INSTANCE";
    
    
    public enum EnergyColorType {
        AM_INSTANCE, AM_VI, FOCUS_INSTANCE, FOCUS_VI, SHADOW_INSTANCE, SHADOW_VI
    }
    
    /**
     * Energies sorted into sets by type
     */
    private Map<EnergyColorType, List<String>> energiesByType;
    /**
     * The parameter for mapping the specific energy to salience
     */
    private Map<String, Double> energy2salience = new HashMap<>();
    private List<String> energies;
    
    
    /**
     * Initialize the colors
     */
    public EnergyColors() {
        energies = new ArrayList<>();
        energiesByType = new HashMap<>();
        for(EnergyColorType ect: EnergyColorType.values()) {
            energiesByType.put(ect, new ArrayList<>());
        }
        // initEnergies();
    }

    
    /**
     * Returns the energy to salience sigmoid parameter for the specified energy color
     * 
     * @param ec
     * @return
     */
    public double getEnergyToSalience(String ec) {
       return energy2salience.get(ec); 
    }
    
    
    /**
     * Creates a new energy type by specifying the name, the type and the sigmoid parameter
     * @param name
     * @param type
     * @param sigmoidParameter
     */
    public void createEnergy(String name, EnergyColorType type, double e2s) {
        // checking if the energy already exists
        if (energies.contains(name)) {
            throw new Error("Energy " + name + " already exists");
        }
        energies.add(name);
        energiesByType.get(type).add(name);
        energy2salience.put(name, e2s);
    }
    
    /**
     * The list of energies used for shadowing instances
     * 
     * @return
     */
    public List<String> getEnergies(EnergyColorType type) {
        return energiesByType.get(type);
    }

    /**
     * The list of energies used for shadowing instances
     * 
     * @return
     */
    public List<String> getAllEnergies() {
        return energies;
    }


    /**
     * Converts the energy to the relative value
     * 
     * @param x
     * @return
     */
    public static double convert(double energy, double param) {
        return 2 / (1 + Math.exp(-param * energy)) - 1;
    }

    
}
