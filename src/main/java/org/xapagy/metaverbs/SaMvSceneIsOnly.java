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

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.ui.formatters.IXwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: Dec 21, 2010
 */
public class SaMvSceneIsOnly extends AbstractSaMetaVerb {
    private static final long serialVersionUID = -2177189665819285218L;

    /**
     * @param name
     */
    public SaMvSceneIsOnly(Agent agent) {
        super(agent, "SaMvSceneIsOnly", ViType.S_V);
    }

    /**
     * Makes the scene defined in the subject current. Clears all the other
     * instances which are not in the specified scene.
     */
    @Override
    public void applyInner(VerbInstance verbInstance) {
        // makes the specified scene current
        Focus fc = agent.getFocus();
        Instance theScene = verbInstance.getSubject();
        agent.getFocus().setCurrentScene(theScene);
        List<Instance> currentMembers = theScene.getSceneMembers();
        // now expire all the other instances in the focus
        for (Instance inst : fc.getInstanceList(EnergyColors.FOCUS_INSTANCE)) {
            if (!currentMembers.contains(inst) && theScene != inst) {
                EnergyQuantum<Instance> eq =
                        EnergyQuantum.createMult(inst, 0.0, EnergyColors.FOCUS_INSTANCE,
                                "SaMvSceneIsOnly");
                fc.applyInstanceEnergyQuantum(eq);
            }
        }
        for (Instance scene : fc.getSceneList(EnergyColors.FOCUS_INSTANCE)) {
            if (scene != theScene) {
                EnergyQuantum<Instance> eq =
                        EnergyQuantum.createMult(scene, 0.0, EnergyColors.FOCUS_INSTANCE,
                                "SaMvSceneIsOnly");
                fc.applyInstanceEnergyQuantum(eq);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see org.xapagy.ui.formatters.IxwFormattable#formatTo(org.xapagy.ui.formatters.IXwFormatter, int)
     */
    @Override
    public void formatTo(IXwFormatter fmt, int detailLevel) {
        fmt.add("SaMvSceneIsOnly");
    }

    /* (non-Javadoc)
     * @see org.xapagy.activity.Activity#extractParameters()
     */
    public void extractParameters() {
    }
}
