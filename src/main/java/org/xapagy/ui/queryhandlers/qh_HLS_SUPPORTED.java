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
package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.AbstractHls;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.FslType;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.formatters.Formatter;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.PwViTemplate;
import org.xapagy.util.SimpleEntryComparator;

public class qh_HLS_SUPPORTED implements IQueryHandler, IQueryAttributes {

    
    /**
     * Generates the main page for the HLSs
     * 
     * @param fmt
     * @param agent
     * @param query
     * @param session
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        Hls hlss = null;
        for (Hls test : agent.getHeadlessComponents().getHlss()) {
            if (test.getIdentifier().equals(identifier)) {
                hlss = test;
                break;
            }
        }
        if (hlss == null) {
            String pp =
                    "Hls with identifier " + identifier
                            + " could not be found!";
            fmt.addErrorMessage(pp);
            return;
        }
        String redheader = "Hls" + fmt.getEmpty().addIdentifier(hlss);
        fmt.addH2(redheader, "class=identifier");
        qh_HLS_SUPPORTED.pwDetailed(fmt, hlss, agent, query, true);
    }

    /**
     * Returns a list of FSLI / value pairs for a given ViLinked, sorted by
     * decreasing weight - only used for printing
     * 
     * @param vir
     * @return
     */
    private static List<SimpleEntry<FslInterpretation, Double>>
            getSupportsForViLinkedSortedBySupport(Hls hls, VerbInstance vir,
                    Agent agent) {
        List<SimpleEntry<FslInterpretation, Double>> retval = new ArrayList<>();
        for (FslInterpretation fsli : hls.getSupportsForViLinked(vir)) {
            SimpleEntry<FslInterpretation, Double> entry =
                    new SimpleEntry<>(fsli, fsli.getTotalSupport(agent));
            retval.add(entry);
        }
        Collections
                .sort(retval, new SimpleEntryComparator<FslInterpretation>());
        Collections.reverse(retval);
        return retval;
    }

    /**
     * Returns all the VI relatives in the decreasing order of the weight, which
     * is the sum of supports of all kinds together. Note that this is not the
     * same as the weight for one particular algorithm or another.
     * 
     * 
     * @return
     */
    private static List<SimpleEntry<VerbInstance, Double>>
            getViLinkedSortedByWeight(Hls hls, Agent agent) {
        List<SimpleEntry<VerbInstance, Double>> sortedListVirs =
                new ArrayList<>();
        for (VerbInstance vir : hls.getViLinkeds()) {
            double weight = 0;
            for (FslInterpretation fsli : hls.getSupportsForViLinked(vir)) {
                weight = weight + fsli.getTotalSupport(agent);
            }
            sortedListVirs.add(new SimpleEntry<>(vir, weight));
        }
        Collections.sort(sortedListVirs,
                new SimpleEntryComparator<VerbInstance>());
        Collections.reverse(sortedListVirs);
        return sortedListVirs;
    }

    /**
     * Very concise printing of a Hls supported, to be used from the link
     * 
     * @param hls
     * @param agent
     * @return
     */
    public static String pwConcise(Hls hls, Agent agent) {
        PwFormatter fmt = new PwFormatter();
        PwViTemplate.pwConcise(fmt, hls.getViTemplate(), agent);
        fmt.add(" Deps: " + hls.getDependencies().size());
        fmt.add(" Supports: " + hls.getSupports().size());
        fmt.addIdentifier(hls);
        return fmt.toString();
    }

    /**
     * Formats the template of the HLS. This can be called from outside, because
     * in the case of choices this is moved to the top of the printing.
     * 
     * @param fmt
     * @param hls
     * @param agent
     * @param query
     * @param explanation - additional explanation which explains what is going on
     */
    public static void pwHlsViTemplate(IXwFormatter fmt, AbstractHls hls, Agent agent,
            RESTQuery query, String explanation) {
        fmt.addH2("VI template");
        if (explanation != null) {
            fmt.explanatoryNote(explanation);
        }
        PwViTemplate.xwDetailed(fmt, hls.getViTemplate(), agent, query);
        // the current VI template had filled in some missing parts, show the
        // original
        if (hls.getViTemplateOriginal().getMissingParts().size() > hls
                .getViTemplate().getMissingParts().size()) {
            fmt.addBold("original template, some of the missing parts had been since filled:");
            fmt.indent();
            PwViTemplate.xwDetailed(fmt, hls.getViTemplateOriginal(), agent,
                    query);
            fmt.deindent();
        }
    }

