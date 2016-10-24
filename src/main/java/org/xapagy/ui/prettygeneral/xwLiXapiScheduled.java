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
}
