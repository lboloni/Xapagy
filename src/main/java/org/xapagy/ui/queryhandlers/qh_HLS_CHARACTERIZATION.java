/*
   This file is part of the Xapagy project
   Created on: May 12, 2013
 
   org.xapagy.ui.prettyhtml.PwHlsCharacterization
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.HlsCharacterization;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * @author Ladislau Boloni
 * 
 */
public class qh_HLS_CHARACTERIZATION {

    /**
     * The detailed description, in an embeddable form - ready to be embedded in
     * another link
     * 
     * @param fmt
     * @param hlss
     * @param agent
     * @param query
     */
    public static void pwDetailed(IXwFormatter fmt, HlsCharacterization hlsc,
            Agent agent, RESTQuery query) {
        String tmp =
                PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                        hlsc.getInstance());
        fmt.addLabelParagraph("Instance: ", tmp);
        tmp =
                qh_CONCEPT_OVERLAY.pwCompact(fmt.getEmpty(), agent,
                        hlsc.getAttributes(), query);
        fmt.addLabelParagraph("To be characterized with:", tmp);
    }

}
