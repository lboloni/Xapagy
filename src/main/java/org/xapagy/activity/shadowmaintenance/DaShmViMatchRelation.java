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
 * This DA is based on the match between a focus VI and VIs in the AM. The degree of the 
 * match is calculated by AmLookup.
 * 
 * It generates three kind of energies:
 * 
 * <ul>
 * <li> SHV_ACTION_MATCH - between the focus and AM VIs
 * <li> SHI_RELATION - between corresponding relation subject and object, if the matching VIs are relations
 * <li> SHI_ACTION - between corresponding subject and object, if the matching VIs are actions
 * </ul>
 * 
 * 
 * @author Ladislau Boloni
 * Created on: Apr 22, 2011
 */
public class DaShmViMatchRelation extends AbstractDaFocusIterator {

	private static final long serialVersionUID = -8569487491044178752L;
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
		scaleRelationDefault = getParameterDouble("scaleRelationDefault");
		scaleRelationSubjectAttribute = getParameterDouble("scaleRelationSubjectAttribute");
		scaleRelationObjectAttribute = getParameterDouble("scaleRelationObjectAttribute");
	}

	/**
	 * 
	 * @param agent
	 * @param name
	 */
	public DaShmViMatchRelation(Agent agent, String name) {
		super(agent, name);
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
		SaicHelper.applySAIC_Instances(agent, saicRelation, timeSlice, "DaShmViMatchRelation.applyFocusVi");
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
		fmt.add("DaShmViMatchRelation");
		fmt.indent();
		fmt.is("scaleRelationDefault", scaleRelationDefault);
		fmt.explanatoryNote("the contribution even if the instances do not have the attribute shadowed");
		fmt.is("scaleRelationSubjectAttribute", scaleRelationSubjectAttribute);
		fmt.explanatoryNote("the contribution from the subject attribute shadows");
		fmt.is("scaleRelationObjectAttribute", scaleRelationObjectAttribute);
		fmt.explanatoryNote("the contribution from the object attribute shadows");
		fmt.deindent();
	}

}
