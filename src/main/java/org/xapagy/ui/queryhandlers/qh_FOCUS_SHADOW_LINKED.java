package org.xapagy.ui.queryhandlers;

import org.xapagy.agents.Agent;
import org.xapagy.agents.Focus;
import org.xapagy.headless_shadows.FocusShadowLinked;
import org.xapagy.headless_shadows.FslInterpretation;
import org.xapagy.headless_shadows.HeadlessComponents;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;
import org.xapagy.ui.prettyprint.Formatter;
import org.xapagy.ui.smartprint.SpFocus;

public class qh_FOCUS_SHADOW_LINKED implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        HeadlessComponents hlc = agent.getHeadlessComponents();
        String identifier = query.getAttribute(Q_ID);
        FocusShadowLinked fsl = null;
        for (FocusShadowLinked fsl2 : hlc.getFsls()) {
            if (fsl2.getIdentifier().equals(identifier)) {
                fsl = fsl2;
                break;
            }
        }
        String pp = null;
        if (fsl == null) {
            pp =
                    "FocusShadowLinked with the given identifier could not be found!";
            fmt.addPre(pp);
            return;
        }
        String redheader =
                "FocusShadowLinked" + fmt.getEmpty().addIdentifier(fsl);
        fmt.addH2(redheader, "class=identifier");
        qh_FOCUS_SHADOW_LINKED.pwDetailed(fmt, fsl, query, agent);
        //
        // Interpretations
        //
        fmt.addH2("Interpretations");
        for (FslInterpretation fsli : hlc.getFslis()) {
            if (fsli.getFsl().equals(fsl)) {
                fmt.openP();
                fmt.progressBar(fsli.getSupportFraction(), 1.0);
                PwQueryLinks.linkToFsli("interpretation", fmt, agent, query, fsli);
                fmt.closeP();
            }
        }

    }

    /**
     * Concise description of the FSL - to be used in the link
     * 
     * @param fmt
     * @param fsl
     * @param agent
     */
    public static String pwConcise(PwFormatter fmt, FocusShadowLinked fsl,
            Agent agent) {
        fmt.addEnum(fsl.getFslType().toString());
        String label = SpFocus.ppsViXapiForm(fsl.getViLinked(), agent);
        fmt.add(" relative:" + label);
        fmt.addIdentifier(fsl.getViLinked());
        fmt.add("F:?" + " S:?"  + " L:"
                + Formatter.fmt(fsl.getLinkStrength(agent)));
        return fmt.toString();
    }

    /**
     * Detailed description of the FocusShadowLinked
     * 
     * Extracted here to be able to be inserted into other ones as well (eg.
     * FSLI)
     * 
     * @param fmt
     * @param fsl
     * @param agent
     * @return
     */
    public static String pwDetailed(PwFormatter fmt, FocusShadowLinked fsl,
            RESTQuery query, Agent agent) {
        Shadows sf = agent.getShadows();
        Focus fc = agent.getFocus();
        fmt.is("Type", fmt.getEmpty().addEnum(fsl.getFslType().toString()));
        fmt.is("Total support", fsl.getTotalSupport(agent));
        //
        // The indirectness and the root component (focus VI or choice)
        //
        if (fsl.isIndirect()) {
            fmt.is("Directness",
                    "indirect FSL (based on a non-instantiated choice)");
            fmt.openP();
            // PwHelper.addBold(fmt, "Root choice");
            fmt.add("Root choice");
            PwQueryLinks.linkToChoice(fmt, agent, query, fsl.getChoice());
            fmt.closeP();
        } else {
            fmt.is("Directness", "direct FSL (based on current focus)");
            fmt.openP();
            // PwHelper.addBold(fmt, "Focus");
            fmt.addBold("Focus:");
            PwQueryLinks.linkToVi(fmt, agent, query, fsl.getViFocus());
            fmt.closeP();
            fmt.indent();
            for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.FOCUS_VI)) {
                fmt.openP();
                double energy = fc.getEnergy(fsl.getViFocus(), ec);
                double salience = fc.getSalience(fsl.getViFocus(), ec);
                fmt.progressBarSlash(salience, energy);
                fmt.add(ec);
                fmt.closeP();
            }
            fmt.deindent();
        }
        //
        // The shadow component
        //
        fmt.openP();
        fmt.addBold("Shadow:");
        PwQueryLinks.linkToVi(fmt, agent, query, fsl.getViShadow());
        fmt.closeP();
        fmt.indent();
        for(String ec: agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            fmt.openP();
            double energy = sf.getEnergy(fsl.getViFocus(), fsl.getViShadow(), ec);
            double salience = sf.getSalience(fsl.getViFocus(), fsl.getViShadow(), ec);
            fmt.progressBarSlash(salience, energy);
            fmt.add(ec);
            fmt.closeP();
        }
        fmt.deindent();
        //
        // The relative component
        //
        fmt.openP();
        fmt.addBold("Link:");
        PwQueryLinks.linkToVi(fmt, agent, query, fsl.getViLinked());
        fmt.closeP();
        fmt.indent();
        fmt.openP();
        fmt.progressBar(fsl.getLinkStrength(agent), 1.0);
        fmt.add("getLinkStrength() value"); 
        fmt.closeP();
        fmt.deindent();
        return fmt.toString();
    }
}
