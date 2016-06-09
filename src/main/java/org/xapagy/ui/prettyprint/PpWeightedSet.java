package org.xapagy.ui.prettyprint;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.instances.Instance;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.WeightedSet;

public class PpWeightedSet {

    /**
     * The detailed level of printing: falls back on detailed printing of the
     * keys
     * 
     * @param wset
     * @param agent
     * @return
     */
    public static <T> String pp(WeightedSet<T> wset, Agent agent,
            PrintDetail detailLevel) {
        Formatter fmt = new Formatter();
        boolean headerDone = false;
        for (SimpleEntry<T, Double> entry : wset.getDecreasingStrengthList()) {
            T key = entry.getKey();
            if (!headerDone) {
                headerDone = true;
                String typeString = "Weighted set";
                if (key instanceof VerbInstance) {
                    typeString = "VI set";
                }
                if (key instanceof Instance) {
                    typeString = "Instance set";
                }
                fmt.add(typeString + " (" + wset.getParticipants().size()
                        + " items " + Formatter.fmt(wset.getSum()) + " sum)\n");
                fmt.indent();

            }
            double value = wset.value(key);
            fmt.addWithMarginNote(Formatter.fmt(value) + "  ",
                    PrettyPrint.pp(key, agent, detailLevel));
        }
        fmt.indent();
        return fmt.toString();
    }

    /**
     * Concise printing
     * 
     * @param wset
     * @param agent
     * @return
     */
    public static <T> String ppConcise(WeightedSet<T> wset, Agent agent) {
        return PpWeightedSet.pp(wset, agent, PrintDetail.DTL_CONCISE);
    }

    /**
     * Concise printing
     * 
     * @param wset
     * @param agent
     * @return
     */
    public static <T> String ppDetailed(WeightedSet<T> wset, Agent agent) {
        return PpWeightedSet.pp(wset, agent, PrintDetail.DTL_DETAIL);
    }

}
