/*
   This file is part of the Xapagy project
   Created on: Apr 4, 2013
 
   org.xapagy.ui.prettyhtml.PwRsOneLine
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyhtml;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RsOneLine;
import org.xapagy.httpserver.RESTQuery;
import org.xapagy.ui.formatters.PwFormatter;
import org.xapagy.ui.prettyprint.PpRsOneLine;

/**
 * @author Ladislau Boloni
 * 
 */
public class PwRsOneLine {

    public static void generate(PwFormatter fmt, Agent agent, RsOneLine rsol,
            RESTQuery query) {
        // String redheader = "RecordedStory";
        // fmt.addH2(redheader, "class=identifier");
        // String id = query.getId();
        // PwHelper.addErrorMessage(
        // fmt,
        // "PwRecordedStory: for the time being RecordedStories have no id, so we cannot do this");
        fmt.addLabelParagraph("RsOneLine:");
        fmt.addPre(PpRsOneLine.ppDetailed(rsol, agent));
    }

}
