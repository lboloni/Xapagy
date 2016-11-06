/*
   This file is part of the Xapagy project
   Created on: Aug 30, 2010
 
   org.xapagy.ui.format.tostring.tostringXInstance
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.ui.formatters.TwFormatter;
import org.xapagy.ui.prettygeneral.xwInstance;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpInstance {

    /**
     * Returns a concise, single line version of the instance description
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static String ppConcise(Instance instance, Agent agent) {
    	return xwInstance.xwConcise(new TwFormatter(), instance, agent);
    	/*
        StringBuffer buf = new StringBuffer();
        buf.append(instance.getIdentifier());
        buf.append(" " + PrettyPrint.ppConcise(instance.getConcepts(), agent));
        return buf.toString();
        */
    }

    /**
     * Returns a detailed version of the instance description which outlines the
     * composites
     * 
     * @param instance
     * @param agent
     * @return
     */
    public static String ppDetailed(Instance instance, Agent agent) {
    	return xwInstance.xwDetailed(new TwFormatter(), instance, agent);
    	/*
        Formatter fmt = new Formatter();
        Focus fc = agent.getFocus();
        fmt.add(PpInstance.ppConcise(instance, agent));
        fmt.addIndented("Scene: "
                + PpInstance.ppConcise(instance.getScene(), agent));
        String prefix = "";
        List<VerbInstance> vis = null;
        // binary context relations, from
        vis = RelationHelper.getRelationsFrom(agent, instance, false);
        if (!vis.isEmpty()) {
            fmt.add("Binary context relations from:");
            fmt.indent();
            for (VerbInstance vi : vis) {
                VerbOverlay vo =
                        MetaVerbHelper.removeMetaVerbs(vi.getVerbs(), agent);
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                fmt.add(prefix + "this -- " + PrettyPrint.ppConcise(vo, agent)
                        + "-->" + PrettyPrint.ppConcise(vi.getObject(), agent));
            }
            fmt.deindent();
        }
        // binary context relations, to
        vis = RelationHelper.getRelationsTo(agent, instance, false);
        if (!vis.isEmpty()) {
            fmt.add("Binary context relations to:");
            fmt.indent();
            for (VerbInstance vi : vis) {
                VerbOverlay vo =
                        MetaVerbHelper.removeMetaVerbs(vi.getVerbs(), agent);
                if (fc.getSalience(vi, EnergyColors.FOCUS_VI) > 0) {
                    prefix = "";
                } else {
                    prefix = "(inactive)";
                }
                fmt.add(PrettyPrint.ppConcise(vi.getSubject(), agent) + "--"
                        + PrettyPrint.ppConcise(vo, agent) + "--> this");
            }
            fmt.deindent();
        }
        // print the firedShadowVis
        if (instance.isScene()) {
            fmt.add("firedShadowVis");
            fmt.addIndented(PpViSet.ppConcise(instance.getSceneParameters()
                    .getFiredShadowVis(), agent));
        }
        return fmt.toString();
        */
    }

    /**
     * Returns a concise, single line version of the instance description
     * 
     * @param instance
     * @param detailLevel
     * @param agent
     * @return
     */
    public static String ppSuperConcise(Instance instance, Agent agent) {
    	return xwInstance.xwSuperConcise(new TwFormatter(), instance, agent);
    	/*
        for (SimpleEntry<Concept, Double> entry : instance.getConcepts()
                .getList()) {
            Concept c = entry.getKey();
            if (c.getName().startsWith("\"")) {
                return c.getName();
            }
        }
        return instance.getIdentifier();
        */
    }
}
