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
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PpVerbInstanceTemplate;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni Created on: Jun 11, 2011
 */
public class XwFslInterpretation {

	/**
	 * One line summarization
	 * 
	 * @param xw
	 * @param fsli
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xw, FslInterpretation fsli, Agent agent) {
		StringBuffer buf = new StringBuffer();
		buf.append("FSLI:");
		buf.append(fsli.getFsl().getFslType());
		buf.append(" -- " + PpVerbInstanceTemplate.ppConciseViTemplate(fsli.getViInterpretation(), agent));
		buf.append(" -- " + Formatter.fmt(fsli.getTotalSupport(agent)));
		buf.append(" -- " + Formatter.fmt(fsli.getSupportFraction() * 100.0) + "%");
		xw.addPre(buf.toString());
		return xw.toString();
	}

	/**
	 * Detailed description
	 * 
	 * @param xw
	 * @param fsli
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xw, FslInterpretation fsli, Agent agent) {
		xw.addLabelParagraph("FslInterpetation:",
				fsli.getFsl().getFslType() + " totalSupport =" + Formatter.fmt(fsli.getTotalSupport(agent)));
		xw.indent();
		xw.add("verb instance template:");
		xw.indent();
		xwVerbInstance.xwDetailed(xw, fsli.getViInterpretation(), agent);
		xw.deindent();
		xw.is("supportPercent", fsli.getSupportFraction());
		xw.addLabelParagraph("fsl:");
		xw.indent();
		XwFocusShadowLinked.xwDetailed(xw, fsli.getFsl(), agent);
		xw.deindent();
		return xw.toString();
	}

}
