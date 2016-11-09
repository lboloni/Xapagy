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
package org.xapagy.headless_shadows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xapagy.activity.hlsmaintenance.ChoiceScore;
import org.xapagy.activity.hlsmaintenance.SaHlsmInternalLiInitialShadow;
import org.xapagy.agents.Agent;
import org.xapagy.agents.Execute;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.metaverbs.SaMvInsertHlsLocation;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

/**
 * @author Ladislau Boloni
 * Created on: May 18, 2014
 */
public class ExecuteChoice {

    /**
     * Performs the execution of a choice
     */
    public static List<VerbInstance> executeChoice(Agent agent, Choice choice) {
        List<VerbInstance> executionResult = new ArrayList<>();
        Set<Instance> referencedScenes = new HashSet<>();
        //
        // instantiate the dependencies: create the instances to which the choice
        // refers but are not in the focus
        //
        while (true) {
            VerbInstance vi = choice.instantiateDependency(agent);
            if (vi == null) {
                break;
            }
            referencedScenes.addAll(vi.getReferencedScenes());
            Execute.executeVIandSAs(agent, vi, null);
            choice.resolveDependency(vi.getCreatedInstance());
            executionResult.add(vi);
        }
        //
        //
        //
        VerbInstance vi = choice.instantiate(agent);
        referencedScenes.addAll(vi.getReferencedScenes());
        if (choice.getChoiceType().equals(ChoiceType.MISSING_ACTION)) {
            // for missing actions we do not execute the successor verb in the
            // action verb, instead we execute this special SaMv...
            Hls hls = choice.getHls();
            SaMvInsertHlsLocation samv =
                    new SaMvInsertHlsLocation(agent, vi.getViType(), hls);
            Execute.executeVIandSAs(agent, vi, samv);
        } else {
            Execute.executeVIandSAs(agent, vi, null);
        }
        executionResult.add(vi);
        // if the VI is coming from an HLS, and this is the end of the
        // instantiation, convert the HLS to be the shadow
        if (choice.getHls() != null) {
            SaHlsmInternalLiInitialShadow saHlsmInitialShadow =
                    new SaHlsmInternalLiInitialShadow(agent, "HlsmInternalLiInitialShadow", choice);
            saHlsmInitialShadow.apply(vi);
        }
        //
        //  The fired shadow vis:
        //   -for the scenes which are referred by the VIs instantiated by the template
        //   -they contain the virtual shadows of the VIs which had been used to generate the choice
        //   -the idea is that those instances will not be reused in the creation of new VIs (a sort 
        //   of inhibition)
        //
        if (choice.getChoiceType().equals(ChoiceType.CONTINUATION)) {
            ViSet virtualShadow = choice.getChoiceScore().getVirtualShadow();
            ViSet addon = new ViSet();
            addon.mergeIn(virtualShadow, 1.0);
            // adding the VI itself? why?
            // addon.change(vi, 1.0);
            Set<Instance> scenes =
                    ChoiceScore.getScenesReferencedByTemplates(choice.getHls());
            for (Instance scene : scenes) {
                // 
                Formatter fmt = new Formatter();
                fmt.add("addFiredShadowVis called");
                fmt.indent();
                fmt.is("scene", PrettyPrint.ppConcise(scene, agent));
                fmt.add(PrettyPrint.ppConcise(addon, agent));
                TextUi.println(fmt);
                scene.getSceneParameters().addFiredShadowVis(addon);
            }
        }
        // continuations create interstitial energy, other ones use it
        for (Instance scene : referencedScenes) {
            if (choice.getChoiceType().equals(ChoiceType.CONTINUATION)) {
                scene.getSceneParameters().resetInterstitialEnergy();
                scene.getSceneParameters().useEnergy(EnergyColors.SCENE_CONTINUATION);
            } else {
                scene.getSceneParameters().useEnergy(EnergyColors.SCENE_INTERSTITIAL);
            }
        }
        return executionResult;
    }

    
}
