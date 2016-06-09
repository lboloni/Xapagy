/*
   This file is part of the Xapagy project
   Created on: Dec 27, 2014
 
   org.xapagy.summarization.SummarizationHelper
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.summarization;

import org.xapagy.agents.Agent;
import org.xapagy.concepts.Hardwired;
import org.xapagy.instances.VerbInstance;
import org.xapagy.set.ViSet;

/**
 * Functions which decide things about summarization VIs
 * 
 * @author Ladislau Boloni
 *
 */
public class SummarizationHelper {

    
    /**
     * A VI is a summarization if it has a SUMMARIZATION_BEGIN link
     * 
     * @param agent
     * @param vi
     * @return
     */
    public static boolean isSummarization(Agent agent, VerbInstance vi) {
        ViSet begins = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_ELABORATION_BEGIN);
        if (begins.isEmpty()) {
            return false;
        }
        return true;
    }

    
    /**
     * A VI is a closed summarization if it is a summarization and it has a 
     * SUMMARIZATION_CLOSE link.
     * 
     * @param agent
     * @param vi
     * @return
     */
    public static boolean isClosedSummarization(Agent agent, VerbInstance vi) {
        if (!isSummarization(agent, vi)) {
            return false;
        }
        ViSet viset = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_ELABORATION_CLOSE);
        if (viset.isEmpty()) {
            return false;
        }
        return true;
    }

    
    /**
     * A VI is an open summarization if it is a summarization and it does not have 
     * a SUMMARIZATION_CLOSE link.
     * 
     * @param agent
     * @param vi
     * @return
     */
    public static boolean isOpenSummarization(Agent agent, VerbInstance vi) {
        if (!isSummarization(agent, vi)) {
            return false;
        }
        ViSet viset = agent.getLinks().getLinksByLinkName(vi, Hardwired.LINK_ELABORATION_CLOSE);
        if (viset.isEmpty()) {
            return true;
        }
        return false;
    }

}
