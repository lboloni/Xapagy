/*
   This file is part of the Xapagy project
   Created on: May 15, 2014
 
   org.xapagy.activity.shadowmaintenance.DaShmLinkStructure
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import org.xapagy.activity.AbstractDaFocusIterator;
import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.links.Links;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 *
 * This DA creates SHV_LINK_STRUCTURE energy between pairs of focus and shadow
 * VI who are characterized only by the fact that they have the same temporal
 * relation to a focus and shadow VI pair which has SHV_GENERIC energy mapping. 
 * 
 * <pre>
 * fvi ----- link ------> flink
 * |                       |
 * shv_generic adding... shv_link_structure 
 * |                       |
 * svi ------ link -----> slink
 * </pre>
 * 
 * The added energy is scaled with:
 * <ul>
 * <li>the salience of fvi/svi generic shadow
 * <li>the minimum of the two link values
 * <li>a scaling parameter strength that varies from scene to scene (but
 * currently is only initialized from the main parameters)
 * </ul>
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmLinkStructure extends AbstractDaFocusIterator {
	private static final long serialVersionUID = -5099533341430630677L;

	/**
	 * 
	 * @param agent
	 * @param name
	 */
	public DaShmLinkStructure(Agent agent, String name) {
		super(agent, name);
	}

	/**
	 * 
	 * For every verb instance in the focus, calculated the energy quantums to
	 * be applied for the SUCCESSOR, PRECEDENCE and COINCIDENCE links. These are
	 * scaled by the current link scale parameters of the dominant scene of the
	 * VI
	 * 
	 */
	@Override
	protected void applyFocusVi(VerbInstance fvi, double timeSlice) {
		// dominant scene
		Instance scene = fvi.getReferencedScenes().iterator().next();
		List<EnergyQuantum<VerbInstance>> eqs = new ArrayList<>();
		List<EnergyQuantum<VerbInstance>> eqslink;
		double strength;
		//
		// Successor links
		//
		strength = scene.getSceneParameters().getLinkStructureWeight().get(Hardwired.LINK_SUCCESSOR);
		eqslink = applyForLinkType(fvi, timeSlice, Hardwired.LINK_SUCCESSOR, strength, EnergyColors.SHV_TEMPORAL_ORDER);
		eqs.addAll(eqslink);
		//
		// Predecessor links
		//
		strength = scene.getSceneParameters().getLinkStructureWeight().get(Hardwired.LINK_PREDECESSOR);
		eqslink = applyForLinkType(fvi, timeSlice, Hardwired.LINK_PREDECESSOR, strength,
				EnergyColors.SHV_TEMPORAL_ORDER);
		eqs.addAll(eqslink);
		//
		// Coincidence links
		//
		strength = scene.getSceneParameters().getLinkStructureWeight().get(Hardwired.LINK_COINCIDENCE);
		eqslink = applyForLinkType(fvi, timeSlice, Hardwired.LINK_COINCIDENCE, strength,
				EnergyColors.SHV_TEMPORAL_ORDER);
		eqs.addAll(eqslink);
		// now perform all (or maybe a subset) of the changes
		for (EnergyQuantum<VerbInstance> eq : eqs) {
			sf.applyViEnergyQuantum(eq);
		}
	}

	/**
	 * For all pairs of focus-shadow VIs, look for all the pairs of similar
	 * links from them.
	 * 
	 * <pre>
	 * fvi ----- link ------> flink
	 * | 
	 * shv_generic adding... shv_link_structure 
	 * | 
	 * svi ------ link -----> slink
	 * </pre>
	 * 
	 * The added energy is scaled with:
	 * <ul>
	 * <li>the salience of fvi/svi generic shadow
	 * <li>the minimum of the two link values
	 * <li>a scaling parameter strenght which varies from scene to scene (but
	 * currently only initialized from the main parameters)
	 * </ul>
	 * 
	 * @param fvi
	 * @param timeSlice
	 * @param linkType
	 * @param strength
	 *            - scaling parameter. This is normally coming from the
	 *            parameter attached
	 * @param ec
	 *            - the shadow energy which is added. This is normaly
	 *            SHV_TEMPORAL_ORDER
	 */
	protected List<EnergyQuantum<VerbInstance>> applyForLinkType(VerbInstance fvi, double timeSlice, String linkType,
			double strength, String ec) {
		List<EnergyQuantum<VerbInstance>> retval = new ArrayList<>();
		Links la = agent.getLinks();
		List<SimpleEntry<VerbInstance, Double>> fviLinks = la.getLinksByLinkName(fvi, linkType).getList();
		List<VerbInstance> shadows = sf.getMembers(fvi, EnergyColors.SHV_GENERIC);
		for (VerbInstance svi : shadows) {
			// double fvisviSalience =
			// sf.getSalience(fvi, svi, EnergyColor.SHV_GENERIC);
			List<SimpleEntry<VerbInstance, Double>> sviLinks = la.getLinksByLinkName(svi, linkType).getList();
			for (SimpleEntry<VerbInstance, Double> focusEntry : fviLinks) {
				for (SimpleEntry<VerbInstance, Double> shadowEntry : sviLinks) {
					double linkStrength = Math.min(focusEntry.getValue(), shadowEntry.getValue());
					// double addition = strength * linkStrength *
					// fvisviSalience;
					double addition = strength * linkStrength;
					VerbInstance fLink = focusEntry.getKey();
					VerbInstance sLink = shadowEntry.getKey();
					EnergyQuantum<VerbInstance> eq = EnergyQuantum.createAdd(fLink, sLink, addition, timeSlice, ec,
							"DaShmLinkStructure + " + linkType);
					retval.add(eq);
				}
			}
		}
		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xapagy.activity.DiffusionActivity#extractParameters()
	 */
	@Override
	public void extractParameters() {
		// TODO Auto-generated method stub

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
		fmt.add("DaShmLinkStructure");
	}
}
