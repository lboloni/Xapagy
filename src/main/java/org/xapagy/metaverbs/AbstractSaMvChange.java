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
import org.xapagy.agents.Focus;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.TextUi;

/**
 * @author Ladislau Boloni
 * Created on: Aug 18, 2010
 */
public abstract class AbstractSaMvChange extends AbstractSaMetaVerb {

    private static final long serialVersionUID = 3512703713289011223L;

    /**
     * If true, the attributes will be copied to the new instance
     */
    private boolean copyAttributes = false;
    /**
     * If true, the relations will be transferred to the new instance
     */
    private boolean transferRelations = true;

    /**
     * Creates the AbstractSaMvChange spike. The changes verb can only used in
     * an adjective type system
     * 
     * @param agent
     * @param name
     * @param copyAttributes
     * @param transferRelations
     */
    public AbstractSaMvChange(Agent agent, String name, boolean copyAttributes,
            boolean transferRelations) {
        super(agent, name, ViType.S_ADJ);
        this.copyAttributes = copyAttributes;
        this.transferRelations = transferRelations;
    }

    /**
     * The change verb side effect.
     * 
     * Creates a new instance, copies the overlays, add the newly changed
     * attributes, with impact.
     * 
     * Then it creates an identity relation between the new and the old one
     * 
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        TextUi.println("SaMvChange.applyInner");
        Instance subject = verbInstance.getSubject();
        Focus fc = agent.getFocus();
        Shadows sf = agent.getShadows();
        Instance instanceChanged = agent.createInstance(subject.getScene());
        if (copyAttributes) {
            instanceChanged.getConcepts().addOverlay(subject.getConcepts());
        }
        instanceChanged.getConcepts().addOverlayImpacted(
                verbInstance.getAdjective(), 1.0);
        for (String label : verbInstance.getAdjective().getLabels()) {
            instanceChanged.getConcepts().addFullLabel(label, agent);
        }
        //
        // transfer the relations
        //
        if (transferRelations) {
            for (VerbInstance vi : RelationHelper.getRelationsFrom(agent,
                    subject, true)) {
                RelationHelper.createAndAddRelation(agent, vi.getVerbs(),
                        instanceChanged, vi.getObject());
            }
            for (VerbInstance vi : RelationHelper.getRelationsTo(agent,
                    subject, true)) {
                RelationHelper.createAndAddRelation(agent, vi.getVerbs(),
                        vi.getSubject(), instanceChanged);
            }
        }
        //
        // replace it in the focus and the shadow
        //
        EnergyQuantum<Instance> eq1 =
                EnergyQuantum.createMult(subject, 0.0, EnergyColors.FOCUS_INSTANCE,
                        "AbstractSaMvChange");
        fc.applyInstanceEnergyQuantum(eq1);

        EnergyQuantum<Instance> eq2 =
                EnergyQuantum.createAdd(instanceChanged,
                        Focus.INITIAL_ENERGY_INSTANCE, EnergyColors.FOCUS_INSTANCE,
                        "AbstractSaMvChange");
        fc.applyInstanceEnergyQuantum(eq2);
        //
        // Move the old shadow into the new one
        //
        TextUi.println("Change: only moving SHADOW_GENERIC color!!!");
        for (Instance si : sf.getMembers(subject, EnergyColors.SHI_GENERIC)) {
            double valAbsSi =
                    sf.getEnergy(subject, si, EnergyColors.SHI_GENERIC);
            EnergyQuantum<Instance> sq =
                    EnergyQuantum.createAdd(instanceChanged, si, valAbsSi,
                            EnergyQuantum.TIMESLICE_ONE,
                            EnergyColors.SHI_GENERIC, "SaMvChange");
           sf.applyInstanceEnergyQuantum(sq);
        }
        //
        // Makes the old instance identity related to the new one
        //
        IdentityHelper.createIdentityRelation(subject, instanceChanged, agent);
        verbInstance.setCreatedInstance(instanceChanged);
    }
}
