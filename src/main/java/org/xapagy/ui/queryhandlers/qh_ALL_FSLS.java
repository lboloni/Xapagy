package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslComparator;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.smartprint.XapiPrint;
import org.xapagy.util.SimpleEntryComparator;

public class qh_ALL_FSLS implements IQueryHandler {

    /**
     * Creates a printing of the FSLs by linked
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    private static void addFslsByLinked(PwFormatter fmt, Agent agent,
            RESTQuery query) {
        int idCount = 0;
        HeadlessComponents hlc = agent.getHeadlessComponents();
        //
        // Extract all the possible relatives
        //
        Map<VerbInstance, Double> linkeds = new HashMap<>();
        for (FocusShadowLinked fsl : hlc.getFsls()) {
            Double d = linkeds.get(fsl.getViLinked());
            if (d == null) {
                linkeds.put(fsl.getViLinked(), fsl.getTotalSupport(agent));
            } else {
                double value;
                if (fsl.getFslType() != FslType.IN_SHADOW) {
                    value = d + fsl.getTotalSupport(agent);
                } else {
                    value = d - fsl.getTotalSupport(agent);
                }
                linkeds.put(fsl.getViLinked(), value);
            }
        }
        //
        // Sort them in the decreasing order of total support
        //
        List<SimpleEntry<VerbInstance, Double>> list = new ArrayList<>();
        for (VerbInstance vi : linkeds.keySet()) {
            list.add(new SimpleEntry<>(vi, linkeds.get(vi)));
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
            List<FocusShadowLinked> sublist = new ArrayList<>();
            for (FocusShadowLinked fsl : hlc.getFsls()) {
                if (fsl.getViLinked().equals(vi)) {
                    sublist.add(fsl);
                }
            }
            Collections.sort(sublist, new FslComparator(agent));
            Collections.reverse(sublist);
            PwFormatter fmtLocal = fmt.getEmpty();
            fmtLocal.indent();
            for (FocusShadowLinked fsl : sublist) {
                double totalSupport = fsl.getTotalSupport(agent);
                fmtLocal.openP();
                if (fsl.getFslType() != FslType.IN_SHADOW) {
                    fmtLocal.progressBar(totalSupport, 1.0);
                } else {
                    fmtLocal.progressBar(-totalSupport, -1.0);
                }
                PwQueryLinks.linkToFsl(fmtLocal, agent, query, fsl);
                fmtLocal.closeP();
            }
            fmtLocal.deindent();
            fmt.progressBar(entry.getValue(), maxValue);
            fmt.addBold(XapiPrint.ppsViXapiForm(vi, agent));
            fmt.add(" (" + sublist.size() + " items)");
            fmt.addExtensible("fslbylinked" + idCount++, "expand>> ",
                    "<< hide", fmtLocal.toString(), false);
            fmt.add("<br/>");
        }
    }

    /**
     * Add the FSLs of a type, into a collapsible link.
     * 
     * @param type
     * @param fmt
     * @param agent
     * @param query
     */
    private static void addFslsOfAType(String label, FslType type,
            PwFormatter fmt, Agent agent, RESTQuery query, String id) {
        PwFormatter fmtLocal = fmt.getEmpty();
        @SuppressWarnings("unused")
        double sumSupport = 0; // will show the total support in this direction
        // fmt.addH2(label);
        HeadlessComponents hlc = agent.getHeadlessComponents();
        List<FocusShadowLinked> fsls = new ArrayList<>();
        for (FocusShadowLinked fsl : hlc.getFsls()) {
            if (fsl.getFslType() == type) {
                fsls.add(fsl);
            }
        }
        Comparator<FocusShadowLinked> comp = new FslComparator(agent);
        Collections.sort(fsls, comp);
        Collections.reverse(fsls);
        for (FocusShadowLinked fsl : fsls) {
            double totalSupport = fsl.getTotalSupport(agent);
            sumSupport += totalSupport;
            fmtLocal.openP();
            fmtLocal.progressBar(totalSupport, 1.0);
            PwQueryLinks.linkToFsl(fmtLocal, agent, query, fsl);
            fmtLocal.closeP();
        }
        fmt.addExtensibleH3(id, label + " (" + fsls.size() + " items)", fmtLocal.toString(), false);
    }

    /**
     * Generates the page with all the FSLs
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        int idCount = 0;
        //
        // The FSLs grouped by the relative component
        //
        fmt.addH2("FocusShadowLinked components grouped by ViLinked",
                "class=identifier");
        fmt.explanatoryNote("The FSLs are sorted by common ViLinked. Then the ViLinked groups are grouped according "
                + "to an algorithm sum of all supports - support of IN_SHADOW type. Note that this is only a vague indicator of what "
                + "the HLS algorithm will do with these.");
        qh_ALL_FSLS.addFslsByLinked(fmt, agent, query);
        //
        // The FSLs organized by types
        //
        fmt.addH2("FocusShadowLinked components grouped by type", "class=identifier");
        qh_ALL_FSLS.addFslsOfAType("Successor", FslType.SUCCESSOR, fmt, agent,
                query, "id" + idCount++);
        qh_ALL_FSLS.addFslsOfAType("Predecessor", FslType.PREDECESSOR, fmt,
                agent, query, "id" + idCount++);
        qh_ALL_FSLS.addFslsOfAType("Context", FslType.CONTEXT, fmt, agent, query,
                "id" + idCount++);
        qh_ALL_FSLS.addFslsOfAType("Context implication",
                FslType.CONTEXT_IMPLICATION, fmt, agent, query, "id"
                        + idCount++);
        qh_ALL_FSLS.addFslsOfAType("Question", FslType.QUESTION, fmt, agent,
                query, "id" + idCount++);
        qh_ALL_FSLS.addFslsOfAType("Answer", FslType.ANSWER, fmt, agent, query,
                "id" + idCount++);
        qh_ALL_FSLS.addFslsOfAType("Coincidence", FslType.COINCIDENCE, fmt,
                agent, query, "id" + idCount++);
        qh_ALL_FSLS.addFslsOfAType("In-Shadow", FslType.IN_SHADOW, fmt, agent,
                query, "id" + idCount++);
    }

}
