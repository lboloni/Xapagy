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
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.agents.AbstractLoopItem.LoopItemState;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.smartprint.XapiPrint;

public class xwLiHlsChoiceBased {
	/**
	 * Prints a detailed description of the loopitem. For some of it, it falls
	 * back to the generic loopitem description
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xwf, liHlsChoiceBased li, Agent agent) {
		xwf.addLabelParagraph("liViBased");
		xwf.indent();
		// common stuff for LoopItem
		xwAbstractLoopItem.xwDetailedSubset(xwf, li, agent);
		xwf.is("willingness", li.getWillingness());
		xwf.is("choice", li.getChoice());
		xwf.is("other recorded choices:", li.getRecordedChoices().size());
		return xwf.toString();
	}
	
	/**
	 * Prints a concise description of the HLS Choice based loopitem. Note that this
	 * was taken from the ToStringObserver, so it is more suitable for tracing
	 * than for investigation
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xwf, liHlsChoiceBased li, Agent agent) {
		StringBuffer buff = new StringBuffer();		
		if (li.getState().equals(LoopItemState.EXECUTED)) {
			buff.append("I~~~"); // showing is an approximation
			for (VerbInstance vi : li.getExecutionResult()) {
				buff.append(XapiPrint.ppsViXapiForm(vi, agent));
			}
		} else {
			buff.append("~~~ internal loopitem execution in progress...");
		}
		buff.append("(coming from choice:");
		buff.append(li.getChoice().getChoiceType() + " w=" + Formatter.fmt(li.getWillingness()));
		buff.append(")");	
		xwf.addPre(buff.toString());
		return xwf.toString();
	}
}
