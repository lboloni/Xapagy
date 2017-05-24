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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyQuantum;

/**
 * @author lboloni
 * Created on: Nov 23, 2016
 */
public class dsWeightedDiscountedTargetbased implements IDriveScore {

	private double timeDiscount = 0.9;
	private Map<String, Double> weights = new HashMap<>();
	
	/* (non-Javadoc)
	 * @see org.xapagy.agents.IDriveScore#score(org.xapagy.agents.Agent, org.xapagy.agents.Drives, double, org.xapagy.instances.VerbInstance)
	 */
	@Override
	public double score(Agent agent, Drives drives, double delayTime, VerbInstance vi) {
		List<EnergyQuantum<Instance>> changes = drives.getDriveChanges(vi, 1.0, 1.0);
		return 0;
	}

}
