/*
   This file is part of the Xapagy project
   Created on: Apr 5, 2012
 
   org.xapagy.ui.prettygraphviz.pgvVerbInstance
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettygraphviz;

import java.util.HashSet;
import java.util.Set;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViClassifier;
import org.xapagy.instances.ViClassifier.ViClass;
import org.xapagy.instances.ViStructureHelper.ViType;
import org.xapagy.set.ViSet;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * @author Ladislau Boloni
 * 
 */
public class pgvVerbInstance {

    /**
     * Links between instances: succession, coincidence, summarization
     * 
     * @param instance
     * @param agent
     * @param fmt
     * @param param
     * @return the set of the verb instances referred - these will need to be
     *         created!
     */
    public static Set<VerbInstance> pgvLinks(VerbInstance vi, Agent agent,
            GvFormatter fmt, GvParameters param) {
        Set<VerbInstance> retval = new HashSet<>();
        ViSet visSucc = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_SUCCESSOR);
        // only print the strongest successor
        if (!visSucc.isEmpty()) {
            VerbInstance viSucc =
                    visSucc.getDecreasingStrengthList().get(0).getKey();
            fmt.openLink(vi.getIdentifier(), viSucc.getIdentifier());
            fmt.is(GvParameters.COLOR, param.link_color_successor);
            fmt.close();
            retval.add(viSucc);
        }
        retval.addAll(pgvLinkClass(Hardwired.LINK_SUMMARIZATION_BEGIN,
                param.link_color_summarization_begin, vi, agent, fmt, param));
        retval.addAll(pgvLinkClass(Hardwired.LINK_SUMMARIZATION_BODY,
                param.link_color_summarization_body, vi, agent, fmt, param));
        retval.addAll(pgvLinkClass(Hardwired.LINK_SUMMARIZATION_CLOSE,
                param.link_color_summarization_close, vi, agent, fmt, param));

        
        // print links of a certain type
        /*
        ViSet elaborations = vl.getLink(ViLinkDB.ELABORATION, vi);
        for (VerbInstance elab : elaborations.getParticipants()) {
            fmt.openLink(vi.getIdentifier(), elab.getIdentifier());
            fmt.is(GvParameters.COLOR, param.link_color_summarization);
            fmt.close();
            retval.add(elab);
        }
        */
        // print the summarization-begin links

        // if the vi is a quote, link to the stuff
        if (vi.getViType().equals(ViType.QUOTE)) {
            fmt.openLink(vi.getIdentifier(), vi.getQuote().getIdentifier());
            fmt.is(GvParameters.COLOR, param.link_color_quote);
            fmt.is(GvParameters.WEIGHT, param.link_weight_quote);
            fmt.is(GvParameters.PENWIDTH, param.link_penwidth_quote);
            fmt.close();
            // TextUi.println("Adding referred quote:"
            // + vi.getQuote().getIdentifier());
            retval.add(vi.getQuote());
        }
        return retval;
    }

    /**
     * Creates the links of a certain type
     * 
     * @return
     */
    private static Set<VerbInstance> pgvLinkClass(String linkType,
            String color, VerbInstance vi, Agent agent, GvFormatter fmt,
            GvParameters param) {
        Set<VerbInstance> retval = new HashSet<>();
        ViSet elaborations = agent.getLinks().getLinksByLinkName(vi, linkType);
        for (VerbInstance elab : elaborations.getParticipants()) {
            fmt.openLink(vi.getIdentifier(), elab.getIdentifier());
            fmt.is(GvParameters.COLOR, color);
            fmt.close();
            retval.add(elab);
        }
        return retval;
    }

    /**
     * Creates the graphviz node corresponding to a VI.
     * 
     * Returns true if the node has been actually created, and false if it was
     * not.
     * 
     * @param instance
     * @param agent
     * @param fmt
     * @param param
     */
    public static boolean pgvNode(VerbInstance vi, Agent agent,
            GvFormatter fmt, GvParameters param) {
        // all the relations are handled differently
        if (ViClassifier.decideViClass(ViClass.RELATION, vi, agent)) {
            return false;
        }
        // action vi
        if (ViClassifier.decideViClass(ViClass.ACTION, vi, agent)) {
            String label = XapiPrint.ppsViXapiForm(vi, agent);
            label = GraphVizHelper.wrapLabel(label, param.vi_wrap);
            // label = TextUiHelper.wrap(label, param.vi_wrap);
            // label = label.replaceAll("\n", "\\\\n");
            fmt.openNode(vi.getIdentifier());
            fmt.is(GvParameters.SHAPE, param.vi_shape);
            fmt.is(GvParameters.STYLE, param.vi_style);
            fmt.is(GvParameters.FILLCOLOR, param.vi_fillcolor_action);
            fmt.is(GvParameters.FONTNAME, param.fontname);
            fmt.is(GvParameters.FONTSIZE, "" + param.fontsize);
            fmt.label(label);
            fmt.close();
            return true;
        }
        // create-other types
        String label = XapiPrint.ppsViXapiForm(vi, agent);
        label = GraphVizHelper.wrapLabel(label, param.vi_wrap);
        // label = TextUiHelper.wrap(label, param.vi_wrap);
        // label = label.replaceAll("\n", "\\\\n");
        fmt.openNode(vi.getIdentifier());
        fmt.is(GvParameters.SHAPE, param.vi_shape);
        fmt.is(GvParameters.STYLE, param.vi_style);
        fmt.is(GvParameters.FILLCOLOR, param.vi_fillcolor_other);
        fmt.is(GvParameters.FONTNAME, param.fontname);
        fmt.is(GvParameters.FONTSIZE, "" + param.fontsize);
        fmt.label(label);
        fmt.close();
        return true;
    }

}
