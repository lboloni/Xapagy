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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwDrives;

/**
 * 
 * @author lboloni Created on: Nov 17, 2016
 */
public class Drives implements Serializable {

	private static final long serialVersionUID = -6400025065547224624L;
	/**
	 * The drive caused by hunger. It is increased by time - in increments of
	 * hours. It is decreased by eating. Equilibrium value is zero.
	 */
	public final String DRIVE_HUNGER = "Drive_Hunger";
	/**
	 * The drive caused by thirst. It is increased by time - in increments of
	 * hours. It is decreased by drinking water etc. Equilibrium value is zero.
	 */
	public final String DRIVE_THIRST = "Drive_Thirst";
	/**
	 * The drive caused by being tired. It is increased by exhausing physical
	 * activities. It is decreased by rest. This can happen over minutes...
	 */
	public final String DRIVE_TIREDNESS = "Drive_Tiredness";
	/**
	 * The drive caused by being sleepy. It is increased in time - over the span
	 * of a day. It is decreased by sleeping.
	 */
	public final String DRIVE_SLEEPINESS = "Drive_Sleepiness";
	/**
	 * It is increased when environments have unclear properties. It is
	 * decreased by increased knowledge.
	 * 
	 * FIXME: this needs to be clarified - it is unlikely that all this thing
	 * can be expressed in terms of actions???
	 */
	public final String DRIVE_CURIOSITY = "Drive_Curiosity";
	/**
	 * It is increased by events that cause pain. It is decreased by actions
	 * that are tied to reducing pain.
	 * 
	 * FIXME: There is a question here that we don't know which actions caused
	 * pain and which ones reduced it. This is certainly something that is
	 * reactive.
	 */
	public final String DRIVE_PAIN_AVOIDANCE = "Drive_PainAvoidance";
	/**
	 * It is increased by events that cause pleasure.
	 * 
	 * FIXME: this is also not very clear on how are they working
	 */
	public final String DRIVE_PLEASURE_SEEKING = "Drive_PleasureSeeking";
	/**
	 * It is increased by lack of events / actions. It is decreased by events.
	 * 
	 * FIXME: this is also not very clear on how it is working
	 */
	public final String DRIVE_BOREDOM = "Drive_Boredom";
	/**
	 * It is increased by repeated successful executions ... see Dimitrios'
	 * fights in the Iliad
	 */
	public final String DRIVE_FLOW = "Drive_Flow";

	/**
	 * The list of all the drives
	 */
	private List<String> drives = new ArrayList<>();
	/**
	 * Map keeping the current values of the drives, indexed by the drive name
	 */
	private Map<String, Double> drivesCurrent = new HashMap<>();
	/**
	 * Map keeping the equilibrium values of the drives, indexed by the drive
	 * name
	 */
	private Map<String, Double> drivesEquilibrium = new HashMap<>();
	/**
	 * Map keeping the target values of the drives, indexed by
	 */
	private Map<String, Double> drivesTarget = new HashMap<>();
	/**
	 * Link back to the agent
	 */
	private Agent agent;
	/**
	 * Double last updated
	 */
	double lastUpdated = 0;

	/**
	 * Default constructor, initializes the drives...
	 */
	public Drives(Agent agent) {
		this.agent = agent;
		lastUpdated = agent.getTime();
		initDrive();
	}

	/**
	 * In this call we update the drives
	 */
	public void updateDrives() {
		Focus fc = agent.getFocus();
		double time = agent.getTime() - lastUpdated;
		lastUpdated = agent.getTime();
		if (time == 0.0) return;
		TextUi.println("updating the drives for a period of " + Formatter.fmt(time));
		for(VerbInstance fvi: fc.getViList(EnergyColors.FOCUS_VI)) {
			double salience = fc.getSalience(fvi, EnergyColors.FOCUS_VI);
			
			// for(String driveName: getDriveNames()) {
			// 	agent.get
			// }
		}
	}

	
	/**
	 * This function calculates a set of drive changes for an instance
	 * 
	 * @param vi - the vi whose impacts we are calculating
	 * @param target - the instance which is assumed to have feelings... 
	 */
	public Map<String, Double> getDriveChanges(VerbInstance vi, Instance target) {
		// check whether the target is the subject
		// check whether the target is the object
		// if not, nothing		
		Map<String, Double> retval = new HashMap<>();
		return retval;
	}
	
	
	/**
	 * Returns the current value of the specified drive
	 * 
	 * @param driveName
	 * @return
	 */
	public double getCurrentValue(String driveName) {
		return drivesCurrent.get(driveName);
	}

	/**
	 * Sets the current value of the specified drive
	 * 
	 * @param driveName
	 * @return
	 */
	public double setCurrentValue(String driveName, double currentValue) {
		return drivesCurrent.put(driveName, currentValue);
	}

	/**
	 * Returns the target value of the specified drive
	 * 
	 * @param driveName
	 * @return
	 */
	public double getTargetValue(String driveName) {
		return drivesTarget.get(driveName);
	}

	/**
	 * Sets the target value of the specified drive
	 * 
	 * @param driveName
	 * @return
	 */
	public double setTargetValue(String driveName, double targetValue) {
		return drivesTarget.put(driveName, targetValue);
	}

	/**
	 * Returns the equilibrium value of the specified drive
	 * 
	 * @param driveName
	 * @return
	 */
	public double getEquilibriumValue(String driveName) {
		return drivesEquilibrium.get(driveName);
	}

	/**
	 * Sets the equilibrium value of the specified drive - normally, this should
	 * not be necessary
	 * 
	 * @param driveName
	 * @return
	 */
	public double setEquilibriumValue(String driveName, double targetValue) {
		return drivesTarget.put(driveName, targetValue);
	}

	/**
	 * Returns the list of drives
	 * 
	 * @return
	 */
	public List<String> getDriveNames() {
		return Collections.unmodifiableList(drives);
	}

	/**
	 * Adding an initial set of drives
	 */
	private void initDrive() {
		// physiological drives
		addDrive(DRIVE_HUNGER, 0.0, 0.0, 0.0);
		addDrive(DRIVE_THIRST, 0.0, 0.0, 0.0);
		addDrive(DRIVE_TIREDNESS, 0.0, 0.0, 0.0);
		addDrive(DRIVE_SLEEPINESS, 0.0, 0.0, 0.0);
		// pain and pleasure
		addDrive(DRIVE_PAIN_AVOIDANCE, 0.0, 0.0, 0.0);
		addDrive(DRIVE_PLEASURE_SEEKING, 0.0, 1.0, 1.0);
		// intellectual drives: curiosity, boredom and flow
		addDrive(DRIVE_CURIOSITY, 0.0, 0.0, 0.0);
		addDrive(DRIVE_BOREDOM, 0.0, 0.0, 0.0);
		addDrive(DRIVE_FLOW, 0.0, 0.0, 0.0);
	}

	/**
	 * Creates or overwrites a drive, setting initial values for the parameters
	 * 
	 * @param driveName
	 * @param current
	 * @param equilibrium
	 * @param target
	 */
	public void addDrive(String driveName, double current, double equilibrium, double target) {
		if (!drives.contains(driveName)) {
			drives.add(driveName);
		}
		drivesCurrent.put(driveName, current);
		drivesEquilibrium.put(driveName, equilibrium);
		drivesTarget.put(driveName, target);
	}

	/**
	 * Detailed formatting
	 */
	@Override
	public String toString() {
		TwFormatter twf = new TwFormatter();
		xwDrives.xwDetailed(twf, this, null);
		return twf.toString();
	}
}
