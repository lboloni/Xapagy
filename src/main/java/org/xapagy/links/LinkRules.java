/*
   This file is part of the Xapagy project
   Created on: Mar 12, 2015
 
   org.xapagy.links.LinkRules
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.links;

import java.util.ArrayList;
import java.util.List;

import org.xapagy.agents.Agent;

/**
 * This class implements the fact that some of the link modifications have 
 * side effects. Basically, the class gets a LinkQuantum and returns multiple link
 * quantums.
 * 
 * @author Ladislau Boloni
 *
 */
public class LinkRules {

    public static List<LinkQuantum> linkRules(Agent agent, LinkQuantum lq) {
        List<LinkQuantum> retval = new ArrayList<>();
        /*
        switch(lq.getLinkName()) {
        // summarization links remove succession
        case LinkAPI.SUMMARIZATION_BEGIN:
        case LinkAPI.SUMMARIZATION_BODY:
        case LinkAPI.SUMMARIZATION_CLOSE:
        }
        if (lq.getLinkName().equals(LinkAPI.SUMMARIZATION_BEGIN))
        */
        // default behavior 
        retval.add(lq);
        return retval;
    }
    
}
