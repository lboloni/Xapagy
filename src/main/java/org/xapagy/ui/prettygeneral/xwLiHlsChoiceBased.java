package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.ui.formatters.IXwFormatter;

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
}
