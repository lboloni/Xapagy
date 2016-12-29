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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xapagy.agents.Agent;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * A diffusion activity composed of a number of member DAs which will be called
 * in order.
 * 
 * @author Ladislau Boloni Created on: Jan 5, 2016
 */
public class DaComposite extends DiffusionActivity {

	private static final long serialVersionUID = 2419337758833747308L;

	/**
	 * 
	 * @param agent
	 * @param name
	 */
	public DaComposite(Agent agent, String name) {
		super(agent, name);
	}

	private List<DiffusionActivity> members = new ArrayList<>();

	/**
	 * Returns an unmodifiable list of the member DAs
	 * 
	 * @return
	 */
	public List<DiffusionActivity> getDiffusionActivities() {
		return Collections.unmodifiableList(members);
	}

	/**
	 * Adds a new diffusion activity at the end of the member DA list
	 * 
	 * @param da
	 */
	public void addDiffusionActivity(DiffusionActivity da) {
		members.add(da);
	}

	public void clearDiffusionActivities() {
		members.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.DiffusionActivity#applyInner(double)
	 */
	@Override
	protected void applyInner(double time) {
		for (DiffusionActivity member : members) {
			member.applyInner(time);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.DiffusionActivity#extractParameters()
	 */
	@Override
	public void extractParameters() {
		// Nothing here, composite does not have parameters
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
		fmt.add("DaComposite");
		fmt.indent();
		for (Activity member : members) {
			member.formatTo(fmt, detailLevel);
		}
		fmt.deindent();
	}

}
