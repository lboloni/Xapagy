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

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.formatters.IXwFormatter;

public class DaShmViMatchAction extends AbstractDaFocusIterator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4497179837030474787L;
	/**
	 * Multiplies the value of SHADOW_GENERIC energy applied to ACTION VIs
	 */
	private double scale;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.DiffusionActivity#extractParameters()
	 */
	@Override
	public void extractParameters() {
		scale = getParameterDouble("scale");
	}

	/**
	 * 
	 * @param agent
	 * @param name
	 */
	public DaShmViMatchAction(Agent agent, String name) {
		super(agent, name);
	}

	/**
	 *  For action VIs in the focus, add matching action verbs from the AM. 
	 *  
	 *  The weight of the different VIs depend on their frequency etc. according to the algorithm
	 *  in AmLookup.lookupVo
	 */
	@Override
	protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
		// if not an action VI, bail out
		if (!ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
			return;
		}
		// find all matching ones in AM with AM_VI
		// add the shadows with SHV_ACTION_MATCH for the VI and SHI_ACTION for instances
		ShmAddItemCollection saic = new ShmAddItemCollection();
		ViSet matches = AmLookup.lookupVi(agent, fvi, EnergyColors.AM_VI);
		for (VerbInstance svi : matches.getParticipants()) {
			double matchLevel = matches.value(svi);
			double addEnergy = matchLevel * scale;
			ShmAddItem sai = new ShmAddItem(agent, svi, fvi, addEnergy, EnergyColors.SHV_ACTION_MATCH,
					EnergyColors.SHI_ACTION);
			saic.addShmAddItem(sai);
		}
		SaicHelper.applySAIC_VisAndInstances(agent, saic, timeSlice, "DaShmViMatchAction.applyFocusVi");
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
		fmt.add("DaShmViMatchAction");
		fmt.indent();
		fmt.is("scale", scale);
		fmt.explanatoryNote("Scales the overall level of energies added to the shadows");
		fmt.deindent();
	}

}
