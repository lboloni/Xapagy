/*
   This file is part of the Xapagy project
   Created on: Feb 5, 2011
 
   org.xapagy.domain.basic.vbRecall
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class SaMvRecall extends AbstractSaMetaVerb {
    private static final long serialVersionUID = 4029531111895523783L;

    public SaMvRecall(Agent agent) {
        super(agent, "SaMvRecall", ViType.S_V);
    }

    /**
     * Removes the object as a composite
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Instance scene = verbInstance.getSubject();
        scene.getSceneParameters().setEnergy(1000.0, EnergyColors.SCENE_CONTINUATION);
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvRecall");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
