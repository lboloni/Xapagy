/*
   This file is part of the Xapagy project
   Created on: Sep 14, 2011
 
   org.xapagy.ui.prettyprint.PpLoopItem
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.agents.liXapiReading;
import org.xapagy.agents.liViBased;
import org.xapagy.agents.liXapiScheduled;
import org.xapagy.ui.smartprint.XapiPrint;

import org.xapagy.agents.AbstractLoopItem;

/**
 * Pretty printing the loop item
 * 
 * @author Ladislau Boloni
 * 
 */
public class PpLoopItem {

	/**
	 * Try to summarize in one line the essence of the loop item
	 * 
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String ppConcise(AbstractLoopItem li, Agent agent) {
		StringBuffer buf = new StringBuffer();
		// prefix: executed / not executed
		switch (li.getState()) {
		case EXECUTED:
			buf.append("X:" + Formatter.fmt(li.getExecutionTime()) + " ");
			break;
		case NOT_EXECUTED:
			break;
		}
		if (li instanceof liXapiScheduled) {
			buf.append("External: " + ((liXapiScheduled)li).getXapiText());
		}
		if (li instanceof liHlsChoiceBased) {
			liHlsChoiceBased li2 = (liHlsChoiceBased) li;
			buf.append("Internal: ");
			buf.append(PrettyPrint.ppConcise(li2.getChoice(), agent));
		}
		if (li instanceof liViBased) {
			liViBased li2 = (liViBased) li;
			buf.append(XapiPrint.ppsViXapiForm(li2.getForcedVi(), agent));
		}
		if (li instanceof liXapiReading) {
			buf.append("Reading: " + li.getXapiText());
		}
		return buf.toString();
	}

	public static String ppDetailed(AbstractLoopItem li, Agent agent) {
		Formatter fmt = new Formatter();
		if (li instanceof liXapiScheduled) {
			fmt.add("LoopItem - External");
			fmt.indent();
			fmt.is("scheduled time", ((liXapiScheduled) li).getScheduledExecutionTime());
		}
		if (li instanceof liHlsChoiceBased) {
			liHlsChoiceBased li2 = (liHlsChoiceBased) li;
			fmt.add("LoopItem - Internal");
			fmt.indent();
			fmt.add(PrettyPrint.ppDetailed(li2.getChoice(), agent));
		}
		if (li instanceof liViBased) {
			liViBased li2 = (liViBased) li;
			fmt.add("LoopItem - liViBased");
			fmt.indent();
			fmt.add(XapiPrint.ppsViXapiForm(li2.getForcedVi(), agent));
		}
		if (li instanceof liXapiReading) {
			liXapiReading li2 = (liXapiReading) li;
			String header = "LoopItem - Reading";
			if (li2.getFileName() != null) {
				header = header + " (" + li2.getFileName() + ":" + li2.getFileLineNo() + ")";
			} else {
				header = header + "(directly added)";
			}
			fmt.add(header);
			fmt.add(li2.getXapiText());
			fmt.indent();
		}
		switch (li.getState()) {
		case EXECUTED:
			fmt.add("STATE: EXECUTED");
			fmt.is("Execution time", li.getExecutionTime());
			fmt.addIndented(PpListPartial.ppListPartial(li.getExecutionResult(), agent, PrintDetail.DTL_CONCISE, 100));
			break;
		case NOT_EXECUTED:
			fmt.add("STATE: not executed");
			break;
		}
		return fmt.toString();
	}

}
