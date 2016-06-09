/*
   This file is part of the Xapagy project
   Created on: Dec 25, 2010
 
   org.xapagy.xapi.Verbalize
 
   Copyright (c) 2008-2010 Ladislau Boloni
 */

package org.xapagy.verbalize;

import java.io.Serializable;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.ConceptOverlay;
import org.xapagy.instances.VerbInstance;
import org.xapagy.instances.ViStructureHelper.ViPart;

/**
 * Translates the VI into a parseable (and hopefully readable) Xapi form.
 * 
 * @author Ladislau Boloni
 * 
 */
public class Verbalize implements Serializable {

    private static final long serialVersionUID = 565514061221827554L;

    private Agent agent;

    public Verbalize(Agent agent) {
        this.agent = agent;
    }

    /**
     * Verbalizes a certain VI.
     * 
     * @param vi
     * @return
     */
    public String verbalize(VerbInstance vi) {
        String retval = "";
        switch (vi.getViType()) {
        case S_V_O: {
            retval =
                    "The "
                            + VrblzInstance.verbalizeInstance(agent, vi,
                                    ViPart.Subject)
                            + " / "
                            + VerbalizeVo.verbalizeVerb(agent, vi.getVerbs(), vi)
                            + " / the "
                            + VrblzInstance.verbalizeInstance(agent, vi,
                                    ViPart.Object) + ".";
            break;
        }
        case S_V: {
            retval =
                    "The "
                            + VrblzInstance.verbalizeInstance(agent, vi,
                                    ViPart.Subject) + " / "
                            + VerbalizeVo.verbalizeVerb(agent, vi.getVerbs(), vi) + ".";
            break;
        }
        case S_ADJ: {
            ConceptOverlay adjective = vi.getAdjective();
            retval =
                    "The "
                            + VrblzInstance.verbalizeInstance(agent, vi,
                                    ViPart.Subject)
                            + " / "
                            + VerbalizeVo.verbalizeVerb(agent, vi.getVerbs(), vi)
                            + " / "
                            + VrblzAdjective.verbalizeAdjective(agent,
                                    adjective, true) + ".";
            break;
        }
        case QUOTE: {
            String quoteVerbalized = verbalize(vi.getQuote());
            if (quoteVerbalized.startsWith("T")) {
                quoteVerbalized = "t" + quoteVerbalized.substring(1);
            }
            retval =
                    "The "
                            + VrblzInstance.verbalizeInstance(agent, vi,
                                    ViPart.Subject)
                            + " / "
                            + VerbalizeVo.verbalizeVerb(agent, vi.getVerbs(), vi)
                            + " in "
                            + VrblzInstance.verbalizeScene(agent, vi,
                                    ViPart.QuoteScene) + " // "
                            + quoteVerbalized;
            break;
        }
        }
        // capitalize the first letter
        // retval = retval.substring(0, 1).toUpperCase() + retval.substring(1);
        return retval;
    }

}
