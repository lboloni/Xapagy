package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.FsliComparator;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.parameters.Parameters;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.smartprint.SpFocus;
import org.xapagy.util.SimpleEntryComparator;

/**
 * This creates a page from which all the currently active FSLIs can
 * be inspected. 
 * 
 * @author Lotzi Boloni
 * 
 */
public class qh_ALL_FSLIS implements IQueryHandler {

    /**
     * If it is set to true, cut off those which are irrelevantly weak
     */
    public static boolean cutOffIrrelevant = true;

    /**
     * Creates a printing of the FSLIs by ViLinked
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    private static void addFslisByViLinked(PwFormatter fmt, Agent agent,
            RESTQuery query) {
        int idCount = 0;
        HeadlessComponents hlc = agent.getHeadlessComponents();
        //
        // Extract all the possible ViLinkeds
        //
        Map<VerbInstance, Double> relatives = new HashMap<>();
        for (FslInterpretation fsli : hlc.getFslis()) {
            Double d = relatives.get(fsli.getFsl().getViLinked());
            if (d == null) {
                relatives.put(fsli.getFsl().getViLinked(),
                        fsli.getTotalSupport(agent));
            } else {
                double value = 0;
                if (fsli.getFsl().getFslType() != FslType.IN_SHADOW) {
                    value = d + fsli.getTotalSupport(agent);
                } else {
                    value = d - fsli.getTotalSupport(agent);
                }
                relatives.put(fsli.getFsl().getViLinked(), value);
            }
        }
        //
        // Sort them in the decreasing order of total support
        //
        List<SimpleEntry<VerbInstance, Double>> list = new ArrayList<>();
        for (VerbInstance vi : relatives.keySet()) {
            list.add(new SimpleEntry<>(vi, relatives.get(vi)));
        }
        Collections.sort(list, new SimpleEntryComparator<VerbInstance>());
        Collections.reverse(list);
        if (list.isEmpty()) {
            return;
        }
        double maxValue = list.get(0).getValue();
        //
        // Now, create the subsets
        //
        for (SimpleEntry<VerbInstance, Double> entry : list) {
            VerbInstance vi = entry.getKey();
            // Create the sublist, sorted by total value
            List<FslInterpretation> sublist = new ArrayList<>();
            for (FslInterpretation fsli : hlc.getFslis()) {
                if (fsli.getFsl().getViLinked().equals(vi)) {
                    sublist.add(fsli);
                }
            }
            Collections.sort(sublist, new FsliComparator(agent));
            Collections.reverse(sublist);
            PwFormatter fmtLocal = fmt.getEmpty();
            fmtLocal.indent();
            for (FslInterpretation fsli : sublist) {
                double totalSupport = fsli.getTotalSupport(agent);
                fmtLocal.openP();
                if (fsli.getFsl().getFslType() != FslType.IN_SHADOW) {
                    fmtLocal.progressBar(totalSupport, 1.0);
                } else {
                    fmtLocal.progressBar(-totalSupport, -1.0);
                }
                PwQueryLinks.linkToFsli("interpretation", fmtLocal, agent, query,
                        fsli);
                fmtLocal.closeP();
            }
            fmtLocal.deindent();
            fmt.progressBar(entry.getValue(), maxValue);
            fmt.addBold(SpFocus.ppsViXapiForm(vi, agent));
            fmt.addExtensible("fslibylinked" + idCount++, "expand>> ",
                    "<< hide", fmtLocal.toString(), false);
            fmt.add("<br/>");
        }
    }

    /**
     * Adds all the FSLIs of a given type, sorted
     * 
     * @param string
     * @param fslType
     * @param fmt
     * @param agent
     * @param query
     */
    private static void addFslisOfAType(String string, FslType fslType,
            PwFormatter fmt, Agent agent, RESTQuery query, String id) {
        PwFormatter fmtLocal = fmt.getEmpty();
        HeadlessComponents hlc = agent.getHeadlessComponents();
        List<FslInterpretation> list = new ArrayList<>();
        for (FslInterpretation fsli : hlc.getFslis()) {
            if (fsli.getFsl().getFslType().equals(fslType)) {
                list.add(fsli);
            }
        }
        Collections.sort(list, new FsliComparator(agent));
        Collections.reverse(list);
        for (FslInterpretation svir : list) {
            fmtLocal.openP();
            fmtLocal.progressBar(svir.getTotalSupport(agent), 1.0);
            fmtLocal.add("--");
            fmtLocal.progressBar(svir.getSupportFraction(), 1.0);
            PwQueryLinks.linkToFsli("interpretation", fmtLocal, agent, query, svir);
            fmtLocal.closeP();
        }
        fmt.addExtensibleH3(id, string, fmtLocal.toString(), false);
    }

    /**
     * The main function, generates the page on the formatter.
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        int idCount = 0;
        Parameters p = agent.getParameters();
        //
        // List of FSLIs, grouped by the list of ViLinked
        //
        fmt.addH2("FslInterpretation components - by ViLinked",
                "class=identifier");
        fmt.explanatoryNote("The FSLIs are grouped by common ViLinked. Then the ViLinked groups are sorted according "
                + "to an algorithm sum of all supports - support of IN_SHADOW type. Note that this is only a vague indicator of what "
                + "the HLS algorithm will do with these.");

        qh_ALL_FSLIS.addFslisByViLinked(fmt, agent, query);
        //
        // List of FSLIs, sorted by support
        //
        if (!qh_ALL_FSLIS.cutOffIrrelevant) {
            fmt.addH2("All currently active FslInterpretation objects",
                    "class=identifier");
        } else {
            fmt.addH2(
                    "All FslInterpretation objects with relevant strength (> "
                            + Formatter.fmt(p.get("A_HLSM",
                                    "G_GENERAL",
                                    "N_MINIMUM_FSL_STRENGTH"))
                            + ")", "class=identifier");
        }
        fmt.explanatoryNote("Total support / Support fraction / link to FSLFSLI");
        qh_ALL_FSLIS.addFslisOfAType("Successor", FslType.SUCCESSOR, fmt, agent,
                query, "id" + idCount++);
        qh_ALL_FSLIS.addFslisOfAType("Predecessor", FslType.PREDECESSOR, fmt,
                agent, query, "id" + idCount++);
        qh_ALL_FSLIS.addFslisOfAType("Context", FslType.CONTEXT, fmt, agent,
                query, "id" + idCount++);
        qh_ALL_FSLIS.addFslisOfAType("Context implication",
                FslType.CONTEXT_IMPLICATION, fmt, agent, query, "id"
                        + idCount++);
        qh_ALL_FSLIS.addFslisOfAType("Question", FslType.QUESTION, fmt, agent,
                query, "id" + idCount++);
        qh_ALL_FSLIS.addFslisOfAType("Answer", FslType.ANSWER, fmt, agent, query,
                "id" + idCount++);
        qh_ALL_FSLIS.addFslisOfAType("Coincidence", FslType.COINCIDENCE, fmt,
                agent, query, "id" + idCount++);
        qh_ALL_FSLIS.addFslisOfAType("In-Shadow", FslType.IN_SHADOW, fmt, agent,
                query, "id" + idCount++);
    }

}
