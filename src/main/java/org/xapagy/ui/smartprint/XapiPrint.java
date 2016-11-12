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
package org.xapagy.ui.smartprint;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.verbalize.VerbalMemoryHelper;

/**
 * The objective of this class is to print proper Xapi code that should
 * be re-ingestible by Xapagy
 * @author lboloni
 *
 */
public class XapiPrint {

	/**
	 * Creates a printed form of the VI which looks similar to the way in which
	 * it would have looked in Xapi. If the VI had a Xapi form in the memory, it
	 * returns it. Otherwise it performs part-by-part verbalization
	 * 
	 * FIXME: this performs very badly for things such as quotes
	 * 
	 * @param key
	 * @param agent
	 * @return
	 */
	public static String ppsViXapiForm(VerbInstance vi, Agent agent) {
	    StringBuffer buf = new StringBuffer();
	    String xapiText = VerbalMemoryHelper.getXapiStatementOfVi(vi, agent);
	    if (xapiText != null) {
	        buf.append(xapiText);
	    } else {
	        buf.append(agent.getVerbalize().verbalize(vi));
	    }
	    return buf.toString();
	}

}
