package org.xapagy.ui.prettygeneral;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.ui.formatters.IXwFormatter;

public class xwConceptOverlay {

	public static String xwConcise(IXwFormatter xw, ConceptOverlay co, Agent agent) {
		return xwOverlay.xwDetailed(xw, co, agent);
	}

	public static String xwDetailed(IXwFormatter xw, ConceptOverlay co, Agent agent) {
		return xwOverlay.xwDetailed(xw, co, agent);
	}

}
