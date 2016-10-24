package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.AbstractLoopItem;
import org.xapagy.agents.AbstractLoopItem.LoopItemState;
import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.smartprint.XapiPrint;

public class xwAbstractLoopItem {
	
	/**
	 * Provides the printing of the common subset of the different loopitems
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 */
	public static void xwDetailedSubset(IXwFormatter xwf, AbstractLoopItem li,
            Agent agent) {
		xwf.is("State", li.getState());
		xwf.is("Identifier", li.getIdentifier());
		if (li.getState().equals(LoopItemState.EXECUTED)) {
			xwf.is("Execution time", li.getExecutionTime());
			xwf.add("Execution result:");
			xwf.indent();
			for(VerbInstance vi: li.getExecutionResult()) {
				// FIXME: an xwVerbInstance would be nice here to make links
				// for the time being, let us just print the text
				xwf.add(XapiPrint.ppsViXapiForm(vi, agent));
			}
			xwf.deindent();
		}
    }


}
