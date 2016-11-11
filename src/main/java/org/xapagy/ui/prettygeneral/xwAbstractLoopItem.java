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
package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.agents.AbstractLoopItem.LoopItemState;
import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.smartprint.XapiPrint;

public class xwAbstractLoopItem {
	
	/**
	 * Provides the printing of the common subset of the different loopitems
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 */
	public static void xwDetailedSubset(IXwFormatter xwf, AbstractLoopItem li,
            Agent agent) {
		xwf.is("State", li.getState());
		xwf.is("Identifier", li.getIdentifier());
		if (li.getState().equals(LoopItemState.EXECUTED)) {
			xwf.is("Execution time", li.getExecutionTime());
			xwf.add("Execution result:");
			xwf.indent();
			for(VerbInstance vi: li.getExecutionResult()) {
				// FIXME: an xwVerbInstance would be nice here to make links
				// for the time being, let us just print the text
				xwf.add(XapiPrint.ppsViXapiForm(vi, agent));
			}
			xwf.deindent();
		}
    }


}
