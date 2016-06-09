package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.TextUi;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyhtml.PwViTemplate;
import org.xapagy.ui.smartprint.SpFocus;

public class qh_FSL_INTERPRETATION implements IQueryHandler, IQueryAttributes {

    /**
     * Generates an FSL interpretation
     * 
     * @param fmt
     * @param agent
     * @param query
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        FslInterpretation fsli = null;
        for (FslInterpretation fsli2 : agent.getHeadlessComponents().getFslis()) {
            if (fsli2.getIdentifier().equals(identifier)) {
                fsli = fsli2;
                break;
            }
        }
        String pp = null;
        if (fsli == null) {
            pp =
                    "FocusShadowLinked with the given identifier could not be found!";
            fmt.addPre(pp);
            return;
        }
        String redheader =
                "FslInterpretation" + fmt.getEmpty().addIdentifier(fsli);
        fmt.addH2(redheader, "class=identifier");
        qh_FSL_INTERPRETATION.pwDetailed(fmt, fsli, agent, query);
    }

    /**
     * A concise description of the FSLI. This version prints the relative -
     * which
     * 
     * @return
     */
    public static String pwConcise(String howtolabel, IXwFormatter fmt,
            FslInterpretation fsli, Agent agent) {
        fmt.add("FSLI:" + fsli.getFsl().getFslType());
        fmt.addIdentifier(fsli);
        switch (howtolabel) {
        case "relative": {
            VerbInstance vi = fsli.getFsl().getViLinked();
            String label = "relative: " + SpFocus.ppsViXapiForm(vi, agent);
            label += fmt.getEmpty().addIdentifier(vi);
            fmt.add(label);
            return fmt.toString();
        }
        case "shadow": {
            VerbInstance vi = fsli.getFsl().getViShadow();
            String label = "shadow: " + SpFocus.ppsViXapiForm(vi, agent);
            label += fmt.getEmpty().addIdentifier(vi);
            fmt.add(label);
            return fmt.toString();
        }
        case "interpretation": {
            PwViTemplate.pwConcise(fmt, fsli.getViInterpretation(), agent);
            return fmt.toString();
        }
        default: {
            TextUi.abort("PwFslInterpretation.pwConcise: could not understand howtolabel = "
                    + howtolabel);
        }
        }
        return null;
    }

    /**
     * An extensive description of the FSLI
     * 
     * @param fsli
     * @param agent
     * @param query
     * @return
     */
    public static String pwDetailed(PwFormatter fmt, FslInterpretation fsli,
            Agent agent, RESTQuery query) {
        fmt.is("Total support:", fsli.getTotalSupport(agent));
        fmt.is("Fraction % of parent FSL", fsli.getSupportFraction() * 100);
        fmt.addLabelParagraph("VI template");
        PwViTemplate.xwDetailed(fmt, fsli.getViInterpretation(), agent, query);
        fmt.addLabelParagraph("Parent FSL",
                PwQueryLinks.linkToFsl(fmt.getEmpty(), agent, query, fsli.getFsl()));
        fmt.startEmbedX("FSL");
        qh_FOCUS_SHADOW_LINKED.pwDetailed(fmt, fsli.getFsl(), query, agent);
        fmt.endEmbedX();
        return fmt.toString();
    }

}
