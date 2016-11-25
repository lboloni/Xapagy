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
package org.xapagy.instances;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * 
 * The verb instance
 * 
 * @author Ladislau Boloni Created on: May 30, 2011
 */
public class VerbInstance implements XapagyComponent, Serializable {

	private static final long serialVersionUID = -7521207007599312221L;

	/**
	 * Creates a Vi from a ViTemplate, only for the case when all the missing
	 * parts are resolved.
	 * 
	 * @param agent
	 *            the agent
	 * @param viOld
	 *            - the original template
	 * @return
	 */
	public static VerbInstance createViFromResolvedTemplate(Agent agent, VerbInstance viOld) {
		if (!viOld.missingParts.isEmpty()) {
			String errorMessage = "Cannot create Vi from a VerbInstanceTemplate with missing parts"
					+ viOld.missingParts;
			throw new Error(errorMessage);
		}
		if (!viOld.newParts.isEmpty()) {
			throw new Error("Cannot create VI from a VerbInstanceTemplate with new parts!");
		}
		VerbInstance vi = agent.createVerbInstance(viOld.viType, viOld.getVerbs());
		for (ViPart part : ViStructureHelper.getAllowedParts(viOld.viType)) {
			if (part != ViPart.Verb) {
				XapagyComponent object = viOld.resolvedParts.get(part);
				if (object == null) {
					throw new Error("Null resolved part!" + part);
				}
				vi.setResolvedPart(part, object);
			}
		}
		return vi;
	}

	/**
	 * Factory function:
	 * 
	 * Constructs a VI template with the type and the instance specified and all
	 * the other parts missing. Used in the FsliInterpreter to generate the FSLI
	 * and in the StoryLineReasoning
	 * 
	 * @param agent
	 * @param viType
	 * @param verbs
	 *            - the specified verb overlay that will be copied in the new
	 *            overlay
	 */
	public static VerbInstance createViTemplate(Agent agent, ViType viType, VerbOverlay verbs) {
		VerbInstance vit = new VerbInstance();
		vit.viType = viType;
		VerbOverlay myverbs = new VerbOverlay(agent);
		myverbs.addOverlay(verbs);
		vit.setResolvedPart(ViPart.Verb, myverbs);
		// everything can be missing, except the verb
		for (ViPart part : ViStructureHelper.getAllowedParts(viType)) {
			if (part != ViPart.Verb) {
				vit.missingParts.add(part);
			}
		}
		return vit;
	}

	/**
	 * Copy constructor (for templates)
	 * 
	 * @param model
	 */
	public static VerbInstance createViTemplateFromModel(VerbInstance model) {
		VerbInstance vit = new VerbInstance();
		vit.viType = model.viType;
		vit.missingParts.addAll(model.missingParts);
		for (ViPart part : model.resolvedParts.keySet()) {
			XapagyComponent object = model.resolvedParts.get(part);
			if (object != null) {
				vit.resolvedParts.put(part, object);
			}
		}
		for (ViPart part : model.newParts.keySet()) {
			ConceptOverlay co = model.newParts.get(part);
			if (co != null) {
				vit.newParts.put(part, co);
			}
		}
		return vit;
	}

	/**
	 * The "expectedness" of an instantiated VI. Set in
	 */
	private double expectedness = 0.0;
	private String identifier;
	/**
	 * If this VI is part of a quote, it points to the inquit
	 */
	private VerbInstance inquit = null;
	/**
	 * Shows which parts are missing
	 */
	private Set<ViPart> missingParts = new HashSet<>();
	/**
	 * New parts: the concept overlays based on which the new parts must be
	 * instantiated
	 */
	private Map<ViPart, ConceptOverlay> newParts = new HashMap<>();
	/**
	 * Resolved parts
	 */
	private Map<ViPart, XapagyComponent> resolvedParts = new HashMap<>();
	/**
	 * Verb instance type
	 */
	private ViType viType;
	/**
	 * If the execution of the verb instance created a new instance, this will
	 * be kept here
	 */
	private Instance createdInstance = null;
	/**
	 * The agent time when it was created
	 */
	private double creationTime;

	/**
	 * @return the creationTime
	 */
	public double getCreationTime() {
		return creationTime;
	}

	public Instance getCreatedInstance() {
		return createdInstance;
	}

	public void setCreatedInstance(Instance createdInstance) {
		this.createdInstance = createdInstance;
	}

	/**
	 * Empty constructor for deserialization
	 */
	public VerbInstance() {
	}

	/**
	 * Constructor for a VerbInstance type VI. Called from
	 * Agent.createVerbInstance
	 * 
	 * @param viType
	 *            - the type of the VI
	 * @param identifier
	 * @param verbs
	 *            - the Verb part of the VI
	 * @param agent
	 */
	public VerbInstance(ViType viType, String identifier, VerbOverlay verbs, Agent agent) {
		this.identifier = identifier;
		this.viType = viType;
		setResolvedPart(ViPart.Verb, verbs);
		// everything can be missing, except the verb
		for (ViPart part : ViStructureHelper.getAllowedParts(viType)) {
			if (part != ViPart.Verb) {
				missingParts.add(part);
			}
		}
		creationTime = agent.getTime();
	}

	public ConceptOverlay getAdjective() {
		return (ConceptOverlay) getPart(ViPart.Adjective);
	}

