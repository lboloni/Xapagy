/*
   This file is part of the Xapagy project
   Created on: Nov 3, 2012
 
   org.xapagy.metaverbs.SaMvSameAsBefore
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.SceneRelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.reference.rrState;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * 
 */
public class SaMvCreateSuccessionIdentity extends AbstractSaMetaVerb {

    private static final long serialVersionUID = 7364665226561949049L;

    public SaMvCreateSuccessionIdentity(Agent agent) {
        super(agent, "SaMvCreateSuccessionIdentity", ViType.S_ADJ);
    }

    /**
     * Connects the subject with the strong items in the shadow which can be
     * referred to by the attributes!
     * 
     * FIXME: calibrate the hardwired value of 0.2
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Instance inst = verbInstance.getSubject();
        ConceptOverlay co = verbInstance.getAdjective();
        Set<Instance> scenes =
                SceneRelationHelper.previousChainOfScenes(agent,
                        inst.getScene());
        // for each item in the scenes
        for (Instance scene : scenes) {
            for (Instance candidate : scene.getSceneMembers()) {
                // double valAbsSi = sf.getAbsoluteValue(inst, candidate);
                rrState resconf =
                        rrState.createCalculated(candidate.getConcepts(), co);
                double overallScore = resconf.getOverallScore();
                if (overallScore > 0.2) {
                    IdentityHelper.createIdentityRelation(inst, candidate,
                            agent);
                }
            }
        }
    }

    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvCreateSuccessionIdentity");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
