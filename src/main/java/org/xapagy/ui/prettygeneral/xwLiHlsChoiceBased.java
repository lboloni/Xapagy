package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liHlsChoiceBased;
import org.xapagy.agents.AbstractLoopItem.LoopItemState;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.smartprint.XapiPrint;

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
	
	/**
	 * Prints a concise description of the HLS Choice based loopitem. Note that this
	 * was taken from the ToStringObserver, so it is more suitable for tracing
	 * than for investigation
	 * 
	 * @param xwf
	 * @param li
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xwf, liHlsChoiceBased li, Agent agent) {
		StringBuffer buff = new StringBuffer();		
		if (li.getState().equals(LoopItemState.EXECUTED)) {
			buff.append("I~~~"); // showing is an approximation
			for (VerbInstance vi : li.getExecutionResult()) {
				buff.append(XapiPrint.ppsViXapiForm(vi, agent));
			}
		} else {
			buff.append("~~~ internal loopitem execution in progress...");
		}
		buff.append("(coming from choice:");
		buff.append(li.getChoice().getChoiceType() + " w=" + Formatter.fmt(li.getWillingness()));
		buff.append(")");	
		xwf.addPre(buff.toString());
		return xwf.toString();
	}
}
