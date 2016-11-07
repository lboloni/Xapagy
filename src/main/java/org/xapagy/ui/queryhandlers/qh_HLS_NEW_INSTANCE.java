package org.xapagy.ui.queryhandlers;

import java.util.AbstractMap.SimpleEntry;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.Hls;
import org.xapagy.headless_shadows.HlsNewInstance;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.ViStructureHelper.ViPart;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwConceptOverlay;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.smartprint.SpFocus;

public class qh_HLS_NEW_INSTANCE implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        HlsNewInstance hlsni = null;
        for (HlsNewInstance test : agent.getHeadlessComponents()
                .getHlsNewInstances()) {
            if (test.getIdentifier().equals(identifier)) {
                hlsni = test;
                break;
            }
        }
        // add a red header, see what are we doing
        String redheader = "HlsNewInstance id = " + identifier;
        fmt.addH2(redheader, "class=identifier");
        if (hlsni == null) {
            fmt.addPre("HlsNewInstance could not be found!");
            return;
        }
        fmt.explanatoryNote("A HlsNewInstance component is the possibility to create a new"
                + " instance in a given scene. HlsNewInstance objects are pre-requisites"
                + " to the instantiation of certain HlsSupported-s (and the choices built on them)");

        fmt.add(qh_HLS_NEW_INSTANCE.pwDetailed(hlsni, query, agent));
    }

    /**
     * Printing a concise one liner for a HlsNewInstance object - to be used
     * from the PwHelper link
     * 
     * @param hlsni
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String pwConcise(HlsNewInstance hlsni, Agent agent) {
        PwFormatter fmt = new PwFormatter();
        fmt.addBold("HlsNewInstance ");
        if (hlsni.isResolved()) {
            fmt.add(" (resolved) ");
        }
        fmt.add("Scene: " + SpFocus.ppsScene(hlsni.getScene(), agent, false));
        fmt.add("Attributes: "
                + xwConceptOverlay.xwConcise(fmt.getEmpty(), hlsni.getAttributes(), agent));
        fmt.add("No. of supports: " + hlsni.getSupports().size());
        fmt.addIdentifier(hlsni);
        return fmt.toString();
    }

    /**
     * Detailed printing of the HlsNewInstance
     * 
     * @param hlsni
     * @param agent
     * @param detailLevel
     * @return
     */
    public static String pwDetailed(HlsNewInstance hlsni, RESTQuery query,
            Agent agent) {
        PwFormatter fmt = new PwFormatter();
        fmt.is("Identifier:", hlsni.getIdentifier());
        fmt.is("Scene:",
                PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                        hlsni.getScene()));
        fmt.is("Attributes:",
                xwConceptOverlay.xwConcise(fmt.getEmpty(), hlsni.getAttributes(), agent));
        fmt.is("Resolved", hlsni.isResolved());
        if (hlsni.isResolved()) {
            fmt.is("Resolved to: ", PwQueryLinks.linkToInstance(new PwFormatter(),
                    agent, query, hlsni.getResolvedInstance()));
        }
        // supports
        fmt.addLabelParagraph("Supports:");
        fmt.indent();
        for (SimpleEntry<Hls, ViPart> entry : hlsni.getSupports()) {
            ViPart part = entry.getValue();
            Hls hls = entry.getKey();
            fmt.openP();
            fmt.addBold(part.toString() + " - ");
            PwQueryLinks.linkToHls(fmt, agent, query, hls);
            fmt.closeP();
        }
        fmt.deindent();
        return fmt.toString();
    }

}
