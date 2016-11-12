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

import org.xapagy.agents.Agent;
import org.xapagy.headless_shadows.HlsCharacterization;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.IXwFormatter;
import org.xapagy.ui.prettyhtml.PwQueryLinks;

/**
 * @author Ladislau Boloni
 * Created on: May 12, 2013
 */
public class qh_HLS_CHARACTERIZATION {

    /**
     * The detailed description, in an embeddable form - ready to be embedded in
     * another link
     * 
     * @param fmt
     * @param hlss
     * @param agent
     * @param query
     */
    public static void pwDetailed(IXwFormatter fmt, HlsCharacterization hlsc,
            Agent agent, RESTQuery query) {
        String tmp =
                PwQueryLinks.linkToInstance(fmt.getEmpty(), agent, query,
                        hlsc.getInstance());
        fmt.addLabelParagraph("Instance: ", tmp);
        tmp =
                qh_CONCEPT_OVERLAY.pwCompact(fmt.getEmpty(), agent,
                        hlsc.getAttributes(), query);
        fmt.addLabelParagraph("To be characterized with:", tmp);
    }

}
