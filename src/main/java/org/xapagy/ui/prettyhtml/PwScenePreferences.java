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
package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Choice.ChoiceType;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.instances.SceneParameters;
import org.xapagy.set.EnergyColors;
import org.xapagy.set.ViSet;
import org.xapagy.ui.formatters.PwFormatter;

/**
 * @author Ladislau Boloni
 * Created on: May 27, 2013
 */
public class PwScenePreferences {

    /**
     * Prints a separate subsection for the fired shadow VIs
     * 
     * @param fmt
     * @param instance
     * @param agent
     * @param gq
     */
    private static void pwFiredShadowVis(PwFormatter fmt, SceneParameters sp,
            Agent agent, RESTQuery query) {
        fmt.addLabelParagraph("firedShadowVis");
        fmt.explanatoryNote("The set of the shadow VIs which had been fired by a "
                + "continuation choice in this scene. These will be inhibited for firing again.");
        ViSet vis = sp.getFiredShadowVis();
        PwQueryLinks.linkToViSet(fmt, agent, query, vis);
    }

    /**
     * Prints the scene preferences
     * 
     * @param fmt
     * @param instance
     * @param agent
     * @param gq
     */
    public static void pwScenePreferences(PwFormatter fmt, SceneParameters sp,
            Agent agent, RESTQuery query) {
        fmt.is("energyContinuation", sp.getEnergy(EnergyColors.SCENE_CONTINUATION));
        fmt.is("energyInterstitial", sp.getEnergy(EnergyColors.SCENE_INTERSTITIAL));
        //
        // Characterization weight
        //
        fmt.addLabelParagraph("Characterization weights");
        fmt.indent();
        fmt.is("characterizationWeightEssence",
                sp.getCharacterizationWeightEssence());
        fmt.is("characterizationWeightUniqueness",
                sp.getCharacterizationWeightUniqueness());
        fmt.is("characterizationWeightTargetDifference",
                sp.getCharacterizationWeightTargetDifference());
        fmt.deindent();
        //
        // Choice preferences
        //
        fmt.addLabelParagraph("Choice type preferences");
        fmt.indent();
        for (ChoiceType ct : ChoiceType.values()) {
            fmt.is(ct.toString(), sp.getChoiceTypePreferences().get(ct));
        }
        fmt.deindent();
        //
        // Story consistency weights
        //
        fmt.addLabelParagraph("Story consistency weights");
        fmt.indent();
        for (String linkType : agent.getLinks().getLinkTypeNames()) {
            fmt.is(linkType, sp.getLinkStructureWeight().get(linkType));
        }
        fmt.deindent();
        //
        // Fired shadow VIs
        //
        PwScenePreferences.pwFiredShadowVis(fmt, sp, agent, query);

    }

}