	/**
	 * Returns the expectedness of the VI - only printed from PwVerbInstance
	 * 
	 * @return the expectedness
	 */
	public double getExpectedness() {
		return expectedness;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	public VerbInstance getInquit() {
		return inquit;
	}

	/**
	 * @return the missingParts
	 */
	public Set<ViPart> getMissingParts() {
		return missingParts;
	}

	/**
	 * @return the newParts
	 */
	public Map<ViPart, ConceptOverlay> getNewParts() {
		return newParts;
	}

	public Instance getObject() {
		return (Instance) getPart(ViPart.Object);
	}

	/**
	 * Returns a part based on the specification. Unfortunately type is lost
	 * here and need to be reproduced later.
	 * 
	 * @param vip
	 * @return
	 */
	public XapagyComponent getPart(ViPart vip) {
		if (!ViStructureHelper.allowed(vip, viType)) {
			return null;
		}
		XapagyComponent object = getResolvedParts().get(vip);
		if (object == null) {
			throw new Error("Accessing resolved part " + vip + " but it is not resolved.");
		}
		return object;
	}

	/**
	 * This is the verb instance subject, aka the quote
	 * 
	 * @return
	 */
	public VerbInstance getQuote() {
		return (VerbInstance) getPart(ViPart.Quote);
	}

	/**
	 * @return
	 */
	public Instance getQuoteScene() {
		return (Instance) getPart(ViPart.QuoteScene);
	}

	/**
	 * Returns all the scenes referenced by the instance (also adds the scene of
	 * the referenced item)
	 * 
	 * @return
	 */
	public Set<Instance> getReferencedScenes() {
		Set<Instance> retval = new HashSet<>();
		for (ViPart part : ViStructureHelper.getAllowedInstanceParts(getViType())) {
			Instance instance = (Instance) resolvedParts.get(part);
			if (instance != null) {
				retval.add(instance.getScene());
			}
		}
		return retval;
	}

	/**
	 * @return the resolvedParts
	 */
	public Map<ViPart, XapagyComponent> getResolvedParts() {
		return resolvedParts;
	}

	public Instance getSubject() {
		return (Instance) getPart(ViPart.Subject);
	}

	public VerbOverlay getVerbs() {
		return (VerbOverlay) getPart(ViPart.Verb);
	}

	/**
	 * The summarization level of a VI is the maximum of the summarization
	 * levels of the verbs
	 * 
	 * @return
	 */
	public int getSummarizationLevel() {
		int level = -1;
		for (SimpleEntry<Verb, Double> entry : getVerbs().getList()) {
			level = Math.max(level, entry.getKey().getSummarizationLevel());
		}
		return level;
	}

	/**
	 * @return the viType
	 */
	public ViType getViType() {
		return viType;
	}

	/**
	 * Returns true if the vi already contains the instance - it can only
	 * contain it once... Currently used in the FSL interpreter
	 * 
	 * @param resolvedInstance
	 * @return
	 */
	public boolean hasInstance(Instance inst) {
		for (Object o : resolvedParts.values()) {
			if (o.equals(inst)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds this VI to the referringVis component of the instances which are
	 * part of this VI. This should be only done at the moment when the instance
	 * is added to the focus
	 */
	public void markReferringInstances() {
		for (ViPart part : ViStructureHelper.getAllowedInstanceParts(getViType())) {
			Instance inst = (Instance) getPart(part);
			inst.addReferringVi(this);
		}
	}

	/**
	 * If a Vi is not completely resolved
	 * 
	 * @return
	 */
	public boolean notCompletelyResolved() {
		if (!newParts.isEmpty()) {
			return true;
		}
		if (!missingParts.isEmpty()) {
			return true;
		}
		return false;
	}

	public void setAdjective(ConceptOverlay conceptObject) {
		setResolvedPart(ViPart.Adjective, conceptObject);
	}

	/**
	 * Sets the expectedness of the VI
	 * 
	 * @param expectedness
	 *            the expectedness to set
	 */
	public void setExpectedness(double expectedness) {
		this.expectedness = expectedness;
	}

	public void setInquit(VerbInstance inquit) {
		this.inquit = inquit;
	}

	/**
	 * Sets a part. The part will be moved into newParts
	 * 
	 * @param part
	 * @param key
	 */
	public void setNewPart(ViPart part, ConceptOverlay co) {
		missingParts.remove(part);
		newParts.put(part, co);
	}

	public void setObject(Instance object) {
		setResolvedPart(ViPart.Object, object);
	}

	/**
	 * @param quoteVerbInstance
	 */
	public void setQuote(VerbInstance quoteVerbInstance) {
		setResolvedPart(ViPart.Quote, quoteVerbInstance);
	}

	/**
	 * @param quoteSceneInstance
	 */
	public void setQuoteScene(Instance instanceQuoteScene) {
		setResolvedPart(ViPart.QuoteScene, instanceQuoteScene);
	}

	/**
	 * Sets a part.
	 * 
	 * The part will be moved into resolved and removed from the missing and new
	 * 
	 * @param part
	 * @param key
	 */
	public void setResolvedPart(ViPart part, XapagyComponent resolvedPart) {
		if (resolvedPart == null) {
			throw new Error("Resolving for an instance which is null!!!");
		}
		if (!ViStructureHelper.allowed(part, viType)) {
			throw new Error("Trying to resolve a part which is not allowed!!!");
		}
		newParts.remove(part);
		missingParts.remove(part);
		resolvedParts.put(part, resolvedPart);
		// set the inquit
		if (part == ViPart.Quote) {
			VerbInstance quote = (VerbInstance) resolvedPart;
			quote.setInquit(this);
		}
	}

	public void setSubject(Instance subject) {
		setResolvedPart(ViPart.Subject, subject);
	}

	@Override
	public String toString() {
		return PrettyPrint.ppString(this);
	}

}
