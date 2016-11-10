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

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.operations.Incompatibility;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Nov 8, 2011
 */
public class SaMvActsLike extends AbstractSaMetaVerb {
    private static final long serialVersionUID = -331369907021910792L;

    /**
     * Returns the relations between the same nodes (or on the same node if
     * unary)
     * 
     * To be used by SaMvRemoveRelation as well
     * 
     * @return
     */
    public static List<VerbInstance> returnMatchingActsLikes(
            VerbInstance verbInstance, Agent agent) {
        Focus fc = agent.getFocus();
        Verb vbActsLike = agent.getVerbDB().getConcept(Hardwired.VM_ACTS_LIKE);
        List<VerbInstance> retval = new ArrayList<>();
        for (VerbInstance vi : fc.getViList(EnergyColors.FOCUS_VI)) {
            if (vi.getViType() != ViType.S_ADJ) {
                continue;
            }
            if (vi.getSubject() != verbInstance.getSubject()) {
                continue;
            }
            if (vi.getVerbs().getEnergy(vbActsLike) > 0) {
                retval.add(vi);
            }
        }
        return retval;
    }

    public SaMvActsLike(Agent agent) {
        super(agent, "SaMvActsLike", null);
    }

    /**
     * The relation will be this VI itself.
     * 
     * Here, we are going to expire those relations which are not similar
     * 
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Focus fc = agent.getFocus();
        List<VerbInstance> candidates =
                SaMvActsLike.returnMatchingActsLikes(verbInstance, agent);

        List<VerbInstance> actsLikeToRemove = new ArrayList<>();
        ConceptOverlay coNew = verbInstance.getAdjective();
        for (VerbInstance vi : candidates) {
            ConceptOverlay co = vi.getAdjective();
            if (Incompatibility.decideIncompatibility(co, coNew)) {
                actsLikeToRemove.add(vi);
            }
        }
        for (VerbInstance vi : actsLikeToRemove) {
            EnergyQuantum<VerbInstance> eq =
                    EnergyQuantum.createMult(vi, 0.0, EnergyColors.FOCUS_VI,
                            "SaMvActsLike");
            fc.applyViEnergyQuantum(eq);
        }
    }
    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvActsLike");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
