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
package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyprint.PpRecordedStory;

/**
 * @author Ladislau Boloni
 * Created on: Apr 4, 2013
 */
public class PwRecordedStory {

    public static void generate(PwFormatter fmt, Agent agent, RecordedStory rs,
            RESTQuery query) {
        String redheader = "RecordedStory";
        fmt.addH2(redheader, "class=identifier");
        // String id = query.getId();
        // PwHelper.addErrorMessage(
        // fmt,
        // "PwRecordedStory: for the time being RecordedStories have no id, so we cannot do this");
        fmt.addLabelParagraph("RecordedStory:");
        fmt.addPre(PpRecordedStory.ppDetailed(rs, agent));
    }

}
