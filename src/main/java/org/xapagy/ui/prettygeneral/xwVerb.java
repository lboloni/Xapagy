package org.xapagy.ui.prettygeneral;

import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

public class xwVerb {

	/**
	 * Adds the words that refer to this verb
	 * 
	 * @param fmt
	 * @param verb
	 * @param agent
	 * @param cdb
	 */
	private static void addReferringWords(IXwFormatter fmt, Verb verb, Agent agent) {
		// Add the area
		fmt.add("Referring words");
		fmt.indent();
		//
		// Adding the overlaps, decreasing order
		//
		List<String> words = agent.getXapiDictionary().getVerbWords();
		for (String word : words) {
			VerbOverlay voDirect = agent.getXapiDictionary().getVoForWord(word);
			VerbOverlay voImpact = new VerbOverlay(agent);
			voImpact.addOverlayImpacted(voDirect);
			if (voDirect.getEnergy(verb) > 0) {
				fmt.is(word, PrettyPrint.ppConcise(voDirect, agent));
			} else if (voImpact.getEnergy(verb) > 0) {
				fmt.is(".." + word, PrettyPrint.ppConcise(voDirect, agent));
			}
		}
		fmt.deindent();
	}

	/**
	 * Concise printing of the verb: only add the name
	 * 
	 * @param xw
	 * @param verb
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xw, Verb verb, Agent agent) {
		xw.add(verb.getName());
		return xw.toString();
	}

	/**
	 * Detailed printing of the attributes of verb
	 * 
	 * @param xw
	 * @param verb
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xw, Verb verb, Agent agent) {
		AbstractConceptDB<Verb> cdb = agent.getVerbDB();
		xw.add("Verb: [" + verb.getName() + "]");
		xw.add("Documentation:" + verb.getDocumentation());
		if (verb.getSpike() == null) {
			xw.add("No associated spike activity");
		} else {
			xw.is("Associated spike activity:", verb.getSpike().toString());
		}

		xw.is("SummarizationLevel", verb.getSummarizationLevel());
		xwDetailedDriveImpacts(xw, verb, agent);
		xwConcept.addAreaOverlapAndImpact(xw, verb, agent, cdb);
		xw.indent();
		addReferringWords(xw, verb, agent);
		xw.deindent();
		return xw.toString();
	}

	/**
	 * Adds to the passed formatter the detailed drive impacts
	 * 
	 * @param xw
	 * @param verb
	 * @param agent
	 */
	public static void xwDetailedDriveImpacts(IXwFormatter xw, Verb verb, Agent agent) {
		// Drive impacts on the subject
		Map<String, Double> imps = verb.getDriveImpactsOnSubject();
		if (imps.isEmpty()) {
			xw.addLabelParagraph("Drive impacts on subject:", "none");
		} else {
			xw.addLabelParagraph("Drive impacts on subject:");
			xw.indent();
			for (String key : imps.keySet()) {
				xw.is(key, imps.get(key));
			}
			xw.deindent();
		}
		imps = verb.getDriveImpactsOnObject();
		if (imps.isEmpty()) {
			xw.addLabelParagraph("Drive impacts on object", "none");
		} else {
			xw.addLabelParagraph("Drive impacts on object");
			xw.indent();
			for (String key : imps.keySet()) {
				xw.is(key, imps.get(key));
			}
			xw.deindent();
		}

	}
}
