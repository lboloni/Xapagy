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
package org.xapagy.links;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.parameters.Parameters;
import org.xapagy.set.ViSet;

/**
 * The API for the accesss to the links. The implementation of the links should
 * be hidden behind this API
 *
 * @author Ladislau Boloni Created on: Feb 18, 2015
 */
public class Links implements Serializable {

	private static final long serialVersionUID = -1278646509095724054L;
	private List<LinkQuantum> quantums = new ArrayList<>();
	private Agent agent;
	private Map<String, LinkValues> linkValues = new HashMap<>();

	public Links(Agent agent) {
		this.agent = agent;
	}

	/**
	 * Changes the link by name. If needed performs a logging in the form of
	 * LinkQuantums
	 * 
	 * @param linkName
	 * @param viFrom
	 * @param viTo
	 * @param delta
	 */
	private void applyQuantum(LinkQuantum lq) {
		boolean recordLinkQuantum = agent.getParameters().getBoolean("A_DEBUG", "G_GENERAL", "N_RECORD_LINK_QUANTUMS");
		if (!recordLinkQuantum) {
			quantums.clear();
		}
		if (recordLinkQuantum) {
			double val = getLinkValueByLinkName(lq.getFrom(), lq.getTo(), lq.getLinkName());
			lq.setValueBefore(val);
		}
		changeLink(lq.getLinkName(), lq.getFrom(), lq.getTo(), lq.getDelta());
		if (recordLinkQuantum) {
			double val = getLinkValueByLinkName(lq.getFrom(), lq.getTo(), lq.getLinkName());
			lq.setValueAfter(val);
			lq.setAgentTimeWhenApplied(agent.getTime());
			quantums.add(lq);
		}
	}

	/**
	 * Changes the value of a named link, considering one way and two way links
	 * 
	 * Only called from LinkAPI.changeLinkByName
	 * 
	 * @param linkName
	 *            - the name of the link (might be a reverse one)
	 * @param vi1
	 *            - the vi from which we have the link
	 * @param vi2
	 *            - the vi to which we have the link
	 * @param delta
	 *            - the value with which the link will be change. For one-way
	 *            link, this might flip it in the other direction
	 */
	void changeLink(String linkName, VerbInstance vi1, VerbInstance vi2, double delta) {
		LinkValues linkValues = getLinkValues(linkName);
		if (!linkValues.isDirectionPaired()) {
			// undirected links, they can have values on both directions
			double existingValue = linkValues.getLink(vi1, vi2);
			double newValue = existingValue + delta;
			linkValues.putLink(vi1, vi2, newValue);
		} else {
			// for directed links, they can only have value in one direction
			LinkValues irel = getLinkValues(linkValues.getNameReverse());
			double existingValue = linkValues.getLink(vi1, vi2) - irel.getLink(vi1, vi2);
			double newValue = existingValue + delta;
			if (newValue >= 0) {
				linkValues.putLink(vi1, vi2, newValue);
				linkValues.putLink(vi2, vi1, 0);
				irel.putLink(vi2, vi1, newValue);
				irel.putLink(vi1, vi2, 0);
			} else {
				linkValues.putLink(vi1, vi2, 0);
				linkValues.putLink(vi2, vi1, -newValue);
				irel.putLink(vi2, vi1, 0);
				irel.putLink(vi1, vi2, -newValue);
			}
		}
	}

	/**
	 * Changes the link based on the name, subject to the rules (it will create
	 * side impacts)
	 * 
	 * @param linkName
	 *            - one of the linkNames
	 * @param viFrom
	 * @param viTo
	 * @param delta
	 */
	public void changeLinkByName(String linkName, VerbInstance viFrom, VerbInstance viTo, double delta, String source) {
		LinkQuantum lq = LinkQuantum.createQuantum(linkName, viFrom, viTo, delta, source);
		// make it go through the rules, which might expand this, and apply all
		// of them
		List<LinkQuantum> lqlist = LinkRules.linkRules(agent, lq);
		for (LinkQuantum lqx : lqlist) {
			applyQuantum(lqx);
		}
	}

	/**
	 * Copies all the links from viLeader to verbInstance. Used in the creation
	 * of coincidence links
	 * 
	 * @param viFrom
	 * @param viTo
	 */
	public void copyAllLinks(VerbInstance viFrom, VerbInstance viTo, String source) {
		for (String linkName : getLinkTypeNames()) {
			ViSet viset = getLinksByLinkName(viFrom, linkName);
			for (SimpleEntry<VerbInstance, Double> entry : viset.getList()) {
				VerbInstance other = entry.getKey();
				changeLinkByName(linkName, viTo, other, entry.getValue(), source + "+copyAllLinks");
				// vdb.changeLink(linkName, viTo, other, entry.getValue());
				if (linkName.equals(Hardwired.LINK_COINCIDENCE)) {
					// vdb.changeLink(linkName, other, viTo, entry.getValue());
					changeLinkByName(linkName, other, viTo, entry.getValue(), source + "+copyAllLinks(coincidence)");
				}
			}
		}
	}

