/*
   This file is part of the Xapagy project
   Created on: Nov 16, 2012
 
   org.xapagy.ui.prettyhtml.PwInstanceShadowExplanation
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.queryhandlers;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.Instance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwQuantumEnergyList;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * @author Ladislau Boloni
 * 
 */
public class qh_INSTANCE_SHADOW_EXPLANATION implements IQueryHandler, IQueryAttributes {


    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        String fiId = query.getAttribute(Q_ID);
        Instance fi = am.getInstance(fiId);
        String siId = query.getAttribute(Q_SECOND_ID);
        Instance si = am.getInstance(siId);
        Shadows sd = agent.getShadows();

        String redheader = "Explanation for the shadow of an instance";
        fmt.addH2(redheader, "class=identifier");
        //
        // the focus instance
        //
        fmt.addLabelParagraph("Focus instance");
        fmt.indent();
        PwQueryLinks.linkToInstance(fmt, agent, query, fi);
        fmt.deindent();
        //
        // the shadow instance
        //
        fmt.addLabelParagraph("Shadow instance");
        fmt.indent();
        PwQueryLinks.linkToInstance(fmt, agent, query, si);
        fmt.deindent();
        //
        // the different values of the shadow
        //
        fmt.addLabelParagraph("Shadow energies:");
        fmt.indent();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
            double valueSalience = sd.getSalience(fi, si, ec);
            double valueEnergy = sd.getEnergy(fi, si, ec);
            fmt.addBold(ec);
            fmt.progressBarSlash(valueSalience, valueEnergy);
        }
        fmt.deindent();
        //
        // now list the quantums
        //
        boolean debugRecordShadowQuantums =
                agent.getParameters().getBoolean("A_DEBUG",
                        "G_GENERAL",
                        "N_RECORD_SHADOW_QUANTUMS");
        if (debugRecordShadowQuantums) {
            for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_INSTANCE)) {
                fmt.addH3("Shadow EnergyColor: " + ec);
                List<EnergyQuantum<?>> list = new ArrayList<>();
                list.addAll(sd.getEnergyQuantums(fi, si, ec));
                xwQuantumEnergyList.xwEnergyQuantumList(fmt, list);
            }
        } else {
            fmt.explanatoryNote("If the quantum recording would be enabled, the quantums would be listed here.");
        }
    }

}
