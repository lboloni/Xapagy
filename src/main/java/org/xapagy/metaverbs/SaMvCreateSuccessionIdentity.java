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
 * Created on: Nov 3, 2012
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
