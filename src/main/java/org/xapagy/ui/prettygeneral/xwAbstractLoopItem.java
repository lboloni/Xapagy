package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.agents.Agent;
import org.xapagy.agents.liViBased;
import org.xapagy.ui.formatters.IXwFormatter;

public class xwAbstractLoopItem {
	
	/**
	 * Provides the printing of the common subset of the different loopitems
	 * @param xwf
	 * @param li
	 * @param agent
	 */
	public static void xwDetailedSubset(IXwFormatter xwf, AbstractLoopItem li,
            Agent agent) {
		xwf.is("State", li.getState());
		xwf.is("Identifier", li.getIdentifier());
		// xwf.is
    }


}
