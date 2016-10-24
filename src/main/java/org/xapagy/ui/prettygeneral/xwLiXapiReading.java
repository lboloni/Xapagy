package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liXapiReading;
import org.xapagy.ui.formatters.IXwFormatter;

public class xwLiXapiReading {
	/**
	 * Prints a detailed description of the loopitem. For some of it, it falls
	 * back to the generic loopitem description
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xwf, liXapiReading li, Agent agent) {
		xwf.addLabelParagraph("liXapiReading");
		xwf.indent();
		// common stuff for LoopItem
		xwAbstractLoopItem.xwDetailedSubset(xwf, li, agent);
		xwf.is("xapiText", li.getXapiText());
		return xwf.toString();
	}
}
