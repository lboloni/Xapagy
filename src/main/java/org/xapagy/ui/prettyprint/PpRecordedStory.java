/*
   This file is part of the Xapagy project
   Created on: Feb 17, 2013
 
   org.xapagy.ui.prettyprint.PpRecordedStory
 
   Copyright (c) 2008-2014 Ladislau Boloni
 */

package org.xapagy.ui.prettyprint;

import org.xapagy.agents.Agent;
import org.xapagy.debug.storygenerator.RecordedStory;
import org.xapagy.debug.storygenerator.RsScene;
import org.xapagy.instances.VerbInstance;
import org.xapagy.ui.smartprint.XapiPrint;

/**
 * @author Ladislau Boloni
 * 
 */
public class PpRecordedStory {
    /**
     * Concise printing: fall back on the toString
     * 
     * @param rs
     * @param agent
     * @return
     */
    public static String ppConcise(RecordedStory rs, Agent agent) {
        return rs.toString();
    }

    public static String ppDetailed(RecordedStory rs, Agent agent) {
        Formatter fmt = new Formatter();
        fmt.add("RecordedStory");
        fmt.indent();
        fmt.add("Stories");
        fmt.indent();
        fmt.add("Initialization");
        fmt.addIndented(rs.getStoryInitialization());
        fmt.add("Post-initialization");
        fmt.addIndented(rs.getStoryPostInitialization());
        for (int i = 0; i != rs.getStories().size(); i++) {
            fmt.add("Story " + (i + 1));
            fmt.addIndented(rs.getStories().get(i));
        }
        fmt.deindent();
        //
        // The scenes - it will also print the instances here
        //
        fmt.add("Scenes:");
        fmt.indent();
        for (RsScene rsScene : rs.getRsScenes()) {
            fmt.add(rsScene.getName());
            fmt.addIndented(PrettyPrint.ppDetailed(rsScene, agent));
        }
        fmt.deindent();
        //
        // The recorded verb instances
        //
        fmt.add("VIs recorded from the execution");
        fmt.indent();
        for (VerbInstance vi : rs.getRecordedVis()) {
            fmt.add(XapiPrint.ppsViXapiForm(vi, agent));
        }
        fmt.deindent();
        return fmt.toString();
    }
}
