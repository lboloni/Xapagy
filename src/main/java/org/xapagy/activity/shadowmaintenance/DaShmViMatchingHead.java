/*
   This file is part of the Xapagy project
   Created on: Apr 22, 2011
 
   org.xapagy.story.activity.DAViMatchingHead
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * 
 * Looks up in the memory verb instances which core match the head verb
 * instance. Adds them to the instances shadow - and their corresponding parts
 * to the corresponding parts shadow.
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmViMatchingHead extends AbstractDaFocusIterator {

	private static final long serialVersionUID = -8569487491044178752L;
	/**
	 * Multiplies the value of SHADOW_GENERIC energy applied to ACTION VIs
	 */
	private double scaleActionVi;
	/**
	 * the contribution even if the instances do not attribute shadowed
	 */
	private double scaleRelationDefault;
	/**
	 * the contribution from the subject attribute shadows
	 */
	private double scaleRelationSubjectAttribute;
	/**
	 * the contribution from the object attribute shadows
	 */
	private double scaleRelationObjectAttribute;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.DiffusionActivity#extractParameters()
	 */
	@Override
	public void extractParameters() {
		scaleActionVi = getParameterDouble("scaleActionVi");
		scaleRelationDefault = getParameterDouble("scaleRelationDefault");
		scaleRelationSubjectAttribute = getParameterDouble("scaleRelationSubjectAttribute");
		scaleRelationObjectAttribute = getParameterDouble("scaleRelationObjectAttribute");
	}

	/**
	 * 
	 * @param agent
	 * @param name
	 */
	public DaShmViMatchingHead(Agent agent, String name) {
		super(agent, name);
	}

	/**
	 * Performing the match when the focus VI is an action. We already know that
	 * it is an action, so we don't check again
	 * 
	 * @param fvi
	 * @param timeSlice
	 */
	private void applyFocusViAction(VerbInstance fvi, double timeSlice) {
		ShmAddItemCollection saicGeneric = new ShmAddItemCollection();
		ViSet matches = AmLookup.lookupVi(agent, fvi, EnergyColors.AM_VI);
		for (VerbInstance match : matches.getParticipants()) {
			double matchLevel = matches.value(match);
			double addEnergy = matchLevel * scaleActionVi;
			ShmAddItem sai = new ShmAddItem(agent, match, fvi, addEnergy, EnergyColors.SHV_ACTION_MATCH,
					EnergyColors.SHI_ACTION);
			saicGeneric.addShmAddItem(sai);
		}
		SaicHelper.applySAIC_VisAndInstances(agent, saicGeneric, timeSlice, "DaShmViMachingHead.applyFocusVi");
	}

	/**
	 * Performing the match when the focus VI is an relation. We already know
	 * that this is a relation, so we don't check again
	 * 
	 * @param fvi
	 * @param timeSlice
	 */

	private void applyFocusViRelation(VerbInstance fvi, double timeSlice) {
		Shadows sh = agent.getShadows();
		ShmAddItemCollection saicRelation = new ShmAddItemCollection();
		ViSet matches = AmLookup.lookupVi(agent, fvi, EnergyColors.AM_VI);
		for (VerbInstance match : matches.getParticipants()) {
			double matchLevel = matches.value(match);
			// identify the subject and focus objects
			Instance fiSubject = fvi.getSubject();
			Instance shSubject = match.getSubject();
			Instance fiObject = fvi.getObject();
			Instance shObject = match.getObject();
			// scaler based on the attribute match
			double salienceAttributeSubject = sh.getEnergy(fiSubject, shSubject, EnergyColors.SHI_ATTRIBUTE);
			double salienceAttributeObject = sh.getEnergy(fiObject, shObject, EnergyColors.SHI_ATTRIBUTE);
			//
			// the energy creation is a combination of the matches of the
			// VI, the
			// subject and the object attribute matches
			//
			double scale = scaleRelationDefault + scaleRelationSubjectAttribute * salienceAttributeSubject
					+ scaleRelationObjectAttribute * salienceAttributeObject;
			double score = scale * matchLevel;
			//
			// add SHI_RELATION energy to the two subjects
			//
			ShmAddItem sai = new ShmAddItem(agent, shSubject, fiSubject, score, EnergyColors.SHI_RELATION);
			saicRelation.addShmAddItem(sai);
			// add SHI_RELATION energy to the two objects
			sai = new ShmAddItem(agent, shObject, fiObject, score, EnergyColors.SHI_RELATION);
			saicRelation.addShmAddItem(sai);
		}
		SaicHelper.applySAIC_Instances(agent, saicRelation, timeSlice, "DaShmViMachingHead.applyFocusVi");
	}

	/**
	 * Looks up VIs which match the fvi from the AM based on the match of the
	 * verb (it relies on the AmLookup.lookupVi which ignores some of the
	 * frequently encountered metaverbs and gets rid of the incompatible VIs.
	 * 
	 * From this, calculates increases
	 * 
	 * NOTE May 15, 2014: I made the SHADOW_GENERIC change only apply to ACTION
	 * VIs. The relations get the SHADOW_RELATION, but this will need to be
	 * transformed into SHADOW_GENERIC with a new DA.
	 * 
	 * 
	 */
	@Override
	protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
		if (ViClassifier.decideViClass(ViClass.RELATION, fvi, agent)) {
			applyFocusViRelation(fvi, timeSlice);
			return;
		}
		if (ViClassifier.decideViClass(ViClass.ACTION, fvi, agent)) {
			applyFocusViAction(fvi, timeSlice);
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters
	 * .IXwFormatter, int)
	 */
	@Override
	public void formatTo(IXwFormatter fmt, int detailLevel) {
		fmt.add("DaShmViMatchingHead");
		fmt.indent();
		fmt.is("scaleActionVi", scaleActionVi);
		fmt.explanatoryNote("Multiplies the value of SHADOW_GENERIC energy applied to ACTION VIs");
		fmt.is("scaleRelationDefault", scaleRelationDefault);
		fmt.explanatoryNote("the contribution even if the instances do not attribute shadowed");
		fmt.is("scaleRelationSubjectAttribute", scaleRelationSubjectAttribute);
		fmt.explanatoryNote("the contribution from the subject attribute shadows");
		fmt.is("scaleRelationObjectAttribute", scaleRelationObjectAttribute);
		fmt.explanatoryNote("the contribution from the object attribute shadows");
		fmt.deindent();
	}

}
