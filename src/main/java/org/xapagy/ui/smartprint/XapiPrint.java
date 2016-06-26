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
