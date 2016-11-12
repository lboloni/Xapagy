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

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;
import org.xapagy.agents.AutobiographicalMemory;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.EnergyColors.EnergyColorType;
import org.xapagy.set.EnergyQuantum;
import org.xapagy.shadows.Shadows;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettygeneral.xwQuantumEnergyList;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * Creating a web page which explains the way the shadow of a VI had been formed
 * 
 * @author Ladislau Boloni
 * Created on: Nov 16, 2012
 */
public class qh_VI_SHADOW_EXPLANATION implements IQueryHandler, IQueryAttributes {

    /**
     * Generates a web page which explains how a certain shadow value had been
     * created
     */
    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        AutobiographicalMemory am = agent.getAutobiographicalMemory();
        Shadows sd = agent.getShadows();
        String fviId = query.getAttribute(Q_ID);
        VerbInstance fvi = am.getVerbInstance(fviId);
        String sviId = query.getAttribute(Q_SECOND_ID);
        VerbInstance svi = am.getVerbInstance(sviId);
        String redheader = "Explanation for the shadow of a verb instance";
        fmt.addH2(redheader, "class=identifier");
        //
        // the focus VI
        //
        fmt.addLabelParagraph("Focus VI");
        fmt.indent();
        PwQueryLinks.linkToVi(fmt, agent, query, fvi);
        fmt.deindent();
        //
        // the shadow VI
        //
        fmt.addLabelParagraph("Shadow VI");
        fmt.indent();
        PwQueryLinks.linkToVi(fmt, agent, query, svi);
        fmt.deindent();
        //
        // the different values of the shadow
        //
        fmt.addLabelParagraph("Shadow:");
        fmt.indent();
        for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
            double valueSalience = sd.getSalience(fvi, svi, ec);
            double valueEnergy = sd.getEnergy(fvi, svi, ec);
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
            for (String ec : agent.getEnergyColors().getEnergies(EnergyColorType.SHADOW_VI)) {
                fmt.addH3("Shadow EnergyColor: " + ec);
                List<EnergyQuantum<?>> list = new ArrayList<>();
                list.addAll(sd.getEnergyQuantums(fvi, svi, ec));
                xwQuantumEnergyList.xwEnergyQuantumList(fmt, list);
            }
        } else {
            fmt.explanatoryNote("If the quantum recording would be enabled, the quantums would be listed here.");
        }

    }

}
