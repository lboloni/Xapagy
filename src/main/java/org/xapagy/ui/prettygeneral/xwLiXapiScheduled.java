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
