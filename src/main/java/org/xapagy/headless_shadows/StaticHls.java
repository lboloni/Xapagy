/*
   This file is part of the Xapagy project
   Created on: Aug 31, 2014

   org.xapagy.headless_shadows.StaticHls

   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.headless_shadows;

import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors;

/**
 * The static headless shadow is inferred from the StaticFSLI-s.
 *
 * NOTE: the current implementation does not support dependencies!!!
 *
 * @author Ladislau Boloni
 *
 */
public class StaticHls extends AbstractHls implements Comparable<StaticHls> {

    private static final long serialVersionUID = -8890731377192880255L;
    /**
     * A simple supportEnergy, the sum of the scores of the StaticFSLIs
     */
    private double supportEnergy = 0;
    @SuppressWarnings("unused")
    private Agent agent;

    /**
     * The set of supports for this HLS (in the form of FslInterpretation
     * objects whose interpretations are compatible with the current ViTemplate)
     */
    private Set<StaticFSLI> supports = new HashSet<>();

    /**
     * Create a StaticHls from a viTemplate
     *
     * @param viTemplate
     * @param agent
     */
    public StaticHls(VerbInstance viTemplate, Agent agent) {
        this.agent = agent;
        this.viTemplate = viTemplate;
        // make a copy of the original viTemplate for later debugging
        this.viTemplateOriginal =
                VerbInstance.createViTemplateFromModel(viTemplate);
        this.identifier =
                agent.getIdentifierGenerator().getStaticHlsIdentifier();
    }

    /**
     * @param sfsli
     */
    public void addSupport(StaticFSLI sfsli) {
        supports.add(sfsli);
        supportEnergy += sfsli.getTotalSupport();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(StaticHls other) {
        return Double.compare(getSupportSalience(), other.getSupportSalience());
    }

    public Set<StaticFSLI> getSupports() {
        return supports;
    }

    /**
     * Get the salience of the support energy... it is unclear what energy are we working on here???
     * @FIXME: What energy are we working on here???
     * 
     * @return the supportEnergy
     */
    public double getSupportSalience() {
        double param = 1.0; // what energy color is this???
        return EnergyColors.convert(supportEnergy, param);
    }

}
