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
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author lboloni
 * Created on: Jan 5, 2017
 */
public class SaFcmSummarization extends SpikeActivity {

	/**
	 * @param agent
	 * @param name
	 */
	public SaFcmSummarization(Agent agent, String name) {
		super(agent, name);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2592801386676711424L;

	/* (non-Javadoc)
	 * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
	 */
	@Override
	public void formatTo(IXwFormatter fmt, int detailLevel) {
		fmt.add("SaFcmSummarization");
	}

	/* (non-Javadoc)
	 * @see org.xapagy.activity.SpikeActivity#applyInner(org.xapagy.instances.VerbInstance)
	 */
	@Override
	public void applyInner(VerbInstance vi) {
		TextUi.println("Here we are in SaFcmSummarization");		
	}

	/* (non-Javadoc)
	 * @see org.xapagy.activity.Activity#extractParameters()
	 */
	@Override
	public void extractParameters() {
		// For the time being, we have no parameters here		
	}

}
