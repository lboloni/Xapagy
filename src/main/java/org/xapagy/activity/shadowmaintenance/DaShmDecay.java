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
package org.xapagy.activity.shadowmaintenance;

import java.util.HashMap;
import java.util.Map;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * DA performing a multiplicative decay of all the shadow energies across all
 * instances and VIs of all kinds. The different energies decay at different
 * rates specified by the parameters
 * 
 * @author Ladislau Boloni
 * Created on: Apr 22, 2011
 */
public class DaShmDecay extends AbstractDaFocusIterator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5751314173536351172L;
	/**
	 * The multiplier for the energy decay for instance shadows, indexed by
	 * shadow energy type
	 */
	private Map<String, Double> instanceShadowMultiplier;
	/**
	 * The multiplier for the energy decay for VI shadows, indexed by shadow
	 * energy type
	 */
	private Map<String, Double> viShadowMultiplier;

	/**
	 * @param agent
	 * @param name
	 */
	public DaShmDecay(Agent agent, String name) {
		super(agent, name);
	}

	/**
	 * Extract the parameters passed to the DA into the two maps
	 */
	@Override
	public void extractParameters() {
		// instance shadow energies
		instanceShadowMultiplier = new HashMap<>();
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
			double value = getParameterDouble("ISh_Mult_" + ec.toString());
			instanceShadowMultiplier.put(ec.toString(), value);
		}
		// VI shadow energies
		viShadowMultiplier = new HashMap<>();
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
			double value = getParameterDouble("ViSh_Mult_" + ec.toString());
			viShadowMultiplier.put(ec.toString(), value);
		}
	}

	/**
	 * Decaying of the shadows of non-scene instances
	 * 
	 * @param fi
	 * @param timeSlice
	 */
	@Override
	protected void applyFocusNonSceneInstance(Instance fi, double timeSlice) {
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
			double instanceRemainingEnergyRatio = instanceShadowMultiplier.get(ec.toString());
			for (Instance si : sf.getMembers(fi, ec)) {
				EnergyQuantum<Instance> sq = EnergyQuantum.createMult(fi, si, instanceRemainingEnergyRatio, timeSlice,
						ec, "DaShmDecay + applyFocusNonSceneInstance");
				sf.applyInstanceEnergyQuantum(sq);
			}
		}
	}

	/**
	 * Decaying of the shadows of scenes
	 * 
	 * @param fi
	 * @param timeSlice
	 */
	@Override
	protected void applyFocusScene(Instance fi, double timeSlice) {
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
			double instanceRemainingEnergyRatio = instanceShadowMultiplier.get(ec.toString());
			for (Instance si : sf.getMembers(fi, ec)) {
				EnergyQuantum<Instance> sq = EnergyQuantum.createMult(fi, si, instanceRemainingEnergyRatio, timeSlice,
						ec, "DaShmDecay + applyFocusScene");
				sf.applyInstanceEnergyQuantum(sq);
			}
		}
	}

	/**
	 * @param fvi
	 * @param timeSlice
	 */
	@Override
	protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
			double viRemainingEnergyRatio = viShadowMultiplier.get(ec.toString());
			for (VerbInstance svi : sf.getMembers(fvi, ec)) {
				EnergyQuantum<VerbInstance> sq = EnergyQuantum.createMult(fvi, svi, viRemainingEnergyRatio, timeSlice,
						ec, "DaShmDecay + applyFocusVi");
				sf.applyViEnergyQuantum(sq);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters
	 * .IXwFormatter, int)
	 */
	@Override
	public void formatTo(IXwFormatter fmt, int detailLevel) {
		fmt.add("DaShmDecay:  DA performing a multiplicative decay of all the shadow energies across "
				+ "all instances and VIs of all kinds. The different energies decay at different "
				+ "rates specified by the parameters");
		fmt.indent();
		fmt.add("Instance shadow energy multipliers");
		fmt.indent();
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
			double value = instanceShadowMultiplier.get(ec.toString());
			fmt.is("instanceShadowMultiplier_" + ec, value);
		}
		fmt.deindent();
		fmt.add("VI shadow energy multipliers");
		fmt.indent();
		for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
			double value = viShadowMultiplier.get(ec.toString());
			fmt.is("viShadowMultiplier_" + ec, value);
		}
		fmt.deindent();
		fmt.deindent();
	}

}
