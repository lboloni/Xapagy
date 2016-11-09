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
 * Created on: Mar 12, 2015
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