    /**
     * The detailed description, in an embeddable form - ready to be embedded in
     * another link
     * 
     * @param fmt
     * @param hls
     * @param agent
     * @param query
     * @param printTemplate
     *            - if true, it prints the template as well. It is not necessary
     *            for the case when we are embedding in the printing of the
     *            Choice, because the template is printed at the top.
     */
    public static void pwDetailed(IXwFormatter fmt, Hls hls, Agent agent,
            RESTQuery query, boolean printTemplate) {
        if (printTemplate) {
            pwHlsViTemplate(fmt, hls, agent, query, null);
        }
        //
        // The score, according to different algorithms
        //
        fmt.addH2("Scores");
        qh_HLS_SUPPORTED.pwScores(fmt, hls, agent);
        //
        // The dependencies
        //
        if (!hls.getDependencies().isEmpty()) {
            fmt.addH2("Dependencies:" + hls.getDependencies().size()
                    + " instances must be created.");
            fmt.explanatoryNote("Links to HlsNewInstances which must be instantiated, before this HlsSupported can be instantiated.");
            for (ViPart part : hls.getDependencies().keySet()) {
                HlsNewInstance hlsni = hls.getDependencies().get(part);
                fmt.openP();
                PwQueryLinks.linkToHlsNewInstance(fmt, agent, query, hlsni);
                fmt.closeP();
            }
            // fmt.deindent();
        } else {
            fmt.addH2("Dependencies: none, all instances are already there");
        }
        if (!hls.getResolvedDependencies().isEmpty()) {
            for (ViPart part : hls.getResolvedDependencies().keySet()) {
                HlsNewInstance hlsni = hls.getResolvedDependencies().get(part);
                fmt.openP();
                PwQueryLinks.linkToHlsNewInstance(fmt, agent, query, hlsni);
                fmt.closeP();
            }
            // fmt.deindent();
        } else {
            fmt.addP("No resolved dependencies");
        }
        //
        // The supports
        //
        fmt.addH2("Supports");
        qh_HLS_SUPPORTED.pwSupports(fmt, hls, agent, query);
    }

    /**
     * The list of the scores of the HlsSupported
     * 
     * @param fmt
     * @param hlss
     * @param agent
     * @param query
     */
    private static void pwScores(IXwFormatter fmt, Hls hls, Agent agent) {
        // the supports calculated according to different algorithms
        for (FslType alg : FslType.values()) {
            double value = hls.summativeSupport(alg, agent).getValue();
            if (value > 0.0) {
                fmt.is(alg.toString().toLowerCase(), value);
            } else {
                fmt.grayoutStart();
                fmt.is(alg.toString().toLowerCase(), value);
                fmt.grayoutEnd();
            }
        }
    }

    /**
     * Generates the list of supports of the HLS (which are FslInterpretation
     * objects) and links to them
     * 
     * @param fmt
     * @param hls
     * @param agent
     * @param query
     */
    private static void pwSupports(IXwFormatter fmt, Hls hls, Agent agent,
            RESTQuery query) {
        fmt.explanatoryNote("Support strength / link to the supporting FSLIs, grouped by Linked");
        // organize stuff based on the ViLinkeds
        for (SimpleEntry<VerbInstance, Double> entry : qh_HLS_SUPPORTED
                .getViLinkedSortedByWeight(hls, agent)) {
            VerbInstance vir = entry.getKey();
            String label = PwQueryLinks.linkToVi(fmt.getEmpty(), agent, query, vir);
            label += " total weight " + Formatter.fmt(entry.getValue());
            fmt.addH3(label);
            // now add the FSLIs, sorted
            fmt.indent();
            for (SimpleEntry<FslInterpretation, Double> entry2 : qh_HLS_SUPPORTED
                    .getSupportsForViLinkedSortedBySupport(hls, vir, agent)) {
                FslInterpretation fsli = entry2.getKey();
                double value = entry2.getValue();
                fmt.openP();
                fmt.progressBar(value, 1.0);
                PwQueryLinks.linkToFsli("shadow", fmt, agent, query, fsli);
                fmt.closeP();
            }
            fmt.deindent();
        }
    }

}
