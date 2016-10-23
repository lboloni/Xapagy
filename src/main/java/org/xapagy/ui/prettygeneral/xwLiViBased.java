package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.agents.liViBased;
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
        // TODO add here
        xwf.is("forcedVI", XapiPrint.ppsViXapiForm(li.getForcedVi(), agent));
        xwf.is("forcedTimeAfter", li.getForcedTimeAfter());
        return xwf.toString();
    }

}
