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
