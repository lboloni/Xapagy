package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Drives;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.IXwFormatter;

public class xwDrives {
	/**
	 * Detailed printing of the drives
	 * 
	 * @param xw
	 * @param drives
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xw, Drives drives, Agent agent) {
		xw.add("Drives:");
		xw.indent();
		xw.addBold("Name = currentValue / targetValue / equilibriumValue ");
		for (String driveName : drives.getDriveNames()) {
			xw.is(driveName,
					"" + Formatter.fmt(drives.getCurrentValue(driveName)) + " / "
							+ Formatter.fmt(drives.getTargetValue(driveName)) + " / "
							+ Formatter.fmt(drives.getEquilibriumValue(driveName)));
		}
		xw.deindent();
		return xw.toString();
	}

	/**
	 * Concise printing of the drives: difficult to imagine how can we print these 
	 * possible, so just fall back to the detailed one.
	 * 
	 * @param xw
	 * @param drives
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xw, Drives drives, Agent agent) {
		return xwDetailed(xw, drives, agent);
	}

}
