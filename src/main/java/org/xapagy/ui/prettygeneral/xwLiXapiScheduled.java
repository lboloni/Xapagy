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

import org.xapagy.agents.Agent;
import org.xapagy.agents.liXapiScheduled;
import org.xapagy.ui.formatters.IXwFormatter;

public class xwLiXapiScheduled {
	/**
	 * Prints a detailed description of the loopitem. For some of it, it falls
	 * back to the generic loopitem description
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xwf, liXapiScheduled li, Agent agent) {
		xwf.addLabelParagraph("liViBased");
		xwf.indent();
		// common stuff for LoopItem
		xwAbstractLoopItem.xwDetailedSubset(xwf, li, agent);
		xwf.is("xapiText", li.getXapiText());
		xwf.is("scheduled at", li.getScheduledExecutionTime());
		return xwf.toString();
	}

	/**
	 * Prints a concise description of the scheduled loopitem. Note that this
	 * was taken from the ToStringObserver, so it is more suitable for tracing
	 * than for investigation
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xwf, liXapiScheduled li, Agent agent) {
		StringBuffer buff = new StringBuffer();
		buff.append(li.getXapiText());
		buff.append(" --- ");
		buff.append("(was scheduled at " + String.format("%5.1f", agent.getTime()) + ")");
		xwf.addPre(buff.toString());
		return xwf.toString();
	}
}
