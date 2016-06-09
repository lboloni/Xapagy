/*
   This file is part of the Xapagy project
   Created on: Sep 4, 2014
 
   org.xapagy.ui.prettyhtml.PwSOSP
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.SOSP;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.smartprint.SpInstance;

/**
 * Functions which generate a visualization for an SOSP
 * 
 * @author Ladislau Boloni
 *
 */
public class qh_SOSP implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query,
            Session session) {
        //
        // Identify the SOSP we are referring too
        //
        String identifier = query.getAttribute(Q_ID);
        SOSP sosp = null;
        for (SOSP temp : agent.getHeadlessComponents().getSOSPs()) {
            if (temp.getIdentifier().equals(identifier)) {
                sosp = temp;
                break;
            }
        }
        if (sosp == null) {
            fmt.addErrorMessage("Could not find SOSP with identifier "
                    + identifier);
            return;
        }
        //
        // Header
        //
        String redheader = "SOSP " + sosp.getIdentifier();
        fmt.addH2(redheader, "class=identifier");
        xwSOSP(sosp, agent, query, fmt);
        fmt.explanatoryNote("visualization of SOSP not implemented yet");
    }

    /**
     * Formatting of a SOSP object to an xw formatter
     * 
     * @param sosp
     * @param agent
     * @param query
     * @param fmt
     */
    public static void xwSOSP(SOSP sosp, Agent agent, RESTQuery query,
            IXwFormatter fmt) {
        fmt.is("Identifier", sosp.getIdentifier());
        if (sosp.isSV()) {
            fmt.is("Type", "Subject-Verb");
        } else {
            fmt.is("Type", "Subject-Verb-Object");
        }
        fmt.is("Score", sosp.getScore());
        //
        // the focus values
        //
        String tmp =
                PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                        sosp.getFiSubject());
        fmt.addLabelParagraph("Focus subject", tmp);
        if (!sosp.isSV()) {
            tmp =
                    PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                            sosp.getFiObject());
            fmt.addLabelParagraph("Focus object", tmp);
        }
        //
        // the shadow values
        //
        tmp =
                PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                        sosp.getShadowScene());
        fmt.addLabelParagraph("Shadow scene", tmp);
        tmp =
                PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                        sosp.getSiSubject());
        fmt.addLabelParagraph("Shadow subject", tmp);
        if (!sosp.isSV()) {
            tmp =
                    PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                            sosp.getFiObject());
            fmt.addLabelParagraph("Shadow object", tmp);
        }
    }

    /**
     * Currently used in Link
     * 
     * @param agent
     * @param sosp
     * @return
     */
    public static String concise(Agent agent, SOSP sosp) {
        StringBuffer buf = new StringBuffer();
        if (!sosp.isSV()) {
            buf.append("SOSP: S-V-O");
            buf.append("F: " + SpInstance.spc(sosp.getFiSubject(), agent)
                    + " - " + SpInstance.spc(sosp.getFiObject(), agent));
            buf.append(" S: " + SpInstance.spc(sosp.getSiSubject(), agent)
                    + " - " + SpInstance.spc(sosp.getSiObject(), agent));
        } else {
            buf.append("SOSP: S-V-O");
            buf.append("F: " + SpInstance.spc(sosp.getFiSubject(), agent));
            buf.append(" S: " + SpInstance.spc(sosp.getSiSubject(), agent));
        }
        return buf.toString();
    }


}
