package org.xapagy.ui.prettygeneral;

import java.io.File;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liXapiReading;
import org.xapagy.agents.liXapiScheduled;
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
	
	/**
	 * Prints a concise description of the reading based loopitem. Note that this
	 * was taken from the ToStringObserver, so it is more suitable for tracing
	 * than for investigation
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xwf, liXapiReading li, Agent agent) {
		StringBuffer buff = new StringBuffer();
		buff.append(li.getXapiText());
		buff.append(" --- ");
		if (li.getFileName() != null) {
			File f = new File(li.getFileName());
			buff.append("(" + f.getName() + ":" + li.getFileLineNo() + ")");
		} else {
			buff.append("(reading/generated)");
		}	
		xwf.addPre(buff.toString());
		return xwf.toString();
	}
}
