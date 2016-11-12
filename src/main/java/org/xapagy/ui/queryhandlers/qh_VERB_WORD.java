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
import org.xapagy.xapi.VerbWord;

public class qh_VERB_WORD implements IQueryHandler, IQueryAttributes {

    @Override
    public void generate(PwFormatter fmt, Agent agent, RESTQuery query, Session session) {
        String identifier = query.getAttribute(Q_ID);
        Collection<VerbWord> vws =
                agent.getXapiDictionary().getDbVerbWords().values();
        VerbWord vw = null;
        for (VerbWord vw2 : vws) {
            if (vw2.getIdentifier().equals(identifier)) {
                vw = vw2;
                break;
            }
        }
        if (vw == null) {
            fmt.addPre("could not find the verb word with the identifier "
                    + identifier);
            return;
        }
        //
        // Identifier block
        //
        String redheader = "VerbWord " + vw.getTextFormat();
        fmt.addH2(redheader, "class=identifier");
        fmt.explanatoryNote(vw.getComment());
        //
        // Structure
        //
        fmt.is("Identifier", vw.getIdentifier());
        fmt.is("Text", vw.getTextFormat());
        qh_VERB_OVERLAY.generate(fmt, agent, vw.getVo(), query, false);
        // list of sentences where it had been referred to
        // agent.getVerbalMemory().
    }

}
