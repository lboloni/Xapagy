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

import java.util.Collection;

import org.xapagy.agents.Agent;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.httpserver.Session;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyhtml.IQueryAttributes;
import org.xapagy.ui.prettyhtml.IQueryHandler;
import org.xapagy.xapi.ConceptWord;

public class qh_CONCEPT_WORD implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        Collection<ConceptWord> cws =
                agent.getXapiDictionary().getDbConceptWords().values();
        ConceptWord cw = null;
        for (ConceptWord cw2 : cws) {
            if (cw2.getIdentifier().equals(identifier)) {
                cw = cw2;
                break;
            }
        }
        if (cw == null) {
            fmt.addPre("could not find the concept word with the identifier "
                    + identifier);
            return;
        }
        //
        // Identifier block
        //
        String redheader = "ConceptWord " + cw.getTextFormat();
        fmt.addH2(redheader, "class=identifier");
        fmt.explanatoryNote(cw.getComment());
        //
        // Structure
        //
        fmt.is("Identifier", cw.getIdentifier());
        fmt.is("Text", cw.getTextFormat());
        qh_CONCEPT_OVERLAY.generate(fmt, agent, cw.getCo(), query, false);
    }

}
