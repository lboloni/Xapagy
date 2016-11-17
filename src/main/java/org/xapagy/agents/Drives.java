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
package org.xapagy.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lboloni
 * Created on: Nov 17, 2016
 */
public class Drives implements Serializable {

	public final String DRIVE_HUNGER = "Drive_Hunger";
	public final String DRIVE_THIRST = "Drive_Thirst";
	public final String DRIVE_TIREDNESS = "Drive_Tiredness";
	public final String DRIVE_SLEEPINESS = "Drive_Sleepiness";
	public final String DRIVE_CURIOSITY = "Drive_Curiosity";
	
	
	private List<String> drives = new ArrayList<>();
	private Map<String, Double> drivesCurrent = new HashMap<>();
	private Map<String, Double> drivesEquilibrium = new HashMap<>();
	private Map<String, Double> drivesTarget = new HashMap<>();
	
	/**
	 * Default constructor, initializes the drives...
	 */
	public void Drives() {
		
	}
	
	private void initDrive() {
		
	}
	
}
