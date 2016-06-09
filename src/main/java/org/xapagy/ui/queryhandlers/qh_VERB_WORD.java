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
