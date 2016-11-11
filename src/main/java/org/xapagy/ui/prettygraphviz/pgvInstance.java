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
package org.xapagy.ui.prettygraphviz;

import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.instances.GroupHelper;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.prettygeneral.xwVerbOverlay;
import org.xapagy.ui.smartprint.SpInstance;

/**
 * @author Ladislau Boloni
 * Created on: Apr 4, 2012
 */
public class pgvInstance {

    /**
     * Create the relations from an instance
     * 
     * @param instance
     * @param agent
     * @param fmt
     * @param param
     */
    public static void pgvLinks(Instance instance, Agent agent,
            GvFormatter fmt, GvParameters param) {
        Focus fc = agent.getFocus();
        String color = "";
        // binary context relations, from
        List<VerbInstance> vis =
                RelationHelper.getRelationsFrom(agent, instance, false);
        for (VerbInstance vi : vis) {
            if (!ViClassifier.decideViClass(ViClass.IDENTITY, vi, agent)) {
                if (param.relations_show_nonIdentityRelations) {
                    fmt.openLink(instance.getIdentifier(), vi.getObject()
                            .getIdentifier());
                    // fixme: classify the identity type
                    fmt.is(GvParameters.FONTNAME, param.fontname);
                    fmt.is(GvParameters.FONTSIZE, param.fontsize);
                    String label =
                            xwVerbOverlay.ppRelationLabel(vi.getVerbs(), agent);
                    fmt.label(label);
                    if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                        color = param.relations_color_nonIdentity;
                    } else {
                        color = "gray";
                    }
                    fmt.is(GvParameters.COLOR, color);
                    fmt.close();
                }
                continue;
            } else {
                // identity relation
                if (param.relations_show_identity) {
                    fmt.openLink(instance.getIdentifier(), vi.getObject()
                            .getIdentifier());
                    fmt.is(GvParameters.COLOR, param.relations_color_identity);
                    fmt.is(GvParameters.STYLE, param.relations_style_identity);
                    fmt.close();
                }
            }
        }
    }

    /**
     * Creates the node representing an instance
     * 
     * @param instance
     * @param agent
     * @param fmt
     * @param param
     */
    public static void pgvNode(Instance instance, Agent agent, GvFormatter fmt,
            GvParameters param) {
        Instance scene = instance.getScene();
        String spi = SpInstance.spInstance(instance, scene, agent, false);
        if (spi == null) {
            @SuppressWarnings("unused")
            String spi2 = SpInstance.spInstance(instance, scene, agent, true);
            TextUi.errorPrint("Null while sp-ing an instance???");
        }
        fmt.openNode(instance.getIdentifier());
        fmt.is(GvParameters.SHAPE, param.instance_shape);
        fmt.is(GvParameters.STYLE, param.instance_style);
        fmt.is(GvParameters.FONTNAME, param.fontname);
        fmt.is(GvParameters.FONTSIZE, param.fontsize);
        String color = null;
        String fontColor = null;
        // use different colors depending on whether the instance is active or
        // not
        if (agent.getFocus().getSalience(instance, EnergyColors.FOCUS_INSTANCE) > 0) {
            color = param.instance_color_active;
            fontColor = param.instance_fontcolor_active;
        } else {
            color = param.instance_color_inactive;
            fontColor = param.instance_fontcolor_inactive;
        }
        fmt.is(GvParameters.COLOR, color);
        fmt.is(GvParameters.FONTCOLOR, fontColor);
        // different colors for the group
        String fillColor = null;
        if (GroupHelper.decideGroup(instance, agent)) {
            fillColor = param.instance_fillcolor_group;
        } else {
            fillColor = param.instance_fillcolor_regular;
        }
        fmt.is(GvParameters.FILLCOLOR, fillColor);
        String label = GraphVizHelper.wrapLabel(spi, param.instance_wrap);
        fmt.label(label);
        fmt.close();
    }

}