	/**
	 * Creates a pair of one way links and initializes the parameters
	 * 
	 * @param linkTypeName
	 *            the name of the "forward" newly created link name. Link names
	 *            cannot be overwritten so an error is thrown in the name exists
	 * @param defaultWeight
	 *            the default weight "forward" link
	 * @param reverseLinkTypeName
	 *            the name of the "reverse" newly created link name. Link names
	 *            cannot be overwritten so an error is thrown in the name exists
	 * @param reverseWeight
	 *            the default weight "reverse" link
	 * 
	 */
	public void createDirectionPairedLinkTypes(String linkTypeName, double defaultWeight, String reverseLinkTypeName,
			double reverseWeight, String description) {
		if (linkValues.keySet().contains(linkTypeName)) {
			throw new Error("createOnewayLinkTypePair - " + linkTypeName + " already exists.");
		}
		if (linkValues.keySet().contains(reverseLinkTypeName)) {
			throw new Error("createOnewayLinkTypePair - " + reverseLinkTypeName + " already exists.");
		}
		LinkValues lv = new LinkValues(linkTypeName, reverseLinkTypeName, true, description);
		linkValues.put(lv.getNameDirect(), lv);
		lv = new LinkValues(reverseLinkTypeName, linkTypeName, true, description);
		linkValues.put(lv.getNameDirect(), lv);
		Parameters p = agent.getParameters();
		p.addParam("A_SHM", "G_LINK_STRUCTURE", "N_WEIGHT-" + linkTypeName, defaultWeight,
				"Default weight of link type " + linkTypeName);
		p.addParam("A_SHM", "G_LINK_STRUCTURE", "N_WEIGHT-" + reverseLinkTypeName, reverseWeight,
				"Default weight of link type " + reverseLinkTypeName);
	}

	/**
	 * Creates an undirected link type and initializes its default weigh.
	 * 
	 * 
	 * @param linkTypeName
	 *            the name of the newly created link name. Link names cannot be
	 *            overwritten so an error is thrown in the name exists
	 * @param defaultWeight
	 *            the default weight of the link
	 * 
	 */
	public void createRegularLinkType(String linkTypeName, double defaultWeight, String description) {
		if (linkValues.keySet().contains(linkTypeName)) {
			throw new Error("createUndirectedLinkType - " + linkTypeName + " already exists.");
		}
		LinkValues lv = new LinkValues(linkTypeName, linkTypeName, false, description);
		linkValues.put(lv.getNameDirect(), lv);
		Parameters p = agent.getParameters();
		p.addParam("A_SHM", "G_LINK_STRUCTURE", "N_WEIGHT-" + linkTypeName, defaultWeight,
				"Default weight of link type " + linkTypeName);
	}

	/**
	 * Returns the set of VIs that are connected to vi using a link specified
	 * with the linkName. The strength in the resulting viSet is the strength of
	 * the link
	 * 
	 * @param vi
	 * @param linkName
	 * 
	 * @return
	 */
	public ViSet getLinksByLinkName(VerbInstance vi, String linkName) {
		return getLinkValues(linkName).getLink(vi);
	}

	/**
	 * Returns a set of all the link names
	 * 
	 * @return
	 */
	public Set<String> getLinkTypeNames() {
		return linkValues.keySet();
	}

	/**
	 * Returns the value of the link
	 * 
	 * @param from
	 *            - the VI from where the link starts
	 * @param to
	 *            - the VI where the link ends
	 * @param linkName
	 *            - the name of the link
	 * 
	 * @return
	 */
	public double getLinkValueByLinkName(VerbInstance from, VerbInstance to, String linkName) {
		return getLinksByLinkName(from, linkName).value(to);
	}

	/**
	 * Returns the link values object associate with the link name
	 * 
	 * @param linkName
	 * @return
	 */
	LinkValues getLinkValues(String linkName) {
		LinkValues retval = linkValues.get(linkName);
		return retval;
	}

	public List<LinkQuantum> getQuantums() {
		return quantums;
	}

	/**
	 * Removes the ELABORATION_BEGIN link between fvi and viBegin
	 * 
	 * FIXME: currently relies on setting to zero, more efficient
	 * implementation!!!
	 * 
	 * @param fvi
	 * @param viBegin
	 */
	public void removeElaborationBegin(VerbInstance fvi, VerbInstance viBegin, String source) {
		ViSet begins = getLinksByLinkName(fvi, Hardwired.LINK_ELABORATION_BEGIN);
		double value = begins.value(viBegin);
		changeLinkByName(Hardwired.LINK_ELABORATION_BEGIN, fvi, viBegin, -value, source + "+removeElaborationBegin");
	}

	/**
	 * Removes the ELABORATION_BODY link between fvi and viBody
	 * 
	 * @param fvi
	 * @param viBegin
	 */
	public void removeElaborationBody(VerbInstance fvi, VerbInstance viBody, String source) {
		ViSet begins = getLinksByLinkName(fvi, Hardwired.LINK_ELABORATION_BODY);
		double value = begins.value(viBody);
		changeLinkByName(Hardwired.LINK_ELABORATION_BODY, fvi, viBody, -value, source + "+removeElaborationBody");
	}

}
