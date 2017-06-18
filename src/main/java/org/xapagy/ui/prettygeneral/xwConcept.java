package org.xapagy.ui.prettygeneral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConcept;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PrettyPrint;
import org.xapagy.util.SimpleEntryComparator;

public class xwConcept {
	/**
	 * Adds to the formatter the details of the concept or verb
	 * 
	 * @param xw
	 * @param concept - concept or verb
	 * @param agent
	 * @param cdb - the database
	 */
	public static <T extends AbstractConcept> void addAreaOverlapAndImpact(IXwFormatter xw, T concept, Agent agent,
			AbstractConceptDB<T> cdb) {
		// Add the area
		xw.indent();
		xw.is("Area", cdb.getArea(concept));
		//
		// Adding the overlaps, decreasing order
		//
		Map<T, Double> overlaps = cdb.getOverlaps(concept);
		if (overlaps.isEmpty()) {
			xw.add("Overlaps: None.");
		} else {
			xw.add("Overlaps:");
			xw.indent();
			// sort them in decreasing order
			List<SimpleEntry<T, Double>> listOverlap = new ArrayList<>();
			for (T c : overlaps.keySet()) {
				listOverlap.add(new SimpleEntry<>(c, overlaps.get(c)));
			}
			Collections.sort(listOverlap, new SimpleEntryComparator<T>());
			Collections.reverse(listOverlap);
			for (SimpleEntry<T, Double> entry : listOverlap) {
				xw.is(entry.getKey().getName(), entry.getValue());
			}
			xw.deindent();
		}
		//
		// Adding the implications, decreasing order
		//
		Map<T, Double> impacts = cdb.getImpacts(concept);
		if (impacts == null) {
			xw.add("Implications: None.");
		} else {
			xw.add("Implications: ");
			xw.indent();
			// sort them in decreasing order
			List<SimpleEntry<T, Double>> listImpacts = new ArrayList<>();
			for (T c : impacts.keySet()) {
				listImpacts.add(new SimpleEntry<>(c, impacts.get(c)));
			}
			Collections.sort(listImpacts, new SimpleEntryComparator<T>());
			Collections.reverse(listImpacts);
			for (SimpleEntry<T, Double> entry : listImpacts) {
				xw.is(entry.getKey().getName(), entry.getValue());
			}
			xw.deindent();
		}
		xw.deindent();
	}

	/**
	 * Adds the words which refer to this concept
	 * 
	 * @param xw
	 * @param concept
	 * @param agent
	 */
	public static void addReferringWords(IXwFormatter xw, Concept concept, Agent agent) {
		// Add the area
		xw.add("Referring words");
		xw.indent();
		//
		// Adding the overlaps, decreasing order
		//
		List<String> words = agent.getXapiDictionary().getConceptWords();
		for (String word : words) {
			ConceptOverlay coDirect = agent.getXapiDictionary().getCoForWord(word);
			ConceptOverlay coImpact = new ConceptOverlay(agent);
			coImpact.addOverlayImpacted(coDirect);
			if (coDirect.getEnergy(concept) > 0) {
				xw.is(word, PrettyPrint.ppConcise(coDirect, agent));
			} else if (coImpact.getEnergy(concept) > 0) {
				xw.is(".." + word, PrettyPrint.ppConcise(coDirect, agent));
			}
		}
		xw.deindent();
	}

	/**
	 * Returns a list of the concepts which overlap with concept
	 * 
	 * This is a very expensive operation, and it should normally only be used
	 * for listing and debugging purposes
	 * 
	 * @param concept
	 * @return
	 */
	public static List<SimpleEntry<Concept, Double>> getOverlaps(AbstractConceptDB<Concept> cdb, Concept concept) {
		List<SimpleEntry<Concept, Double>> retval = new ArrayList<>();
		for (Concept c : cdb.getAllConcepts()) {
			if (c.equals(concept)) {
				continue;
			}
			double overlap = cdb.getOverlap(concept, c);
			if (overlap != 0.0) {
				retval.add(new SimpleEntry<>(c, overlap));
			}
		}
		return retval;
	}

	/**
	 * Very simple printing, only the name of the concept
	 * 
	 * @param xw
	 * @param concept
	 * @param agent
	 * @return
	 */
	public static String xwConcise(IXwFormatter xw, Concept concept, Agent agent) {
        xw.add(concept.getName());
        return xw.toString();
	}

	/**
	 * Detailed printing of a concept: includes area, and impacts
	 * 
	 * @param xw
	 * @param concept
	 * @param agent
	 * @return
	 */
	public static String xwDetailed(IXwFormatter xw, Concept concept, Agent agent) {
		AbstractConceptDB<Concept> cdb = agent.getConceptDB();
		xw.add("Concept: [" + concept.getName() + "]");
		xw.add("Comment:" + concept.getDocumentation());
		addAreaOverlapAndImpact(xw, concept, agent, cdb);
		addReferringWords(xw, concept, agent);
		return xw.toString();
	}
}
