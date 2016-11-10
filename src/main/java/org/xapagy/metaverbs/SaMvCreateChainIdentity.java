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
 * Created on: Nov 9, 2011
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
