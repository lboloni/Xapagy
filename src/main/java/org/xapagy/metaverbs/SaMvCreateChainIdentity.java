/*
   This file is part of the Xapagy project
   Created on: Nov 9, 2011
 
   org.xapagy.metaverbs.SaMvIsTheSameAs
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.metaverbs;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.reference.rrState;
import org.xapagy.set.EnergyColors;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Creates identity relations between the specified instance, and instances in
 * its shadow which match the specified concept
 * 
 * FIXME: the hardwired value 0.2 must be calibrated
 * 
 * @author Ladislau Boloni
 * 
 */
public class SaMvCreateChainIdentity extends AbstractSaMetaVerb {

    private static final long serialVersionUID = -4096466248131984555L;

    public SaMvCreateChainIdentity(Agent agent) {
        super(agent, "SaMvCreateChainIdentity", ViType.S_ADJ);
    }

    /**
     * Connects the subject with the other one FIXME: calibrate the hardwired
     * value
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Instance inst = verbInstance.getSubject();
        ConceptOverlay co = verbInstance.getAdjective();
        // reinforce the ones which are matching CO
        Shadows sf = agent.getShadows();
        int identitiesCreated = 0;
        for (Instance shadowInst : sf.getMembers(inst,
                EnergyColors.SHI_GENERIC)) {
            double valAbsSi =
                    sf.getSalience(inst, shadowInst,
                            EnergyColors.SHI_GENERIC);
            rrState resconf =
                    rrState.createCalculated(shadowInst.getConcepts(), co);
            double overallScore = resconf.getOverallScore();
            if (overallScore * valAbsSi > 0.2) {
                IdentityHelper.createIdentityRelation(inst, shadowInst, agent);
                identitiesCreated++;
            }
        }
        if (identitiesCreated == 0) {
            TextUi.errorPrint("SaMvChainIdentity: no identity created.");
        }
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvCreateChainIdentity");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
    
}
