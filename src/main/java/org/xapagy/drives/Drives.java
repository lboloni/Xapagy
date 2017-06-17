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
package org.xapagy.drives;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.set.EnergySet;
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
	public final String DRIVE_PAIN_AVOIDANCE = "Drive_Pain_Avoidance";
	/**
	 * It is increased by events that cause pleasure.
	 * 
	 * FIXME: this is also not very clear on how are they working
	 */
	public final String DRIVE_PLEASURE_SEEKING = "Drive_Pleasure_Seeking";
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
	 * The drive energies mapped by instance
	 */
	EnergySet<Instance> energyDrives;
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
	private double lastUpdated = 0;
	/**
	 * Instance representing the current self
	 */
	private Instance currentSelf = null;
	
	/**
	 * Returns the current self
	 * @return
	 */
	public Instance getCurrentSelf() {
		return currentSelf;
	}

	/**
	 * The list of the selves
	 */
	private List<Instance> selfHistory = new ArrayList<>();
	/**
	 * Tracking the drives of other instances...
	 */
	private Map<String, Drives> drivesOfOthers = new HashMap<>();

	/**
	 * Default constructor, initializes the drives...
	 */
	public Drives(Agent agent) {
		this.agent = agent;
		lastUpdated = agent.getTime();
		energyDrives = new EnergySet<>(agent);
		initDrive();
	}

	/**
	 * Sets the current self of the drives
	 * 
	 * @param instance
	 */
	public void setSelf(Instance instance) {
		if (instance.equals(currentSelf)) {
			return;
		}
		currentSelf = instance;
		selfHistory.add(currentSelf);
	}

	/**
	 * Adding a "drive of others" concept
	 * 
	 * @param name
	 * @param instance
	 */
	public void addDriveOfOthers(String name, Instance instance) {
		Drives dr = new Drives(agent);
		drivesOfOthers.put(name, dr);
		dr.setSelf(instance);
	}

	/**
	 * Sets the self of others
	 * 
	 * @param name
	 * @param newSelf
	 */
	public void setSelfOfOthers(String name, Instance newSelf) {
		drivesOfOthers.get(name).setSelf(newSelf);
	}

	/**
	 * Return the drive object of others
	 * 
	 * @param name
	 * @return
	 */
	public Drives getDrivesOfOthers(String name) {
		return drivesOfOthers.get(name);
	}

	/**
	 * In this call we update the drives. This is called periodically in the
	 * loop.
	 * 
	 * The drives are updated by the VIs in the focus, and whether they do it, 
	 * it depends whether their subject or object is the self.
	 * 
	 */
	public void updateDrives() {
		//TextUi.println("Begin update drives");
		Focus fc = agent.getFocus();
		double timeSlice = agent.getTime() - lastUpdated;
		lastUpdated = agent.getTime();
		if (timeSlice == 0.0)
			return;
		List<EnergyQuantum<Instance>> quantums = new ArrayList<>();
		// consider the impact of the verbs in the vi's
		for (VerbInstance fvi : fc.getViList(EnergyColors.FOCUS_VI)) {
			double salience = fc.getSalience(fvi, EnergyColors.FOCUS_VI);
			quantums.addAll(getDriveChanges(fvi, timeSlice, salience));
		}
		// consider the impact of the passage of the time
		quantums.addAll(getDriveChangesInTime(timeSlice));
		for (EnergyQuantum<Instance> eq : quantums) {
			energyDrives.applyEnergyQuantum(eq);
		}
		// call for the drives of others
		for (Drives drives : drivesOfOthers.values()) {
			drives.updateDrives();
		}
	}

	/**
	 * Returns the set of energy quantums describing the impact of the passing
	 * of the time. By convention, the additive values here are stored in the
	 * V_DOES_NOTHING verb
	 * 
	 * @return
	 */
	public List<EnergyQuantum<Instance>> getDriveChangesInTime(double timeSlice) {
		List<EnergyQuantum<Instance>> retval = new ArrayList<>();
		AbstractConceptDB<Verb> vdb = agent.getVerbDB();
		Verb verb = vdb.getConcept(Hardwired.V_DOES_NOTHING);
		Map<String, Double> impacts = verb.getDriveImpactsOnSubject();
		for (String drive : impacts.keySet()) {
			double impact = impacts.get(drive);
			EnergyQuantum<Instance> eq = null;
			// positive impacts are additive, negatives multiplicative...
			if (impact > 0) {
				eq = EnergyQuantum.createAdd(currentSelf, impact, timeSlice, drive, "impact from passing of time");
			} else {
				eq = EnergyQuantum.createMult(currentSelf, -impact, timeSlice, drive, "impact from passing of time");
			}
			retval.add(eq);
		}
		return retval;
	}

	/**
	 * Returns a set of energy quantums describing the impact of specified VI on
	 * the drives of the self
	 * 
	 * @param vi
	 *            - the vi whose impacts we are calculating
	 * @param timeSlice
	 *            - the timeslice over which we are calculating the stuff
	 * @param salience
	 *            - the salience of the VI, scales the changes
	 * @return the list of energy quantums for the different drives
	 */
	public List<EnergyQuantum<Instance>> getDriveChanges(VerbInstance vi, double timeSlice, double salience) {
		List<EnergyQuantum<Instance>> retval = new ArrayList<>();
		AbstractConceptDB<Verb> vdb = agent.getVerbDB();
		// check whether the target is the subject
		if (vi.getSubject().equals(currentSelf)) {
			// for all the verbs in the VI
			for (SimpleEntry<Verb, Double> entry : vi.getVerbs().getSortedByExplicitEnergy()) {
				Verb verb = entry.getKey();
				double strength = entry.getValue();
				Map<String, Double> impacts = verb.getDriveImpactsOnSubject();
				for (String drive : impacts.keySet()) {
					EnergyQuantum<Instance> eq = null;
					double implicitTimeSlice = salience * strength * timeSlice;
					double impact = impacts.get(drive);
					// positive impacts are additive, negatives
					// multiplicative...
					if (impact > 0) {
						eq = EnergyQuantum.createAdd(currentSelf, impact, implicitTimeSlice, drive,
								"subject impact from " + verb.getName());
					} else {
						eq = EnergyQuantum.createMult(currentSelf, -impact, implicitTimeSlice, drive,
								"subject impact from " + verb.getName());
					}
					retval.add(eq);
				}
			}
			return retval;
		}
		// check whether the target is the object
		if (vi.getViType().equals(ViType.S_V_O) && vi.getObject().equals(currentSelf)) {
			// for all the verbs in the VI
			for (SimpleEntry<Verb, Double> entry : vi.getVerbs().getSortedByExplicitEnergy()) {
				Verb verb = entry.getKey();
				double strength = entry.getValue();
				Map<String, Double> impacts = verb.getDriveImpactsOnObject();
				for (String drive : impacts.keySet()) {
					EnergyQuantum<Instance> eq = null;
					double implicitTimeSlice = salience * strength * timeSlice;
					double impact = impacts.get(drive);
					// positive impacts are additive, negatives
					// multiplicative...
					if (impact > 0) {
						eq = EnergyQuantum.createAdd(currentSelf, impact, timeSlice, drive,
								"object impact from " + verb.getName());
					} else {
						eq = EnergyQuantum.createMult(currentSelf, -impact, implicitTimeSlice, drive,
								"object impact from " + verb.getName());
					}
					retval.add(eq);
				}
			}
			return retval;
		}
		// if not, nothing
		return retval;
	}

	/**
	 * Returns the current value of the specified drive
	 * 
	 * @param driveName
	 * @return
	 */
	public double getCurrentValue(String driveName) {
		return energyDrives.valueEnergy(currentSelf, driveName);
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
		// drivesCurrent.put(driveName, current);
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
