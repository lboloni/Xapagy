/*
   This file is part of the Xapagy project
   Created on: Nov 19, 2012
 
   org.xapagy.metaverbs.SaMvForget
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * This verb "forgets" a specific instance from the focus, reducing it to zero
 * (it is used to drop some things from the scene)
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvForget extends AbstractSaMetaVerb {

    private static final long serialVersionUID = 3243496860166834303L;

    public SaMvForget(Agent agent) {
        super(agent, "SaMvForget", ViType.S_V);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.SpikeActivity#applyInner()
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Focus fc = agent.getFocus();
        Instance instance = verbInstance.getSubject();
        EnergyQuantum<Instance> eq =
                EnergyQuantum.createMult(instance, 0.0, EnergyColors.FOCUS_INSTANCE,
                        "SaMvForget");
        fc.applyInstanceEnergyQuantum(eq);
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvForget");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
