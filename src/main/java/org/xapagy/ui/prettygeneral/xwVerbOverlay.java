package org.xapagy.ui.prettygeneral;

import java.util.List;
import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.AbstractConceptDB;
import org.xapagy.concepts.Hardwired;
import org.xapagy.concepts.Verb;
import org.xapagy.concepts.VerbOverlay;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.prettyprint.PrettyPrint;

public class xwVerbOverlay {
    /**
     * @param verbs
     * @return
     */
    public static String getLabels(VerbOverlay vo) {
        Formatter fmt = new Formatter();
        List<String> labels = vo.getLabels();
        if (labels == null) {
            fmt.add("No labels");
        } else {
            fmt.add("Labels:");
            fmt.indent();
            for (String label : labels) {
                fmt.add(label);
            }
        }
        return fmt.toString();
    }

    /**
     * Formats a verb overlay in a short, concise format. Collapses the meta
     * verbs (succ, pred) into markers for action verbs etc.
     * 
     * @param xw
     * @param vo
     * @param agent
     * @return
     */
    public static String xwConcise(IXwFormatter xw, VerbOverlay vo, Agent agent) {
        return xwOverlay.xwConcise(xw, vo, agent);
    }

    /**
     * Formats a verb overlay in a detailed format
     * @param xw
     * @param vo
     * @param agent
     * @return
     */
    public static String xwDetailed(IXwFormatter xw, VerbOverlay vo, Agent agent) {
        xw.add("VerbOverlay");
        xw.indent();
        xwOverlay.xwDetailed(xw, vo, agent);
        // labels
        xw.add(getLabels(vo));
        return xw.toString();
    }

    /**
     * Prettily printed, intelligent extraction of the label of a relation. At
     * the same time, it verifies that this is a relation, and throws errors if
     * it is not.
     * 
     * @param vo
     *            - the VO of the relation
     * @param agent
     * @return
     */
    public static String ppRelationLabel(VerbOverlay vo, Agent agent) {
        AbstractConceptDB<Verb> cdb = agent.getVerbDB();
        String retval = "";
        for (SimpleEntry<Verb, Double> entry : vo.getSortedByExplicitEnergy()) {
            Verb verb = entry.getKey();
            if (verb.getName().equals(Hardwired.VM_RELATION_MARKER)) {
                continue;
            }
            // make sure it overlaps with relation
            double overlap =
                    cdb.getOverlap(verb, cdb.getConcept(Hardwired.VMC_RELATION));
            if (overlap == 0) {
                throw new Error("Verb which does not overlap with relation:"
                        + verb.getName());
            }
            String temp = verb.getName();
            temp = temp.substring(3);
            if (!retval.equals("")) {
                retval += " ";
            }
            retval += temp;
        }
        // if (!hasRelationMarker) {
        // throw new
        // Error("This supposed relation VO does not have a relation marker");
        // }
        if (retval.equals("")) {
            throw new Error(
                    "There was no relation in this supposed relation!!!"
                            + PrettyPrint.ppConcise(vo, agent));
        }
        return retval;
    }

    /**
     * Formats a verb overlay in a short, concise format. Collapses the meta
     * verbs (succ, pred) into markers for action verbs etc.
     * 
     * @param verbs
     * @return
     */
    public static String xwVeryConcise(IXwFormatter xw, VerbOverlay verbs, Agent agent) {
        StringBuffer buf = new StringBuffer();
        for (SimpleEntry<Verb, Double> entry : verbs
                .getSortedByExplicitEnergy()) {
            Verb verb = entry.getKey();
            buf.append(verb.getName());
            buf.append(", ");
        }
        if (buf.length() != 0) {
            buf.delete(buf.length() - 2, buf.length());
        }
        xw.accumulate(buf.toString());
        return xw.toString();
    }
}
