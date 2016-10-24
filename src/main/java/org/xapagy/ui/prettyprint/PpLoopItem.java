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
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwLiHlsChoiceBased;
import org.xapagy.ui.prettygeneral.xwLiViBased;
import org.xapagy.ui.prettygeneral.xwLiXapiReading;
import org.xapagy.ui.prettygeneral.xwLiXapiScheduled;
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
			buf.append("Reading: " + ((liXapiReading)li).getXapiText());
		}
		return buf.toString();
	}

	public static String ppDetailed(AbstractLoopItem li, Agent agent) {
		PwFormatter fmt = new PwFormatter();
        if (li instanceof liXapiScheduled) {
        	xwLiXapiScheduled.xwDetailed(fmt, (liXapiScheduled)li, agent);
        }
        if (li instanceof liHlsChoiceBased) {
        	xwLiHlsChoiceBased.xwDetailed(fmt, (liHlsChoiceBased)li, agent);
        }
        if (li instanceof liXapiReading) {
        	xwLiXapiReading.xwDetailed(fmt, (liXapiReading)li, agent);
        }
        if (li instanceof liViBased) {
        	xwLiViBased.xwDetailed(fmt, (liViBased)li, agent);
        }
        return fmt.toString();
	}

}
