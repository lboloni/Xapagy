package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liViBased;
import org.xapagy.agents.liXapiScheduled;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.smartprint.XapiPrint;

public class xwLiViBased {

	/**
	 * Prints a detailed description of the loopitem. For some of it, it falls back to the
	 * generic loopitem description 
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xwf, liViBased li,
            Agent agent) {
        xwf.addLabelParagraph("liViBased");
        xwf.indent();
        // common stuff for LoopItem
        xwAbstractLoopItem.xwDetailedSubset(xwf, li, agent);
        xwf.is("forcedVI", XapiPrint.ppsViXapiForm(li.getForcedVi(), agent));
        xwf.is("forcedTimeAfter", li.getForcedTimeAfter());
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
	public static String xwConcise(IXwFormatter xwf, liViBased li, Agent agent) {
		StringBuffer buff = new StringBuffer();
		buff.append(XapiPrint.ppsViXapiForm(li.getForcedVi(), agent));
		buff.append(" --- (explicit VI based, story line reasoning)");
		xwf.addPre(buff.toString());
		return xwf.toString();
	}
}
