/*
   This file is part of the Xapagy project
   Created on: Apr 1, 2014
 
   org.xapagy.activity.shadowmaintenance.DaShmGarbageCollect
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.activity.shadowmaintenance;

import org.xapagy.activity.DiffusionActivity;
import org.xapagy.agents.Agent;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * This DA simply calls the garbage collector on the shadow energy values. This
 * normally should go after the other DAs in the chain.
 * 
 * @author Ladislau Boloni
 * 
 */
public class DaShmGarbageCollect extends DiffusionActivity {
    private static final long serialVersionUID = 3808489624872372544L;

    /**
     * beta - the steepness of the angle
     */
    private double beta;
    /**
     * inflectionPopulation - above this population, the probability to be
     * garbage collected increase - this is the size of the shadow we would like
     * to have
     */
    private double inflectionPopulation;
    /**
     * safeEnergy - the ALL energy above which a shadow will not be garbage
     * collected.
     * 
     */
    private double safeEnergy;

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#extractParameters()
     */
    @Override
    public void extractParameters() {
        beta = getParameterDouble("beta");
        inflectionPopulation = getParameterDouble("inflectionPopulation");
        safeEnergy = getParameterDouble("safeEnergy");
    }

    /**
     * 
     * @param agent
     * @param name
     */
    public DaShmGarbageCollect(Agent agent, String name) {
        super(agent, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.DiffusionActivity#applyInner(double)
     */
    @Override
    protected void applyInner(double timeSlice) {
        agent.getShadows().garbageCollect(timeSlice, safeEnergy,
                inflectionPopulation, beta);
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
        fmt.add("DaShmGarbageCollect");
        fmt.indent();
        fmt.is("beta", beta);
        fmt.explanatoryNote("beta - the steepness of the angle");
        fmt.is("inflectionPopulation", inflectionPopulation);
        fmt.explanatoryNote("inflectionPopulation - above this population, the "
                + "probability to be garbage collected increase - this is the size of the "
                + "shadow we would like to have");
        fmt.is("safeEnergy", safeEnergy);
        fmt.explanatoryNote(
                "safeEnergy - the ALL energy above which a shadow will not be "
                        + "garbage collected.");
        fmt.deindent();
    }

}
