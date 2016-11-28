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
package org.xapagy.activity.focusmaintenance;

import org.xapagy.activity.SpikeActivity;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * The spike activity which is triggered when a verb instance is inserted into
 * the focus. It creates the energies appropriate to the stuff.
 * 
 * @author Ladislau Boloni Created on: Jun 4, 2011
 */
public class SaFcmInsertVi extends SpikeActivity {

	private static final long serialVersionUID = -6199646656543411299L;

	/**
	 * 
	 * @param agent
	 * @param name
	 * @param vi
	 */
	public SaFcmInsertVi(Agent agent, String name) {
		super(agent, name);
	}

	/**
	 * Performs the insertion of the VI into the focus. Normal VIs are inserted
	 * using FOCUS energy, but the other ones marked as summarization VIs are
	 * inserted with summarization energy.
	 */
	@Override
	public void applyInner(VerbInstance vi) {
		Focus fc = agent.getFocus();
		//
		// Identify the energy types and values to be used
		// FIXME: adapt this based on the VI
		//

		String ecVI = null;
		String ecInstance = null;
		int level = vi.getSummarizationLevel();
		if (level == 0) {
			ecInstance = EnergyColors.FOCUS_INSTANCE;
			ecVI = EnergyColors.FOCUS_VI;
		} else {
			ecInstance = EnergyColors.FOCUS_SUMMARIZATION_VI + "_" + level;
			ecVI = EnergyColors.FOCUS_SUMMARIZATION_INSTANCE + "_" + level;
		}
		//
		// Ok, this is regular VI, we add focus energy
		//
		EnergyQuantum<VerbInstance> eq = EnergyQuantum.createAdd(vi, Focus.INITIAL_ENERGY_VI, ecVI,
				"SaFcmInsertVi + VI");
		fc.applyViEnergyQuantum(eq);
		for (ViPart part : ViStructureHelper.getAllowedInstanceParts(vi.getViType())) {
			Instance instance = (Instance) vi.getPart(part);
			EnergyQuantum<Instance> eq1 = EnergyQuantum.createAdd(instance, Focus.INITIAL_ENERGY_INSTANCE, ecInstance,
					"SaFcmInsertVi + InstStrength");
			fc.applyInstanceEnergyQuantum(eq1);

			EnergyQuantum<Instance> eq2 = EnergyQuantum.createAdd(instance.getScene(), Focus.INITIAL_ENERGY_INSTANCE,
					ecInstance, "SaFcmInsertVi + SceneStrength");
			fc.applyInstanceEnergyQuantum(eq2);
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
		fmt.add("SaFcmInsertVi");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.Activity#extractParameters()
	 */
	public void extractParameters() {
	}
}
