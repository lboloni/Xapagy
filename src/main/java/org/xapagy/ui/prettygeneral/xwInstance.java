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
     * @param fmt - the formatter
     * @param instance
     * @param agent
     * @return
     */
    public static String xwConcise(IXwFormatter fmt, Instance instance, Agent agent) {
        fmt.accumulate(instance.getIdentifier());
        fmt.accumulate(" " + PrettyPrint.ppConcise(instance.getConcepts(), agent));
        return fmt.toString();
    }
	
    /**
     * Returns a detailed version of the instance description which outlines the
     * composites
     * 
     * @param fmt 
     * @param instance
     * @param agent
     * @return
     */
    public static String xwDetailed(IXwFormatter fmt, Instance instance, Agent agent) {
        Focus fc = agent.getFocus();
        xwConcise(fmt, instance, agent);
        fmt.add("");
        fmt.indent();
        fmt.add("Scene: "
                + xwConcise(fmt, instance.getScene(), agent));
        fmt.deindent();
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
            fmt.indent();
            fmt.add(PpViSet.ppConcise(instance.getSceneParameters()
                    .getFiredShadowVis(), agent));
            fmt.deindent();
        }
        return fmt.toString();
    }

    
}
