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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.IdentityHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * Creates a scene which is the clone of the specified scene, it has instances
 * which are identity linked to all the instances of the current scene
 * 
 * @author Ladislau Boloni
 * Created on: Nov 19, 2012
 */
public class SaMvCloneScene extends AbstractSaMetaVerb {

    private static final long serialVersionUID = -7362434745480940112L;

    public SaMvCloneScene(Agent agent) {
        super(agent, "SaMvCloneScene", ViType.S_ADJ);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xapagy.activity.SpikeActivity#applyInner()
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        Focus fc = agent.getFocus();
        Instance currentScene = fc.getCurrentScene();
        Map<Instance, Instance> oldToNew = new HashMap<>();
        // create the new scene, add the new properties and labels
        Instance newScene = agent.createInstance(null);
        newScene.getConcepts().addOverlay(currentScene.getConcepts());
        newScene.getConcepts().addOverlayImpacted(verbInstance.getAdjective());
        for (String label : verbInstance.getAdjective().getLabels()) {
            newScene.getConcepts().addFullLabel(label, agent);
        }
        oldToNew.put(currentScene, newScene);

        EnergyQuantum<Instance> eq =
                EnergyQuantum.createAdd(newScene,
                        Focus.INITIAL_ENERGY_INSTANCE, EnergyColors.FOCUS_INSTANCE,
                        "SaMvCloneScene");
        fc.applyInstanceEnergyQuantum(eq);
        // create all the instances in the new scene
        for (Instance inst : currentScene.getSceneMembers()) {
            Instance newInstance = agent.createInstance(newScene);
            newInstance.getConcepts().addOverlay(inst.getConcepts());
            EnergyQuantum<Instance> eq2 =
                    EnergyQuantum.createAdd(newInstance,
                            Focus.INITIAL_ENERGY_INSTANCE, EnergyColors.FOCUS_INSTANCE,
                            "SaMvCloneScene");
            fc.applyInstanceEnergyQuantum(eq2);
            oldToNew.put(inst, newInstance);
            // should we create identities???

        }
        // create clones of all the relations, as long as we are inside
        for (Instance inst : currentScene.getSceneMembers()) {
            Instance newInst = oldToNew.get(inst);
            // create the identity relation, from new to old
            IdentityHelper.createIdentityRelation(inst, newInst, agent);
            List<VerbInstance> relationsFrom =
                    RelationHelper.getRelationsFrom(agent, inst, true);
            for (VerbInstance vr : relationsFrom) {
                Instance other = vr.getObject();
                Instance newOther = oldToNew.get(other);
                if (newOther == null) {
                    continue;
                }
                // create the same relation between newInst and newOther
                VerbOverlay voNew = new VerbOverlay(agent);
                voNew.addOverlay(vr.getVerbs());
                RelationHelper.createAndAddRelation(agent, voNew, newInst,
                        newOther);

            }
        }
    }

    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvCloneScene");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
