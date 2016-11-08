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
package org.xapagy.ui.prettygeneral;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.concepts.Concept;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.instances.Instance;
import org.xapagy.instances.RelationHelper;
import org.xapagy.instances.VerbInstance;
import org.xapagy.metaverbs.MetaVerbHelper;
import org.xapagy.set.EnergyColors;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.PpViSet;
import org.xapagy.ui.prettyprint.PrettyPrint;

public class xwInstance {

	
    /**
     * Returns a concise, single line version of the instance description
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static String xwSuperConcise(IXwFormatter xw, Instance instance, Agent agent) {   
        for (SimpleEntry<Concept, Double> entry : instance.getConcepts()
                .getList()) {
            Concept c = entry.getKey();
            if (c.getName().startsWith("\"")) {
                xw.accumulate(c.getName());
                return xw.toString();
            }
        }
        xw.accumulate(instance.getIdentifier());
        return xw.toString();
    }
	
	
    /**
     * Returns a concise, single line version of the instance description
     * 
     * @param xw - the formatter
     * @param instance
     * @param agent
     * @return
     */
    public static String xwConcise(IXwFormatter xw, Instance instance, Agent agent) {
        xw.accumulate(instance.getIdentifier());
        xw.accumulate(" " + PrettyPrint.ppConcise(instance.getConcepts(), agent));
        return xw.toString();
    }
	
    /**
     * Returns a detailed version of the instance description which outlines the
     * composites
     * 
     * @param xw
     * @param instance
     * @param agent
     * @return
     */
    public static String xwDetailed(IXwFormatter xw, Instance instance, Agent agent) {
        Focus fc = agent.getFocus();
        xwConcise(xw, instance, agent);
        xw.add("");
        xw.indent();
        xw.add("Scene: "
                + xwConcise(xw, instance.getScene(), agent));
        xw.deindent();
        String prefix = "";
        List<VerbInstance> vis = null;
        // binary context relations, from
        vis = RelationHelper.getRelationsFrom(agent, instance, false);
        if (!vis.isEmpty()) {
            xw.add("Binary context relations from:");
            xw.indent();
            for (VerbInstance vi : vis) {
                VerbOverlay vo =
                        MetaVerbHelper.removeMetaVerbs(vi.getVerbs(), agent);
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                xw.add(prefix + "this -- " + PrettyPrint.ppConcise(vo, agent)
                        + "-->" + PrettyPrint.ppConcise(vi.getObject(), agent));
            }
            xw.deindent();
        }
        // binary context relations, to
        vis = RelationHelper.getRelationsTo(agent, instance, false);
        if (!vis.isEmpty()) {
            xw.add("Binary context relations to:");
            xw.indent();
            for (VerbInstance vi : vis) {
                VerbOverlay vo =
                        MetaVerbHelper.removeMetaVerbs(vi.getVerbs(), agent);
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                xw.add(PrettyPrint.ppConcise(vi.getSubject(), agent) + "--"
                        + PrettyPrint.ppConcise(vo, agent) + "--> this");
            }
            xw.deindent();
        }
        // print the firedShadowVis
        if (instance.isScene()) {
            xw.add("firedShadowVis");
            xw.indent();
            xw.add(PpViSet.ppConcise(instance.getSceneParameters()
                    .getFiredShadowVis(), agent));
            xw.deindent();
        }
        return xw.toString();
    }

    
}
