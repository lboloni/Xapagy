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
package org.xapagy.xapi;

import java.util.List;
import java.util.StringTokenizer;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.debug.ViMatch;
import org.xapagy.debug.ViMatchFilter;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni Created on: Dec 23, 2014
 */
public class KludgeCreateLink {

	enum LinkParseState {
		WAIT_FOR_TYPE, WAIT_FOR_FROM_BRACKET, WAIT_FOR_FROM_TYPE, WAIT_FOR_ARROW, WAIT_FOR_TO_BRACKET, WAIT_FOR_TO_TYPE, WAIT_FOR_END;
	};

	public static class CreateLinkSpec {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CreateLinkSpec [linkName=" + linkName + ", vmfFrom=" + vmfFrom + ", vmfTo=" + vmfTo + ", strenght="
					+ strength + "]";
		}

		public String linkName;
		public ViMatchFilter vmfFrom;
		public ViMatchFilter vmfTo;
		public double strength = 1.0;
	}

	/**
	 * Create a link between two VIs which are identified through filters
	 * 
	 * This one goes directly through the link - it will need to go through
	 * LinkAPI
	 * 
	 * @param text
	 * @param agent
	 */
	public static void kludgeCreateLink(String text, Agent agent) {
		CreateLinkSpec cls = parseCreateLink(text);
		kludgeCreateLink(cls, agent);
	}

	/**
	 * Create a link between two VIs which are identified through filters
	 * 
	 * This one goes directly through the link - it will need to go through
	 * LinkAPI
	 * 
	 * @param cls
	 *            - specifies the way to create a link
	 * @param agent
	 */
	public static void kludgeCreateLink(CreateLinkSpec cls, Agent agent) {
		// verify that the link type is a valid one

		if (!agent.getLinks().getLinkTypeNames().contains(cls.linkName)) {
			String errorString = "Link type: " + cls.linkName + "  does not exist!\n Valid link types are "
					+ agent.getLinks().getLinkTypeNames();
			TextUi.println(errorString);
			throw new Error(errorString);
		}
		ViMatch vim = new ViMatch(agent);
		List<VerbInstance> allFocusVis = agent.getFocus().getViListAllEnergies();
		List<VerbInstance> select = vim.select(allFocusVis, cls.vmfFrom);
		if (select.isEmpty()) {
			throw new Error("Could not find VI specified by: " + cls.vmfFrom);
		}
		VerbInstance from = select.get(0);
		select = vim.select(allFocusVis, cls.vmfTo);
		if (select.isEmpty()) {
			throw new Error("Could not find VI specified by: " + cls.vmfTo);
		}
		VerbInstance to = select.get(0);
		agent.getLinks().changeLinkByName(cls.linkName, from, to, cls.strength, "KludgeCreateLink");
	}

	/**
	 * Parses the link description into the create link stuff
	 * 
	 * @param text
	 * @return
	 */
	public static CreateLinkSpec parseCreateLink(String text) {
		StringTokenizer st = new StringTokenizer(text);
		LinkParseState lps = LinkParseState.WAIT_FOR_TYPE;
		String linkName = "";
		String fromFilter = "";
		String toFilter = "";
		double strength = 1.0;
		while (st.hasMoreElements()) {
			String next = st.nextToken();
			switch (lps) {
			case WAIT_FOR_TYPE:
				linkName = interpretLinkName(next);
				lps = LinkParseState.WAIT_FOR_FROM_BRACKET;
				break;
			case WAIT_FOR_FROM_BRACKET:
				if (next.equals("{")) {
					lps = LinkParseState.WAIT_FOR_FROM_TYPE;
					break;
				}
				throw new Error("WAIT_FOR_FROM_BRACKET: Was waiting for { got" + next);
			case WAIT_FOR_FROM_TYPE:
				if (next.equals("}")) {
					lps = LinkParseState.WAIT_FOR_ARROW;
					break;
				}
				fromFilter += next;
				break;
			case WAIT_FOR_ARROW:
				if (next.equals("==>")) {
					lps = LinkParseState.WAIT_FOR_TO_BRACKET;
					break;
				}
				throw new Error("WAIT_FOR_ARROW: Was expecting ==>, but got" + next);
			case WAIT_FOR_TO_BRACKET:
				if (next.equals("{")) {
					lps = LinkParseState.WAIT_FOR_TO_TYPE;
					break;
				}
				throw new Error("WAIT_FOR_TO_BRACKET: Was waiting for { got" + next);
			case WAIT_FOR_TO_TYPE:
				if (next.equals("}")) {
					lps = LinkParseState.WAIT_FOR_END;
					break;
				}
				toFilter += next;
				break;
			case WAIT_FOR_END:
				// check for strength
				try {
					strength = Double.parseDouble(next);
					break;
				} catch (NumberFormatException e) {
					throw new Error("WAIT_FOR_END: This was supposed to be a number" + next);
				}
				// throw new
				// Error("WAIT_FOR_END: Was waiting for end of line got"
				// + next);
			}
		}
		if (lps != LinkParseState.WAIT_FOR_END) {
			throw new Error("Incomplete expression: " + text);
		}
		// ok, now we have everything... although it might be that the link
		// filters are not correct
		CreateLinkSpec cls = new CreateLinkSpec();
		cls.linkName = linkName;
		cls.vmfFrom = ViMatchFilter.createViMatchFilter(null, fromFilter);
		cls.vmfTo = ViMatchFilter.createViMatchFilter(null, toFilter);
		cls.strength = strength;
		return cls;
	}

	/**
	 * Interprets the name of a link using the full name. We need this because
	 * things
	 * 
	 * @return
	 */
	public static String interpretLinkName(String label) {
		if (label.equalsIgnoreCase("successor")) {
			return Hardwired.LINK_SUCCESSOR;
		}
		if (label.equalsIgnoreCase("predecessor")) {
			return Hardwired.LINK_PREDECESSOR;
		}
		if (label.equalsIgnoreCase("ir_context")) {
			return Hardwired.LINK_IR_CONTEXT;
		}
		if (label.equalsIgnoreCase("ir_context_implication")) {
			return Hardwired.LINK_IR_CONTEXT_IMPLICATION;
		}
		if (label.equalsIgnoreCase("question")) {
			return Hardwired.LINK_QUESTION;
		}
		if (label.equalsIgnoreCase("answer")) {
			return Hardwired.LINK_ANSWER;
		}
		if (label.equalsIgnoreCase("coincidence")) {
			return Hardwired.LINK_COINCIDENCE;
		}
		if (label.equalsIgnoreCase("summarization_begin")) {
			return Hardwired.LINK_SUMMARIZATION_BEGIN;
		}
		if (label.equalsIgnoreCase("summarization_body")) {
			return Hardwired.LINK_SUMMARIZATION_BODY;
		}
		if (label.equalsIgnoreCase("summarization_close")) {
			return Hardwired.LINK_SUMMARIZATION_CLOSE;
		}
		if (label.equalsIgnoreCase("elaboration_begin")) {
			return Hardwired.LINK_ELABORATION_BEGIN;
		}
		if (label.equalsIgnoreCase("elaboration_body")) {
			return Hardwired.LINK_ELABORATION_BODY;
		}
		if (label.equalsIgnoreCase("elaboration_close")) {
			return Hardwired.LINK_ELABORATION_CLOSE;
		}
		throw new Error("Could not interpret LinkName " + label);
	}

}
