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
package org.xapagy.instances;

import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.EnergyQuantum;

/**
 * Decides on the identity relations, based on the IRContextHelper - which means
 * that this identity vi must be in the focus.
 * 
 * @author Ladislau Boloni
 * Created on: Oct 20, 2011
 */
public class IdentityHelper {

    /**
     * Create an identity relation from iFrom to iTo - used by change
     * 
     * @param iFrom
     * @param iTo
     */
    public static VerbInstance createIdentityRelation(Instance iFrom,
            Instance iTo, Agent agent) {
        Focus fc = agent.getFocus();
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        VerbOverlay vo = Hardwired.getIdentityVO(agent);
        VerbInstance vi = agent.createVerbInstance(ViType.S_V_O, vo);
        vi.setSubject(iFrom);
        vi.setObject(iTo);
        // add it and immediately expire it from the focus
        EnergyQuantum<VerbInstance> eqadd =
                EnergyQuantum.createAdd(vi, Focus.INITIAL_ENERGY_VI,
                        EnergyColors.FOCUS_VI, "IdentityHelper");
        fc.applyViEnergyQuantum(eqadd);

        EnergyQuantum<VerbInstance> eqremove =
                EnergyQuantum.createMult(vi, 0.0, EnergyColors.FOCUS_VI, "IdentityHelper");
        fc.applyViEnergyQuantum(eqremove);

        EnergyQuantum<VerbInstance> eqam  = EnergyQuantum.createAdd(vi, 1.0, EnergyColors.AM_VI, "IdentityHelper");
        am.applyViEnergyQuantum(eqam);
        //agent.getAutobiographicalMemory().addEnergy(vi, 1.0, EnergyColor.AM);
        return vi;
    }

    /**
     * View identity: scenes different, scenes connected with view
     * 
     * @param iReality
     *            - the instance in the reality scene
     * @param iView
     *            - the instance in the fictional scene
     * @param agent
     * @return
     */
    public static boolean isFictionalIdentity(Instance iReality,
            Instance iFictional, Agent agent) {
        if (!IdentityHelper.isIdentityRelated(iReality, iFictional, agent)) {
            return false;
        }
        if (!SceneRelationHelper.isFictionalScene(agent, iFictional.getScene(),
                iReality.getScene())) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if iOther is directly and unidirectionally identity related
     * to iFirst
     * 
     * @param iFirst
     * @param iOther
     * @param agent
     * @return
     */
    public static boolean isIdentityRelated(Instance iFirst, Instance iOther,
            Agent agent) {
        if (!RelationHelper.decideRelation(false, agent, Hardwired.VR_IDENTITY,
                iFirst, iOther)) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if the iOther is unidirectionally related to iOther,
     * possibly through multiple hops
     * 
     * @param iFirst
     * @param iOther
     * @param agent
     * @return
     */
    public static boolean isIdentityRelatedRecursive(Instance iFirst,
            Instance iOther, Agent agent) {
        int recursionCount = 3;
        Set<Instance> set =
                RelationHelper.getRecursiveRelationSpecificPart(agent,
                        Hardwired.VR_IDENTITY, iFirst, ViPart.Object,
                        ViPart.Subject, recursionCount);
        if (set.contains(iOther)) {
            return true;
        }
        return false;
    }

    /**
     * Somatic identity: same scene, connected with identity
     * 
     * @param iOrig
     * @param iChanged
     * @param agent
     * @return
     */
    public static boolean isSomaticIdentity(Instance iOrig, Instance iChanged,
            Agent agent) {
        if (!IdentityHelper.isIdentityRelated(iOrig, iChanged, agent)) {
            return false;
        }
        if (iOrig.getScene() != iChanged.getScene()) {
            return false;
        }
        return true;
    }

    /**
     * 
     * Succession identity: scenes different, connected with succession
     * 
     * @param iOrig
     * @param iSuccessor
     * @param agent
     * @return
     */
    public static boolean isSuccessionIdentity(Instance iOrig,
            Instance iSuccessor, Agent agent) {
        if (!IdentityHelper.isIdentityRelated(iOrig, iSuccessor, agent)) {
            return false;
        }
        if (!SceneRelationHelper.previousChainOfScenes(agent,
                iSuccessor.getScene()).contains(iOrig.getScene())) {
            return false;
        }
        return true;
    }

    /**
     * View identity: scenes different, scenes connected with view
     * 
     * @param iReality
     *            - the instance in the reality scene
     * @param iView
     *            - the instance in the view scene
     * @param agent
     * @return
     */
    public static boolean isViewIdentity(Instance iReality, Instance iView,
            Agent agent) {
        if (!IdentityHelper.isIdentityRelated(iReality, iView, agent)) {
            return false;
        }
        if (!SceneRelationHelper.isViewScene(agent, iView.getScene(),
                iReality.getScene())) {
            return false;
        }
        return true;
    }

}
