/*
   
    This file is part of the Xapagy Cognitive Architecture 
    Copyright (C) 2008-2017 Ladislau Boloni

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
   
*/
package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni Created on: Jun 11, 2011
 */
public class xwFocusShadowLinked {

	/**
	 * Fall back on detailed
	 * 
	 * @param xw
	 * @param fsl
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xw, FocusShadowLinked fsl, Agent agent) {
		return xwFocusShadowLinked.xwDetailed(xw, fsl, agent);
	}

	/**
	 * Writes the components of the FSL object 
	 * 
	 * @param xw
	 * @param fsl
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xw, FocusShadowLinked fsl, Agent agent) {
		xw.addLabelParagraph("FocusShadowLinked:", "" + fsl.getFslType() + " is indirect: " + fsl.isIndirect());
		xw.indent();
		if (fsl.isIndirect()) {
			xw.addLabelParagraph("Choice:", PrettyPrint.ppConcise(fsl.getChoice(), agent));
		} else {
			xw.addLabelParagraph("FocusVi:", PrettyPrint.ppConcise(fsl.getViFocus(), agent));
		}
		xw.addLabelParagraph("Shadow: ", PrettyPrint.ppConcise(fsl.getViShadow(), agent));
		xw.addLabelParagraph("Link: ", PrettyPrint.ppConcise(fsl.getViLinked(), agent) + " -- " + fsl.getFslType() + " -- "
				+ Formatter.fmt(fsl.getLinkStrength(agent)));
		return xw.toString();
	}

}
