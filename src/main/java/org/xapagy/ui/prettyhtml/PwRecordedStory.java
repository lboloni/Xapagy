/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2013
 
   org.xapagy.ui.prettyhtml.PpRecordedStory
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyprint.PpRecordedStory;

/**
 * @author Ladislau Boloni
 * 
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
